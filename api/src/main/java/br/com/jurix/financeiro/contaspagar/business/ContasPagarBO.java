package br.com.jurix.financeiro.contaspagar.business;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagarIncidencia;
import br.com.jurix.financeiro.contaspagar.entity.QContasPagar;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumFrequenciaContasPagar;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumSituacaoConta;
import br.com.jurix.financeiro.contaspagar.exception.ContaAtrasadaSemValorPagoException;
import br.com.jurix.financeiro.contaspagar.repository.ContasPagarIncidenciaRepository;
import br.com.jurix.financeiro.contaspagar.repository.ContasPagarRepository;
import br.com.jurix.financeiro.pessoaconta.business.PessoaContaBO;
import br.com.jurix.financeiro.pessoaconta.entity.QPessoaConta;
import br.com.jurix.localidade.entity.QMunicipio;
import br.com.jurix.math.utils.JurixMathUtils;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContasPagarBO {

    @Autowired
    private ContasPagarRepository contasPagarRepository;

    @Autowired
    private ContasPagarIncidenciaRepository contasPagarIncidenciaRepository;

    @Autowired
    private PessoaContaBO pessoaContaBO;

    @Autowired
    private JurixDateTime jurixDateTime;

    @Value("${jurix.prazoDiasConta}")
    private Integer prazoDiasConta;

    @Transactional
    public ContasPagar create(ContasPagar contasPagar) {

        setPessoaConta(contasPagar);
        contasPagar.setDataCriacao(jurixDateTime.getCurrentDateTime());
        contasPagar.setSituacao(EnumSituacaoConta.PENDENTE);

        List<ContasPagarIncidencia> incidencias = contasPagar.getIncidencias();
        contasPagar.setIncidencias(null);
        ContasPagar contaSalva = contasPagarRepository.save(contasPagar);
        salvarIncidencias(contaSalva, incidencias);

        return contaSalva;
    }

    public ContasPagar createIfNotExists(ContasPagar contasPagar, Long contaCentrosCustoId) {

        if(notExistsContaPagarIgualParaMesmoDia(contasPagar, contaCentrosCustoId)) {
            return create(contasPagar);
        }

        return null;
    }

    public List<ContasPagar> buscarcontasPagar(Integer mes, Integer ano, Long centroCustoId) {
        List<ContasPagar> result = contasPagarRepository.buscarcontasPagar(mes, ano, centroCustoId);
        incluirParametrosContasPagar(result);
        return result;
    }

    public ContasPagar findById(Long id) {
        ContasPagar contasPagar =  contasPagarRepository.buscarcontasPagar(id);
        return contasPagar;
    }

    public List<ContasPagar> buscarcontasPagar(BooleanExpression mainBooleanExpression) {

        mainBooleanExpression = mainBooleanExpression.and(QContasPagar.contasPagar.removido.isFalse());

        List<ContasPagar> result = contasPagarRepository.findAll(mainBooleanExpression,
                JoinDescriptor.join(QContasPagar.contasPagar.pessoaConta, QPessoaConta.pessoaConta),
                JoinDescriptor.leftJoin(QPessoaConta.pessoaConta.municipio, QMunicipio.municipio),
                JoinDescriptor.leftJoin(QMunicipio.municipio.estado));

        incluirParametrosContasPagar(result);
        return result;
    }

    @Transactional
    public ContasPagar update(ContasPagar contasPagar) {

        ContasPagar contasPagarExistente = contasPagarRepository.findOne(contasPagar.getId());

        contasPagar.setSituacao(contasPagarExistente.getSituacao());
        contasPagar.setDataCriacao(contasPagarExistente.getDataCriacao());

        setPessoaConta(contasPagar);
        List<ContasPagarIncidencia> incidencias = contasPagar.getIncidencias();
        contasPagar.setIncidencias(null);

        BeanUtils.copyProperties(contasPagar, contasPagarExistente);

        ContasPagar contaSalva =  contasPagarRepository.save(contasPagarExistente);

        salvarIncidencias(contaSalva, incidencias);

        return contaSalva;
    }

    public ContasPagar pagarConta(ContasPagar contasPagar) {

        ContasPagar contasPagarExistente = contasPagarRepository.findOne(contasPagar.getId());

        setValorPagoConta(contasPagar, contasPagarExistente);

        contasPagarExistente.setSituacao(EnumSituacaoConta.PAGA);
        contasPagarExistente.setDataPagamento(contasPagar.getDataPagamento());
        contasPagarExistente.setPagamentoFinal(contasPagar.getPagamentoFinal());

        return contasPagarRepository.save(contasPagarExistente);
    }

    public void delete(Long contaPagarId) {
        ContasPagar contasPagarExistente = contasPagarRepository.findOne(contaPagarId);
        contasPagarExistente.setRemovido(Boolean.TRUE);
        contasPagarRepository.save(contasPagarExistente);
    }

    public List<ContasPagar> buscarcontasPagarComRecorrencia(Date dataReferencia, EnumFrequenciaContasPagar frequenciaContasPagar) {

        Date dataInicio = JurixDateTime.getStartOfDay(dataReferencia);
        Date dataFim = JurixDateTime.getEndOfDay(dataReferencia);

        return contasPagarRepository.buscarcontasPagarComRecorrenciaMesAnterior(dataInicio, dataFim, frequenciaContasPagar);
    }

    public void criarNovasContasMensais(Date dataReferencia){
        Date dataMesAnterior = jurixDateTime.subtrairMeses(dataReferencia, 1);
        List<ContasPagar> contasMensais = buscarcontasPagarComRecorrencia(dataMesAnterior, EnumFrequenciaContasPagar.MENSAL);

        contasMensais.forEach(contasPagar -> criarCopiaConta(contasPagar, dataReferencia));
    }

    public void criarNovasContasSemanais(Date dataReferencia){
        Date dataMesAnterior = jurixDateTime.subtrairDias(dataReferencia, 7);
        List<ContasPagar> contasMensais = buscarcontasPagarComRecorrencia(dataMesAnterior, EnumFrequenciaContasPagar.SEMANAL);

        contasMensais.forEach(contasPagar -> criarCopiaConta(contasPagar, dataReferencia));
    }

    public void criarNovasContasAnuais(Date dataReferencia){
        Date dataMesAnterior = jurixDateTime.subtrairAnos(dataReferencia, 1);
        List<ContasPagar> contasMensais = buscarcontasPagarComRecorrencia(dataMesAnterior, EnumFrequenciaContasPagar.ANUAL);

        contasMensais.forEach(contasPagar -> criarCopiaConta(contasPagar, dataReferencia));
    }

    private void salvarIncidencias(ContasPagar contaSalva, List<ContasPagarIncidencia> incidencias) {
        List<Long> incidenciasIds = incidencias.stream().filter(contasPagarIncidencia ->  Objects.nonNull(contasPagarIncidencia.getId())).map(ContasPagarIncidencia::getId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(incidenciasIds)) {
            contasPagarIncidenciaRepository.deleteAllByContaPagarIdAndIdNotIn(contaSalva.getId(), incidenciasIds);
        }

        atribuirValorIncidencia(contaSalva, incidencias);

        contasPagarIncidenciaRepository.save(incidencias);
    }

    private void atribuirValorIncidencia(ContasPagar contaSalva, List<ContasPagarIncidencia> incidencias) {
        BigDecimal totalParcial = BigDecimal.ZERO;
        for(int i = 0; i < incidencias.size(); i++) {
            ContasPagarIncidencia incidencia = incidencias.get(i);
            incidencia.setContaPagarId(contaSalva.getId());
            incidencia.setValor(JurixMathUtils.getValorPercentual(contaSalva.getValor(), incidencia.getIncidencia()));

            if(incidencias.size() > 1 && i != (incidencias.size() - 1)){
                totalParcial = totalParcial.add(incidencia.getValor());
            }
        }

        if(incidencias.size() > 1){
            ContasPagarIncidencia incidencia = incidencias.get(incidencias.size() - 1);
            incidencia.setValor(JurixMathUtils.arredondar(contaSalva.getValor().subtract(totalParcial)));
        }
    }

    private void criarCopiaConta(ContasPagar contaOrigem, Date dataReferencia){

        ContasPagar novaConta = new ContasPagar();
        novaConta.setDataVencimento(dataReferencia);
        novaConta.setFrequencia(contaOrigem.getFrequencia());
        novaConta.setDescricao(contaOrigem.getDescricao());
        novaConta.setPessoaConta(contaOrigem.getPessoaConta());
        novaConta.setRepetirValor(contaOrigem.getRepetirValor());
        novaConta.setIncidencias(new ArrayList<>());
        if(novaConta.getRepetirValor()){
            novaConta.setValor(contaOrigem.getValor());
        }else{
            novaConta.setValor(BigDecimal.ZERO);
        }
        novaConta.setTipo(contaOrigem.getTipo());

        ContasPagar novaContaCriada = createIfNotExists(novaConta, contaOrigem.getId());
        criarCopiaIncidencias(contaOrigem, novaContaCriada);
    }

    private void criarCopiaIncidencias(ContasPagar contaOrigem, ContasPagar novaContaCriada) {
        if(Objects.nonNull(novaContaCriada)){
            List<ContasPagarIncidencia> incidenciasOrigem = contasPagarIncidenciaRepository.findByContaPagarId(contaOrigem.getId());

            incidenciasOrigem.forEach(incidenciaOrigem -> {

                ContasPagarIncidencia contasPagarIncidencia = new ContasPagarIncidencia();
                contasPagarIncidencia.setContaPagarId(novaContaCriada.getId());
                contasPagarIncidencia.setCentroCusto(incidenciaOrigem.getCentroCusto());
                contasPagarIncidencia.setIncidencia(incidenciaOrigem.getIncidencia());
                contasPagarIncidencia.setValor(incidenciaOrigem.getValor());

                contasPagarIncidenciaRepository.save(contasPagarIncidencia);
            });
        }
    }

    private boolean notExistsContaPagarIgualParaMesmoDia(ContasPagar contasPagar, Long contaCentrosCustoId){

        Date dataInicio = JurixDateTime.getStartOfDay(contasPagar.getDataVencimento());
        Date dataFim = JurixDateTime.getEndOfDay(contasPagar.getDataVencimento());

        return CollectionUtils.isEmpty(contasPagarRepository.buscarcontasPagarPeriodoPorDescricaoECentroCustoDaConta(dataInicio, dataFim, contasPagar.getFrequencia(), contasPagar.getDescricao(), contaCentrosCustoId));
    }

    private void setValorPagoConta(ContasPagar contasPagar, ContasPagar contasPagarExistente) {

        if (jurixDateTime.isDataAtrasa(contasPagarExistente.getDataVencimento())) {

            validarContaAtrasaPossuiValorPago(contasPagar);

            contasPagarExistente.setValorPago(contasPagar.getValorPago());
        } else {
            contasPagarExistente.setValorPago(contasPagarExistente.getValor());
        }
    }

    private void incluirParametrosContasPagar(List<ContasPagar> contasPagar) {

        contasPagar.forEach(contaPagar -> {

            contaPagar.setIsAtrasado(Boolean.FALSE);
            contaPagar.setAVencer(Boolean.FALSE);
            contaPagar.getIncidencias().forEach(inicidencia -> {
                inicidencia.getId();
            });

            if (jurixDateTime.isDataAtrasa(contaPagar.getDataVencimento())) {
                contaPagar.setIsAtrasado(Boolean.TRUE);
            } else {
                contaPagar.setAVencer(JurixDateTime.getQuantidadeDiasEntreData(jurixDateTime.getCurrentDateTime(), contaPagar.getDataVencimento()) <= prazoDiasConta);
            }

        });
    }

    private void validarContaAtrasaPossuiValorPago(ContasPagar contasPagar) {
        if (Objects.isNull(contasPagar.getValorPago())) {
            throw new ContaAtrasadaSemValorPagoException();
        }

    }

    private void setPessoaConta(ContasPagar contasPagar) {
        contasPagar.setPessoaConta(pessoaContaBO.createIfNotExists(contasPagar.getPessoaConta()));
    }

    public List<ContasPagarIncidencia> buscarIncidencias(Long centroCustoId, List<Long> idsContas) {
        return contasPagarIncidenciaRepository.buscarIncidencias(centroCustoId, idsContas);
    }

    public void ajsutarIncidencia() {
        List<ContasPagar> contas = contasPagarRepository.findAll();
        for (ContasPagar conta : contas) {
            salvarIncidencias(conta, conta.getIncidencias());
        }
    }
}

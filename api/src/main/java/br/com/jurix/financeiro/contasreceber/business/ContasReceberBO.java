package br.com.jurix.financeiro.contasreceber.business;

import br.com.jurix.cliente.entity.Contrato;
import br.com.jurix.cliente.enumeration.EnumIndexadorContrato;
import br.com.jurix.cliente.enumeration.EnumTipoContrato;
import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.financeiro.contaspagar.exception.ContaAtrasadaSemValorPagoException;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceber;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceberIncidencia;
import br.com.jurix.financeiro.contasreceber.entity.QContasReceber;
import br.com.jurix.financeiro.contasreceber.enumeration.EnumSituacaoContaReceber;
import br.com.jurix.financeiro.contasreceber.repository.ContasReceberIncidenciaRepository;
import br.com.jurix.financeiro.contasreceber.repository.ContasReceberRepository;
import br.com.jurix.financeiro.pessoaconta.business.PessoaContaBO;
import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import br.com.jurix.financeiro.pessoaconta.entity.QPessoaConta;
import br.com.jurix.financeiro.pessoaconta.enumeration.EnumTipoPessoaConta;
import br.com.jurix.localidade.entity.QMunicipio;
import br.com.jurix.math.utils.JurixMathUtils;
import br.com.jurix.parametro.entity.Parametro;
import br.com.jurix.parametro.repository.ParametroRepository;
import br.com.jurix.parametro.utils.ParamsConst;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContasReceberBO {

    @Autowired
    private ContasReceberRepository contasReceberRepository;

    @Autowired
    private ContasReceberIncidenciaRepository contasReceberIncidenciaRepository;

    @Autowired
    private PessoaContaBO pessoaContaBO;

    @Autowired
    private JurixDateTime jurixDateTime;

    @Autowired
    private ParametroRepository parametroRepository;

    @Value("${jurix.prazoDiasConta}")
    private Integer prazoDiasConta;

    @Transactional
    public ContasReceber create(ContasReceber contasReceber) {

        setPessoaConta(contasReceber);
        contasReceber.setDataCriacao(jurixDateTime.getCurrentDateTime());
        contasReceber.setSituacao(EnumSituacaoContaReceber.PENDENTE);

        if (contasReceber.getPagamentoParcelado()) {

            if (Objects.nonNull(contasReceber.getDataEntrada())) {
                contasReceber.setDataReceber(contasReceber.getDataEntrada());
                contasReceber.setValorReceber(contasReceber.getValorEntrada());
            } else {

                if(Objects.isNull(contasReceber.getNumeroParcela())){
                    contasReceber.setNumeroParcela(1);
                }

                contasReceber.setDataReceber(contasReceber.getDataPrimeiraParcela());
                contasReceber.setValorReceber(contasReceber.getValorParcela());
            }
        }

        List<ContasReceberIncidencia> incidencias = contasReceber.getIncidencias();
        contasReceber.setIncidencias(null);

        ContasReceber contaSalva = contasReceberRepository.save(contasReceber);
        salvarIncidencias(contaSalva, incidencias);
        return contaSalva;
    }

    private void salvarIncidencias(ContasReceber contasReceber, List<ContasReceberIncidencia> incidencias) {

        List<Long> incidenciasIds = incidencias.stream().filter(contasReceberIncidencia ->  Objects.nonNull(contasReceberIncidencia.getId())).map(ContasReceberIncidencia::getId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(incidenciasIds)) {
            contasReceberIncidenciaRepository.deleteAllByContaReceberIdAndIdNotIn(contasReceber.getId(), incidenciasIds);
        }

        atribuirValorIncidencia(contasReceber, incidencias);

        contasReceberIncidenciaRepository.save(incidencias);
    }

    private void atribuirValorIncidencia(ContasReceber contasReceber, List<ContasReceberIncidencia> incidencias) {
        BigDecimal totalParcial = BigDecimal.ZERO;
        for(int i = 0; i < incidencias.size(); i++) {
            ContasReceberIncidencia incidencia = incidencias.get(i);
            incidencia.setContaReceberId(contasReceber.getId());
            incidencia.setValor(JurixMathUtils.getValorPercentual(contasReceber.getValorReceber(), incidencia.getIncidencia()));

            if(incidencias.size() > 1 && i != (incidencias.size() - 1)){
                totalParcial = totalParcial.add(incidencia.getValor());
            }
        }

        if(incidencias.size() > 1){
            ContasReceberIncidencia incidencia = incidencias.get(incidencias.size() - 1);
            incidencia.setValor(JurixMathUtils.arredondar(contasReceber.getValorReceber().subtract(totalParcial)));
        }
    }

    public ContasReceber createIfNotExistsContaContrato(ContasReceber contasReceber, Long contaCentrosCustoId) {

        if (notExistsContaIgualParaMesmoDiaContaContrato(contasReceber, contaCentrosCustoId)) {
            return create(contasReceber);
        }
        return null;
    }

    public ContasReceber createIfNotExistsContaParcelada(ContasReceber contasReceber, Long contaCentrosCustoId) {

        if (notExistsContaIgualParaMesmoDiaContaParcelada(contasReceber, contaCentrosCustoId)) {

            setPessoaConta(contasReceber);
            contasReceber.setDataCriacao(jurixDateTime.getCurrentDateTime());
            contasReceber.setSituacao(EnumSituacaoContaReceber.PENDENTE);

            return contasReceberRepository.save(contasReceber);
        }
        return null;
    }


    @Transactional
    public ContasReceber update(ContasReceber contasReceber) {

        ContasReceber contasReceberExistente = contasReceberRepository.findOne(contasReceber.getId());
        contasReceber.setSituacao(contasReceberExistente.getSituacao());
        contasReceber.setDataCriacao(contasReceberExistente.getDataCriacao());

        if (Objects.nonNull(contasReceberExistente.getContrato())) {

            contasReceber.setCliente(contasReceberExistente.getCliente());
            contasReceber.setContrato(contasReceberExistente.getContrato());
            contasReceber.setPessoaConta(contasReceberExistente.getPessoaConta());

            if (Objects.nonNull(contasReceberExistente.getValorExtraordinario())) {
                BigDecimal valorReceber = contasReceberExistente.getValorReceber().subtract(contasReceberExistente.getValorExtraordinario());
                contasReceber.setValorReceber(valorReceber);
            } else {
                contasReceber.setValorReceber(contasReceberExistente.getValorReceber());
            }

            if (Objects.nonNull(contasReceber.getValorExtraordinario())) {
                contasReceber.setValorReceber(contasReceber.getValorReceber().add(contasReceber.getValorExtraordinario()));
            }

        } else {
            setPessoaConta(contasReceber);
        }

        List<ContasReceberIncidencia> incidencias = contasReceber.getIncidencias();
        contasReceber.setIncidencias(null);

        BeanUtils.copyProperties(contasReceber, contasReceberExistente);
        ContasReceber contaSalva = contasReceberRepository.save(contasReceberExistente);
        salvarIncidencias(contaSalva, incidencias);
        return contaSalva;
    }

    public ContasReceber pagarConta(ContasReceber contasReceber) {

        ContasReceber contasReceberExistente = contasReceberRepository.findOne(contasReceber.getId());

        setValorRecebidoConta(contasReceber, contasReceberExistente);

        contasReceberExistente.setSituacao(EnumSituacaoContaReceber.RECEBIDO);
        contasReceberExistente.setDataRecebimento(contasReceber.getDataRecebimento());

        return contasReceberRepository.save(contasReceberExistente);
    }

    public List<ContasReceber> buscarcontasReceber(Integer mes, Integer ano, Long centroCustoId) {
        List<ContasReceber> result = contasReceberRepository.buscarcontasReceber(mes, ano, centroCustoId);
        incluirParametrosContasReceber(result);
        return result;
    }

    public ContasReceber findById(Long id) {
        return  contasReceberRepository.buscarcontasReceber(id);
    }

    public List<ContasReceber> buscarcontasReceber(BooleanExpression mainBooleanExpression) {

        mainBooleanExpression = mainBooleanExpression.and(QContasReceber.contasReceber.removido.isFalse());
        
        List<ContasReceber> result = contasReceberRepository.findAll(mainBooleanExpression,
                JoinDescriptor.join(QContasReceber.contasReceber.pessoaConta, QPessoaConta.pessoaConta),
                JoinDescriptor.join(QContasReceber.contasReceber.cliente),
                JoinDescriptor.leftJoin(QContasReceber.contasReceber.contrato),
                JoinDescriptor.leftJoin(QPessoaConta.pessoaConta.municipio, QMunicipio.municipio),
                JoinDescriptor.leftJoin(QMunicipio.municipio.estado));

        incluirParametrosContasReceber(result);
        return result;
    }

    public void delete(Long contaReceberId) {
        ContasReceber contasReceberExistente = contasReceberRepository.findOne(contaReceberId);
        salvarContaRemovida(contasReceberExistente);
    }

    private void salvarContaRemovida(ContasReceber contasReceberExistente) {
        contasReceberExistente.setRemovido(Boolean.TRUE);
        contasReceberRepository.save(contasReceberExistente);
    }


    public void criarContaContrato(Contrato contratoSalvo) {

        ContasReceber contasContrato = new ContasReceber();
        contasContrato.setDescricao("Contrato: " + contratoSalvo.getDescricao());
        contasContrato.setCliente(contratoSalvo.getCliente());
        contasContrato.setContrato(contratoSalvo);

        setValorReceber(contratoSalvo, contasContrato);

        contasContrato.setDataReceber(contratoSalvo.getDataVencimento());

        PessoaConta pessoaConta = new PessoaConta();
        pessoaConta.setNome(contratoSalvo.getCliente().getNome());
        pessoaConta.setEndereco(contratoSalvo.getCliente().getEndereco());
        pessoaConta.setMunicipio(contratoSalvo.getCliente().getMunicipio());
        pessoaConta.setCep(contratoSalvo.getCliente().getCep());

        if (Objects.nonNull(contratoSalvo.getCliente().getClienteFisico())) {
            pessoaConta.setDocumento(contratoSalvo.getCliente().getClienteFisico().getCpf());
            pessoaConta.setTipoPessoa(EnumTipoPessoaConta.FISICA);
        } else {
            pessoaConta.setDocumento(contratoSalvo.getCliente().getClienteJuridico().getCnpj());
            pessoaConta.setTipoPessoa(EnumTipoPessoaConta.JURIDICA);
        }

        contasContrato.setPessoaConta(pessoaConta);

        addIncidenciaCentroCusto(contratoSalvo, contasContrato);

        create(contasContrato);
    }

    private void setValorReceber(Contrato contratoSalvo, ContasReceber contasContrato) {
        if (contratoSalvo.getIndexador().equals(EnumIndexadorContrato.REAL)) {
            contasContrato.setValorReceber(contratoSalvo.getQuantidade());
        } else {
            Parametro parametro = parametroRepository.findOneByChave(ParamsConst.INDEXADOR_SALARIO_MINIMO);
            BigDecimal valorIndexadorSalrio = new BigDecimal(parametro.getValor());
            contasContrato.setValorReceber(contratoSalvo.getQuantidade().multiply(valorIndexadorSalrio));
        }
    }

    public void atualizarContaAtualContrato(Contrato contratoSalvo) {

        ContasReceber contaAtualContrato = contasReceberRepository.buscarContaAtualContrato(contratoSalvo.getId());

        if(Objects.nonNull(contaAtualContrato)){
            contaAtualContrato.setDescricao("Contrato: " + contratoSalvo.getDescricao());
            setValorReceber(contratoSalvo, contaAtualContrato);

            Date dataReceberAtual = contaAtualContrato.getDataReceber();
            Integer diaMes = JurixDateTime.getDayOfMonth(contratoSalvo.getDataVencimento());
            contaAtualContrato.setDataReceber(JurixDateTime.setDayOfMonth(dataReceberAtual, diaMes));
        }

    }

    public List<ContasReceber> buscarcontasComRecorrencia(Date dataReferencia, EnumTipoContrato tipoContrato) {

        Date dataInicio = JurixDateTime.getStartOfDay(dataReferencia);
        Date dataFim = JurixDateTime.getEndOfDay(dataReferencia);

        return contasReceberRepository.buscarcontasComRecorrenciaMesAnterior(dataInicio, dataFim, tipoContrato);
    }

    public void criarNovasContasMensais(Date dataReferencia) {
        Date dataMesAnterior = jurixDateTime.subtrairMeses(dataReferencia, 1);
        List<ContasReceber> contasMensais = buscarcontasComRecorrencia(dataMesAnterior, EnumTipoContrato.MENSAL);

        contasMensais.forEach(contasPagar -> criarCopiaContaContrato(contasPagar, dataReferencia));
    }

    public void criarNovasContasParceladas(Date dataReferencia) {
        Date dataMesAnterior = jurixDateTime.subtrairMeses(dataReferencia, 1);

        Date dataInicioMesPassado = JurixDateTime.getStartOfDay(dataMesAnterior);
        Date dataFimMesPassado = JurixDateTime.getEndOfDay(dataMesAnterior);

        Date dataInicioAtual = JurixDateTime.getStartOfDay(dataReferencia);
        Date dataFimAtual = JurixDateTime.getEndOfDay(dataReferencia);

        List<ContasReceber> contasMensais = new ArrayList<>();

        contasMensais.addAll(contasReceberRepository.buscarcontasComParceladasMesAnterior(dataInicioMesPassado, dataFimMesPassado));
        contasMensais.addAll(contasReceberRepository.buscarcontasPrimeiraParcela(dataInicioAtual, dataFimAtual));

        contasMensais.forEach(contasPagar -> criarCopiaContaParcelada(contasPagar, dataReferencia));

    }

    public void criarNovasContasSemestrais(Date dataReferencia) {
        Date dataMesAnterior = jurixDateTime.subtrairMeses(dataReferencia, 6);
        List<ContasReceber> contasMensais = buscarcontasComRecorrencia(dataMesAnterior, EnumTipoContrato.SEMESTRAL);

        contasMensais.forEach(contasPagar -> criarCopiaContaContrato(contasPagar, dataReferencia));
    }

    public void criarNovasContasAnuais(Date dataReferencia) {
        Date dataMesAnterior = jurixDateTime.subtrairAnos(dataReferencia, 1);
        List<ContasReceber> contasMensais = buscarcontasComRecorrencia(dataMesAnterior, EnumTipoContrato.ANUAL);

        contasMensais.forEach(contasPagar -> criarCopiaContaContrato(contasPagar, dataReferencia));
    }

    private void addIncidenciaCentroCusto(Contrato contratoSalvo, ContasReceber contasContrato) {
        ContasReceberIncidencia contasReceberIncidencia = new ContasReceberIncidencia();
        contasReceberIncidencia.setCentroCusto(contratoSalvo.getCentroCusto());
        contasReceberIncidencia.setIncidencia(new BigDecimal(100));
        contasContrato.setIncidencias(Arrays.asList(contasReceberIncidencia));
    }

    private void criarCopiaContaContrato(ContasReceber contasReceberOrigem, Date dataReferencia) {

        ContasReceber novaConta = new ContasReceber();
        novaConta.setDataReceber(dataReferencia);
        novaConta.setDescricao(contasReceberOrigem.getDescricao());
        novaConta.setContrato(contasReceberOrigem.getContrato());
        novaConta.setPessoaConta(contasReceberOrigem.getPessoaConta());
        novaConta.setCliente(contasReceberOrigem.getCliente());
        novaConta.setValorReceber(contasReceberOrigem.getValorReceber());
        novaConta.setIncidencias(new ArrayList<>());

        ContasReceber novaContaCriada = createIfNotExistsContaContrato(novaConta, contasReceberOrigem.getId());
        criarCopiaIncidencias(contasReceberOrigem, novaContaCriada);

    }

    private void criarCopiaIncidencias(ContasReceber contasReceberOrigem, ContasReceber novaContaCriada) {
        if(Objects.nonNull(novaContaCriada)){
            List<ContasReceberIncidencia> incidencias = contasReceberIncidenciaRepository.findByContaReceberId(contasReceberOrigem.getId());
            incidencias.forEach(incidenciaOrigem -> {
                ContasReceberIncidencia novaIncidencia = new ContasReceberIncidencia();
                novaIncidencia.setCentroCusto(incidenciaOrigem.getCentroCusto());
                novaIncidencia.setContaReceberId(novaContaCriada.getId());
                novaIncidencia.setIncidencia(incidenciaOrigem.getIncidencia());
                novaIncidencia.setValor(incidenciaOrigem.getValor());

                contasReceberIncidenciaRepository.save(novaIncidencia);
            });
        }
    }

    private void criarCopiaContaParcelada(ContasReceber contasReceberOrigem, Date dataReferencia) {

        ContasReceber novaConta = new ContasReceber();
        novaConta.setDataReceber(dataReferencia);
        novaConta.setValorReceber(contasReceberOrigem.getValorParcela());

        novaConta.setDescricao(contasReceberOrigem.getDescricao());
        novaConta.setContrato(contasReceberOrigem.getContrato());
        novaConta.setPessoaConta(contasReceberOrigem.getPessoaConta());
        novaConta.setCliente(contasReceberOrigem.getCliente());

        novaConta.setPagamentoParcelado(contasReceberOrigem.getPagamentoParcelado());

        novaConta.setValorEntrada(contasReceberOrigem.getValorEntrada());
        novaConta.setDataEntrada(contasReceberOrigem.getDataEntrada());

        novaConta.setValorParcela(contasReceberOrigem.getValorParcela());
        novaConta.setQuantidadeParcelas(contasReceberOrigem.getQuantidadeParcelas());

        if(Objects.isNull(contasReceberOrigem.getNumeroParcela())){
            novaConta.setNumeroParcela(1);
        }else{
            novaConta.setNumeroParcela(contasReceberOrigem.getNumeroParcela() + 1);
        }

        novaConta.setIncidencias(new ArrayList<>());

        ContasReceber novaContaCriada = createIfNotExistsContaParcelada(novaConta, contasReceberOrigem.getId());
        criarCopiaIncidencias(contasReceberOrigem, novaContaCriada);
    }

    private void validarContaAtrasaPossuiValorPago(ContasReceber contasReceber) {
        if (Objects.isNull(contasReceber.getValorReceber())) {
            throw new ContaAtrasadaSemValorPagoException();
        }

    }

    private void setValorRecebidoConta(ContasReceber contasReceber, ContasReceber contasReceberExistente) {

        if (jurixDateTime.isDataAtrasa(contasReceberExistente.getDataReceber())) {

            validarContaAtrasaPossuiValorPago(contasReceber);

            contasReceberExistente.setValorRecebido(contasReceber.getValorRecebido());
        } else {
            contasReceberExistente.setValorRecebido(contasReceberExistente.getValorReceber());
        }
    }

    private void incluirParametrosContasReceber(List<ContasReceber> contasReceber) {

        contasReceber.forEach(contaPagar -> {

            contaPagar.setIsAtrasado(Boolean.FALSE);
            contaPagar.setAVencer(Boolean.FALSE);

            contaPagar.getIncidencias().forEach(incidencia -> {
                incidencia.getId();
            });

            if (jurixDateTime.isDataAtrasa(contaPagar.getDataReceber())) {
                contaPagar.setIsAtrasado(Boolean.TRUE);
            } else {
                contaPagar.setAVencer(JurixDateTime.getQuantidadeDiasEntreData(jurixDateTime.getCurrentDateTime(), contaPagar.getDataReceber()) <= prazoDiasConta);
            }

        });
    }

    private void setPessoaConta(ContasReceber contasReceber) {
        contasReceber.setPessoaConta(pessoaContaBO.createIfNotExists(contasReceber.getPessoaConta()));
    }

    private boolean notExistsContaIgualParaMesmoDiaContaContrato(ContasReceber contasReceber, Long contaCentrosCustoId) {

        Date dataInicio = JurixDateTime.getStartOfDay(contasReceber.getDataReceber());
        Date dataFim = JurixDateTime.getEndOfDay(contasReceber.getDataReceber());

        return CollectionUtils.isEmpty(contasReceberRepository.buscarcontasPeriodoPorDescricaoECentroCusto(dataInicio, dataFim, contasReceber.getContrato().getTipoContrato(), contasReceber.getDescricao(), contaCentrosCustoId));
    }

    private boolean notExistsContaIgualParaMesmoDiaContaParcelada(ContasReceber contasReceber, Long contaCentrosCustoId) {

        Date dataInicio = JurixDateTime.getStartOfDay(contasReceber.getDataReceber());
        Date dataFim = JurixDateTime.getEndOfDay(contasReceber.getDataReceber());

        return CollectionUtils.isEmpty(contasReceberRepository.buscarcontasParceladasPeriodoPorDescricaoECentroCusto(dataInicio, dataFim, contasReceber.getNumeroParcela(), contasReceber.getDescricao(), contaCentrosCustoId));
    }

    public List<ContasReceberIncidencia> buscarIncidencias(Long centroCustoId, List<Long> idsContas) {
        return contasReceberIncidenciaRepository.buscarIncidencias(centroCustoId, idsContas);
    }

    public ContasReceber buscarContaAtivaContrato(Long contratoId) {
        ContasReceber contasReceber = contasReceberRepository.buscarContaAtualContrato(contratoId);

        if(Objects.nonNull(contasReceber)) {
            Date dataAtual = jurixDateTime.getCurrentDateTime();
            if (contasReceber.getSituacao().equals(EnumSituacaoContaReceber.PENDENTE) && JurixDateTime.isMesmoMes(contasReceber.getDataReceber(), dataAtual)) {
                return contasReceber;
            }
        }
        return null;
    }

    public void removerContaAtivaContrato(Long contratoId) {

        ContasReceber contasReceber = buscarContaAtivaContrato(contratoId);
        if(Objects.nonNull(contasReceber)){
            salvarContaRemovida(contasReceber);
        }
    }

    public void ajsutarIncidencia() {
        List<ContasReceber> contas = contasReceberRepository.findAll();
        for (ContasReceber conta : contas) {
            salvarIncidencias(conta, conta.getIncidencias());
        }
    }
}

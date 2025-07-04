package br.com.jurix.financeiro.business;

import br.com.jurix.centrocusto.entity.CentroCusto;
import br.com.jurix.centrocusto.repository.CentroCustoRepository;
import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.filter.business.FiltroUsuarioBO;
import br.com.jurix.filter.entity.FiltroUsuario;
import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.financeiro.contaspagar.business.ContasPagarBO;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagarIncidencia;
import br.com.jurix.financeiro.contaspagar.entity.QContasPagar;
import br.com.jurix.financeiro.contaspagar.entity.QContasPagarIncidencia;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumSituacaoConta;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumTipoContasPagar;
import br.com.jurix.financeiro.contasreceber.business.ContasReceberBO;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceber;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceberIncidencia;
import br.com.jurix.financeiro.contasreceber.entity.QContasReceber;
import br.com.jurix.financeiro.contasreceber.entity.QContasReceberIncidencia;
import br.com.jurix.financeiro.dto.FinanceiroDTO;
import br.com.jurix.financeiro.filter.FinanceiroFilter;
import br.com.jurix.math.utils.JurixMathUtils;
import com.google.common.base.Strings;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FinanceiroBO {

    @Autowired
    private ContasReceberBO contasReceberBO;

    @Autowired
    private ContasPagarBO contasPagarBO;

    @Autowired
    private FiltroUsuarioBO filtroUsuarioBO;


    @Autowired
    private CentroCustoRepository centroCustoRepository;

    @Autowired
    private JurixDateTime jurixDateTime;

    public FinanceiroDTO buscarDadosFinanceiros(Long centroCustoId, FinanceiroFilter financeiroFilter){

        financeiroFilter.setCentroCustoId(centroCustoId);

        FinanceiroDTO financeiroDTO = new FinanceiroDTO();

        financeiroDTO.setContasPagar(contasPagarBO.buscarcontasPagar(contruirBooleanExpressionContasPagar(financeiroFilter)));
        financeiroDTO.setContasReceber(contasReceberBO.buscarcontasReceber(contruirBooleanExpressionContasReceber(financeiroFilter)));

        setTotaisContasPagar(financeiroDTO, centroCustoId);
        setTotaisReceberPagar(financeiroDTO, centroCustoId);

        return financeiroDTO;
    }

    public void marcarCentroCustoPadrao(Long centroCustoId){

        FiltroUsuario filtroUsuario = buscarFiltroCentroCustoRecente();
        filtroUsuario.setValor(centroCustoId.toString());

        if(Objects.nonNull(filtroUsuario.getId())){
            filtroUsuarioBO.update(filtroUsuario);
        }else{
            filtroUsuarioBO.save(filtroUsuario);
        }

    }

    public CentroCusto buscarCentroCustoRecente() {

        FiltroUsuario filtroUsuario = buscarFiltroCentroCustoRecente();
        if(Objects.nonNull(filtroUsuario.getId())){
            Long centroId = Long.valueOf(filtroUsuario.getValor());
            return centroCustoRepository.findOne(centroId);
        }
        return centroCustoRepository.findTopByOrderByIdDesc();
    }

    private FiltroUsuario buscarFiltroCentroCustoRecente(){
        FiltroUsuario filtroUsuario = filtroUsuarioBO.buscarPadrao(TipoFiltroUsuarioEnum.CENTRO_CUSTO_RECENTE);
        if (Objects.isNull(filtroUsuario)) {
            filtroUsuario = new FiltroUsuario();
            filtroUsuario.setTipo(TipoFiltroUsuarioEnum.CENTRO_CUSTO_RECENTE);
            filtroUsuario.setNome("Filtro Centro Custo Recente");
            filtroUsuario.setPadrao(Boolean.TRUE);
        }
        return filtroUsuario;
    }

    private void setTotaisReceberPagar(FinanceiroDTO financeiroDTO, Long centroCustoId){

        List<Long> idsContas = financeiroDTO.getContasReceber().stream().map(ContasReceber::getId).collect(Collectors.toList());
        Map<Long, ContasReceberIncidencia> incidencias = getIncidenciasContasReceber(centroCustoId, idsContas);

        BigDecimal totalContratosMensais = financeiroDTO.getContasReceber()
                .stream()
                .filter(conta-> Objects.nonNull(conta.getContrato()))
                .map(conta -> getValorContIncidencia(conta, incidencias))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalParcelado = financeiroDTO.getContasReceber()
                .stream()
                .filter(ContasReceber::getPagamentoParcelado)
                .map(conta -> getValorContIncidencia(conta, incidencias))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExtraordinario = financeiroDTO.getContasReceber()
                .stream()
                .filter(conta-> Objects.nonNull(conta.getContrato()) && Objects.nonNull(conta.getValorExtraordinario()))
                .map(ContasReceber::getValorExtraordinario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalContasReceber = financeiroDTO.getContasReceber()
                .stream()
                .map(conta -> getValorContIncidencia(conta, incidencias))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        financeiroDTO.setTotalContratosMensais(totalContratosMensais);
        financeiroDTO.setTotalPagamentosParcelados(totalParcelado);
        financeiroDTO.setTotalPagamentosExtraordinarios(totalExtraordinario);
        financeiroDTO.setTotalContasReceber(totalContasReceber.add(totalExtraordinario));

    }

    private void setTotaisContasPagar(FinanceiroDTO financeiroDTO, Long centroCustoId){


        List<Long> idsContas = financeiroDTO.getContasPagar().stream().map(ContasPagar::getId).collect(Collectors.toList());
        Map<Long, ContasPagarIncidencia> incidencias = getIncidenciasContasPagar(centroCustoId, idsContas);

        BigDecimal totalDespesasFixas = financeiroDTO.getContasPagar()
                .stream()
                .filter(conta->conta.getTipo().equals(EnumTipoContasPagar.FIXA))
                .map(conta -> getValorContIncidencia(conta, incidencias))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesasVariaveis = financeiroDTO.getContasPagar()
                .stream()
                .filter(conta->conta.getTipo().equals(EnumTipoContasPagar.VARIAVEL))
                .map(conta -> getValorContIncidencia(conta, incidencias))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalJurosMultas = financeiroDTO.getContasPagar()
                .stream()
                .filter(conta->conta.getSituacao().equals(EnumSituacaoConta.PAGA))
                .map(conta -> conta.getValorPago().subtract(conta.getValor()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalContasPagar = financeiroDTO.getContasPagar()
                .stream()
                .map(conta -> getValorContIncidencia(conta, incidencias))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        financeiroDTO.setTotalDespesasFixas(JurixMathUtils.arredondar(totalDespesasFixas));
        financeiroDTO.setTotalDespesaVariaveis(JurixMathUtils.arredondar(totalDespesasVariaveis));
        financeiroDTO.setTotalJurosEMultasDespesas(JurixMathUtils.arredondar(totalJurosMultas));
        financeiroDTO.setTotalContasPagar(JurixMathUtils.arredondar(totalContasPagar));
    }

    private BigDecimal getValorContIncidencia(ContasPagar contaPagar, Map<Long, ContasPagarIncidencia> incidencias) {
        return incidencias.get(contaPagar.getId()).getValor();
    }

    private BigDecimal getValorContIncidencia(ContasReceber contasReceber, Map<Long, ContasReceberIncidencia> incidencias) {
        return incidencias.get(contasReceber.getId()).getValor();
    }

    private Map<Long, ContasPagarIncidencia> getIncidenciasContasPagar(Long centroCustoId, List<Long> idsContas) {

        if(CollectionUtils.isNotEmpty(idsContas)) {
            List<ContasPagarIncidencia> incidencias =  contasPagarBO.buscarIncidencias(centroCustoId, idsContas);
            return incidencias.stream().collect(
                    Collectors.toMap(ContasPagarIncidencia::getContaPagarId, contasPagarIncidencia -> contasPagarIncidencia));
        }
        return new HashMap<>();
    }

    private Map<Long, ContasReceberIncidencia> getIncidenciasContasReceber(Long centroCustoId, List<Long> idsContas) {

        if(CollectionUtils.isNotEmpty(idsContas)) {
            List<ContasReceberIncidencia> incidencias =  contasReceberBO.buscarIncidencias(centroCustoId, idsContas);
            return incidencias.stream().collect(
                    Collectors.toMap(ContasReceberIncidencia::getContaReceberId,contasReceberIncidencia -> contasReceberIncidencia));
        }
        return new HashMap<>();
    }



    private BooleanExpression contruirBooleanExpressionContasPagar(FinanceiroFilter financeiroFilter){


        JPQLQuery contasPagar = JPAExpressions.select(QContasPagarIncidencia.contasPagarIncidencia.contaPagarId)
                .from(QContasPagarIncidencia.contasPagarIncidencia)
                .where(QContasPagarIncidencia.contasPagarIncidencia.centroCusto.id.eq(financeiroFilter.getCentroCustoId()));


        if(!Strings.isNullOrEmpty(financeiroFilter.getTipoCompartilhado())){
            if("COMPARTILHADOS".equals(financeiroFilter.getTipoCompartilhado())){
                contasPagar = (JPQLQuery) contasPagar.where(QContasPagarIncidencia.contasPagarIncidencia.incidencia.ne(BigDecimal.valueOf(100l)));
            }else{
                contasPagar = (JPQLQuery) contasPagar.where(QContasPagarIncidencia.contasPagarIncidencia.incidencia.eq(BigDecimal.valueOf(100l)));
            }
        }

        BooleanExpression mainBooleanExpression = QContasPagar.contasPagar.id.in(contasPagar);

        if(Objects.nonNull(financeiroFilter.getDiasVencimento())){

            Date agora = jurixDateTime.getCurrentDateTime();
            Date ontemFinalDia = JurixDateTime.getEndOfDay(JurixDateTime.subtrairDias(agora, 1));
            Date diasVencimentoInicioDia = JurixDateTime.getStartOfDay(JurixDateTime.adicionarDias(agora, financeiroFilter.getDiasVencimento() + 1));

            mainBooleanExpression = mainBooleanExpression.and(QContasPagar.contasPagar.dataVencimento.between(ontemFinalDia, diasVencimentoInicioDia));
        }

        if(Objects.nonNull(financeiroFilter.getDataInicio()) && Objects.nonNull(financeiroFilter.getDataFim())){
            mainBooleanExpression = mainBooleanExpression.and(financeiroFilter.getDatasExpressionContasPagar());
        }


        mainBooleanExpression = mainBooleanExpression.and(QContasPagar.contasPagar.dataVencimento.month().eq(financeiroFilter.getMes()));
        mainBooleanExpression = mainBooleanExpression.and(QContasPagar.contasPagar.dataVencimento.year().eq(financeiroFilter.getAno()));

        return mainBooleanExpression;
    }

    private BooleanExpression contruirBooleanExpressionContasReceber(FinanceiroFilter financeiroFilter){


        JPQLQuery contasReceber = JPAExpressions.select(QContasReceberIncidencia.contasReceberIncidencia.contaReceberId).from(QContasReceberIncidencia.contasReceberIncidencia).where(QContasReceberIncidencia.contasReceberIncidencia.centroCusto.id.eq(financeiroFilter.getCentroCustoId()));
        BooleanExpression mainBooleanExpression = QContasReceber.contasReceber.id.in(contasReceber);

        if(Objects.nonNull(financeiroFilter.getDiasVencimento())) {

            Date agora = jurixDateTime.getCurrentDateTime();
            Date ontemFinalDia = JurixDateTime.getEndOfDay(JurixDateTime.subtrairDias(agora, 1));
            Date diasVencimentoInicioDia = JurixDateTime.getStartOfDay(JurixDateTime.adicionarDias(agora, financeiroFilter.getDiasVencimento() + 1));

            mainBooleanExpression = mainBooleanExpression.and(QContasReceber.contasReceber.dataReceber.between(ontemFinalDia, diasVencimentoInicioDia));
        }

        if(Objects.nonNull(financeiroFilter.getDataInicio()) && Objects.nonNull(financeiroFilter.getDataFim())){
            mainBooleanExpression = mainBooleanExpression.and(financeiroFilter.getDatasExpressionContasReceber());
        }

        mainBooleanExpression = mainBooleanExpression.and(QContasReceber.contasReceber.dataReceber.month().eq(financeiroFilter.getMes()));
        mainBooleanExpression = mainBooleanExpression.and(QContasReceber.contasReceber.dataReceber.year().eq(financeiroFilter.getAno()));

        return mainBooleanExpression;
    }
}

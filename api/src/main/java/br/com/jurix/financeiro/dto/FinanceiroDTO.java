package br.com.jurix.financeiro.dto;

import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceber;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class FinanceiroDTO {

    private List<ContasPagar> contasPagar;

    private List<ContasReceber> contasReceber;

    private BigDecimal totalDespesasFixas;

    private BigDecimal totalDespesaVariaveis;

    private BigDecimal totalJurosEMultasDespesas;

    private BigDecimal totalContasPagar;

    private BigDecimal totalContratosMensais;

    private BigDecimal totalPagamentosParcelados;

    private BigDecimal totalPagamentosExtraordinarios;

    private BigDecimal totalContasReceber;


}

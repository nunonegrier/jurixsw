package br.com.jurix.financeiro.filter;

import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.financeiro.contaspagar.entity.QContasPagar;
import br.com.jurix.financeiro.contasreceber.entity.QContasReceber;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FinanceiroFilter extends SortPaginatorFilter {

    private Integer mes;
    private Integer ano;
    private Integer diasVencimento;
    private Long centroCustoId;
    private String tipoCompartilhado;

    public BooleanExpression getDatasExpressionContasPagar(){
        Date[] intervalo = converterFiltroIntervaloData(this.getDataInicio(), this.getDataFim());
        Date dataInicio = intervalo[0];
        Date dataFim = intervalo[1];
        return QContasPagar.contasPagar.dataVencimento.between(dataInicio, dataFim);
    }

    public BooleanExpression getDatasExpressionContasReceber(){
        Date[] intervalo = converterFiltroIntervaloData(this.getDataInicio(), this.getDataFim());
        Date dataInicio = intervalo[0];
        Date dataFim = intervalo[1];
        return QContasReceber.contasReceber.dataReceber.between(dataInicio, dataFim);
    }

}

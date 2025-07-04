package br.com.jurix.processo.filter;

import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.pautaevento.entity.QPautaEvento;
import br.com.jurix.processo.entity.QParteProcesso;
import br.com.jurix.processo.entity.QProcesso;
import br.com.jurix.processo.enumeration.EnumAreaProcesso;
import br.com.jurix.processo.enumeration.EnumInstanciaProcesso;
import br.com.jurix.processo.enumeration.EnumSituacaoProcesso;
import br.com.jurix.processo.enumeration.EnumTipoProcesso;
import com.google.common.base.Strings;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProcessoFilter extends SortPaginatorFilter {

    public void setIds(List<Long> ids){
        if(CollectionUtils.isNotEmpty(ids)) {
            addToMainBooleanExpression(QProcesso.processo.id.in(ids));
        }
    }

    public void setClienteId(String clienteId){
        if(!Strings.isNullOrEmpty(clienteId)) {
            addToMainBooleanExpression(QProcesso.processo.contrato.cliente.id.eq(Long.valueOf(clienteId)));
        }
    }


    public void setContratoId(String contratoId){
        if(!Strings.isNullOrEmpty(contratoId)) {
            addToMainBooleanExpression(QProcesso.processo.contrato.id.eq(Long.valueOf(contratoId)));
        }
    }

    public void setProcessoPaiId(String processoPaiId){
        if(!Strings.isNullOrEmpty(processoPaiId)) {
            addToMainBooleanExpression(QProcesso.processo.processoVinculadoField.id.eq(Long.valueOf(processoPaiId)));
        }
    }

    public void setNumero(String numero){
        if(!Strings.isNullOrEmpty(numero)) {
            addToMainBooleanExpression(QProcesso.processo.numero.containsIgnoreCase(numero));
        }
    }

    public void setNumeroDescricao(String numeroDescricao){
        if(!Strings.isNullOrEmpty(numeroDescricao)) {
            BooleanExpression expression = QProcesso.processo.numero.containsIgnoreCase(numeroDescricao);
            expression = expression.or(QProcesso.processo.descricao.containsIgnoreCase(numeroDescricao));
            expression = expression.or(QProcesso.processo.contrato.cliente.nome.containsIgnoreCase(numeroDescricao));
            addToMainBooleanExpression(expression);
        }
    }

    public void setNomeParte(String nomeParte){
        if(!Strings.isNullOrEmpty(nomeParte)) {
            QParteProcesso qParteProcesso = QParteProcesso.parteProcesso;
            JPQLQuery sub = JPAExpressions.select(qParteProcesso.processo.id).from(qParteProcesso).where(qParteProcesso.nome.containsIgnoreCase(nomeParte));
            addToMainBooleanExpression(QProcesso.processo.id.in(sub));
        }
    }

    public void setPossuiPauta(String possuiPauta){
        if(!Strings.isNullOrEmpty(possuiPauta)) {
            JPQLQuery processosComPauta = JPAExpressions.select(QPautaEvento.pautaEvento.processo.id).from(QPautaEvento.pautaEvento);
            addToMainBooleanExpression(QProcesso.processo.id.in(processosComPauta));
        }
    }

    public void setSituacao(String situacao){
        if(!Strings.isNullOrEmpty(situacao)) {
            addToMainBooleanExpression(QProcesso.processo.situacao.eq(EnumSituacaoProcesso.valueOf(situacao)));
        }
    }

    public void setProcessoTipo(String processoTipo){
        if(!Strings.isNullOrEmpty(processoTipo)) {
            addToMainBooleanExpression(QProcesso.processo.tipo.eq(EnumTipoProcesso.valueOf(processoTipo)));
        }
    }

    public void setProcessoArea(String processoArea){
        if(!Strings.isNullOrEmpty(processoArea)) {
            addToMainBooleanExpression(QProcesso.processo.area.eq(EnumAreaProcesso.valueOf(processoArea)));
        }
    }

    public void setProcessoTipoAcao(String processoTipoAcao){
        if(!Strings.isNullOrEmpty(processoTipoAcao)) {
            addToMainBooleanExpression(QProcesso.processo.tipoAcao.id.eq(Long.valueOf(processoTipoAcao)));
        }
    }

    public void setAnoDistribuicao(String anoDistribuicao){
        if(!Strings.isNullOrEmpty(anoDistribuicao)) {
            addToMainBooleanExpression(QProcesso.processo.dataDistribuicao.year().eq(Integer.valueOf(anoDistribuicao)));
        }
    }

    public void setForo(String foro){
        if(!Strings.isNullOrEmpty(foro)) {
            addToMainBooleanExpression(QProcesso.processo.foro.containsIgnoreCase(foro));
        }
    }

    public void setVara(String vara){
        if(!Strings.isNullOrEmpty(vara)) {
            addToMainBooleanExpression(QProcesso.processo.vara.containsIgnoreCase(vara));
        }
    }

    public void setProcessoInstancia(String processoInstancia){
        if(!Strings.isNullOrEmpty(processoInstancia)) {
            addToMainBooleanExpression(QProcesso.processo.instancia.eq(EnumInstanciaProcesso.valueOf(processoInstancia)));
        }
    }

    public void setProcessoValor(String processoValor){
        if(!Strings.isNullOrEmpty(processoValor)) {
            addToMainBooleanExpression(QProcesso.processo.valorAcao.eq(new BigDecimal(processoValor)));
        }
    }

    public void setCentroCustoId(String centroCustoId){
        if(!Strings.isNullOrEmpty(centroCustoId)) {
            addToMainBooleanExpression(QProcesso.processo.centroCusto.id.eq(Long.valueOf(centroCustoId)));
        }
    }

    public void setEstadoId(String estadoId){
        if(!Strings.isNullOrEmpty(estadoId)) {
            addToMainBooleanExpression(QProcesso.processo.comarca.estado.id.eq(Long.valueOf(estadoId)));
        }
    }

    public void setComarcaId(String comarcaId){
        if(!Strings.isNullOrEmpty(comarcaId)) {
            addToMainBooleanExpression(QProcesso.processo.comarca.id.eq(Long.valueOf(comarcaId)));
        }
    }

    public void setNaoRemovido(){
        addToMainBooleanExpression(QProcesso.processo.removido.isFalse());
    }
}

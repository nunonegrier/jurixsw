package br.com.jurix.processo.filter;

import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.pautaevento.entity.QPautaEvento;
import br.com.jurix.processo.entity.QParteProcesso;
import br.com.jurix.processo.enumeration.EnumTipoParte;
import com.google.common.base.Strings;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

public class ParteProcessoFilter extends SortPaginatorFilter{

    public void setParteEmProcessoComPauta(String parteEmProcessoComPauta){

        if(!Strings.isNullOrEmpty(parteEmProcessoComPauta)) {
            JPQLQuery processosComPautaId = JPAExpressions.select(QPautaEvento.pautaEvento.processo.id).from(QPautaEvento.pautaEvento);
            addToMainBooleanExpression(QParteProcesso.parteProcesso.processo.id.in(processosComPautaId));
        }
    }

    public void setTipoParte(String tipoParte){
        if(!Strings.isNullOrEmpty(tipoParte)) {
            addToMainBooleanExpression(QParteProcesso.parteProcesso.tipo.eq(EnumTipoParte.valueOf(tipoParte)));
        }
    }
}

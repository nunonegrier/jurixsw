package br.com.jurix.colaborador.filter;

import br.com.jurix.colaborador.entity.QColaborador;
import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.pautaevento.entity.QPautaEvento;
import br.com.jurix.security.enumeration.EnumSituacaoUsuario;
import com.google.common.base.Strings;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;

import java.util.Objects;

public class ColaboradorFilter extends SortPaginatorFilter {

    public void setNome(String nome) {
        if (Objects.nonNull(nome)) {
            addToMainBooleanExpression(QColaborador.colaborador.usuario.nome.containsIgnoreCase(nome));
        }
    }

    public void setExibirRemovidos() {
        addToMainBooleanExpression(QColaborador.colaborador.usuario.situacao.eq(EnumSituacaoUsuario.ATIVO));
    }

    public void setUsuarioId(String usuarioId) {
        if (Objects.nonNull(usuarioId)) {
            addToMainBooleanExpression(QColaborador.colaborador.usuario.id.eq(Long.valueOf(usuarioId)));
        }
    }

    public void setResponsavelPauta(String responsavelPauta) {

        if (!Strings.isNullOrEmpty(responsavelPauta)) {
            JPQLQuery idColaboradorPauta = JPAExpressions.select(QPautaEvento.pautaEvento.colaborador.id).from(QPautaEvento.pautaEvento);
            addToMainBooleanExpression(QColaborador.colaborador.id.in(idColaboradorPauta));
        }
    }

    public void setPerfil(Long perfil) {
        addToMainBooleanExpression(QColaborador.colaborador.usuario.perfil.id.eq(perfil));
    }

}

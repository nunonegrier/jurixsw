package br.com.jurix.filter.filter;

import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.filter.entity.QFiltroUsuario;
import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.security.entity.Usuario;
import com.google.common.base.Strings;

public class FiltroUsuarioFilter extends SortPaginatorFilter {

    public void setTipoFiltro(String tipoFiltro) {
        if (!Strings.isNullOrEmpty(tipoFiltro)) {
            addToMainBooleanExpression(QFiltroUsuario.filtroUsuario.tipo.eq(TipoFiltroUsuarioEnum.valueOf(tipoFiltro)));
        }
    }

    public void setNomeDescricao(String nomeDescricao) {
        if (!Strings.isNullOrEmpty(nomeDescricao)) {
            addToMainBooleanExpression(QFiltroUsuario.filtroUsuario.nome.containsIgnoreCase(nomeDescricao).or(QFiltroUsuario.filtroUsuario.descricao.containsIgnoreCase(nomeDescricao)));
        }
    }

    public void setUsuario(Usuario usuario){
        addToMainBooleanExpression(QFiltroUsuario.filtroUsuario.usuarioCriacao.id.eq(usuario.getId()));
    }

    public void setNaoRemovido() {
        addToMainBooleanExpression(QFiltroUsuario.filtroUsuario.removido.isFalse());
    }
}

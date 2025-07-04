package br.com.jurix.filter.repository;

import br.com.jurix.filter.entity.FiltroUsuario;
import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FiltroUsuarioRepository extends JoinCapableRepository<FiltroUsuario, Long> {

    @Query("SELECT fp FROM FiltroUsuario fp WHERE fp.usuarioCriacao.id = :usuarioId and fp.tipo = :tipo and fp.removido is false")
    public List<FiltroUsuario> buscarPorUsuarioETipoFiltro(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoFiltroUsuarioEnum tipoFiltro);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE FiltroUsuario fp SET fp.padrao = false WHERE fp.usuarioCriacao.id = :usuarioId and fp.tipo = :tipo")
    public void atribuirPadraoFalse(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoFiltroUsuarioEnum tipo);

    @Query("SELECT fp FROM FiltroUsuario fp WHERE fp.usuarioCriacao.id = :usuarioId and fp.usuarioCriacao.id = :usuarioId and fp.tipo = :tipo and fp.removido is false and fp.padrao is true")
    FiltroUsuario buscarPadrao(@Param("usuarioId") Long usuarioId, @Param("tipo") TipoFiltroUsuarioEnum tipo);
}

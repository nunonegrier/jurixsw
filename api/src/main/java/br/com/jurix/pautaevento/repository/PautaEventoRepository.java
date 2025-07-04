package br.com.jurix.pautaevento.repository;

import br.com.jurix.pautaevento.entity.PautaEvento;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PautaEventoRepository extends JoinCapableRepository<PautaEvento, Long> {

    @Query("SELECT pe FROM PautaEvento pe JOIN FETCH pe.colaborador co JOIN FETCH pe.usuarioCriacao us JOIN FETCH co.usuario usco LEFT JOIN FETCH pe.processo WHERE pe.id = ?1")
    PautaEvento buscarPorId(Long id);

    @Query("SELECT pe FROM PautaEvento pe WHERE pe.situacao = 'PENDENTE' AND pe.dataLimite < :dataReferencia ")
    List<PautaEvento> buscarEventosPendentesVencidos(@Param("dataReferencia") Date dataReferencia);

    @Query("SELECT COUNT(pe) FROM PautaEvento pe WHERE (pe.situacao = 'FALHOU' OR  pe.situacao = 'ATRASADO' OR (pe.situacao = 'PENDENTE' AND pe.dataLimite < :dataLimite)) AND (pe.colaborador.usuario.id = :usuarioId OR pe.usuarioCriacao.id = :usuarioId)")
    public Integer countEventosAtrasadosPertoVencimento(@Param("dataLimite") Date dataLimite, @Param("usuarioId") Long usuarioId );
}

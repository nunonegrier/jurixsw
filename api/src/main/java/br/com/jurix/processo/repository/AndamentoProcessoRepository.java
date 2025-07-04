package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.AndamentoProcesso;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AndamentoProcessoRepository extends JoinCapableRepository<AndamentoProcesso, Long> {

    @Query("SELECT ap FROM AndamentoProcesso ap JOIN FETCH ap.tipoAndamento JOIN FETCH ap.processo LEFT JOIN FETCH ap.pautaEvento WHERE ap.id = :id")
    public AndamentoProcesso buscarPorId(@Param("id") Long id);

    @Query("SELECT ap FROM AndamentoProcesso ap " +
            "JOIN FETCH ap.tipoAndamento " +
            "JOIN FETCH ap.processo pr " +
            "LEFT JOIN FETCH ap.resultadoProcesso " +
            "LEFT JOIN FETCH ap.pautaEvento pe " +
            "LEFT JOIN FETCH pe.usuarioCriacao " +
            "LEFT JOIN FETCH pe.colaborador " +
            "WHERE pr.id = :idProcesso AND ap.removido IS FALSE " +
            "ORDER BY ap.data")
    List<AndamentoProcesso> buscarPorProcesso(@Param("idProcesso") Long idProcesso);

}

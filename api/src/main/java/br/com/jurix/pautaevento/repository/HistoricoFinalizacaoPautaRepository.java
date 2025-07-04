package br.com.jurix.pautaevento.repository;

import br.com.jurix.pautaevento.entity.HistoricoFinalizacaoPauta;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoricoFinalizacaoPautaRepository extends JoinCapableRepository<HistoricoFinalizacaoPauta, Long> {


    @Query("SELECT his FROM HistoricoFinalizacaoPauta  his WHERE his.pautaEvento.id = :pautaEventoId ORDER BY his.dataFinalizacao ASC")
    public List<HistoricoFinalizacaoPauta> buscarPorEvento(@Param("pautaEventoId") Long pautaEventoId);
}

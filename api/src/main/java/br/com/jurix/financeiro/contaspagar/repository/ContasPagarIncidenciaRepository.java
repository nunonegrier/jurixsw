package br.com.jurix.financeiro.contaspagar.repository;

import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagarIncidencia;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumFrequenciaContasPagar;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ContasPagarIncidenciaRepository extends JoinCapableRepository<ContasPagarIncidencia, Long> {


    @Query("SELECT cpi FROM ContasPagarIncidencia cpi WHERE cpi.centroCusto.id = :centroCustoId AND cpi.contaPagarId IN (:idsContas)")
    List<ContasPagarIncidencia> buscarIncidencias(@Param("centroCustoId") Long centroCustoId, @Param("idsContas") List<Long> idsContas);

    List<ContasPagarIncidencia> findByContaPagarId(Long contaPagarId);

    void deleteAllByContaPagarIdAndIdNotIn(Long contaPagarId, List<Long> ids);
}

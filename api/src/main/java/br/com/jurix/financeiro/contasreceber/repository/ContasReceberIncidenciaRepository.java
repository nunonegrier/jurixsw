package br.com.jurix.financeiro.contasreceber.repository;

import br.com.jurix.financeiro.contasreceber.entity.ContasReceberIncidencia;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContasReceberIncidenciaRepository extends JoinCapableRepository<ContasReceberIncidencia, Long> {


    @Query("SELECT cri FROM ContasReceberIncidencia cri WHERE cri.centroCusto.id = :centroCustoId AND cri.contaReceberId IN (:idsContas)")
    List<ContasReceberIncidencia> buscarIncidencias(@Param("centroCustoId") Long centroCustoId, @Param("idsContas") List<Long> idsContas);

    List<ContasReceberIncidencia> findByContaReceberId(Long contaReceberId);

    void deleteAllByContaReceberIdAndIdNotIn(Long contaReceberId, List<Long> ids);
}

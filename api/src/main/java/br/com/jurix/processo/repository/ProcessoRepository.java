package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.Processo;
import br.com.jurix.processo.enumeration.EnumSituacaoProcesso;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessoRepository extends JoinCapableRepository<Processo, Long> {

    @Query("SELECT p FROM Processo p JOIN FETCH p.contrato co JOIN FETCH co.cliente cli JOIN FETCH p.tipoAcao JOIN FETCH p.comarca com JOIN FETCH com.estado JOIN FETCH p.centroCusto LEFT JOIN FETCH p.processoVinculadoField WHERE p.id = :processoId")
    public Processo buscarPorId(@Param("processoId") Long processoId);

    public Integer countBySituacaoAndRemovidoFalse(EnumSituacaoProcesso situacaoProcesso);

    public Processo findOneByNumero(String numero);
}

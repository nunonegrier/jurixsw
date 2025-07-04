package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.ParteProcesso;
import br.com.jurix.processo.enumeration.EnumTipoParte;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParteProcessoRepository extends JoinCapableRepository<ParteProcesso, Long> {

    @Query("SELECT pp FROM ParteProcesso pp LEFT JOIN FETCH pp.posicaoParte WHERE pp.processo.id = :processoId AND pp.tipo = :tipo AND pp.removido IS FALSE")
    public List<ParteProcesso> buscarPorProcessoETipo(@Param("processoId") Long processoId, @Param("tipo") EnumTipoParte tipo);

    @Query("SELECT pp FROM ParteProcesso pp WHERE pp.id = (SELECT MIN(ppsub.id) FROM ParteProcesso ppsub WHERE ppsub.processo.id = :processoId AND ppsub.tipo = :tipo AND ppsub.removido IS FALSE)")
    public ParteProcesso primeiraParteProcesso(@Param("processoId") Long processoId, @Param("tipo") EnumTipoParte tipo);
}

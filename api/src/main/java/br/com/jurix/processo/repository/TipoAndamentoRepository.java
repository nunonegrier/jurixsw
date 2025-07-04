package br.com.jurix.processo.repository;


import br.com.jurix.processo.entity.TipoAndamento;
import br.com.jurix.processo.enumeration.EnumFinalidadeTipoAndamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tipoAndamento", path = "tiposAndamento")
public interface TipoAndamentoRepository extends PagingAndSortingRepository<TipoAndamento, Long> {

    @Query("SELECT ta FROM TipoAndamento ta WHERE ta.finalidade = :finalidade AND ta.removido IS FALSE ORDER BY LOWER(ta.descricao)")
    Page<TipoAndamento> findByFinalidade(@Param("finalidade") EnumFinalidadeTipoAndamento finalidade, Pageable page);
}

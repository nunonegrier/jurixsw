package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.TipoAcao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tipoAcao", path = "tiposAcao")
public interface TipoAcaoRepository extends PagingAndSortingRepository<TipoAcao, Long> {

    @Query("SELECT ta FROM TipoAcao ta WHERE ta.removido is false ORDER BY LOWER(ta.descricao)")
    Page<TipoAcao> findAll(Pageable page);
}

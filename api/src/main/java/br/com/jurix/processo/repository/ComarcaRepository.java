package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.Comarca;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "comarca", path = "comarcas")
public interface ComarcaRepository extends PagingAndSortingRepository<Comarca, Long> {

    @Query("SELECT co FROM Comarca co JOIN co.estado es WHERE es.id = :idEstado ORDER BY co.descricao")
    Page<Comarca> findByEstado(@Param("idEstado") Long idEstado, Pageable page);
}

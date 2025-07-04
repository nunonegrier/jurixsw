package br.com.jurix.localidade.repository;

import br.com.jurix.localidade.entity.Municipio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "municipio", path = "municipios")
public interface MunicipioRepository extends PagingAndSortingRepository<Municipio, Long> {

    @Query("SELECT mu FROM Municipio mu JOIN mu.estado es WHERE es.id = :idEstado ORDER BY mu.nome")
    Page<Municipio> findByEstado(@Param("idEstado") Long idEstado, Pageable page);
}

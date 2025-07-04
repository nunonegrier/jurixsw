package br.com.jurix.localidade.repository;

import br.com.jurix.localidade.entity.Estado;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "estado", path = "estados")
public interface EstadoRepository extends PagingAndSortingRepository<Estado, Long> {
}

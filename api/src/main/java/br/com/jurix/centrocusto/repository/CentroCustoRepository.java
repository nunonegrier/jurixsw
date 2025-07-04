package br.com.jurix.centrocusto.repository;

import br.com.jurix.centrocusto.entity.CentroCusto;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "centroCusto", path = "centrosCusto")
public interface CentroCustoRepository extends PagingAndSortingRepository<CentroCusto, Long> {

    CentroCusto findTopByOrderByIdDesc();
}

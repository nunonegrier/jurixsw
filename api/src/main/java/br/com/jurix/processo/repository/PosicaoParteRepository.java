package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.PosicaoParte;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "posicaoParte", path = "posicoesParte")
public interface PosicaoParteRepository extends PagingAndSortingRepository<PosicaoParte, Long> {
}

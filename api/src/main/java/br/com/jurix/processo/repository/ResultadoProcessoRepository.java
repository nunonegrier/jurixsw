package br.com.jurix.processo.repository;

import br.com.jurix.processo.entity.ResultadoProcesso;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "resultadoProcesso", path = "resultadosProcesso")
public interface ResultadoProcessoRepository extends PagingAndSortingRepository<ResultadoProcesso, Long> {
}

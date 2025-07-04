package br.com.jurix.parametro.repository;

import br.com.jurix.parametro.entity.Parametro;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "parametro", path = "parametros")
public interface ParametroRepository extends PagingAndSortingRepository<Parametro, Long> {

    public List<Parametro> findByChave(@Param("chave") String chave);

    public Parametro findOneByChave(String chave);
}
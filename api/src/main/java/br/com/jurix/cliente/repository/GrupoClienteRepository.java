package br.com.jurix.cliente.repository;

import br.com.jurix.cliente.entity.GrupoCliente;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "grupoCliente", path = "gruposCliente")
public interface GrupoClienteRepository extends PagingAndSortingRepository<GrupoCliente, Long> {


}

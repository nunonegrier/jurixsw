package br.com.jurix.cliente.repository;

import br.com.jurix.cliente.entity.Cliente;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository  extends JoinCapableRepository<Cliente, Long> {


    @Query("SELECT cli FROM Cliente cli JOIN FETCH cli.municipio mu JOIN FETCH mu.estado es LEFT JOIN FETCH cli.clienteFisico clf LEFT JOIN FETCH cli.clienteJuridico clj WHERE cli.id = :id ")
    public Cliente findOneWithMunicipio(@Param("id") Long id);

    public Integer countByRemovidoFalse();
}

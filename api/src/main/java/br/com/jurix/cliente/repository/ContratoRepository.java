package br.com.jurix.cliente.repository;

import br.com.jurix.cliente.entity.Contrato;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContratoRepository extends JoinCapableRepository<Contrato, Long> {

    @Query("SELECT con FROM Contrato con JOIN con.cliente cli WHERE cli.id = :idCliente AND con.removido IS FALSE")
    List<Contrato> findByCliente(@Param("idCliente") Long idCliente);
}

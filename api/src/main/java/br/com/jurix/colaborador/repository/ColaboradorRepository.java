package br.com.jurix.colaborador.repository;

import br.com.jurix.colaborador.entity.Colaborador;
import br.com.jurix.querydsql.repository.JoinCapableRepository;
import org.springframework.data.jpa.repository.Query;

public interface ColaboradorRepository extends JoinCapableRepository<Colaborador, Long> {

    @Query("SELECT co FROM Colaborador co JOIN FETCH co.usuario us JOIN FETCH us.perfil pf WHERE co.id = ?1")
    Colaborador buscarPorId(Long id);
}

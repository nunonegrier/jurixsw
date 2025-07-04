package br.com.jurix.security.repository;

import br.com.jurix.security.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    @Query("SELECT pf FROM Perfil pf JOIN  FETCH pf.permissoes WHERE pf.id = :id ")
    Perfil findById(@Param("id") Long id);

    @Query("SELECT pf FROM Perfil pf WHERE pf.removido IS FALSE")
    List<Perfil> findAllAtivos();
}

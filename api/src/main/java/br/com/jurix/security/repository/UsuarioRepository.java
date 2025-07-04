package br.com.jurix.security.repository;

import br.com.jurix.security.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    Usuario findByTokenRecuperacao(String tokenRecuperacao);

    Integer countByPerfil_Id(Long perfilId);
}

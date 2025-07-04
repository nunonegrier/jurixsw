package br.com.jurix.security.repository;

import br.com.jurix.security.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

}

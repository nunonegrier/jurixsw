package br.com.jurix.security.helper;

import br.com.jurix.security.entity.Usuario;
import br.com.jurix.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class LoggedUser {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario getUsuarioLogado(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioRepository.findByEmail(auth.getName());
        addPermissoes(usuario, auth);

        return usuario;
    }

    public void addPermissoes(Usuario usuario, Authentication auth){

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        usuario.setPermissoes(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }
}

package br.com.jurix.security.business;

import br.com.jurix.security.entity.Perfil;
import br.com.jurix.security.repository.PerfilRepository;
import br.com.jurix.security.repository.UsuarioRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilBO {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Perfil> findAll() {
        return perfilRepository.findAllAtivos();
    }


    public Perfil findById(Long id) {
        return perfilRepository.findById(id);
    }

    public Perfil create(Perfil perfil) {
        validarPerfil(perfil);
        return perfilRepository.save(perfil);
    }

    public Perfil update(Perfil perfil) {
        validarPerfil(perfil);
        return perfilRepository.save(perfil);
    }

    private void validarPerfil(Perfil perfil) {

        if(StringUtils.isEmpty(perfil.getDescricao())){
            throw new RuntimeException("Não foi atribuída descrição ao perfil.");
        }

        if(CollectionUtils.isEmpty(perfil.getPermissoes())){
            throw new RuntimeException("Não foi atribuída permissão ao perfil.");
        }
    }

    public void delete(Long id) {

        if(usuarioRepository.countByPerfil_Id(id) > 0){
            throw new RuntimeException("Perfil possui usuários relacionados");
        }

        Perfil perfil = perfilRepository.findById(id);
        perfil.setRemovido(Boolean.TRUE);
        perfilRepository.save(perfil);
    }
}

package br.com.jurix.security.controller;

import br.com.jurix.security.business.PerfilBO;
import br.com.jurix.security.entity.Perfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/perfis")
public class PerfilController {

    @Autowired
    private PerfilBO perfilBO;

    @GetMapping
    public List<Perfil> findAll() {
        return perfilBO.findAll();
    }

    @GetMapping(path = "/{id}")
    public Perfil findById(@PathVariable("id") Long id) {
        return perfilBO.findById(id);
    }

    @PostMapping
    public Perfil create(@RequestBody Perfil perfil){
        return perfilBO.create(perfil);
    }

    @PutMapping(path = "/{id}")
    public Perfil update(@RequestBody Perfil perfil){
        return perfilBO.update(perfil);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        perfilBO.delete(id);
    }
}

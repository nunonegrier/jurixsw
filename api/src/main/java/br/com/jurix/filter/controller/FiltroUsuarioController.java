package br.com.jurix.filter.controller;

import br.com.jurix.filter.business.FiltroUsuarioBO;
import br.com.jurix.filter.entity.FiltroUsuario;
import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.filter.filter.FiltroUsuarioFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/filtrosUsuario")
public class FiltroUsuarioController {

    @Autowired
    private FiltroUsuarioBO filtroUsuarioBO;

    @GetMapping
    public Object findAll(FiltroUsuarioFilter filtroUsuarioFilter) {
        return filtroUsuarioBO.findAll(filtroUsuarioFilter);
    }

    @GetMapping(path = "/padrao/{tipo}")
    public Object buscarPadrao(@PathVariable("tipo") TipoFiltroUsuarioEnum tipo){
        return filtroUsuarioBO.buscarPadrao(tipo);
    }

    @GetMapping(path = "/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return filtroUsuarioBO.findById(id);
    }

    @PostMapping
    public FiltroUsuario save(@RequestBody FiltroUsuario filtroUsuario) {
        return filtroUsuarioBO.save(filtroUsuario);
    }

    @PutMapping(path = "/{id}")
    public FiltroUsuario update(@RequestBody FiltroUsuario filtroUsuario) {
        return filtroUsuarioBO.update(filtroUsuario);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        filtroUsuarioBO.delete(id);
    }
}

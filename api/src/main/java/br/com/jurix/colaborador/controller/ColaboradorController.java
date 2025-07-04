package br.com.jurix.colaborador.controller;

import br.com.jurix.colaborador.business.ColaboradorBO;
import br.com.jurix.colaborador.entity.Colaborador;
import br.com.jurix.colaborador.filter.ColaboradorFilter;
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
@RequestMapping(path = "/colaboradores")
public class ColaboradorController {

    @Autowired
    private ColaboradorBO colaboradorBO;

    @GetMapping
    public Object findAll(ColaboradorFilter filter) {
        return colaboradorBO.findAll(filter);
    }

    @GetMapping(path = "/{id}")
    public Colaborador findById(@PathVariable("id") Long id) {
        return colaboradorBO.findById(id);
    }

    @PostMapping
    public Colaborador save(@RequestBody Colaborador colaborador) {
        return colaboradorBO.save(colaborador);
    }

    @PutMapping(path = "/{id}")
    public Colaborador update(@RequestBody Colaborador colaborador) {
        return colaboradorBO.update(colaborador);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        colaboradorBO.delete(id);
    }
}

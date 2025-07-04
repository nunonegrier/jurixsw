package br.com.jurix.processo.controller;

import br.com.jurix.processo.business.AndamentoProcessoBO;
import br.com.jurix.processo.entity.AndamentoProcesso;
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
@RequestMapping(path = "processos/{idProcesso}/andamentosProcesso")
public class AndamentoProcessoController {

    @Autowired
    private AndamentoProcessoBO andamentoProcessoBO;

    @GetMapping
    public List<AndamentoProcesso> buscarPorProcesso(@PathVariable("idProcesso") Long idProcesso){
        return andamentoProcessoBO.buscarPorProcesso(idProcesso);
    }

    @PostMapping
    public AndamentoProcesso create(@RequestBody AndamentoProcesso andamentoProcesso){
        return andamentoProcessoBO.create(andamentoProcesso);
    }

    @PostMapping(path = "/andamentoReativacao")
    public AndamentoProcesso createAndamentoReativacao(@RequestBody AndamentoProcesso andamentoProcesso){
        return andamentoProcessoBO.createAndamentoReativacao(andamentoProcesso);
    }

    @PutMapping(path = "/{id}")
    public AndamentoProcesso update(@RequestBody AndamentoProcesso andamentoProcesso){
        return andamentoProcessoBO.update(andamentoProcesso);
    }

    @GetMapping(path = "/{id}")
    public AndamentoProcesso findById(@PathVariable("id") Long id){
        return andamentoProcessoBO.findById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id){
        andamentoProcessoBO.delete(id);
    }
}

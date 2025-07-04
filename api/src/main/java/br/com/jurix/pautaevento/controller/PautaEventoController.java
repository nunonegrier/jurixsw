package br.com.jurix.pautaevento.controller;

import br.com.jurix.pautaevento.business.PautaEventoBO;
import br.com.jurix.pautaevento.entity.PautaEvento;
import br.com.jurix.pautaevento.filter.PautaEventoFilter;
import br.com.jurix.pautaevento.task.PautaEventoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "pautaEventos")
public class PautaEventoController {

    @Autowired
    private PautaEventoBO pautaEventoBO;

    @Autowired
    private PautaEventoTask pautaEventoTask;

    @Value("${jurix.minimoCaracteresObsFinalizacaoPauta}")
    private Long minimoCaracteresObsFinalizacaoPauta;

    @GetMapping
    public Object findByFilter(PautaEventoFilter filter) {
        return pautaEventoBO.findByFilter(filter);
    }

    @GetMapping(path = "/geral")
    public Object findByFilterGeneral(PautaEventoFilter filter) {
        return pautaEventoBO.findByFilterGeneral(filter);
    }

    @GetMapping(path = "/{id}")
    public PautaEvento findById(@PathVariable("id") Long id) {
        return pautaEventoBO.findById(id);
    }

    @PostMapping
    public PautaEvento save(@RequestBody PautaEvento pautaEvento) {
        return pautaEventoBO.save(pautaEvento);
    }

    @PutMapping(path = "/{id}")
    public PautaEvento update(@RequestBody PautaEvento pautaEvento) {
        return pautaEventoBO.update(pautaEvento);
    }

    @PutMapping(path = "/{id}/finalizar")
    public PautaEvento finalizar(@RequestBody PautaEvento pautaEvento) {
        return pautaEventoBO.finalizar(pautaEvento);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        pautaEventoBO.remover(id);
    }

    @GetMapping(path = "/falharEventosVencidos")
    public void falharEventosVencidos(){
        pautaEventoTask.falharEventosVencidos();
    }

    @GetMapping(path = "/{id}/historicoFinalizacao")
    public Object historicoFinalizacao(@PathVariable("id") Long id){
        return pautaEventoBO.historicoFinalizacaoPorEvento(id);
    }

    @GetMapping(path = "/minimoCaracteresObsFinalizacaoPauta")
    public Object getMinimoCaracteresObsFinalizacaoPauta(){
        Map<String, Long> resp = new HashMap<>();
        resp.put("minimo", minimoCaracteresObsFinalizacaoPauta);
        return resp;
    }
}

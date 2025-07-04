package br.com.jurix.processo.controller;

import br.com.jurix.processo.business.ProcessoBO;
import br.com.jurix.processo.dto.ProcessoDTO;
import br.com.jurix.processo.filter.ProcessoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(path = "processos")
public class ProcessoController {

    @Autowired
    private ProcessoBO processoBO;


    @GetMapping
    public Object findByFilter(ProcessoFilter filter, @RequestParam(required=false) Boolean incluirParteInteressada, @RequestParam(required=false) Boolean incluirParteContraria) {

        if(Objects.isNull(incluirParteInteressada)){
            incluirParteInteressada = Boolean.FALSE;
        }

        if(Objects.isNull(incluirParteContraria)){
            incluirParteContraria = Boolean.FALSE;
        }

        return processoBO.findByFilter(filter, incluirParteInteressada, incluirParteContraria);
    }

    @PostMapping
    public ProcessoDTO save(@RequestBody ProcessoDTO processoDTO){
        return processoBO.save(processoDTO);
    }

    @GetMapping(path = "/{id}")
    public ProcessoDTO findById(@PathVariable("id") Long id, @RequestParam(value = "marcaRecente", required = false) Boolean marcaRecente) {
        return processoBO.findById(id, marcaRecente);
    }

    @PutMapping(path = "/{id}")
    public ProcessoDTO update(@RequestBody ProcessoDTO processoDTO){
        return processoBO.update(processoDTO);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) {
        processoBO.delete(id);
    }
}

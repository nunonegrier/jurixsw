package br.com.jurix.processo.controller;

import br.com.jurix.processo.business.ParteProcessoBO;
import br.com.jurix.processo.filter.ParteProcessoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "partesProcesso")
public class ParteProcessoController {

    @Autowired
    private ParteProcessoBO parteProcessoBO;

    @GetMapping
    public Object findByFilter(ParteProcessoFilter filter) {
        return parteProcessoBO.findByFilter(filter);
    }

}

package br.com.jurix.financeiro.pessoaconta.controller;

import br.com.jurix.financeiro.pessoaconta.business.PessoaContaBO;
import br.com.jurix.financeiro.pessoaconta.filter.PessoaContaFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/pessoaConta")
public class PessoaContaController {

    @Autowired
    PessoaContaBO pessoaContaBO;

    @GetMapping
    public Object findAll(PessoaContaFilter pessoaContaFilter){
        return pessoaContaBO.buscarPessoaConta(pessoaContaFilter);
    }

}

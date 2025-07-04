package br.com.jurix.cliente.controller;

import br.com.jurix.cliente.business.ContratoBO;
import br.com.jurix.cliente.entity.Contrato;
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
import java.util.Map;

@RestController
@RequestMapping(path = "/clientes/{idCliente}/contratos")
public class ContratoController {

    @Autowired
    private ContratoBO contratoBO;

    @GetMapping
    public List<Contrato> buscarPorCliente(@PathVariable("idCliente") Long idCliente){
        return contratoBO.buscarPorCliente(idCliente);
    }

    @GetMapping(path = "/{id}")
    public Contrato buscarPorId(@PathVariable("id") Long id){
        return contratoBO.buscarPorId(id);
    }

    @PostMapping
    public Contrato criarContrato(@RequestBody Contrato contrato){
        return contratoBO.criarContrato(contrato);
    }

    @PutMapping(path = "/{id}")
    public Contrato atualizarContrato(@RequestBody Contrato contrato){
        return contratoBO.atualizarContrato(contrato);
    }

    @PutMapping(path = "/{id}/inativar")
    public void inativarContrato(@PathVariable("id") Long contratoId, @RequestBody Map params){
        contratoBO.inativarContrato(contratoId, params);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long idContrato){
        contratoBO.delete(idContrato);
    }
}

package br.com.jurix.cliente.controller;

import br.com.jurix.cliente.business.ClienteBO;
import br.com.jurix.cliente.entity.Cliente;
import br.com.jurix.cliente.filter.ClienteFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(path = "/clientes")
public class ClienteController {

    @Autowired
    private ClienteBO clienteBO;

    @GetMapping
    public Object findAll(ClienteFilter clienteFilter, @RequestParam(required=false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date dataNascimento) {
        clienteFilter.setDataNascimentoParam(dataNascimento);
        return clienteBO.findByFilter(clienteFilter);
    }

    @GetMapping(path = "/{id}")
    public Object findById(@PathVariable("id") Long idCliente) {
        return clienteBO.findById(idCliente);
    }

    @PostMapping(path = "/fisico")
    public Cliente createClienteFisico(@RequestBody Cliente cliente) {
        return clienteBO.createClienteFisico(cliente);
    }

    @PostMapping(path = "/juridico")
    public Cliente createClienteJuridico(@RequestBody Cliente cliente) {
        return clienteBO.createClienteJuridico(cliente);
    }

    @PutMapping(path = "/{id}")
    public Cliente update(@PathVariable("id") Long idCliente, @RequestBody Cliente cliente) {
        cliente.setId(idCliente);
        return clienteBO.update(cliente);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long idCliente){
        clienteBO.delete(idCliente);
    }
}

package br.com.jurix.financeiro.contaspagar.controller;

import br.com.jurix.financeiro.contaspagar.business.ContasPagarBO;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Date;

@RestController
@RequestMapping(path = "/contasPagar")
public class ContasPagarController {

    @Autowired
    private ContasPagarBO contasPagarBO;

    @GetMapping(path = "/{centroCustoId}", params = {"mes", "ano"})
    public Object findAll(@PathVariable("centroCustoId") Long centroCustoId, @PathParam("mes") Integer mes, @PathParam("ano") Integer ano) {
        return contasPagarBO.buscarcontasPagar(mes, ano, centroCustoId);
    }

    @GetMapping(path = "/{id}")
    public ContasPagar findById(@PathVariable("id") Long id){
        return contasPagarBO.findById(id);
    }

    @PostMapping
    public ContasPagar create(@RequestBody ContasPagar contasPagar){
        return contasPagarBO.create(contasPagar);
    }

    @PutMapping(path = "/{id}")
    public ContasPagar update(@RequestBody ContasPagar contasPagar){
        return contasPagarBO.update(contasPagar);
    }

    @PutMapping(path = "/pagar/{id}")
    public ContasPagar pagarConta(@RequestBody ContasPagar contasPagar){
        return contasPagarBO.pagarConta(contasPagar);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long contasPagarId){
        contasPagarBO.delete(contasPagarId);
    }

    @GetMapping(path = "/criarContasRecorrentes", params = {"dataReferencia",})
    public void cirarContasComRecorrencia(@PathParam("dataReferencia") Date dataReferencia) {

        contasPagarBO.criarNovasContasMensais(dataReferencia);
        contasPagarBO.criarNovasContasSemanais(dataReferencia);
        contasPagarBO.criarNovasContasAnuais(dataReferencia);
    }

    @GetMapping(path = "/ajsutarIncidencia")
    public void ajsutarIncidencia() {
        contasPagarBO.ajsutarIncidencia();
    }
}

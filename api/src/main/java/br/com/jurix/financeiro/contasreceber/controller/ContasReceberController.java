package br.com.jurix.financeiro.contasreceber.controller;

import br.com.jurix.financeiro.contasreceber.business.ContasReceberBO;
import br.com.jurix.financeiro.contasreceber.entity.ContasReceber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;

@RestController
@RequestMapping(path = "/contasReceber")
public class ContasReceberController {

    @Autowired
    private ContasReceberBO contasReceberBO;

    @GetMapping(path = "/{centroCustoId}", params = {"mes", "ano"})
    public Object findAll(@PathVariable("centroCustoId") Long centroCustoId, @PathParam("mes") Integer mes, @PathParam("ano") Integer ano) {
        return contasReceberBO.buscarcontasReceber(mes, ano, centroCustoId);
    }

    @GetMapping(path="/{id}")
    public ContasReceber findById(@PathVariable("id") Long id){
        return contasReceberBO.findById(id);
    }

    @GetMapping(path="/contrato/{contratoId}/ativa")
    public ContasReceber buscarContaAtivaContrato(@PathVariable("contratoId") Long contratoId){
        return contasReceberBO.buscarContaAtivaContrato(contratoId);
    }

    @PostMapping
    public ContasReceber create(@RequestBody ContasReceber contasReceber){
        return contasReceberBO.create(contasReceber);
    }

    @PutMapping(path="/{id}")
    public ContasReceber update(@RequestBody ContasReceber contasReceber){
        return contasReceberBO.update(contasReceber);
    }

    @PutMapping(path = "/pagar/{id}")
    public ContasReceber pagarConta(@RequestBody ContasReceber contasReceber){
        return contasReceberBO.pagarConta(contasReceber);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long contaReceberId){
        contasReceberBO.delete(contaReceberId);
    }


    @GetMapping(path = "/criarContasRecorrentes", params = {"dataReferencia",})
    public void cirarContasComRecorrencia(@PathParam("dataReferencia") Date dataReferencia) {

        contasReceberBO.criarNovasContasMensais(dataReferencia);
        contasReceberBO.criarNovasContasSemestrais(dataReferencia);
        contasReceberBO.criarNovasContasAnuais(dataReferencia);
        contasReceberBO.criarNovasContasParceladas(dataReferencia);
    }

    @GetMapping(path = "/ajsutarIncidencia")
    public void ajsutarIncidencia() {
        contasReceberBO.ajsutarIncidencia();
    }
}

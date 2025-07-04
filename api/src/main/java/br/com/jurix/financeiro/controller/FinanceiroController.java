package br.com.jurix.financeiro.controller;

import br.com.jurix.financeiro.business.FinanceiroBO;
import br.com.jurix.financeiro.filter.FinanceiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/financeiro")
public class FinanceiroController {

    @Autowired
    private FinanceiroBO financeiroBO;

    @GetMapping(path = "/{centroCustoId}", params = {"mes", "ano"})
    public Object findAll(@PathVariable("centroCustoId") Long centroCustoId, FinanceiroFilter financeiroFilter) {
        return financeiroBO.buscarDadosFinanceiros(centroCustoId, financeiroFilter);
    }

    @GetMapping(path = "/centroCustoDefault")
    public Object getCentroCustoDefault(){
        return financeiroBO.buscarCentroCustoRecente();
    }

    @PostMapping(path = "/centroCustoDefault")
    public void getCentroCustoDefault(@RequestBody Map<String, Long> params){
        financeiroBO.marcarCentroCustoPadrao(params.get("centroId"));
    }

}

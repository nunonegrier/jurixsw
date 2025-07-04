package br.com.jurix.financeiro.contaspagar.exception;

public class ContaAtrasadaSemValorPagoException extends RuntimeException{

    public ContaAtrasadaSemValorPagoException() {
        super("Valor Pago Não Informado");
    }

}

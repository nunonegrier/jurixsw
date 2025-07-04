package br.com.jurix.financeiro.pessoaconta.filter;

import br.com.jurix.filter.SortPaginatorFilter;
import br.com.jurix.financeiro.pessoaconta.entity.QPessoaConta;

import java.util.Objects;

public class PessoaContaFilter extends SortPaginatorFilter {

    public void setNome(String nome) {
        if (Objects.nonNull(nome)) {
            addToMainBooleanExpression(QPessoaConta.pessoaConta.nome.containsIgnoreCase(nome));
        }
    }
}

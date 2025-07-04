package br.com.jurix.financeiro.pessoaconta.repository;

import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import br.com.jurix.querydsql.repository.JoinCapableRepository;

public interface PessoaContaRepository extends JoinCapableRepository<PessoaConta, Long> {
}

package br.com.jurix.financeiro.pessoaconta.business;

import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import br.com.jurix.financeiro.pessoaconta.entity.QPessoaConta;
import br.com.jurix.financeiro.pessoaconta.filter.PessoaContaFilter;
import br.com.jurix.financeiro.pessoaconta.repository.PessoaContaRepository;
import br.com.jurix.localidade.entity.QMunicipio;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PessoaContaBO {

    @Autowired
    private PessoaContaRepository pessoaContaRepository;

    public Page<PessoaConta> buscarPessoaConta(PessoaContaFilter pessoaContaFilter){
        return pessoaContaRepository.findAll(pessoaContaFilter.getMainBooleanExpression(), pessoaContaFilter.getPageRequest(), JoinDescriptor.leftJoin(QPessoaConta.pessoaConta.municipio, QMunicipio.municipio), JoinDescriptor.leftJoin(QMunicipio.municipio.estado));
    }

    public PessoaConta createIfNotExists(PessoaConta pessoaConta){

        PessoaConta pessoaContaSalvar = pessoaConta;
        if(Objects.nonNull(pessoaContaSalvar.getId())){
            pessoaContaSalvar = pessoaContaRepository.findOne(pessoaContaSalvar.getId());
            BeanUtils.copyProperties(pessoaConta, pessoaContaSalvar);
        }

        return pessoaContaRepository.save(pessoaContaSalvar);
    }
}

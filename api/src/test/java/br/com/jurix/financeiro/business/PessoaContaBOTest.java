package br.com.jurix.financeiro.business;

import br.com.jurix.financeiro.pessoaconta.business.PessoaContaBO;
import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import br.com.jurix.financeiro.pessoaconta.repository.PessoaContaRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PessoaContaBOTest {

    @InjectMocks
    private PessoaContaBO pessoaContaBO;

    @Mock
    private PessoaContaRepository pessoaContaRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void salvarNovoSeNaoPossuiID(){

        PessoaConta pessoaConta = new PessoaConta();
        pessoaConta.setNome("João da Silva");

        PessoaConta pessoaContaSalvo = new PessoaConta();
        pessoaContaSalvo.setId(8L);
        pessoaContaSalvo.setNome("João da Silva");

        when(pessoaContaRepository.save(pessoaConta)).thenReturn(pessoaContaSalvo);

        PessoaConta pessoaContaResult = pessoaContaBO.createIfNotExists(pessoaConta);

        assertEquals(pessoaContaResult, pessoaContaSalvo);

        verify(pessoaContaRepository, times(1)).save(pessoaConta);
        verify(pessoaContaRepository, never()).findOne(any(Long.class));
    }

    @Test
    public void buscarSePOssuiId(){

        PessoaConta pessoaConta = new PessoaConta();
        pessoaConta.setId(8L);
        pessoaConta.setNome("João da Silva Editado");

        PessoaConta pessoaContaSalvo = new PessoaConta();
        pessoaContaSalvo.setId(8L);
        pessoaContaSalvo.setNome("João da Silva");

        when(pessoaContaRepository.findOne(pessoaConta.getId())).thenReturn(pessoaContaSalvo);
        when(pessoaContaRepository.save(any(PessoaConta.class))).then(returnsFirstArg());

        PessoaConta pessoaContaResult = pessoaContaBO.createIfNotExists(pessoaConta);

        assertEquals(pessoaContaResult.getNome(), pessoaConta.getNome());

        verify(pessoaContaRepository, times(1)).save(pessoaContaSalvo);
        verify(pessoaContaRepository, times(1)).findOne(pessoaConta.getId());
    }
}

package br.com.jurix.financeiro.business;

import br.com.jurix.centrocusto.entity.CentroCusto;
import br.com.jurix.centrocusto.repository.CentroCustoRepository;
import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.financeiro.contaspagar.business.ContasPagarBO;
import br.com.jurix.financeiro.pessoaconta.business.PessoaContaBO;
import br.com.jurix.financeiro.contaspagar.entity.ContasPagar;
import br.com.jurix.financeiro.pessoaconta.entity.PessoaConta;
import br.com.jurix.financeiro.contaspagar.enumeration.EnumSituacaoConta;
import br.com.jurix.financeiro.contaspagar.exception.ContaAtrasadaSemValorPagoException;
import br.com.jurix.financeiro.contaspagar.repository.ContasPagarRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.com.jurix.testUtils.CommonsUtils.bigDecimalValue;
import static br.com.jurix.testUtils.CommonsUtils.dateFromString;
import static br.com.jurix.testUtils.CommonsUtils.dateTimeFromString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore
@TestPropertySource(properties = { "jurix.prazoDiasConta=3" })
public class ContasPagarBOTest {

    @InjectMocks
    private ContasPagarBO contasPagarBO;

    @Mock
    private ContasPagarRepository contasPagarRepository;

    @Mock
    private CentroCustoRepository centroCustoRepository;

    @Mock
    private PessoaContaBO pessoaContaBO;

    @Mock
    private JurixDateTime jurixDateTime;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    public void salvarNovaContaSemPessoaContaExistente(){
//
//        PessoaConta pessoaConta = new PessoaConta();
//        pessoaConta.setId(8L);
//
//        CentroCusto centroCusto = new CentroCusto();
//        centroCusto.setId(99L);
//
//        ContasPagar contasPagar = new ContasPagar();
//        contasPagar.setPessoaConta(pessoaConta);
//        contasPagar.setCentroCusto(centroCusto);
//
//        Date dataCriacao = new Date();
//
//        when(pessoaContaBO.createIfNotExists(contasPagar.getPessoaConta())).thenReturn(pessoaConta);
//        when(centroCustoRepository.findOne(contasPagar.getCentroCusto().getId())).thenReturn(centroCusto);
//        when(jurixDateTime.getCurrentDateTime()).thenReturn(dataCriacao);
//        when(contasPagarRepository.save(contasPagar)).then(returnsFirstArg());
//
//        ContasPagar contasPagarResult = contasPagarBO.create(contasPagar);
//
//        assertEquals(contasPagarResult.getPessoaConta(), pessoaConta);
//        assertEquals(EnumSituacaoConta.PENDENTE, contasPagarResult.getSituacao());
//        assertEquals(dataCriacao, contasPagarResult.getDataCriacao());
//        assertEquals(centroCusto, contasPagarResult.getCentroCusto());
//
//        verify(pessoaContaBO, times(1)).createIfNotExists(contasPagar.getPessoaConta());
//        verify(centroCustoRepository, times(1)).findOne(contasPagar.getCentroCusto().getId());
//    }
//
//    @Test
//    public void buscarContasTest() throws ParseException {
//
//        ContasPagar contasPagar1 = new ContasPagar();
//        contasPagar1.setId(99L);
//        contasPagar1.setDataVencimento(dateTimeFromString("2018-11-06 09:00:00"));
//
//        ContasPagar contasPagar2 = new ContasPagar();
//        contasPagar2.setId(78L);
//        contasPagar2.setDataVencimento(dateTimeFromString("2018-11-01 09:00:00"));
//
//        ContasPagar contasPagar3 = new ContasPagar();
//        contasPagar3.setId(88L);
//        contasPagar3.setDataVencimento(dateTimeFromString("2018-11-02 09:00:00"));
//
//        ContasPagar contasPagar4 = new ContasPagar();
//        contasPagar4.setId(55L);
//        contasPagar4.setDataVencimento(dateTimeFromString("2018-11-05 09:00:00"));
//
//        Integer mes = 9;
//        Integer ano = 2018;
//        Long centroCustoId = 89L;
//
//
//        Date dataAtual = dateTimeFromString("2018-11-02 09:00:00");
//
//        ReflectionTestUtils.setField(contasPagarBO, "prazoDiasConta", 3);
//
//        when(jurixDateTime.getCurrentDateTime()).thenReturn(dataAtual);
//        when(contasPagarRepository.buscarcontasPagar(mes, ano, centroCustoId)).thenReturn(Arrays.asList(contasPagar1, contasPagar2, contasPagar3, contasPagar4));
//
//        List<ContasPagar> contas = contasPagarBO.buscarcontasPagar(mes, ano, centroCustoId);
//
//        assertEquals(4, contas.size());
//
//        assertEquals(contas.get(0).getId(), contasPagar1.getId());
//        assertFalse(contas.get(0).getIsAtrasado());
//        assertFalse(contas.get(0).getAVencer());
//
//        assertEquals(contas.get(1).getId(), contasPagar2.getId());
//        assertTrue(contas.get(1).getIsAtrasado());
//        assertFalse(contas.get(1).getAVencer());
//
//        assertEquals(contas.get(2).getId(), contasPagar3.getId());
//        assertFalse(contas.get(2).getIsAtrasado());
//        assertTrue(contas.get(2).getAVencer());
//
//        assertEquals(contas.get(3).getId(), contasPagar4.getId());
//        assertFalse(contas.get(3).getIsAtrasado());
//        assertTrue(contas.get(3).getAVencer());
//    }
//
//    @Test
//    public void updateContasPagarPendenteTest(){
//
//        CentroCusto centroCusto1 = new CentroCusto();
//        centroCusto1.setId(89L);
//
//        CentroCusto centroCusto2 = new CentroCusto();
//        centroCusto2.setId(66L);
//
//        ContasPagar contasPagar = new ContasPagar();
//        contasPagar.setId(76L);
//        contasPagar.setSituacao(EnumSituacaoConta.PAGA);
//        contasPagar.setDescricao("Conta Editada");
//        contasPagar.setCentroCusto(centroCusto1);
//
//        Date dataCriacao = new Date();
//
//        ContasPagar contasPagarExistente = new ContasPagar();
//        contasPagarExistente.setId(contasPagar.getId());
//        contasPagarExistente.setCentroCusto(centroCusto2);
//        contasPagarExistente.setSituacao(EnumSituacaoConta.PENDENTE);
//        contasPagarExistente.setDescricao("Conta Existente");
//        contasPagarExistente.setDataCriacao(dataCriacao);
//
//        when(contasPagarRepository.findOne(contasPagar.getId())).thenReturn(contasPagarExistente);
//        when(contasPagarRepository.save(any(ContasPagar.class))).then(returnsFirstArg());
//
//        ContasPagar contaAtualizada = contasPagarBO.update(contasPagar);
//
//        assertEquals(contaAtualizada.getCentroCusto().getId(), contasPagarExistente.getCentroCusto().getId());
//        assertEquals(contaAtualizada.getSituacao(), contasPagarExistente.getSituacao());
//        assertEquals(contaAtualizada.getDescricao(), contasPagar.getDescricao());
//        assertEquals(contaAtualizada.getDataCriacao(), dataCriacao);
//    }
//
//    @Test
//    public void pagarContaPendenteTest() throws ParseException {
//
//        BigDecimal valorConta = bigDecimalValue("10.99");
//        Date dataPagamento = dateFromString("2018-10-10");
//
//        CentroCusto centroCusto1 = new CentroCusto();
//        centroCusto1.setId(89L);
//
//        ContasPagar contasPagar = new ContasPagar();
//        contasPagar.setId(76L);
//        contasPagar.setValorPago(bigDecimalValue("11.99"));
//        contasPagar.setDataPagamento(dataPagamento);
//
//        Date dataAtual = dateFromString("2018-10-10");
//
//        ContasPagar contasPagarExistente = new ContasPagar();
//        contasPagarExistente.setId(contasPagar.getId());
//        contasPagarExistente.setSituacao(EnumSituacaoConta.PENDENTE);
//        contasPagarExistente.setValor(valorConta);
//        contasPagarExistente.setDataVencimento(dataPagamento);
//
//        when(contasPagarRepository.findOne(contasPagar.getId())).thenReturn(contasPagarExistente);
//        when(jurixDateTime.getCurrentDateTime()).thenReturn(dataAtual);
//        when(contasPagarRepository.save(any(ContasPagar.class))).then(returnsFirstArg());
//
//        ContasPagar contaAtualizada = contasPagarBO.pagarConta(contasPagar);
//
//        assertEquals(contaAtualizada.getSituacao(), EnumSituacaoConta.PAGA);
//        assertEquals(contaAtualizada.getValorPago(), valorConta);
//        assertEquals(contaAtualizada.getDataPagamento(), dataPagamento);
//    }
//
//    @Test
//    public void pagarContaAtrasadaSucessTest() throws ParseException {
//
//        BigDecimal valorPago = bigDecimalValue("10.99");
//        Date dataPagamento = dateFromString("2018-10-10");
//        Date dataVencimento = dateFromString("2018-10-09");
//
//        CentroCusto centroCusto1 = new CentroCusto();
//        centroCusto1.setId(89L);
//
//        ContasPagar contasPagar = new ContasPagar();
//        contasPagar.setId(76L);
//        contasPagar.setValorPago(valorPago);
//        contasPagar.setDataPagamento(dataPagamento);
//
//
//        Date dataAtual = dataPagamento;
//
//        ContasPagar contasPagarExistente = new ContasPagar();
//        contasPagarExistente.setId(contasPagar.getId());
//        contasPagarExistente.setSituacao(EnumSituacaoConta.PENDENTE);
//        contasPagarExistente.setDataVencimento(dataVencimento);
//
//        when(contasPagarRepository.findOne(contasPagar.getId())).thenReturn(contasPagarExistente);
//        when(jurixDateTime.getCurrentDateTime()).thenReturn(dataAtual);
//        when(contasPagarRepository.save(any(ContasPagar.class))).then(returnsFirstArg());
//
//        ContasPagar contaAtualizada = contasPagarBO.pagarConta(contasPagar);
//
//        assertEquals(contaAtualizada.getSituacao(), EnumSituacaoConta.PAGA);
//        assertEquals(contaAtualizada.getValorPago(), valorPago);
//        assertEquals(contaAtualizada.getDataPagamento(), dataPagamento);
//    }
//
//    @Test(expected = ContaAtrasadaSemValorPagoException.class)
//    public void pagarContaAtrasadaErrorTest() throws ParseException {
//
//        Date dataPagamento = dateFromString("2018-10-10");
//        Date dataVencimento = dateFromString("2018-10-09");
//
//        CentroCusto centroCusto1 = new CentroCusto();
//        centroCusto1.setId(89L);
//
//        ContasPagar contasPagar = new ContasPagar();
//        contasPagar.setId(76L);
//        contasPagar.setDataPagamento(dataPagamento);
//
//        Date dataAtual = dataPagamento;
//
//        ContasPagar contasPagarExistente = new ContasPagar();
//        contasPagarExistente.setId(contasPagar.getId());
//        contasPagarExistente.setSituacao(EnumSituacaoConta.PENDENTE);
//        contasPagarExistente.setDataVencimento(dataVencimento);
//
//        when(contasPagarRepository.findOne(contasPagar.getId())).thenReturn(contasPagarExistente);
//        when(jurixDateTime.getCurrentDateTime()).thenReturn(dataAtual);
//        when(contasPagarRepository.save(any(ContasPagar.class))).then(returnsFirstArg());
//
//        contasPagarBO.pagarConta(contasPagar);
//    }
//
//    @Test
//    public void deleteTest(){
//        Long contaPagarId = 77L;
//
//        contasPagarBO.delete(contaPagarId);
//
//        verify(contasPagarRepository, times(1)).delete(contaPagarId);
//    }
}

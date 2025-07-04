package br.com.jurix.cliente.business;

import br.com.jurix.cliente.entity.Cliente;
import br.com.jurix.cliente.entity.Contrato;
import br.com.jurix.cliente.repository.ClienteRepository;
import br.com.jurix.cliente.repository.ContratoRepository;
import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.financeiro.contasreceber.business.ContasReceberBO;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ContratoBO {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContasReceberBO contasReceberBO;

    @Autowired
    private JurixDateTime jurixDateTime;


    public List<Contrato> buscarPorCliente(Long idCLiente){
        return contratoRepository.findByCliente(idCLiente);
    }

    @Transactional
    public Contrato criarContrato(Contrato contrato){
        Cliente cliente = clienteRepository.findOne(contrato.getCliente().getId());

        cliente.setPossuiContrato(Boolean.TRUE);
        clienteRepository.save(cliente);

        Contrato contratoSalvo = contratoRepository.save(contrato);
        contasReceberBO.criarContaContrato(contratoSalvo);

        return contratoSalvo;
    }

    public Contrato buscarPorId(Long id) {
        return contratoRepository.findOne(id);
    }

    @Transactional
    public Contrato atualizarContrato(Contrato contrato) {

        Contrato contratoExistente = buscarPorId(contrato.getId());

        contrato.setCliente(contratoExistente.getCliente());

        BeanUtils.copyProperties(contrato, contratoExistente, "ativo", "dataInativacao");

        Contrato contratoSalvo =  contratoRepository.save(contratoExistente);

        contasReceberBO.atualizarContaAtualContrato(contratoSalvo);

        return contratoSalvo;
    }

    @Transactional
    public void inativarContrato(Long contratoId, Map params){

        Contrato contratoExistente = buscarPorId(contratoId);
        contratoExistente.setAtivo(Boolean.FALSE);
        contratoExistente.setDataInativacao(jurixDateTime.getCurrentDateTime());
        contratoRepository.save(contratoExistente);
        removerContaAtiva(params, contratoId);
    }

    public void delete(Long idContrato) {
        Contrato contrato = buscarPorId(idContrato);
        contrato.setRemovido(Boolean.TRUE);
        contratoRepository.save(contrato);
    }

    private void removerContaAtiva(Map params, Long contratoId) {
        Boolean removerConta = MapUtils.getBoolean(params, "removerConta");
        if(removerConta){
            contasReceberBO.removerContaAtivaContrato(contratoId);
        }
    }
}

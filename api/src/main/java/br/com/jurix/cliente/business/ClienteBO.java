package br.com.jurix.cliente.business;

import br.com.jurix.cliente.entity.Cliente;
import br.com.jurix.cliente.entity.ClienteFisico;
import br.com.jurix.cliente.entity.ClienteJuridico;
import br.com.jurix.cliente.entity.QCliente;
import br.com.jurix.cliente.filter.ClienteFilter;
import br.com.jurix.cliente.repository.ClienteFisicoRepository;
import br.com.jurix.cliente.repository.ClienteJuridicoRepository;
import br.com.jurix.cliente.repository.ClienteRepository;
import br.com.jurix.localidade.entity.QMunicipio;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteBO {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteFisicoRepository clienteFisicoRepository;

    @Autowired
    private ClienteJuridicoRepository clienteJuridicoRepository;

    public Cliente createClienteFisico(Cliente cliente) {

        salvarClienteFisico(cliente);

        return clienteRepository.save(cliente);
    }

    public Cliente createClienteJuridico(Cliente cliente) {

        salvarClienteJuridico(cliente);

        return clienteRepository.save(cliente);
    }

    public Cliente update(Cliente cliente){

        Cliente clienteExistente = findById(cliente.getId());

        if(clienteExistente.isClienteFisico()){
            BeanUtils.copyProperties(cliente.getClienteFisico(), clienteExistente.getClienteFisico());
            salvarClienteFisico(clienteExistente);
            cliente.setClienteFisico(clienteExistente.getClienteFisico());
        }else{
            BeanUtils.copyProperties(cliente.getClienteJuridico(), clienteExistente.getClienteJuridico());
            salvarClienteJuridico(clienteExistente);
            cliente.setClienteJuridico(clienteExistente.getClienteJuridico());
        }

        BeanUtils.copyProperties(cliente, clienteExistente);

        return clienteRepository.save(clienteExistente);
    }

    public Cliente findById(Long idCliente){
        return clienteRepository.findOneWithMunicipio(idCliente);
    }

    private void salvarClienteFisico(Cliente cliente) {

        ClienteFisico clienteFisico = clienteFisicoRepository.save(cliente.getClienteFisico());
        cliente.setClienteFisico(clienteFisico);
        cliente.setClienteJuridico(null);
    }

    private void salvarClienteJuridico(Cliente cliente) {

        ClienteJuridico clienteJuridico = clienteJuridicoRepository.save(cliente.getClienteJuridico());
        cliente.setClienteJuridico(clienteJuridico);
        cliente.setClienteFisico(null);

    }

    public Object findByFilter(ClienteFilter clienteFilter) {
        clienteFilter.setNaoExibirRemovidos();
        return clienteRepository.findAll(clienteFilter.getMainBooleanExpression(), clienteFilter.getPageRequest(), JoinDescriptor.leftJoin(QCliente.cliente.municipio, QMunicipio.municipio), JoinDescriptor.leftJoin(QMunicipio.municipio.estado));
    }


    public void delete(Long idCliente) {
        Cliente clienteExistente = findById(idCliente);
        clienteExistente.setRemovido(Boolean.TRUE);
        clienteRepository.save(clienteExistente);
    }
}

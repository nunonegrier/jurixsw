package br.com.jurix.processo.business;

import br.com.jurix.cliente.entity.QContrato;
import br.com.jurix.filter.business.FiltroUsuarioBO;
import br.com.jurix.filter.entity.FiltroUsuario;
import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.processo.dto.FiltroProcessoRecentesDTO;
import br.com.jurix.processo.dto.ProcessoDTO;
import br.com.jurix.processo.entity.AndamentoProcesso;
import br.com.jurix.processo.entity.ParteProcesso;
import br.com.jurix.processo.entity.Processo;
import br.com.jurix.processo.entity.QComarca;
import br.com.jurix.processo.entity.QProcesso;
import br.com.jurix.processo.enumeration.EnumSituacaoProcesso;
import br.com.jurix.processo.enumeration.EnumTipoParte;
import br.com.jurix.processo.filter.ProcessoFilter;
import br.com.jurix.processo.repository.AndamentoProcessoRepository;
import br.com.jurix.processo.repository.ParteProcessoRepository;
import br.com.jurix.processo.repository.ProcessoRepository;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProcessoBO {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ParteProcessoRepository parteProcessoRepository;

    @Autowired
    private AndamentoProcessoRepository andamentoProcessoRepository;

    @Autowired
    private FiltroUsuarioBO filtroUsuarioBO;

    public Page<Processo> findByFilter(ProcessoFilter processoFilter, Boolean incluirParteInteressada, Boolean incluirParteContraria) {
        processoFilter.setNaoRemovido();
        Page<Processo> processoPage = processoRepository.findAll(processoFilter.getMainBooleanExpression(), processoFilter.getPageRequest(),
                JoinDescriptor.join(QProcesso.processo.contrato, QContrato.contrato),
                JoinDescriptor.join(QContrato.contrato.cliente),
                JoinDescriptor.leftJoin(QProcesso.processo.tipoAcao),
                JoinDescriptor.leftJoin(QProcesso.processo.comarca, QComarca.comarca),
                JoinDescriptor.leftJoin(QComarca.comarca.estado));

        adicionarDadosExtrasProcesso(processoPage, incluirParteInteressada, incluirParteContraria);

        return processoPage;
    }


    private void adicionarDadosExtrasProcesso(Page<Processo> processoPage, Boolean incluirParteInteressada, Boolean incluirParteContraria) {

        processoPage.getContent().forEach(processo -> {

            adicionarParteContraria(processo);
            adicionarTextoUltimoAndamento(processo);
            incluirPartesCliente(processo, incluirParteInteressada, incluirParteContraria);
        });
    }

    private void incluirPartesCliente(Processo processo, Boolean incluirParteInteressada, Boolean incluirParteContraria) {


        if (incluirParteInteressada) {
            String partesCliente = parteProcessoRepository.buscarPorProcessoETipo(processo.getId(), EnumTipoParte.CLIENTE)
                    .stream().map(ParteProcesso::getNome).collect(Collectors.joining(", "));
            processo.setParteInteressada(partesCliente);
        }

        if (incluirParteContraria) {
            String partesContrarias = parteProcessoRepository.buscarPorProcessoETipo(processo.getId(), EnumTipoParte.CONTRARIA)
                    .stream().map(ParteProcesso::getNome).collect(Collectors.joining(", "));
            processo.setParteContraria(partesContrarias);
        }

    }

    private void adicionarParteContraria(Processo processo) {
        ParteProcesso parteContrariaProcesso = parteProcessoRepository.primeiraParteProcesso(processo.getId(), EnumTipoParte.CONTRARIA);
        processo.setParteProcesso(parteContrariaProcesso);
    }

    private void adicionarTextoUltimoAndamento(Processo processo) {
        List<AndamentoProcesso> andamentosProcesso = andamentoProcessoRepository.buscarPorProcesso(processo.getId());
        if (CollectionUtils.isNotEmpty(andamentosProcesso)) {
            andamentosProcesso.sort(Comparator.comparing(AndamentoProcesso::getData));
            Collections.reverse(andamentosProcesso);

            List<String> ultimosAndamentos = new ArrayList<>();
            for(int i = 0; i < andamentosProcesso.size() && i < 3; i++) {
                ultimosAndamentos.add(andamentosProcesso.get(i).getDescricao());
            }

            processo.setUltimoAndamento(String.join(" \n\n", ultimosAndamentos));
        }
    }

    @Transactional
    public ProcessoDTO save(ProcessoDTO processoDTO) {

        processoDTO.getProcesso().setSituacao(EnumSituacaoProcesso.ATIVO);
        processoDTO.getProcesso().setProcessoVinculadoField(processoDTO.getProcesso().getProcessoVinculado());

        validarProcessoDuplicado(processoDTO);

        Processo processoSalvo = processoRepository.save(processoDTO.getProcesso());
        processoDTO.setProcesso(processoSalvo);

        salvarParteClienteProcesso(processoDTO);
        salvarParteContrariaProcesso(processoDTO);

        return processoDTO;
    }

    @Transactional
    public ProcessoDTO update(ProcessoDTO processoDTO) {

        processoDTO.getProcesso().setProcessoVinculadoField(processoDTO.getProcesso().getProcessoVinculado());

        validarProcessoDuplicado(processoDTO);

        Processo processoExistente = processoRepository.buscarPorId(processoDTO.getProcesso().getId());
        BeanUtils.copyProperties(processoDTO.getProcesso(), processoExistente);
        processoRepository.save(processoDTO.getProcesso());

        salvarParteClienteProcesso(processoDTO);
        salvarParteContrariaProcesso(processoDTO);
        salvarPartesRemover(processoDTO);

        return processoDTO;
    }

    private void validarProcessoDuplicado(ProcessoDTO processoDTO) {

        if(StringUtils.isNotEmpty(processoDTO.getProcesso().getNumero())) {
            String numeroProcesso = processoDTO.getProcesso().getNumero();
            Long idProcesso = processoDTO.getProcesso().getId();
            Processo processoExistente = processoRepository.findOneByNumero(numeroProcesso);
            if(Objects.nonNull(processoExistente) && (Objects.isNull(idProcesso)|| !processoExistente.getId().equals(idProcesso) )){
                throw new RuntimeException("Já existe processo cadastrado com esse número");
            }
        }
    }

    private void salvarPartesRemover(ProcessoDTO processoDTO) {
        if (CollectionUtils.isNotEmpty(processoDTO.getPartesRemover())) {
            processoDTO.getPartesRemover().forEach(parteProcesso -> {
                parteProcesso.setRemovido(Boolean.TRUE);
                parteProcessoRepository.save(parteProcesso);
            });
        }
    }

    private void salvarParteContrariaProcesso(ProcessoDTO processoDTO) {
        if (CollectionUtils.isNotEmpty(processoDTO.getPartesCliente())) {
            processoDTO.getPartesContraria().forEach(parteProcesso -> {
                parteProcesso.setProcesso(processoDTO.getProcesso());
                parteProcesso.setTipo(EnumTipoParte.CONTRARIA);
                parteProcessoRepository.save(parteProcesso);
            });
        }
    }

    private void salvarParteClienteProcesso(ProcessoDTO processoDTO) {

        if (CollectionUtils.isNotEmpty(processoDTO.getPartesCliente())) {
            processoDTO.getPartesCliente().forEach(parteProcesso -> {

                removerPosicaoParteCliente(parteProcesso);
                parteProcesso.setProcesso(processoDTO.getProcesso());
                parteProcesso.setTipo(EnumTipoParte.CLIENTE);
                parteProcessoRepository.save(parteProcesso);
            });
        }
    }

    private void removerPosicaoParteCliente(ParteProcesso parteProcesso) {
        if (parteProcesso.getPosicaoParte().getId() == -1L) {
            parteProcesso.setPosicaoParte(null);
        }
    }

    public ProcessoDTO findById(Long processoId, Boolean marcaRecente) {
        ProcessoDTO processoDTO = findById(processoId);
        marcarRecente(marcaRecente, processoDTO);
        return processoDTO;
    }

    private void marcarRecente(Boolean marcaRecente, ProcessoDTO processoDTO) {
        if(Objects.nonNull(processoDTO) && BooleanUtils.isTrue(marcaRecente)){
            addParaRecente(processoDTO.getProcesso().getId());
        }
    }

    private void addParaRecente(Long processoId) {

        FiltroUsuario filtroUsuario = buscarFiltroProcessoRecente();

        FiltroProcessoRecentesDTO filtroProcessoRecentesDTO = parseFiltroRecentes(filtroUsuario);
        adicionarProcessoIdRecente(processoId, filtroProcessoRecentesDTO);

        String filtroJson = parseFiltroProcessoRecentes(filtroProcessoRecentesDTO);
        filtroUsuario.setValor(filtroJson);

        if(Objects.nonNull(filtroUsuario.getId())){
            filtroUsuarioBO.update(filtroUsuario);
        }else{
            filtroUsuarioBO.save(filtroUsuario);
        }
    }

    private void adicionarProcessoIdRecente(Long processoId, FiltroProcessoRecentesDTO filtroProcessoRecentesDTO) {
        int index = filtroProcessoRecentesDTO.getProcessoIds().indexOf(processoId);

        if (index != -1) {
            filtroProcessoRecentesDTO.getProcessoIds().remove(index);
        }

        List<Long> novaLista = new ArrayList<>();
        novaLista.add(processoId);
        for (int i = 0; i < 4 && i < filtroProcessoRecentesDTO.getProcessoIds().size() ; i++) {
            Long processoId_ = filtroProcessoRecentesDTO.getProcessoIds().get(i);
            novaLista.add(processoId_);
        }
        filtroProcessoRecentesDTO.setProcessoIds(novaLista);
    }

    private String parseFiltroProcessoRecentes(FiltroProcessoRecentesDTO filtroProcessoRecentesDTO) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(filtroProcessoRecentesDTO);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private FiltroUsuario buscarFiltroProcessoRecente(){
        FiltroUsuario filtroUsuario = filtroUsuarioBO.buscarPadrao(TipoFiltroUsuarioEnum.PROCESSOS_RECENTES);
        if (Objects.isNull(filtroUsuario)) {
            filtroUsuario = new FiltroUsuario();
            filtroUsuario.setTipo(TipoFiltroUsuarioEnum.PROCESSOS_RECENTES);
            filtroUsuario.setNome("Filtro Processo Recentes");
            filtroUsuario.setPadrao(Boolean.TRUE);
        }
        return filtroUsuario;
    }

    public FiltroProcessoRecentesDTO getFiltroRecente(){

        FiltroUsuario filtroUsuario = buscarFiltroProcessoRecente();
        if (Objects.nonNull(filtroUsuario.getId())) {
             return parseFiltroRecentes(filtroUsuario);
        }

        return null;
    }

    private FiltroProcessoRecentesDTO parseFiltroRecentes(FiltroUsuario filtroUsuario) {
        try {
            if(StringUtils.isEmpty(filtroUsuario.getValor())){
                return new FiltroProcessoRecentesDTO();
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(filtroUsuario.getValor(), FiltroProcessoRecentesDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public ProcessoDTO findById(Long processoId) {
        ProcessoDTO processoDTO = new ProcessoDTO();
        processoDTO.setProcesso(processoRepository.buscarPorId(processoId));
        processoDTO.setPartesCliente(parteProcessoRepository.buscarPorProcessoETipo(processoId, EnumTipoParte.CLIENTE));
        processoDTO.setPartesContraria(parteProcessoRepository.buscarPorProcessoETipo(processoId, EnumTipoParte.CONTRARIA));
        processoDTO.getProcesso().setProcessoVinculado(processoDTO.getProcesso().getProcessoVinculadoField());
        return processoDTO;
    }

    public void delete(Long processoId) {
        Processo processo = processoRepository.buscarPorId(processoId);
        processo.setRemovido(Boolean.TRUE);
        processoRepository.save(processo);
    }
}

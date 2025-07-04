package br.com.jurix.processo.business;

import br.com.jurix.pautaevento.business.PautaEventoBO;
import br.com.jurix.pautaevento.entity.PautaEvento;
import br.com.jurix.processo.entity.AndamentoProcesso;
import br.com.jurix.processo.entity.Processo;
import br.com.jurix.processo.entity.TipoAndamento;
import br.com.jurix.processo.enumeration.EnumFinalidadeTipoAndamento;
import br.com.jurix.processo.enumeration.EnumSituacaoProcesso;
import br.com.jurix.processo.repository.AndamentoProcessoRepository;
import br.com.jurix.processo.repository.ProcessoRepository;
import br.com.jurix.processo.repository.TipoAndamentoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AndamentoProcessoBO {

    @Autowired
    private AndamentoProcessoRepository andamentoProcessoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private TipoAndamentoRepository tipoAndamentoRepository;

    @Autowired
    private PautaEventoBO pautaEventoBO;

    @Value("${jurix.tipoAndamento.tipoReativacaoId}")
    private Long tipoReativacaoId;

    public List<AndamentoProcesso> buscarPorProcesso(Long idProcesso){
        return andamentoProcessoRepository.buscarPorProcesso(idProcesso);
    }

    @Transactional
    public AndamentoProcesso create(AndamentoProcesso andamentoProcesso){

        salvarPautaEvento(andamentoProcesso);
        setTipoAndamento(andamentoProcesso);
        setProcesso(andamentoProcesso);

        if(isAndamentoFinalizacaoProcesso(andamentoProcesso)){
            andamentoProcesso.getProcesso().setSituacao(EnumSituacaoProcesso.FINALIZADO);
            processoRepository.save(andamentoProcesso.getProcesso());
        }else if(isAndamentoReativacaoProcesso(andamentoProcesso)){
            andamentoProcesso.getProcesso().setSituacao(EnumSituacaoProcesso.ATIVO);
            processoRepository.save(andamentoProcesso.getProcesso());
        }

        AndamentoProcesso andamentoSalvo = andamentoProcessoRepository.save(andamentoProcesso);
        return findById(andamentoSalvo.getId());
    }

    public AndamentoProcesso createAndamentoReativacao(AndamentoProcesso andamentoProcesso) {

        TipoAndamento tipoAndamento = tipoAndamentoRepository.findOne(tipoReativacaoId);
        andamentoProcesso.setTipoAndamento(tipoAndamento);
        andamentoProcesso.setDescricao("Reativação de processo.");

        return create(andamentoProcesso);
    }

    private void setTipoAndamento(AndamentoProcesso andamentoProcesso) {
        andamentoProcesso.setTipoAndamento(tipoAndamentoRepository.findOne(andamentoProcesso.getTipoAndamento().getId()));
    }

    @Transactional
    public AndamentoProcesso update(AndamentoProcesso andamentoProcesso){

        AndamentoProcesso andamentoExistente = findById(andamentoProcesso.getId());
        salvarPautaEvento(andamentoProcesso);
        BeanUtils.copyProperties(andamentoProcesso, andamentoExistente);
        andamentoProcessoRepository.save(andamentoProcesso);
        return findById(andamentoExistente.getId());
    }

    public AndamentoProcesso findById(Long id){
        return andamentoProcessoRepository.buscarPorId(id);
    }


    public void salvarPautaEvento(AndamentoProcesso andamentoProcesso){

        if(andamentoProcesso.getCriaEventoPauta()) {
            PautaEvento pautaEventoSalva = pautaEventoBO.save(andamentoProcesso.getPautaEvento());
            andamentoProcesso.setPautaEvento(pautaEventoSalva);
        }else{
            andamentoProcesso.setPautaEvento(null);
        }
    }


    @Transactional
    public void delete(Long id) {
        AndamentoProcesso andamentoProcesso = findById(id);
        andamentoProcesso.setRemovido(Boolean.TRUE);
        if(Objects.nonNull(andamentoProcesso.getPautaEvento())) {
            pautaEventoBO.remover(andamentoProcesso.getPautaEvento().getId());
        }
        andamentoProcessoRepository.save(andamentoProcesso);
    }

    private static boolean isAndamentoFinalizacaoProcesso(AndamentoProcesso andamentoProcesso){
        return andamentoProcesso.getTipoAndamento().getFinalidade().equals(EnumFinalidadeTipoAndamento.FINALIZAR_PROCESSO);
    }

    private static boolean isAndamentoReativacaoProcesso(AndamentoProcesso andamentoProcesso){
        return andamentoProcesso.getTipoAndamento().getFinalidade().equals(EnumFinalidadeTipoAndamento.REATIVAR_PROCESSO);
    }

    public void setProcesso(AndamentoProcesso andamentoProcesso) {
        Processo processo = processoRepository.findOne(andamentoProcesso.getProcesso().getId());
        andamentoProcesso.setProcesso(processo);
    }


}

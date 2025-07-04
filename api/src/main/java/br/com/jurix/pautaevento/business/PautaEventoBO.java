package br.com.jurix.pautaevento.business;

import br.com.jurix.colaborador.entity.QColaborador;
import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.pautaevento.entity.HistoricoFinalizacaoPauta;
import br.com.jurix.pautaevento.entity.PautaEvento;
import br.com.jurix.pautaevento.entity.QPautaEvento;
import br.com.jurix.pautaevento.enumeration.EnumSituacaoEvento;
import br.com.jurix.pautaevento.filter.PautaEventoFilter;
import br.com.jurix.pautaevento.repository.HistoricoFinalizacaoPautaRepository;
import br.com.jurix.pautaevento.repository.PautaEventoRepository;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import br.com.jurix.security.helper.LoggedUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PautaEventoBO {

    @Value("${jurix.prazoDiasEventoPauta}")
    private Integer prazoDiasEventoPauta;

    @Autowired
    private PautaEventoRepository pautaEventoRepository;

    @Autowired
    private HistoricoFinalizacaoPautaRepository historicoFinalizacaoPautaRepository;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private JurixDateTime jurixDateTime;

    @Transactional
    public PautaEvento save(PautaEvento pautaEvento) {

        pautaEvento.setSituacao(EnumSituacaoEvento.PENDENTE);
        pautaEvento.setUsuarioCriacao(loggedUser.getUsuarioLogado());
        pautaEvento.setDataCriacao(jurixDateTime.getCurrentDateTime());
        pautaEvento.setDataAlteracao(jurixDateTime.getCurrentDateTime());
        pautaEvento.setDataAlteracaoDataLimite(jurixDateTime.getCurrentDateTime());
        return pautaEventoRepository.save(pautaEvento);
    }

    @Transactional
    public PautaEvento update(PautaEvento pautaEvento) {
        PautaEvento pautaEventoExistente = findById(pautaEvento.getId());

        salvarHistoricoFinalizacaoSeMudouSituacao(pautaEvento, pautaEventoExistente);
        reabrirPautaEvento(pautaEvento, pautaEventoExistente);

        BeanUtils.copyProperties(pautaEvento, pautaEventoExistente);
        pautaEventoExistente.setDataAlteracao(jurixDateTime.getCurrentDateTime());

        return pautaEventoRepository.save(pautaEventoExistente);
    }

    public Object findByFilter(PautaEventoFilter filter) {
        filter.setNaoRemovido();
        filter.setUsuario(loggedUser.getUsuarioLogado());
        filter.criarFiltroIntervaloData(jurixDateTime.getCurrentDateTime());

        Page<PautaEvento> pautaEventosPage = pautaEventoRepository.findAll(filter.getMainBooleanExpression(), filter.getPageRequest(), JoinDescriptor.join(QPautaEvento.pautaEvento.usuarioCriacao),
                JoinDescriptor.join(QPautaEvento.pautaEvento.colaborador, QColaborador.colaborador),
                JoinDescriptor.join(QColaborador.colaborador.usuario),
                JoinDescriptor.leftJoin(QPautaEvento.pautaEvento.processo));

        atribuirParametroAVencerParaPautaEvento(pautaEventosPage.getContent());

        return pautaEventosPage;
    }

    public Object findByFilterGeneral(PautaEventoFilter filter) {

        filter.setNaoRemovido();
        filter.criarFiltroIntervaloData(jurixDateTime.getCurrentDateTime());

        Page<PautaEvento> pautaEventosPage = pautaEventoRepository.findAll(filter.getMainBooleanExpression(), filter.getPageRequest(), JoinDescriptor.join(QPautaEvento.pautaEvento.usuarioCriacao),
                JoinDescriptor.join(QPautaEvento.pautaEvento.colaborador, QColaborador.colaborador),
                JoinDescriptor.join(QColaborador.colaborador.usuario),
                JoinDescriptor.leftJoin(QPautaEvento.pautaEvento.processo));

        atribuirParametroAVencerParaPautaEvento(pautaEventosPage.getContent());

        return pautaEventosPage;
    }

    public PautaEvento findById(Long id) {
        return pautaEventoRepository.buscarPorId(id);
    }

    @Transactional
    public PautaEvento finalizar(PautaEvento pautaEvento) {
        PautaEvento pautaEventoExistente = pautaEventoRepository.findOne(pautaEvento.getId());

        pautaEventoExistente.setDataAlteracao(jurixDateTime.getCurrentDateTime());
        pautaEventoExistente.setDataFinalizacao(jurixDateTime.getCurrentDateTime());
        pautaEventoExistente.setFinalizadoAutomaticamente(Boolean.FALSE);
        pautaEventoExistente.setSituacao(pautaEvento.getSituacao());
        pautaEventoExistente.setObservacaoFinalizacao(pautaEvento.getObservacaoFinalizacao());

        salvarHistoricoFinalizacao(pautaEventoExistente);

        return pautaEventoRepository.save(pautaEventoExistente);
    }

    @Transactional
    public void remover(Long pautaEventoId){
        PautaEvento pautaEvento = findById(pautaEventoId);
        pautaEvento.setRemovido(Boolean.TRUE);
        pautaEvento.setDataAlteracao(jurixDateTime.getCurrentDateTime());
        pautaEventoRepository.save(pautaEvento);
    }

    private void salvarHistoricoFinalizacaoSeMudouSituacao(PautaEvento pautaEvento, PautaEvento pautaEventoExistente) {
        if(!pautaEvento.getSituacao().equals(pautaEventoExistente.getSituacao()) && (pautaEvento.getSituacao().equals(EnumSituacaoEvento.FINALIZADO) || pautaEvento.getSituacao().equals(EnumSituacaoEvento.FALHOU))){
            pautaEvento.setDataFinalizacao(jurixDateTime.getCurrentDateTime());
            salvarHistoricoFinalizacao(pautaEvento);
        }
    }

    public void salvarHistoricoFinalizacao(PautaEvento pautaEvento){

        HistoricoFinalizacaoPauta historico = new HistoricoFinalizacaoPauta();
        historico.setObservacaoFinalizacao(pautaEvento.getObservacaoFinalizacao());
        historico.setSituacao(pautaEvento.getSituacao());
        historico.setPautaEvento(pautaEvento);
        historico.setDataFinalizacao(jurixDateTime.getCurrentDateTime());
        historico.setDataLimite(pautaEvento.getDataLimite());
        historico.setDataAlteracaoDataLimite(pautaEvento.getDataAlteracaoDataLimite());
        historico.setDataAlteracao(pautaEvento.getDataAlteracao());
        historicoFinalizacaoPautaRepository.save(historico);
    }

    public List<HistoricoFinalizacaoPauta> historicoFinalizacaoPorEvento(Long pautaEventoId){
        return historicoFinalizacaoPautaRepository.buscarPorEvento(pautaEventoId);
    }

    private void reabrirPautaEvento(PautaEvento pautaEvento, PautaEvento pautaEventoExistente) {

        if(!pautaEvento.getSituacao().equals(EnumSituacaoEvento.PENDENTE) && pautaEvento.getDataLimite().after(pautaEventoExistente.getDataLimite())){

            pautaEvento.setSituacao(EnumSituacaoEvento.PENDENTE);
            pautaEvento.setObservacaoFinalizacao(null);
            pautaEvento.setDataFinalizacao(null);
            pautaEvento.setFinalizadoAutomaticamente(Boolean.FALSE);
            pautaEvento.setDataAlteracaoDataLimite(jurixDateTime.getCurrentDateTime());
        }

    }

    private void atribuirParametroAVencerParaPautaEvento(List<PautaEvento> pautaEventos) {
        for (PautaEvento pautaEvento : pautaEventos) {

            pautaEvento.setAVencer(Boolean.FALSE);
            pautaEvento.setAtrasado(Boolean.FALSE);

            if(JurixDateTime.getStartOfDay(jurixDateTime.getCurrentDateTime()).after(pautaEvento.getDataLimite())){
                pautaEvento.setAtrasado(Boolean.TRUE);
            }else{
                pautaEvento.setAVencer(JurixDateTime.getQuantidadeDiasEntreData(jurixDateTime.getCurrentDateTime(), pautaEvento.getDataLimite()) <= prazoDiasEventoPauta);
            }
        }
    }


}

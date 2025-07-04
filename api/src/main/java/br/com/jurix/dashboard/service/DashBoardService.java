package br.com.jurix.dashboard.service;

import br.com.jurix.cliente.repository.ClienteRepository;
import br.com.jurix.dashboard.dto.DashBoardDTO;
import br.com.jurix.dashboard.dto.ProcessoDashBoardDTO;
import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.pautaevento.repository.PautaEventoRepository;
import br.com.jurix.processo.business.ProcessoBO;
import br.com.jurix.processo.dto.FiltroProcessoRecentesDTO;
import br.com.jurix.processo.entity.Processo;
import br.com.jurix.processo.enumeration.EnumSituacaoProcesso;
import br.com.jurix.processo.filter.ProcessoFilter;
import br.com.jurix.processo.repository.ProcessoRepository;
import br.com.jurix.security.helper.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashBoardService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PautaEventoRepository pautaEventoRepository;

    @Autowired
    private JurixDateTime jurixDateTime;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private ProcessoBO processoBO;

    @Value("${jurix.prazoDiasEventoPauta}")
    private Integer prazoDiasEventoPauta;

    public DashBoardDTO buscarDashBoard(){

        Date dataAtrasado = JurixDateTime.getStartOfDay(jurixDateTime.getCurrentDateTime());
        Date dataLimite = JurixDateTime.adicionarDias(dataAtrasado, prazoDiasEventoPauta);

        DashBoardDTO dashBoardDTO = DashBoardDTO.builder().build();
        dashBoardDTO.setTotalProcessoAtivos(processoRepository.countBySituacaoAndRemovidoFalse(EnumSituacaoProcesso.ATIVO));
        dashBoardDTO.setTotalClientesAtivos(clienteRepository.countByRemovidoFalse());
        dashBoardDTO.setTotalAlertasPauta(pautaEventoRepository.countEventosAtrasadosPertoVencimento(dataLimite, loggedUser.getUsuarioLogado().getId()));

        setProcessos(dashBoardDTO);

        return dashBoardDTO;
    }

    private void setProcessos(DashBoardDTO dashBoardDTO) {

        FiltroProcessoRecentesDTO filtro = processoBO.getFiltroRecente();

        if (Objects.nonNull(filtro)) {

            ProcessoFilter processoFilter = new ProcessoFilter();
            processoFilter.setIds(filtro.getProcessoIds());
            processoFilter.setSize(100);
            Page<Processo> processosBanco = processoRepository.findAll(processoFilter.getMainBooleanExpression(), processoFilter.getPageRequest());
            List<ProcessoDashBoardDTO> processosDTO = processosBanco.getContent().stream().map(this::processoParaDashBoard).collect(Collectors.toList());

            processosDTO.forEach(processoDTO -> {
                processoDTO.setOrdem(filtro.getProcessoIds().indexOf(processoDTO.getId()));
            });
            processosDTO.sort(Comparator.comparing(ProcessoDashBoardDTO::getOrdem));

            dashBoardDTO.setProcessos(processosDTO);
        } else {
            dashBoardDTO.setProcessos(new ArrayList<>());
        }
     }

     public ProcessoDashBoardDTO processoParaDashBoard(Processo processo){
        return ProcessoDashBoardDTO.builder()
                .id(processo.getId())
                .numeroProcesso((Objects.nonNull(processo.getNumero()) ?  processo.getNumero() + " - " : "") + processo.getDescricao())
                .build();
     }
}

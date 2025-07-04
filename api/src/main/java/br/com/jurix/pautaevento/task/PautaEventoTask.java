package br.com.jurix.pautaevento.task;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.pautaevento.business.PautaEventoBO;
import br.com.jurix.pautaevento.entity.PautaEvento;
import br.com.jurix.pautaevento.enumeration.EnumSituacaoEvento;
import br.com.jurix.pautaevento.repository.PautaEventoRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class PautaEventoTask {

    private static final Logger logger = Logger.getLogger(PautaEventoTask.class);

    @Value("${jurix.prazoDiasEventoPauta}")
    private Integer prazoDiasEventoPauta;

    @Autowired
    private PautaEventoRepository pautaEventoRepository;

    @Autowired
    private PautaEventoBO pautaEventoBO;

    @Autowired
    private JurixDateTime jurixDateTime;

    @PostConstruct
    public void falharEventosVencidosNaInicialicacao(){
        this.falharEventosVencidos();
    }

    // segundo, minuto, hora, dia, mês, dia da semana
    // executa todos os dias as 00:01:00
    @Scheduled(cron = "0 30 23 * * *")
    @Transactional
    public void falharEventosVencidos() {

        logger.info("************* Atualizando eventos vencidos **************");


        Date dataReferencia = JurixDateTime.getEndOfDay(JurixDateTime.subtrairDias(jurixDateTime.getCurrentDateTime(), getDiasVencimento()));

        List<PautaEvento> eventosVencidos = pautaEventoRepository.buscarEventosPendentesVencidos(dataReferencia);

        eventosVencidos.forEach(evento -> {
            evento.setSituacao(EnumSituacaoEvento.FALHOU);
            evento.setFinalizadoAutomaticamente(Boolean.TRUE);
            evento.setDataFinalizacao(jurixDateTime.getCurrentDateTime());
            evento.setObservacaoFinalizacao("Finalizado automaticamente após vencido data limite.");

            pautaEventoBO.salvarHistoricoFinalizacao(evento);
        });

        pautaEventoRepository.save(eventosVencidos);


        logger.info("************* Finalizado atualização eventos vencidos **************");
    }

    private Integer getDiasVencimento(){
        return prazoDiasEventoPauta + 1;
    }
}

package br.com.jurix.financeiro.contaspagar.task;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.financeiro.contaspagar.business.ContasPagarBO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class ContasPagarTask {

    private static final Logger logger = Logger.getLogger(ContasPagarTask.class);

    @Autowired
    private ContasPagarBO contasPagarBO;

    @Autowired
    private JurixDateTime jurixDateTime;

    // segundo, minuto, hora, dia, mês, dia da semana
    // executa todos os dias as 00:01:00
    @Scheduled(cron = "0 01 00 * * *")
    @Transactional
    public void criarContasPagarComRecorrencia(){

        logger.info("************* Criando contas a pagar com recorrência **************");

        Date dataReferencia = jurixDateTime.getCurrentDateTime();

        contasPagarBO.criarNovasContasMensais(dataReferencia);
        contasPagarBO.criarNovasContasSemanais(dataReferencia);
        contasPagarBO.criarNovasContasAnuais(dataReferencia);

        logger.info("************* Finalizad criação de contas a pagar com recorrência **************");
    }


}

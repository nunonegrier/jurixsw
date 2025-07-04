package br.com.jurix.processo.dto;

import br.com.jurix.processo.entity.ParteProcesso;
import br.com.jurix.processo.entity.Processo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProcessoDTO {

    private Processo processo;
    private List<ParteProcesso> partesCliente;
    private List<ParteProcesso> partesContraria;
    private List<ParteProcesso> partesRemover;

}


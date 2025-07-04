package br.com.jurix.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashBoardDTO {

    private Integer totalProcessoAtivos = 0;
    private Integer totalClientesAtivos = 0;
    private Integer totalAudiencias = 0;
    private Integer totalAlertasPauta = 0;

    private List<ProcessoDashBoardDTO> processos;
}

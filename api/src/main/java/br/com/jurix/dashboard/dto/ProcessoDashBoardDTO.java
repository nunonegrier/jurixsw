package br.com.jurix.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessoDashBoardDTO {

    private Long id;
    private String numeroProcesso;
    private Integer ordem;
}

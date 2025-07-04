package br.com.jurix.processo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FiltroProcessoRecentesDTO {

    private List<Long> processoIds = new ArrayList<>();
}

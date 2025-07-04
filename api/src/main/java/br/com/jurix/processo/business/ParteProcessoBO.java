package br.com.jurix.processo.business;

import br.com.jurix.processo.entity.ParteProcesso;
import br.com.jurix.processo.filter.ParteProcessoFilter;
import br.com.jurix.processo.repository.ParteProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ParteProcessoBO {

    @Autowired
    private ParteProcessoRepository parteProcessoRepository;

    public Page<ParteProcesso> findByFilter(ParteProcessoFilter parteProcessoFilter) {
        return parteProcessoRepository.findAll(parteProcessoFilter.getMainBooleanExpression(), parteProcessoFilter.getPageRequest());
    }
}

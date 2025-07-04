package br.com.jurix.filter.business;

import br.com.jurix.datetime.business.JurixDateTime;
import br.com.jurix.filter.entity.FiltroUsuario;
import br.com.jurix.filter.enumeration.TipoFiltroUsuarioEnum;
import br.com.jurix.filter.filter.FiltroUsuarioFilter;
import br.com.jurix.filter.repository.FiltroUsuarioRepository;
import br.com.jurix.security.helper.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FiltroUsuarioBO {

    @Autowired
    private FiltroUsuarioRepository filtroUsuarioRepository;

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private JurixDateTime jurixDateTime;

    @Transactional
    public FiltroUsuario save(FiltroUsuario filtroUsuario) {

        filtroUsuario.setUsuarioCriacao(loggedUser.getUsuarioLogado());
        filtroUsuario.setDataCriacao(jurixDateTime.getCurrentDateTime());
        filtroUsuario.setRemovido(Boolean.FALSE);

        atualizarFiltrosPadrao(filtroUsuario);

        return filtroUsuarioRepository.save(filtroUsuario);
    }

    private FiltroUsuario buscarPorId(Long filtroId){
        return filtroUsuarioRepository.findOne(filtroId);
    }

    @Transactional
    public FiltroUsuario update(FiltroUsuario filtroUsuario) {

        FiltroUsuario filtroExistente = buscarPorId(filtroUsuario.getId());
        filtroExistente.setValor(filtroUsuario.getValor());
        filtroExistente.setNome(filtroUsuario.getNome());
        filtroExistente.setPadrao(filtroUsuario.getPadrao());

        atualizarFiltrosPadrao(filtroUsuario);

        return filtroUsuarioRepository.save(filtroExistente);
    }

    public void delete(Long id) {

        FiltroUsuario filtroExistente = buscarPorId(id);
        filtroExistente.setRemovido(Boolean.TRUE);
        filtroUsuarioRepository.save(filtroExistente);
    }

    public Object findById(Long id) {
        return filtroUsuarioRepository.findOne(id);
    }

    public Object findAll(FiltroUsuarioFilter filtroUsuarioFilter) {
        filtroUsuarioFilter.setNaoRemovido();
        filtroUsuarioFilter.setUsuario(loggedUser.getUsuarioLogado());
        return filtroUsuarioRepository.findAll(filtroUsuarioFilter.getMainBooleanExpression(), filtroUsuarioFilter.getPageRequest());
    }

    public FiltroUsuario buscarPadrao(TipoFiltroUsuarioEnum tipo){
        return filtroUsuarioRepository.buscarPadrao(loggedUser.getUsuarioLogado().getId(), tipo);
    }

    private void atualizarFiltrosPadrao(FiltroUsuario filtroUsuario){
        if(filtroUsuario.getPadrao()){
            filtroUsuarioRepository.atribuirPadraoFalse(filtroUsuario.getUsuarioCriacao().getId(), filtroUsuario.getTipo());
        }
    }
}

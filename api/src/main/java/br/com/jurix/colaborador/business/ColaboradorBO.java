package br.com.jurix.colaborador.business;

import br.com.jurix.colaborador.entity.Colaborador;
import br.com.jurix.colaborador.entity.QColaborador;
import br.com.jurix.colaborador.filter.ColaboradorFilter;
import br.com.jurix.colaborador.repository.ColaboradorRepository;
import br.com.jurix.querydsql.descriptor.JoinDescriptor;
import br.com.jurix.security.entity.Usuario;
import br.com.jurix.security.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorBO {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Colaborador save(Colaborador colaborador) {
        Usuario usuarioColaborador = criarUsuarioParaColaborador(colaborador);
        colaborador.setUsuario(usuarioColaborador);

        return colaboradorRepository.save(colaborador);
    }

    @Transactional
    public Colaborador update(Colaborador colaborador) {
        Colaborador colaboradorExistente = findById(colaborador.getId());
        colaborador.getUsuario().setSenha(colaboradorExistente.getUsuario().getSenha());

        BeanUtils.copyProperties(colaborador, colaboradorExistente);
        colaboradorExistente.getUsuario().setNome(colaborador.getUsuario().getNome());
        colaboradorExistente.getUsuario().setEmail(colaborador.getEmailProfissional());

        usuarioRepository.save(colaboradorExistente.getUsuario());

        return colaboradorRepository.save(colaborador);
    }

    public Object findAll(ColaboradorFilter filter) {
        return colaboradorRepository.findAll(filter.getMainBooleanExpression(), filter.getPageRequest(), JoinDescriptor.leftJoin(QColaborador.colaborador.usuario));
    }

    public Colaborador findById(Long id) {
        return colaboradorRepository.buscarPorId(id);
    }

    private Usuario criarUsuarioParaColaborador(Colaborador colaborador) {
        Usuario usuario = new Usuario();
        usuario.setNome(colaborador.getUsuario().getNome());
        usuario.setSenha(bCryptPasswordEncoder.encode(colaborador.getUsuario().getSenha()));
        usuario.setEmail(colaborador.getEmailProfissional());
        usuario.setPerfil(colaborador.getUsuario().getPerfil());

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return usuarioSalvo;
    }

    public void delete(Long id) {
        Colaborador colaborador = findById(id);
        colaborador.setRemovido(Boolean.TRUE);
        colaboradorRepository.save(colaborador);
    }
}

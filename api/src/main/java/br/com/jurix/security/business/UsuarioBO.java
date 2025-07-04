package br.com.jurix.security.business;

import br.com.jurix.security.entity.Usuario;
import br.com.jurix.security.helper.LoggedUser;
import br.com.jurix.security.repository.UsuarioRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Service
public class UsuarioBO {

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public void gerarToken(String email, String url){
        Usuario usuario = usuarioRepository.findByEmail(email);
        if(Objects.isNull(usuario)){
            throw new RuntimeException("Usuário não encontrado");
        }

        String token = bCryptPasswordEncoder.encode(RandomStringUtils.randomAlphanumeric(10).toUpperCase());

        usuario.setTokenRecuperacao(token);
        usuarioRepository.save(usuario);

        enviarEmailRecuperarSenha(usuario, url);
    }

    private void enviarEmailRecuperarSenha(Usuario usuario, String url) {

        try {
            String link = url + "/index.html#/recuperarSenha?token=" + usuario.getTokenRecuperacao();
            String email = usuario.getEmail();
            String titulo = "Recuperação senha JURIX";
            String corpo = "Clique <a href=\"" + link + "\">aqui</a> para recuperar a senha.";

            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(email);
            helper.setSubject(titulo);
            helper.setText(corpo, true);

            javaMailSender.send(msg);
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }

    public void atualizarSenha(String senhaAtual, String novaSenha){

        Usuario usuario = loggedUser.getUsuarioLogado();
        if(!bCryptPasswordEncoder.matches(senhaAtual, usuario.getSenha())){
            throw new RuntimeException("A senha atual está incorreta.");
        }

        usuario.setSenha(bCryptPasswordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
    }

    public void atribuirNovaSenha(String token, String senha) {

        Usuario usuario = usuarioRepository.findByTokenRecuperacao(token);
        if(Objects.isNull(usuario) || Objects.isNull(usuario.getTokenRecuperacao()) || !usuario.getTokenRecuperacao().equals(token)){
            throw new RuntimeException("Token inválido");
        }
        usuario.setSenha(bCryptPasswordEncoder.encode(senha));
        usuario.setTokenRecuperacao(null);
        usuarioRepository.save(usuario);
    }

    public void criar(Usuario usuario) {
        usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));

        usuarioRepository.save(usuario);
    }
}

package br.com.jurix.security.controller;

import br.com.jurix.security.business.UsuarioBO;
import br.com.jurix.security.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioBO usuarioBO;

    @RequestMapping(method = RequestMethod.POST)
    public void criar(@RequestBody Usuario usuario) {
        usuarioBO.criar(usuario);
    }

    @RequestMapping(path = "/atualizarSenha", method = RequestMethod.PUT)
    public void atualizarSenha(@RequestBody Map<String, String> dadosSenha) {
       usuarioBO.atualizarSenha(dadosSenha.get("senhaAtual"), dadosSenha.get("novaSenha"));
    }

    @RequestMapping(path = "/gerarToken", method = RequestMethod.POST)
    public void gerarToken(@RequestBody Map<String, String> dadosSenha, HttpServletRequest request) {
        usuarioBO.gerarToken(dadosSenha.get("email"), request.getHeader("origin"));
    }

    @RequestMapping(path = "/atribuirNovaSenha", method = RequestMethod.POST)
    public void atribuirNovaSenha(@RequestBody Map<String, String> dadosSenha) {
        usuarioBO.atribuirNovaSenha(dadosSenha.get("token"), dadosSenha.get("senha"));
    }
}

package br.com.jurix.security.controller;

import br.com.jurix.security.helper.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
public class UsuarioSessaoController {

    @Autowired
    LoggedUser loggedUer;

    @RequestMapping(path = "/isLogged", method = RequestMethod.GET)
    public void getSession() {

    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Object getUser(){
        return loggedUer.getUsuarioLogado();
    }
}

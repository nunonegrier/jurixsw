package br.com.jurix.security.handler;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = Logger.getLogger(DefaultAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {


        String url = request.getHeader("referer") + "#/login?error";

        response.sendRedirect(url);

        logger.warn("Autenticação inválida para o usuário - " + request.getParameter("username"));
    }
}

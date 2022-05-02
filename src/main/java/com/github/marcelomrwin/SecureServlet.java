package com.github.marcelomrwin;

import org.keycloak.adapters.saml.SamlPrincipal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SecureServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        SamlPrincipal principal = (SamlPrincipal) request.getUserPrincipal();

        response.getWriter().println(principal.getName());
        response.getWriter().println(principal.getAttributes());
        response.getWriter().println(principal.getAssertionDocument());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log("do post");
        response.getWriter().println("do post");
    }
}

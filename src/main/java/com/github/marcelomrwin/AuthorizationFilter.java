package com.github.marcelomrwin;

import org.keycloak.adapters.saml.SamlPrincipal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class AuthorizationFilter implements Filter {

    Properties properties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        InputStream resource = filterConfig.getServletContext().getResourceAsStream("/app-roles.properties");
        properties = new Properties();
        try {
            properties.load(resource);
        } catch (IOException e) {
            throw new ServletException(e);
        }
        System.out.println(this.getClass() + " initiated!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SamlPrincipal principal = (SamlPrincipal) ((HttpServletRequest) request).getUserPrincipal();
        if (principal != null) {
            List<String> roles = principal.getAttributes("Roles");

            Optional<Object> filter = properties.keySet().stream().filter(p -> String.valueOf(p).contains(((HttpServletRequest) request).getServletPath())).findFirst();
            if (filter.isPresent()) {
                if (roles.contains(properties.getProperty((String) filter.get())))
                    chain.doFilter(request, response);
                else
                    ((HttpServletResponse) response).sendRedirect("/no-access");
            } else {
                //no rule for path
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {
        System.out.println(this.getClass() + " destroyed!");
    }
}
package com.github.marcelomrwin;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.keycloak.adapters.saml.servlet.SamlFilter;

import javax.servlet.DispatcherType;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

public class ServerMain {

    public static void main(String[] args) throws Exception {

        // Create a server that listens on port 8080.
        Server server = new Server(8080);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.addServlet(SecureServlet.class, "/secure/*");
        FilterHolder filterHolder = webAppContext.addFilter(SamlFilter.class, "/secure/*", EnumSet.of(DispatcherType.REQUEST));
        filterHolder.setInitParameter("keycloak.config.path", "/keycloak-saml.xml");
        webAppContext.addFilter(filterHolder, "/saml", EnumSet.of(DispatcherType.REQUEST));
        server.setHandler(webAppContext);

        // Load static content from inside the jar file.
        URL webAppDir =
                ServerMain.class.getClassLoader().getResource("META-INF/resources");
        try {
            webAppContext.setResourceBase(webAppDir.toURI().toString());
        } catch (Exception e) {
            URL url = ServerMain.class.getResource("/webapp/keycloak-saml.xml");
            URI baseURI = url.toURI().resolve("./");
            webAppContext.setBaseResource(Resource.newResource(baseURI));
        }

        // Look for annotations in the classes directory (dev server) and in the
        // jar file (live server)
        webAppContext.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/target/classes/|.*\\.jar");

        // Start the server! 🚀
        server.start();
        System.out.println("Server started!");

        // Keep the main thread alive while the server is running.
        server.join();

    }
}

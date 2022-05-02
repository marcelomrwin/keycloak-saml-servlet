package com.github.marcelomrwin;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.keycloak.adapters.saml.servlet.SamlFilter;

import javax.servlet.DispatcherType;
import java.net.URI;
import java.net.URL;
import java.util.EnumSet;

import static io.restassured.RestAssured.get;

public class SamlSecureTest extends AbstractHttpClientServerTest {

    @Rule
    public TestingDir testdir = new TestingDir();

    @Before
    public void setup()throws Exception{
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(SecureServlet.class,"/secure/*");
        FilterHolder filterHolder = context.addFilter(SamlFilter.class, "/secure/*", EnumSet.of(DispatcherType.REQUEST));

        filterHolder.setInitParameter("keycloak.config.path","/keycloak-saml.xml");

        URL url = this.getClass().getResource("/META-INF/resources/keycloak-saml.xml");
        URI baseURI = url.toURI().resolve("./");
        context.setBaseResource(Resource.newResource(baseURI));

        start(context);

        RestAssured.baseURI = scheme + "://localhost";
        RestAssured.port = connector.getLocalPort();
    }

    @Test
    public void should_have_index() throws Exception {
        Response response = get("/secure");
        response.then().statusCode(200);

        System.out.println(response.getBody().asString());
    }

}

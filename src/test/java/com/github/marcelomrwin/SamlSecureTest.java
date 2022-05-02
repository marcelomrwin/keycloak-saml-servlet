package com.github.marcelomrwin;


import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.keycloak.adapters.saml.servlet.SamlFilter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.servlet.DispatcherType;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.EnumSet;

public class SamlSecureTest extends AbstractHttpClientServerTest {

    private static WebDriver driver;

    @BeforeClass
    public static void openBrowser() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public static void cleanUp() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @Before
    public void setup() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(SecureServlet.class, "/secure/*");
        FilterHolder filterHolder = context.addFilter(SamlFilter.class, "/secure/*", EnumSet.of(DispatcherType.REQUEST));
        context.addFilter(filterHolder, "/saml", EnumSet.of(DispatcherType.REQUEST));
        filterHolder.setInitParameter("keycloak.config.path", "/keycloak-saml.xml");

        URL url = this.getClass().getResource("/META-INF/resources/keycloak-saml.xml");
        URI baseURI = url.toURI().resolve("./");
        context.setBaseResource(Resource.newResource(baseURI));

        start(context);

        RestAssured.baseURI = scheme + "://localhost";
        RestAssured.port = connector.getLocalPort();
    }

    @Test
    public void loginTest() throws Exception {
//        Response response = get("/secure");
//        response.then().statusCode(200);
//        System.out.println(response.getBody().asString());

        String url = new StringBuilder("http://localhost:").append(connector.getLocalPort()).append("/secure").toString();
        driver.navigate().to(url);

        WebElement username = driver.findElement(By.id("username"));
        username.clear();
        username.sendKeys("marcelo");

        WebElement password = driver.findElement(By.id("password"));
        password.clear();
        password.sendKeys("123456");

        WebElement btLogin = driver.findElement(By.id("kc-login"));
        btLogin.click();

        System.out.println(driver.getTitle());

    }

}

package com.github.marcelomrwin;

import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.toolchain.test.TestTracker;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.junit.After;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public abstract class AbstractHttpClientServerTest {

    @Rule
    public final TestTracker tracker = new TestTracker();

    protected SslContextFactory sslContextFactory;
    protected String scheme;
    protected Server server;

    protected ServerConnector connector;

    public AbstractHttpClientServerTest() {
        this.scheme = HttpScheme.HTTP.asString();
    }

//    public AbstractHttpClientServerTest(SslContextFactory sslContextFactory) {
//        this.sslContextFactory = sslContextFactory;
//        this.scheme = (sslContextFactory == null ? HttpScheme.HTTP : HttpScheme.HTTPS).asString();
//    }

    public void start(Handler handler) throws Exception {
        startServer(handler);
    }

    protected void startServer(Handler handler) throws Exception {
        if (sslContextFactory != null) {
            sslContextFactory.setEndpointIdentificationAlgorithm("");
            sslContextFactory.setKeyStorePath("src/test/resources/keystore.jks");
            sslContextFactory.setKeyStorePassword("123456");
            sslContextFactory.setTrustStorePath("src/test/resources/keystore.jks");
            sslContextFactory.setTrustStorePassword("123456");
        }

        if (server == null) {
            QueuedThreadPool serverThreads = new QueuedThreadPool();
            serverThreads.setName("server");
            server = new Server(serverThreads);
        }
        connector = new ServerConnector(server, sslContextFactory);
        server.addConnector(connector);
        server.setHandler(handler);
        server.start();
    }


    @After
    public void dispose() throws Exception {
        if (server != null)
            server.stop();
        server = null;
    }
}

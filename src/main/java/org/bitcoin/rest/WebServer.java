package org.bitcoin.rest;

import org.bitcoin.bll.IBusinessLogicLayer;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class WebServer {
    private final IBusinessLogicLayer bll;

    public WebServer(IBusinessLogicLayer bll) {
        this.bll = bll;
    }

    public void run() throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(getJerseyServlet(), "/*");

        server.setHandler(context);

        server.start();
        server.join();
    }

    private ServletHolder getJerseyServlet() {
        ResourceConfig config = new ResourceConfig();

        // Register REST services
        config.register(new PriceResource(bll));

        // Enable cross-origin requests
        config.register(CorsFilter.class);

        return new ServletHolder(new ServletContainer(config));
    }
}

package com.codegence.local_development_client;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


public class WebSocketServer extends WebSocketServlet {
	
	private static final long serialVersionUID = 4736829438880763050L;

	public static void main(String[] args) {
		 Server server = new Server();
	        ServerConnector connector = new ServerConnector(server);
	        connector.setPort(8080);
	        server.addConnector(connector);

	        // Setup the basic application "context" for this application at "/"
	        // This is also known as the handler tree (in jetty speak)
	        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context.setContextPath("/");
	        server.setHandler(context);
	        
	        // Add a websocket to a specific path spec
	        ServletHolder holderEvents = new ServletHolder("ws-events", WebSocketServer.class);
	        context.addServlet(holderEvents, "/events/*");

	        try
	        {
	            server.start();
	            server.dump(System.err);
	            server.join();
	        }
	        catch (Throwable t)
	        {
	            t.printStackTrace(System.err);
	        }
		
	}

	@Override
	public void configure(WebSocketServletFactory factory)
    {
        factory.register(EventSocket.class);
    }
	
}

/*
    Copyright (C) 2014  Gabriel Flores
    This file is part of Codegence.

    Codegence is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Codegence is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Codegence. If not, see <http://www.gnu.org/licenses/>.
*/
package com.codegence.phalanx;

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

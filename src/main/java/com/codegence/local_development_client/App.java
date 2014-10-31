package com.codegence.local_development_client;


import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 * Hello world! ws://<host>:<port>/<application>/local
 *
 */

public class App {

	public static void main(String[] args) {

		Properties prop = new Properties();
		InputStream input = null;
		WebSocketClient client = new WebSocketClient();

		try {
			String filename = "config.properties";
    		input = App.class.getClassLoader().getResourceAsStream(filename);
//			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			String local = prop.getProperty("local");
			String server = prop.getProperty("server");

			if (args.length > 0) {
				server = args[0];
			}
			SimpleEchoSocket socket = new SimpleEchoSocket(local);
			client.start();
			URI echoUri = new URI(server);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(socket, echoUri, request);
			System.out.printf("Connecting to : %s%n", echoUri);
			while(true);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				client.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

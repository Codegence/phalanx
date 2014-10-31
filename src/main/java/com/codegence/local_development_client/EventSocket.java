package com.codegence.local_development_client;
import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EventSocket extends WebSocketAdapter {
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		System.out.println("Socket Connected: " + sess);
		while(true) {
			try {
				getRemote().sendString("{ \"id\": 1, \"uri\" : \"/info\", \"payload\": \"\", \"method\" : \"GET\" }");
				getRemote().sendString("{ \"id\": 2, \"uri\" : \"/sectors/1/factions/2/recyclers/3\", \"payload\": {\"dradisContacts\":[],\"health\":500,\"position\":[12.0,44.0],\"radius\":2.50,\"resources\":50,\"rotation\":0.0,\"status\":\"born\"}  , \"method\" : \"POST\" }");
				getRemote().sendString("{ \"id\": 2, \"uri\" : \"/sectors/1/factions/2/drones/3\", \"payload\": {\"dradisContacts\":[],\"health\":500,\"position\":[12.0,44.0],\"radius\":2.50,\"resources\":50,\"rotation\":0.0,\"status\":\"born\"}  , \"method\" : \"POST\" }");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		System.out.println("Received TEXT message: " + message);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		System.out.println("Socket Closed: [" + statusCode + "] " + reason);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace(System.err);
	}
}
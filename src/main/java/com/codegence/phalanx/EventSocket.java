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

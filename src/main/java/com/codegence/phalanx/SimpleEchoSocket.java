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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleEchoSocket {

	@SuppressWarnings("unused")
	private Session session;
	private String local;

	public SimpleEchoSocket(String local) {
		this.local = local;
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
		this.session = null;
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		System.out.printf("Got connect: %s%n", session);
		this.session = session;
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		System.out.printf("Got msg: %s%n", msg);
		// uri: method: payload:
		ObjectMapper objectMapper = new ObjectMapper();
		String response = "";
		try {
			CodegenceRequest request = objectMapper.readValue(msg.getBytes(), CodegenceRequest.class);
			URL url = new URL(local + request.getUri() );
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(request.getMethod());
			if( !request.getMethod().equals(HttpMethod.GET.asString()) ) {
				con.setRequestProperty("Content-Type", "application/json");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(  objectMapper.writeValueAsBytes(request.getPayload()) );
			}
			int responseCode = con.getResponseCode();

			CodegenceResonse codegenceResonse = new CodegenceResonse();
			codegenceResonse.setStatus(responseCode);
			codegenceResonse.setId(request.getId());
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responsePayload = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responsePayload.append(inputLine);
			}
			in.close();
			codegenceResonse.setPayload(objectMapper.readValue( responsePayload.toString().getBytes(), Object.class) );
			response = objectMapper.writeValueAsString(codegenceResonse);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			session.getRemote().sendString(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

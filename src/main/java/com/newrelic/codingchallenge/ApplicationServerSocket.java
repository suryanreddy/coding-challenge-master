package com.newrelic.codingchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import com.newrelic.logging.handler.LoggerHandler;

public class ApplicationServerSocket extends Thread {

	private static Logger logger = LoggerHandler.getLogger(ApplicationServerSocket.class.getName());

	private Socket clientSocket;
	private PrintWriter serverOutputStream;
	private BufferedReader clientInputReader;
	private ApplicationServer server;

	private final static String TERMINATOR_HINT = "terminate";
	private final static String ACK_HINT = "ack";

	public ApplicationServerSocket(Socket clientSocket, ApplicationServer server) {
		this.clientSocket = clientSocket;
		this.server = server;
	}

	public void run() {
		boolean shutDown = false;
		try {
			serverOutputStream = new PrintWriter(clientSocket.getOutputStream(), true);
			clientInputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String clientInput;
			while ((clientInput = clientInputReader.readLine()) != null) {

				if (clientInput.trim().equalsIgnoreCase(TERMINATOR_HINT)) {
					serverOutputStream.println(TERMINATOR_HINT);
					shutDown = true;
					break;
				} else {
					server.getQueue().add(clientInput);
					serverOutputStream.println(ACK_HINT);
				}
			}
		} catch (IOException e) {
			logger.severe(e.getLocalizedMessage());
		} finally {

			try {
				clientInputReader.close();
				serverOutputStream.close();
			} catch (IOException e) {
				logger.severe(e.getLocalizedMessage());
			}

			if (shutDown) {
				server.shutDown();
			}
		}
	}

}

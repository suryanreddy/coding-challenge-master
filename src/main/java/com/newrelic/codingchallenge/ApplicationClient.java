package com.newrelic.codingchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.newrelic.logging.handler.LoggerHandler;

public class ApplicationClient {

	private static Logger logger = LoggerHandler.getLogger(ApplicationClient.class.getName());

	private Socket clientSocket;

	private PrintWriter clientOutputStream;
	private BufferedReader serverResponseReader;

	private final String lineSeparator = System.getProperty("line.separator");
	private final static String TERMINATOR_HINT = "terminate";

	public void startClientConnection(String ip, int port) throws UnknownHostException, IOException {
		createClientSocket(ip, port);

		reset();
	}

	private void reset() throws IOException {
		clientOutputStream = new PrintWriter(clientSocket.getOutputStream(), true);
		serverResponseReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}

	private void createClientSocket(String ip, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(ip, port);
		clientSocket.setKeepAlive(true);
	}

	public void sendMessage(String message) {

		try {
			terminateClientIfInputInvalid(message);

			if (clientSocket.isClosed()) {
				logger.info("client is closed so not sending message to server.");
				return;
			}

			clientOutputStream.println(message.trim());

			String serverResponse = serverResponseReader.readLine();
			if (TERMINATOR_HINT.equals(serverResponse)) {
				stopConnection();
			}

		} catch (IOException ex) {
			logger.severe(ex.getLocalizedMessage());
		}
	}

	public void terminateClientIfInputInvalid(String message) throws IOException {
		if (null == message || !message.contains(lineSeparator) || message.length() < 10) {
			logger.warning(MessageFormat.format("{0} is invalid.", message));
			stopConnection();
		} else {
			String[] values = message.split(lineSeparator);
			String inputWithoutLineSeparator = values[0];

			Pattern integerPattern = Pattern.compile("\\d+");
			boolean isInteger = integerPattern.matcher(inputWithoutLineSeparator).matches();
			if ((!isInteger || values.length > 1) && !TERMINATOR_HINT.equalsIgnoreCase(inputWithoutLineSeparator)) {
				logger.warning(MessageFormat.format(
						"{0} is invalid. all chars must be digits with followed by new line terminal or it must be 'terminate'.",
						message));
				stopConnection();
			}
		}
	}

	public void stopConnection() throws IOException {
		serverResponseReader.close();
		clientOutputStream.close();
		clientSocket.close();
		logger.info("client is closed...");
	}

}

package com.newrelic.codingchallenge;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import com.newrelic.logging.handler.LoggerHandler;

public class ApplicationServer {

	private static Logger logger = LoggerHandler.getLogger(ApplicationServer.class.getName());

	private ServerSocket server;

	private static int clients = 5;

	private List<Socket> serverSockets = new ArrayList<>();

	private RelicFileUtil relicFileUtil = RelicFileUtil.getRelicFileWritter();

	private ApplicationReport applicationReport = new ApplicationReport();

	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

	private boolean isShutDownInProgress = false;

	public static void main(String[] args) {

		var server = new ApplicationServer();
		try {
			int port = 4000; // TO-DO read port and maximum clients supported from CommandLine.

			server.start(port);
		} catch (IOException e) {
			logger.severe(e.getLocalizedMessage());
		}
	}

	public void start(int port) throws IOException {

		server = new ServerSocket(port);
		logger.info("ApplicationServer started at port: " + port);

		applicationReport.getServerClosed().set(server.isClosed());
		initApplicationReport(applicationReport);
		initQueueProcessor(queue);

		while (!isShutDownInProgress) {

			Socket socket = server.accept();

			if (!isShutDownInProgress && serverSockets.size() < clients) {
				serverSockets.add(socket);

				new ApplicationServerSocket(socket, this).start();
			} else {
				if (isShutDownInProgress) {
					logger.warning("Server shut down is in progress...!!!");
				}
				socket.close();
			}
		}
	}

	private void initQueueProcessor(ConcurrentLinkedQueue<String> queue) {
		new MessageQueueProcessor(applicationReport, queue, this.relicFileUtil).start();
	}

	private void initApplicationReport(ApplicationReport applicationReport) {
		new ApplicationReportStats(applicationReport).start();
	}

	void shutDown() {
		this.isShutDownInProgress = true;
		flushQueue();
		shutDownInitiated();
	}

	private void flushQueue() {
		applicationReport.getShutDownSignaled().set(isShutDownInProgress);
	}

	private void shutDownInitiated() {
		logger.warning("Server shut down is in progress...!!!");
		do {
			// do nothing. This loops until is queue emptied
		} while (this.queue.size() > 0);

		shutDownWhenQueueEmptied();
		logger.warning("Server shut down is completed...!!!");
	}

	private void shutDownWhenQueueEmptied() {
		if (this.queue.size() == 0) {
			try {
				closeAllOpenClients();

				server.close();

				applicationReport.getServerClosed().set(server.isClosed());
			} catch (IOException e) {
				logger.severe(e.getLocalizedMessage());
			}
		}
	}

	private void closeAllOpenClients() {

		serverSockets.forEach(socket -> {
			try {
				socket.close();
			} catch (IOException e) {
				logger.severe(e.getLocalizedMessage());
			}
		});

	}

	ApplicationReport getApplicationReport() {
		return this.applicationReport;
	}

	ConcurrentLinkedQueue<String> getQueue() {
		return this.queue;
	}

	RelicFileUtil getRelicFileUtil() {
		return this.relicFileUtil;
	}

}

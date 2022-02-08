package com.newrelic.codingchallenge;

import java.text.MessageFormat;
import java.util.logging.Logger;

import com.newrelic.logging.handler.LoggerHandler;

public class ApplicationReportStats extends Thread {

	private static Logger logger = LoggerHandler.getLogger(ApplicationReportStats.class.getName());

	private ApplicationReport applicationReport;

	public ApplicationReportStats(ApplicationReport applicationReport) {
		this.applicationReport = applicationReport;
	}

	public void run() {
		while (!applicationReport.getServerClosed().get()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.severe(e.getLocalizedMessage());
			}

			long duplicateCount = applicationReport.getDuplicateNumbersCount().get();
			long uniqueCount = applicationReport.getUniqueNumbersCount().get();
			long totalUniqueCount = applicationReport.getTotalUniqueNumbersCount().get();

			applicationReport.getUniqueNumbersCount().addAndGet(-uniqueCount);
			applicationReport.getDuplicateNumbersCount().addAndGet(-duplicateCount);

			logger.info(MessageFormat.format("Received {0} unique numbers, {1} duplicates. Unique total: {2}",
					uniqueCount, duplicateCount, totalUniqueCount));
		}
		logger.info("Server is closed so, no stats will be reported.");
	}

}

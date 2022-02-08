package com.newrelic.codingchallenge;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.newrelic.logging.handler.LoggerHandler;

public class MessageQueueProcessor extends Thread {

	private static Logger logger = LoggerHandler.getLogger(MessageQueueProcessor.class.getName());

	private ApplicationReport applicationReport;
	private ConcurrentLinkedQueue<String> queue;

	private RelicFileUtil relicFileWritter;

	private Set<String> uniqueMessages = new HashSet<>();

	private AtomicInteger counter = new AtomicInteger();

	private final static String lineSeparator = System.getProperty("line.separator");

	public MessageQueueProcessor(ApplicationReport applicationReport, ConcurrentLinkedQueue<String> queue,
			RelicFileUtil relicFileWritter) {
		this.applicationReport = applicationReport;
		this.queue = queue;
		this.relicFileWritter = relicFileWritter;
	}

	public void run() {

		logger.info("Queue process entered.");
		int batchSize = 1000000;
		while (!applicationReport.getServerClosed().get()) {

			if (applicationReport.getShutDownSignaled().get()) {
				finalWrite(true);
				break;
			}

			if (queue.size() > 0) {

				if (uniqueMessages.size() < batchSize) {

					String msg = queue.poll();
					if (counter.get() == 1) {
						if (uniqueMessages.size() < batchSize) {
							if (!uniqueMessages.add(msg + lineSeparator)) {
								applicationReport.getDuplicateNumbersCount().getAndAdd(1);
							} else {
								applicationReport.getUniqueNumbersCount().getAndAdd(1);
								applicationReport.getTotalUniqueNumbersCount().getAndAdd(1);
							}
						}
					} else {
						if (uniqueMessages.size() < batchSize) {

							boolean isDuplicate = uniqueMessages.contains(msg + lineSeparator);
							if (!isDuplicate) {
								isDuplicate = relicFileWritter.checkDuplicate(msg.trim());
							}

							if (isDuplicate) {
								applicationReport.getDuplicateNumbersCount().getAndAdd(1);
							} else {
								applicationReport.getUniqueNumbersCount().getAndAdd(1);
								applicationReport.getTotalUniqueNumbersCount().getAndAdd(1);

								uniqueMessages.add(msg + lineSeparator);
							}
						} else {
							writeAndIncrementCounter(true);
						}
					}

				} else {
					writeAndIncrementCounter(true);
				}
			}
		}
		logger.info("Queue process ended.");
	}

	private void writeAndIncrementCounter(boolean clearMessages) {
		finalWrite(clearMessages);

		counter.incrementAndGet();
	}

	private void finalWrite(boolean clearMessages) {
		String text = uniqueMessages.toString().replaceAll(", ", "");
		relicFileWritter.write(text.substring(1, text.length() - 1));

		if (clearMessages) {
			uniqueMessages.clear();
		}
	}

}

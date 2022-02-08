package com.newrelic.codingchallenge;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Random;

public class ApplicationClientHelper extends Thread {

	private String threadName;

	public ApplicationClientHelper(String threadName) {
		this.threadName = threadName;
	}

	public void run() {

		try {
			var random = new Random();
			var startTime = System.currentTimeMillis();
			var client = new ApplicationClient();
			client.startClientConnection("127.0.0.1", 4000);
			for (int i = 0; i < 5000; i++) {
				String unique9DigitNumber = String.format("%09d", random.nextInt(1000000000));
				client.sendMessage(unique9DigitNumber + "\n");
			}
			var endTime = System.currentTimeMillis();
			System.out.println(MessageFormat.format("Thread {0} Total Time Taken: {1} seconds", this.threadName,
					(endTime - startTime) / 1000));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

package com.newrelic.codingchallenge;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {

//		System.out.println("Starting up server ....");

		// Add your code here
		Thread thread1 = new ApplicationClientHelper("Client 1");
		Thread thread2 = new ApplicationClientHelper("Client 2");
		Thread thread3 = new ApplicationClientHelper("Client 3");
		Thread thread4 = new ApplicationClientHelper("Client 4");
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		try {
			var client = new ApplicationClient();
			client.startClientConnection("127.0.0.1", 4000);
			client.sendMessage("terminate\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
package com.newrelic.logging.handler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LoggerHandler {
	
	private static Logger loggerHandler = Logger.getLogger(LoggerHandler.class.getName()); 
	
	private static FileHandler fh;
	
	static {
		try {
			String currentDir = System.getProperty("user.dir");
			fh = new FileHandler(currentDir + "/../coding-challenge-master.log");
			fh.setFormatter(new PlainTextFormatter());
		} catch (SecurityException e) {
			loggerHandler.severe(e.getLocalizedMessage());
		} catch (IOException e) {
			loggerHandler.severe(e.getLocalizedMessage());
		}
	}
	
	private LoggerHandler() {
	}

	public static Logger getLogger(String className) {

		Logger logger = Logger.getLogger(className);
		try {
			logger.addHandler(fh);
		} catch (SecurityException e) {
			loggerHandler.severe(e.getLocalizedMessage());
		}

		return logger;
	}

}

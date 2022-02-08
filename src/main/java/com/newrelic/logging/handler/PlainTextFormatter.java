package com.newrelic.logging.handler;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PlainTextFormatter extends Formatter {
	
	private static final String lineSeparator = System.getProperty("line.separator");;

	@Override
	public synchronized String format(LogRecord record) {
		return String.format(formatMessage(record), lineSeparator);
	}

}
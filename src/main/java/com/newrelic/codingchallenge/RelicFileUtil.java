package com.newrelic.codingchallenge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.logging.Logger;

import com.newrelic.logging.handler.LoggerHandler;

public class RelicFileUtil {

	private static Logger logger = LoggerHandler.getLogger(RelicFileUtil.class.getName());

	private static FileWriter fileWriter = null;
	private static RelicFileUtil relicFileWritter = null;
	private static String fileName = null;

	static {
		String currentDir = System.getProperty("user.dir");
		fileName = currentDir + "/.." + "/numbers.log";
		File newFile = new File(fileName);
		try {
			fileWriter = new FileWriter(newFile);
		} catch (IOException e) {
			logger.severe(e.getLocalizedMessage());
		}
	}

	public static RelicFileUtil getRelicFileWritter() {

		if (relicFileWritter == null) {
			relicFileWritter = new RelicFileUtil();
		}

		return relicFileWritter;
	}

	public void write(String text) {
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(text);
		printWriter.close();
	}

	public boolean checkDuplicate(String text) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			Optional<String> textFound = reader.lines().parallel().filter(row -> row.equals(text)).findFirst();
			if (textFound.isPresent()) {
				return true;
			}
		} catch (FileNotFoundException e) {
			logger.severe(e.getLocalizedMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.severe(e.getLocalizedMessage());
				}
			}
		}

		return false;
	}

}

package com.newrelic.codingchallenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageCache {

	private AtomicInteger key = new AtomicInteger();
	private Map<Integer, List<String>> messagesList = new ConcurrentHashMap<>();

	private static MessageCache messageCache = new MessageCache();

	private MessageCache() {
	}

	public static MessageCache getMessageCache() {
		return messageCache;
	}

	public void add(List<String> messages) {
		this.messagesList.put(key.incrementAndGet(), messages);
	}

	public synchronized List<String> getMessagesList() {

		List<String> messages = new ArrayList<>();
		for (Entry<Integer, List<String>> entry : messagesList.entrySet()) {

			messages.addAll(entry.getValue());

			entry.getValue().removeAll(entry.getValue());
		}

		return messages;
	}

}

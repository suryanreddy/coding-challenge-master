package com.newrelic.codingchallenge;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ApplicationReport {

	private AtomicLong uniqueNumbersCount = new AtomicLong();
	private AtomicLong duplicateNumbersCount = new AtomicLong();
	private AtomicLong totalUniqueNumbersCount = new AtomicLong();

	private AtomicLong activeCount = new AtomicLong();

	private AtomicBoolean serverClosed = new AtomicBoolean();
	private AtomicBoolean shutDownSignaled = new AtomicBoolean();

	public AtomicLong getUniqueNumbersCount() {
		return uniqueNumbersCount;
	}

	public void setUniqueNumbersCount(AtomicLong uniqueNumbersCount) {
		this.uniqueNumbersCount = uniqueNumbersCount;
	}

	public AtomicLong getDuplicateNumbersCount() {
		return duplicateNumbersCount;
	}

	public void setDuplicateNumbersCount(AtomicLong duplicateNumbersCount) {
		this.duplicateNumbersCount = duplicateNumbersCount;
	}

	public AtomicLong getTotalUniqueNumbersCount() {
		return totalUniqueNumbersCount;
	}

	public void setTotalUniqueNumbersCount(AtomicLong totalUniqueNumbersCount) {
		this.totalUniqueNumbersCount = totalUniqueNumbersCount;
	}

	public AtomicBoolean getServerClosed() {
		return serverClosed;
	}

	public void setServerClosed(AtomicBoolean serverClosed) {
		this.serverClosed = serverClosed;
	}

	public AtomicBoolean getShutDownSignaled() {
		return shutDownSignaled;
	}

	public void setShutDownSignaled(AtomicBoolean shutDownSignaled) {
		this.shutDownSignaled = shutDownSignaled;
	}

	public AtomicLong getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(AtomicLong activeCount) {
		this.activeCount = activeCount;
	}

	@Override
	public String toString() {
		return "ApplicationReport [UniqueNumbersCount = " + uniqueNumbersCount.get() + ", DuplicateNumbersCount = "
				+ duplicateNumbersCount.get() + ", TotalUniqueNumbersCount = " + totalUniqueNumbersCount.get()
				+ ", ServerClosed = " + serverClosed.get() + ", ShutDownSignaled = " + shutDownSignaled.get() + "]";
	}

}

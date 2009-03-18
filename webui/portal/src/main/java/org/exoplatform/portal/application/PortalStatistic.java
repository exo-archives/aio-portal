package org.exoplatform.portal.application;

public class PortalStatistic {

	private String portalName;
	private final long[] times = new long[1000];
	private final long[] throughput = new long[1000];
	// will be use for number of request in 10 second
	private final long throughputTime = 10;
	// counter varible, for first in first out purpose in times array
	private int counter = 0;;
	private long maxTime = 0;
	private long minTime = 0;
	// length varible, store the length of array
	private int length = 0;

	// count varible, store number of request
	private long countRequest = 0;

	public PortalStatistic(String portalName) {
		this.portalName = portalName;
	}

	public void updateTime(long time) {

		times[counter] = time;
		throughput[counter] = System.currentTimeMillis();
		// if time > max time then put a new max time value
		if (time > maxTime) {
			maxTime = time;
		}
		// generate first value for min time
		if (minTime == 0) {
			minTime = time;
		}
		// if time < min time then put a new min time value
		if (time < minTime) {
			minTime = time;
		}
		counter++;
		length++;
		countRequest++;
		if (counter == times.length) {
			counter = 0;
		}
		if (length >= times.length) {
			length = times.length;
		}
	}

	public double getMaxTime() {
		return ((double) maxTime) / 60;
	}

	public double getMinTime() {
		return ((double) minTime) / 60;
	}

	public double getAverageTime() {
		long sumTime = 0;
		for (int index = 0; index < length; index++) {
			sumTime += times[index];
		}
		return (length == 0) ? 0 : ((double) sumTime) / 60 / length;
	}

	public long viewCount() {
		return countRequest;
	}
	
	public double getThroughput() {
		long now = System.currentTimeMillis();
		int index = length-1;
		long numberRequest = 0;
		while ((index >= 0) && ((now - throughputTime*60) > throughput[index])) {
		   numberRequest++;
		   index--;
		}
		return ((double)numberRequest)/throughputTime;
 
	}
}

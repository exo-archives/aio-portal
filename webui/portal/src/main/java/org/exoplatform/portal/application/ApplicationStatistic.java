package org.exoplatform.portal.application;

public class ApplicationStatistic {
	
	private String appId;
	private final long[] times = new long[1000];
	
	private long maxTime = 0;
	private long minTime = 0;
	
	public ApplicationStatistic(String appId) {
		this.appId = appId;
	}
	
	public void setMaxTime(long time) {
		times[times.length] = time;
		if(time > maxTime) maxTime = time;
	}
}

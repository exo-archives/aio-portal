package org.exoplatform.portal.application;


public class PortalStatistic {
	
	private String portalName;
	private final long[] times = new long[1000];
	
	private long maxTime = 0;
	private long minTime = 0;

	private int counter = 0;
	public PortalStatistic(String portalName) {
		this.portalName = portalName;
	}
	
	public void updateTime(long time) {
		int temp = counter++;
		times[temp] = time;
		if(time > maxTime) maxTime = time;
		if(time < minTime) minTime = time;
	}
	
	public long getMaxTime() { return maxTime; }
}

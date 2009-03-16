package org.exoplatform.portal.application;

import java.util.LinkedList;

public class ApplicationStatistic {
  
  private String appId;
  private final long[] times = new long[1000];
  
  // counter varible, for first in first out purpose in times array
  private int counter = 0;
  private long maxTime = 0;
  private long minTime = 0;
  
  // length varible, store the length of array
  private int length = 0;
  
  // count varible, store number of request
  private long countRequest = 0;
  public ApplicationStatistic(String appId) {
    this.appId = appId;
  }
  
  public void setTime(long time) {
    
    times[counter] = time;
    // if time > max time then put a new max time value
    if(time > maxTime) { 
      maxTime = time;
    }
    // generate first value for min time
    if (minTime == 0) {
      minTime = time; 
    }
    // if time < min time then put a new min time value
    if(time < minTime) {
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
  
  public long getMaxTime(){
    return maxTime;
  }
  
  public long getMinTime(){
    return minTime;
  }
  
  public double getAverageTime() {
    long sumTime = 0;
    for (int index = 0; index < length; index++) {
      sumTime += times[index];
    }
    return (length == 0) ? 0 :sumTime/length;
  }
  
  public long executionCount(){
    return countRequest;
  }
}

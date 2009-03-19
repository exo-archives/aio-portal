package org.exoplatform.portal.application;

import org.exoplatform.portal.application.util.AtomicPositiveLong;
import org.exoplatform.portal.application.util.LongSampler;


/**
 * Created by The eXo Platform SAS
 * Author : tam.nguyen
 *          tam.nguyen@exoplatform.com
 * Mar 17, 2009  
 */

public class ApplicationStatistic {

  /** . */
  private static final int ONE_SECOND = 1000;

  private String        appId;

  private final LongSampler times = new LongSampler(1000);

  private final AtomicPositiveLong maxTime = new AtomicPositiveLong(-1);

  private final AtomicPositiveLong minTime = new AtomicPositiveLong(-1);

  // count varible, store number of request
  private volatile long countRequest = 0;

  public ApplicationStatistic(String appId) {
    this.appId = appId;
  }

  /**
   * Log the time.
   *
   * @param timeMillis the time to log in milliseconds
   */
  public void logTime(long timeMillis) {

    //
    times.add(timeMillis);

    // if time > max time then put a new max time value
    maxTime.setIfGreater(timeMillis);

    // generate first value for min time
    minTime.setIfLower(timeMillis);
  }

  public double getMaxTime() {
    long maxTime = this.maxTime.get();
    if (maxTime == -1) {
      return -1;
    }
    return ((double) maxTime) / 60D;
  }

  public double getMinTime() {
    long minTime = this.minTime.get();
    if (minTime == -1) {
      return -1;
    }
    return ((double) minTime) / 60D;
  }

  public double getAverageTime() {
    return times.average() / 60D;
  }

  /**
   * Compute the throughput.
   *
   * @return the throughput
   */
  public double getThroughput() {
    return times.countAboveThreshold(System.currentTimeMillis() - ONE_SECOND);
  }

  public long executionCount() {
    return countRequest;
  }
}

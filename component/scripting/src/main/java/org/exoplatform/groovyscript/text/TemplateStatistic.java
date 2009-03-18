/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.groovyscript.text;

import org.exoplatform.management.ManagementAware;
import org.exoplatform.management.ManagementContext;

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Mar 17, 2009  
 */

public class TemplateStatistic implements ManagementAware {
  private ManagementContext context;
  
private final long[] times = new long[1000];
  
  private String name;
  // counter varible, for first in first out purpose in times array
  private int counter = 0;
  private long maxTime = 0;
  private long minTime = 0;
  
  // length varible, store the length of array
  private int length = 0;
  
  // count varible, store number of request
  private long countRequest = 0;
  
  public TemplateStatistic(String name) {
    this.name = name;
  }
  public void setTime(long time) {
    
    System.out.println("ggg" + time);
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
  
  public double getMaxTime(){
    return ((double)maxTime)/60;
  }
  
  public double getMinTime(){
    return ((double)minTime)/60;
  }
  
  public double getAverageTime() {
    long sumTime = 0;
    for (int index = 0; index < length; index++) {
      sumTime += times[index];
    }
    return (length == 0) ? 0 :((double)sumTime)/length/60;
  }
  
  public long executionCount(){
    return countRequest;
  }
  
  public void setContext(ManagementContext context) {
    this.context = context;
  }
}

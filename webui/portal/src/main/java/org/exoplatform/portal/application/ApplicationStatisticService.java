/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
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
package org.exoplatform.portal.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.application.registry.Application;
import org.exoplatform.application.registry.ApplicationRegistryService;
import org.exoplatform.management.ManagementAware;
import org.exoplatform.management.ManagementContext;
import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;
import org.picocontainer.Startable;

/**
 * @author <a href="mailto:trongtt@gmail.com">Tran The Trong</a>
 * @version $Revision$
 */
@Managed
@NameTemplate(@Property(key = "service", value = "ApplicationStatistic"))
@ManagedDescription("Application manager")
public class ApplicationStatisticService implements ManagementAware, Startable {

  private ManagementContext                 context;

  private ApplicationRegistryService        appRegistryService;

  private Map<String, ApplicationStatistic> apps = new ConcurrentHashMap<String, ApplicationStatistic>();

  private final String                      ASC  = "ASC";

  private final String                      DESC = "DESC";

  public ApplicationStatisticService(ApplicationRegistryService appRegistryService) {
    this.appRegistryService = appRegistryService;
  }

  public void setContext(ManagementContext context) {
    this.context = context;
  }

  /*
   * returns a list of applicationId sorted alphabetically
   */
  @Managed
  @ManagedDescription("returns a list of applicationId sorted alphabetically")
  public String[] list() {
    List<Application> list = null;
    try {
      list = appRegistryService.getAllApplications();
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<String> appIds = new ArrayList<String>();
    for (Application app : list) {
      appIds.add(app.getId());
    }
    Collections.sort(appIds);
    return appIds.toArray(new String[appIds.size()]);
  }

  /*
   * get ApplicationStatistic by application id, if it isn't exits, create a new one
   */
  public ApplicationStatistic getApplicationStatistic(String appId) {
    ApplicationStatistic app = apps.get(appId);
    if (app == null) {
      app = new ApplicationStatistic(appId);
      apps.put(appId, app);
    }
    return app;
  }

  /*
   * return max time of an specify application
   */
  @Managed
  @ManagedDescription("return max time of an specify application")
  public double getMaxTime(String appId) {
    ApplicationStatistic app = apps.get(appId);
    return app.getMaxTime();
  }

  /*
   * return min time of an specify application
   */
  @Managed
  @ManagedDescription("return min time of an specify application")
  public double getMinTime(String appId) {
    ApplicationStatistic app = apps.get(appId);
    return app.getMinTime();
  }

  /*
   * return average time of an specify application
   */
  @Managed
  @ManagedDescription("return average time of an specify application")
  public double getAverageTime(String appId) {
    ApplicationStatistic app = apps.get(appId);
    return app.getAverageTime();
  }

  /*
   * return count of an specify application
   */
  @Managed
  @ManagedDescription("return count of an specify application")
  public long executionCount(String appId) {
    ApplicationStatistic app = apps.get(appId);
    return app.executionCount();
  }

  /*
   * returns  10 slowest applications
   */
  @Managed
  @ManagedDescription("returns  10 slowest applications")
  public String[] slowestApplications() {
    List<Application> list = null;
    Map application = new HashMap();
    try {
      list = appRegistryService.getAllApplications();
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (Application app : list) {
      ApplicationStatistic appSta = getApplicationStatistic(app.getId());
      // remove application haven't loaded
      if (appSta.getAverageTime() != 0) {
        application.put(app.getId(), appSta.getAverageTime());
      }
    }

    return sort(application, DESC);
  }

  /*
   * returns  10 fastest applications
   */
  @Managed
  @ManagedDescription("returns  10 fastest applications")
  public String[] fastestApplications() {
    List<Application> list = null;
    Map application = new HashMap();
    try {
      list = appRegistryService.getAllApplications();
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (Application app : list) {
      ApplicationStatistic appSta = getApplicationStatistic(app.getId());
      // remove application haven't loaded
      if (appSta.getAverageTime() != 0) {
        application.put(app.getId(), appSta.getAverageTime());
      }
    }

    return sort(application, ASC);
  }

  /*
   * returns  10 most executed applications
   */
  @Managed
  @ManagedDescription("returns  10 most executed applications")
  public String[] mostExecutedApplications() {
    List<Application> list = null;
    Map application = new HashMap();
    try {
      list = appRegistryService.getAllApplications();
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (Application app : list) {
      ApplicationStatistic appSta = getApplicationStatistic(app.getId());
      // remove application haven't loaded
      if (appSta.executionCount() != 0) {
        application.put(app.getId(), appSta.executionCount());
      }
    }

    return sort(application, DESC);
  }

  /*
   * sort map by value asc or desc
   */
  private String[] sort(Map source, String order) {
    String[] app = new String[10];
    List<Object> list = new LinkedList<Object>(source.entrySet());
    if (order.equals(ASC)) {
      Collections.sort(list, new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
          double value1 = Double.parseDouble(((Map.Entry) (o1)).getValue().toString());
          double value2 = Double.parseDouble(((Map.Entry) (o2)).getValue().toString());
          if (value1 > value2) {
            return 1;
          } else if (value1 < value2) {
            return -1;
          } else {
            return 0;
          }
        }
      });
    } else if (order.equals(DESC)) {
      Collections.sort(list, new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
          double value1 = Double.parseDouble(((Map.Entry) (o1)).getValue().toString());
          double value2 = Double.parseDouble(((Map.Entry) (o2)).getValue().toString());
          if (value2 > value1) {
            return 1;
          } else if (value2 < value1) {
            return -1;
          } else {
            return 0;
          }
        }
      });
    }

    int index = 0;
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      app[index] = (String) entry.getKey();
      index++;
      if (index >= app.length) {
        break;
      }
    }
    return app;

  }

  public void start() {
  }

  public void stop() {
  }
}

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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.annotations.ManagedName;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;
import org.exoplatform.resolver.ResourceResolver;

/**
 * Created by The eXo Platform SAS Author : tam.nguyen tamndrok@gmail.com Mar
 * 17, 2009
 */

@Managed
@NameTemplate(@Property(key = "service", value = "templatestatistic"))
@ManagedDescription("Template statistic service")
public class TemplateManaged {

  private Map<String, TemplateStatistic> apps = new ConcurrentHashMap<String, TemplateStatistic>();

  private final String                   ASC  = "ASC";

  private final String                   DESC = "DESC";

  private TemplateService service;

  public TemplateManaged(TemplateService service) {
    this.service = service;
    service.managed = this;
  }

  /*
   * get TemplateStatistic by name, if TemplateStatistic isn't exits, create a
   * new one.
   */
  public TemplateStatistic getTemplateStatistic(String name) {
    TemplateStatistic app = apps.get(name);
    if (app == null) {
      app = new TemplateStatistic(name);
      apps.put(name, app);
    }
    return app;
  }

  /*
   * returns a list of templateId sorted alphabetically
   */
  @Managed
  @ManagedDescription("The list of template identifiers sorted alphabetically")
  public String[] getTemplateList() {
    List<Object> list = new LinkedList<Object>(apps.entrySet());
    String[] app = new String[list.size()];
    int index = 0;
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      app[index] = (String) entry.getKey();
      index++;
    }
    return app;
  }

  /*
   * Clear the templates cache
   */
  @Managed
  @ManagedDescription("Clear the template cache")
  public void reload() {
    try {
      service.getTemplatesCache().clearCache();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * Clear the template cache by name
   */
  @Managed
  @ManagedDescription("Clear the template cache for a specified template identifier")
  public void reload(@ManagedDescription("The template id") @ManagedName("templateId") String name) {
    try {
      TemplateStatistic app = apps.get(name);
      ResourceResolver resolver = app.getResolver();
      service.getTemplatesCache().remove(resolver.createResourceId(name));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*
   * return max time of an specify template
   */
  @Managed
  @ManagedDescription("The maximum rendering time of a specified template in seconds")
  public double getMaxTime(@ManagedDescription("The template id") @ManagedName("templateId") String name) {
    TemplateStatistic app = apps.get(name);
    return toSeconds(app.getMaxTime());
  }

  /*
   * return min time of an specify template
   */
  @Managed
  @ManagedDescription("The minimum rendering time of a specified template in seconds")
  public double getMinTime(@ManagedDescription("The template id") @ManagedName("templateId") String name) {
    TemplateStatistic app = apps.get(name);
    return toSeconds(app.getMinTime());
  }

  /*
   * return count of an specify template
   */
  @Managed
  @ManagedDescription("The rendering count of a specified template")
  public long getExecutionCount(@ManagedDescription("The template id") @ManagedName("templateId") String name) {
    TemplateStatistic app = apps.get(name);
    return app.executionCount();
  }

  /*
   * return average time of an specify template
   */
  @Managed
  @ManagedDescription("The average rendering time of a specified template in seconds")
  public double getAverageTime(@ManagedDescription("The template id") @ManagedName("templateId") String name) {
    TemplateStatistic app = apps.get(name);
    return toSeconds(app.getAverageTime());
  }

  /*
   * returns 10 slowest template
   */
  @Managed
  @ManagedDescription("The list of the 10 slowest templates")
  public String[] getSlowestTemplate() {

    Map application = new HashMap();
    List<Object> list = new LinkedList<Object>(apps.entrySet());
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      String url = (String) entry.getKey();
      application.put(url, getAverageTime(url));
    }

    return sort(application, DESC);
  }

  /*
   * returns 10 slowest template
   */
  @Managed
  @ManagedDescription("The list of the 10 most executed templates")
  public String[] getMostExecutedTemplate() {

    Map application = new HashMap();
    List<Object> list = new LinkedList<Object>(apps.entrySet());
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      String url = (String) entry.getKey();
      application.put(url, getExecutionCount(url));
    }

    return sort(application, DESC);
  }

  /*
   * returns 10 fastest template
   */
  @Managed
  @ManagedDescription("The list of the 10 fastest templates")
  public String[] getFastestTemplates() {

    Map application = new HashMap();
    List<Object> list = new LinkedList<Object>(apps.entrySet());
    for (Iterator it = list.iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      String url = (String) entry.getKey();
      application.put(url, getAverageTime(url));
    }

    return sort(application, ASC);
  }

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

  private double toSeconds(double value) {
    return value == -1 ? -1 : value / 1000D;
  }
}

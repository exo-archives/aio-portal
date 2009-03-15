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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.management.ManagementAware;
import org.exoplatform.management.ManagementContext;
import org.exoplatform.management.annotations.Managed;
import org.exoplatform.management.annotations.ManagedDescription;
import org.exoplatform.management.jmx.annotations.NameTemplate;
import org.exoplatform.management.jmx.annotations.Property;

/**
 * @author <a href="mailto:trongtt@gmail.com">Tran The Trong</a>
 * @version $Revision$
 */
@Managed
@NameTemplate(@Property(key="service", value="PortalStatistic"))
@ManagedDescription("Application manager")
public class PortalStatisticService implements ManagementAware {

  private ManagementContext context;

  private Map<String, PortalStatistic> apps = new ConcurrentHashMap<String, PortalStatistic>();
  
  public void setContext(ManagementContext context) {
    this.context = context;
  }
  
  public PortalStatistic getPortalStatistic(String appId) {
  	PortalStatistic app = apps.get(appId);
  	if(app == null) {
  		app = new PortalStatistic(appId);
  		apps.put(appId, app);
  	}
  	return app;
	}
  
  @Managed
  public long getMaxTime(String id) {
  	return apps.get(id).getMaxTime();
  }

  @Managed
  public long getMinTime(String id) {
  	return apps.get(id).getMinTime();
  }
  
  @Managed
  public double getAverageTime(String id) {
  	return apps.get(id).getAverageTime() ;
  }
}

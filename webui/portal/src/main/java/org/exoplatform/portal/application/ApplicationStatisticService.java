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

/**
 * @author <a href="mailto:trongtt@gmail.com">Tran The Trong</a>
 * @version $Revision$
 */
@Managed
@NameTemplate(@Property(key="service", value="ApplicationStatistic"))
@ManagedDescription("Application manager")
public class ApplicationStatisticService implements ManagementAware {

  private ManagementContext context;

  private ApplicationRegistryService appRegistryService;

  private Map<String, ApplicationStatistic> apps = new ConcurrentHashMap<String, ApplicationStatistic>();
  
  public ApplicationStatisticService(ApplicationRegistryService appRegistryService) {
    this.appRegistryService = appRegistryService;
  }

  public void setContext(ManagementContext context) {
    this.context = context;
  }
  
  @Managed
  @ManagedDescription("returns a list of applicationId sorted alphabetically")
  public String[] list() {
    List<Application> list = null;
		try {
			list = appRegistryService.getAllApplications();
		} catch (Exception e) {
			e.printStackTrace();
		}
    List<String> appIds = new ArrayList<String>() ;
    for(Application app : list) {
    	appIds.add(app.getId()) ;
    }
    Collections.sort(appIds) ;
    return appIds.toArray(new String[list.size()]) ;
  }
  
  public ApplicationStatistic getApplicationStatistic(String appId) {
  	ApplicationStatistic app = apps.get(appId);
  	if(app == null) {
  		app = new ApplicationStatistic(appId);
  		apps.put(appId, app);
  	}
  	return app;
	}
}

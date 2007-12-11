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
package org.exoplatform.portal.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Widgets;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserEventListener;

/**
 * Created by The eXo Platform SAS
 * May 29, 2007  
 */
public class UserPortalConfigListener extends UserEventListener {
  
  public void preDelete(User user) throws Exception {
    PortalContainer container  = PortalContainer.getInstance() ;
    UserPortalConfigService portalConfigService = 
      (UserPortalConfigService)container.getComponentInstanceOfType(UserPortalConfigService.class) ;
    DataStorage dataStorage = (DataStorage)container.getComponentInstanceOfType(DataStorage.class) ;
    String userName = user.getUserName() ;
    
    Query<Page> query = new Query<Page>(PortalConfig.USER_TYPE, userName, Page.class) ;
    PageList pageList = dataStorage.find(query) ;
    pageList.setPageSize(10) ;
    int i =  1;
    while(i <= pageList.getAvailablePage()) {
      List<?> list = pageList.getPage(i) ;
      Iterator<?> iterator = list.iterator() ;
      while(iterator.hasNext()) portalConfigService.remove((Page) iterator.next()) ;
      i++;
    }
    
    Query<PortletPreferences> portletPrefQuery = 
      new Query<PortletPreferences>(PortalConfig.USER_TYPE, userName, PortletPreferences.class) ;
    pageList = dataStorage.find(portletPrefQuery) ;
    i = 1 ;
    while(i <= pageList.getAvailablePage()) {
      List<?> list = pageList.getPage(i) ;
      Iterator<?> iterator = list.iterator() ;
      while(iterator.hasNext()) dataStorage.remove((PortletPreferences)iterator.next()) ;
      i++ ;
    }
   
    String id = PortalConfig.USER_TYPE + "::" + userName ;
    PageNavigation navigation = dataStorage.getPageNavigation(id) ;
    if (navigation != null) portalConfigService.remove(navigation) ;

    Widgets widgets = dataStorage.getWidgets(id) ;
    if (widgets != null) portalConfigService.remove(widgets);
  }
  @SuppressWarnings("unused")
  public void preSave(User user, boolean isNew) throws Exception {
    PortalContainer container  = PortalContainer.getInstance() ;
    UserPortalConfigService portalConfigService = 
      (UserPortalConfigService)container.getComponentInstanceOfType(UserPortalConfigService.class) ;
    DataStorage dataStorage = (DataStorage)container.getComponentInstanceOfType(DataStorage.class) ;
    String userName = user.getUserName() ;
    String id = PortalConfig.USER_TYPE + "::" + userName ;
    PageNavigation navigation = dataStorage.getPageNavigation(id) ;
    if (navigation != null) return;
    PageNavigation pageNav = new PageNavigation();
    pageNav.setOwnerType(PortalConfig.USER_TYPE);
    pageNav.setOwnerId(userName);
    pageNav.setPriority(5);
    pageNav.setNodes(new ArrayList<PageNode>());
    portalConfigService.create(pageNav);
    
    Widgets widgets = new Widgets() ;
    widgets.setOwnerType(PortalConfig.USER_TYPE) ;
    widgets.setOwnerId(userName) ;
    widgets.setChildren(new ArrayList<Container>()) ;
    portalConfigService.create(widgets) ;
  }
}

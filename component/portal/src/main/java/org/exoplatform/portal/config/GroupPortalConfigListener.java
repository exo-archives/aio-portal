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

import java.util.Iterator;
import java.util.List;

import org.exoplatform.commons.utils.LazyPageList;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import org.exoplatform.services.jcr.ext.registry.RegistryService;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * May 29, 2007  
 */
public class GroupPortalConfigListener extends GroupEventListener {

  public void preDelete(Group group) throws Exception {
    ExoContainer container  = ExoContainerContext.getCurrentContainer();
    UserPortalConfigService portalConfigService =
      (UserPortalConfigService)container.getComponentInstanceOfType(UserPortalConfigService.class) ;
    DataStorage dataStorage = (DataStorage)container.getComponentInstanceOfType(DataStorage.class) ;
    String groupId = group.getId().trim();
    if(groupId.charAt(0)=='/') groupId = groupId.substring(1);
    Query<Page> pageQuery = new Query<Page>(PortalConfig.GROUP_TYPE, groupId,  Page.class) ;
    LazyPageList pageList = dataStorage.find(pageQuery) ;

    int i = 1 ;
    while(i <= pageList.getAvailablePage()) {
      List<?> list = pageList.getPage(i) ;
      Iterator<?> iterator = list.iterator() ;
      while(iterator.hasNext()){
        portalConfigService.remove((Page)iterator.next() ) ;
      }
      i++ ;
    }

    Query<PortletPreferences> portletPrefQuery =
      new Query<PortletPreferences>(PortalConfig.GROUP_TYPE, groupId, PortletPreferences.class) ;
    pageList = dataStorage.find(portletPrefQuery) ;
    i = 1 ;
    while(i <= pageList.getAvailablePage()) {
      List<?> list = pageList.getPage(i) ;
      Iterator<?> iterator = list.iterator() ;
      while(iterator.hasNext()) dataStorage.remove((PortletPreferences)iterator.next()) ;
      i++ ;
    }

    PageNavigation navigation = dataStorage.getPageNavigation(PortalConfig.GROUP_TYPE, groupId) ;
    if (navigation != null) portalConfigService.remove(navigation) ;
  }

  @Override
  public void preSave(Group group, boolean isNew) throws Exception {
    ExoContainer container = ExoContainerContext.getCurrentContainer();
    /*
     * TODO Call start method on RegistryService to allow ecm, ultimate can run
     * with JDK6. This is uncommon behavior. We need find other way to fix it I
     * hope that this issues will be fixed when we use the lastest version of
     * PicoContainer Comment by Hoa Pham.
     */
    RegistryService registryService = (RegistryService) container.getComponentInstanceOfType(RegistryService.class);
    registryService.start();
    DataStorage dataStorage = (DataStorage) container.getComponentInstanceOfType(DataStorage.class);
    String groupId = group.getId();

    // Bug in hibernate org service implementation
    if (groupId == null) {
      groupId = "/" + group.getGroupName();
    }

    // Bug in JCR org service implementation
    if ("/administrators".equals(groupId)) {
      groupId = "/platform/administrators";
    } else if ("/users".equals(groupId)) {
      groupId = "/platform/users";
    } else if ("/guests".equals(groupId)) {
      groupId = "/platform/guests";
    } else if ("/management".equals(groupId)) {
      groupId = "/organization/management";
    } else if ("/executive-board".equals(groupId)) {
      groupId = "/organization/management/executive-board";
    } else if ("/human-resources".equals(groupId)) {
      groupId = "/organization/management/human-resources";
    } else if ("/communication".equals(groupId)) {
      groupId = "/organization/communication";
    } else if ("/marketing".equals(groupId)) {
      groupId = "/organization/communication/marketing";
    } else if ("/press-and-media".equals(groupId)) {
      groupId = "/organization/communication/press-and-media";
    } else if ("/operations".equals(groupId)) {
      groupId = "/organization/operations";
    } else if ("/sales".equals(groupId)) {
      groupId = "/organization/operations/sales";
    } else if ("/finances".equals(groupId)) {
      groupId = "/organization/operations/finances";
    }

    // Need to insert the corresponding user site
    PortalConfig cfg = dataStorage.getPortalConfig(PortalConfig.GROUP_TYPE, groupId);
    if (cfg == null) {
      cfg = new PortalConfig(PortalConfig.GROUP_TYPE);
      cfg.setName(groupId);
      dataStorage.create(cfg);
    }
  }
}

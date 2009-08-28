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
package org.exoplatform.portal.pom.config;

import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.config.Query;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.SiteBody;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.commons.utils.LazyPageList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.exoplatform.portal.pom.config.tasks.PageTask;
import org.exoplatform.portal.pom.config.tasks.PortalConfigTask;
import org.exoplatform.portal.pom.config.tasks.PageNavigationTask;
import org.exoplatform.portal.pom.config.tasks.PortletPreferencesTask;
import org.exoplatform.portal.pom.config.tasks.SearchTask;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class POMDataStorage implements DataStorage {

  /** . */
  private final POMSessionManager pomMgr;

  public POMDataStorage(POMSessionManager pomMgr) {
    this.pomMgr = pomMgr;
  }

  private <T extends AbstractPOMTask> T execute(T task) throws Exception {
    pomMgr.execute(task);
    return task;
  }

  public POMSessionManager getPOMSessionManager() {
    return pomMgr;
  }

  public PortalConfig getPortalConfig(String portalName) throws Exception {
    return execute(new PortalConfigTask.Load(PortalConfig.PORTAL_TYPE, portalName)).getConfig();
  }

  public PortalConfig getPortalConfig(String ownerType, String portalName) throws Exception {
    return execute(new PortalConfigTask.Load(ownerType, portalName)).getConfig();
  }

  public void create(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Save(config, true));
  }

  public void save(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Save(config, true));
  }

  public void remove(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Remove(config.getType(), config.getName()));
  }

  public Page getPage(String pageId) throws Exception {
    return execute(new PageTask.Load(pageId)).getPage();
  }

  public void remove(Page page) throws Exception {
    execute(new PageTask.Remove(page));
  }

  public void create(Page page) throws Exception {
    execute(new PageTask.Save(page));
  }

  public void save(Page page) throws Exception {
    execute(new PageTask.Save(page));
  }

  public PageNavigation getPageNavigation(String fullId) throws Exception {
    return execute(new PageNavigationTask.Load(fullId)).getPageNavigation();
  }

  public PageNavigation getPageNavigation(String ownerType, String id) throws Exception {
    return execute(new PageNavigationTask.Load(ownerType + "::" + id)).getPageNavigation();
  }

  public void save(PageNavigation navigation) throws Exception {
    execute(new PageNavigationTask.Save(navigation, true));
  }

  public void create(PageNavigation navigation) throws Exception {
    execute(new PageNavigationTask.Save(navigation, false));
  }

  public void remove(PageNavigation navigation) throws Exception {
    execute(new PageNavigationTask.Remove(navigation));
  }

  public void save(PortletPreferences portletPreferences) throws Exception {
    execute(new PortletPreferencesTask.Save(portletPreferences));
  }

  public PortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    return execute(new PortletPreferencesTask.Load(windowID)).getPreferences();
  }

  public void remove(PortletPreferences portletPreferences) throws Exception {
    execute(new PortletPreferencesTask.Remove(portletPreferences));
  }

  public <T> LazyPageList<T> find(Query<T> q) throws Exception {
    return find(q, null);
  }

  public <T> LazyPageList<T> find(Query<T> q, Comparator<T> sortComparator) throws Exception {
    if (Page.class.equals(q.getClassType())) {
      return (LazyPageList<T>)execute(new SearchTask.FindPage((Query<Page>)q)).getResult();
    } else if (PageNavigation.class.equals(q.getClassType())) {
      return (LazyPageList<T>)execute(new SearchTask.FindNavigation((Query<PageNavigation>)q)).getResult();
    } else if (PortletPreferences.class.equals(q.getClassType())) {
      return (LazyPageList<T>)execute(new SearchTask.FindPortletPreferences((Query<PortletPreferences>)q)).getResult();
    } else if (PortalConfig.class.equals(q.getClassType())) {
      return (LazyPageList<T>)execute(new SearchTask.FindSite((Query<PortalConfig>)q)).getResult();
    } else {
      throw new UnsupportedOperationException();
    }
  }
  
  public Container getSharedLayout() {
    Container sharedLayout = new Container();
    sharedLayout.setTemplate("system:/groovy/portal/webui/container/UIContainer.gtmpl");
    Container toolbarContainer = new Container();
    toolbarContainer.setTemplate("system:/groovy/portal/webui/container/UIToolbarContainer.gtmpl");
    toolbarContainer.setAccessPermissions(new String[]{"*:/platform/administrators", "*:/organization/management/executive-board"});
    
    Container starToolBarPortletContainer = new Container();
    starToolBarPortletContainer.setId("StarToolBarPortlet");
    starToolBarPortletContainer.setTemplate("system:/groovy/portal/webui/container/UIContainer.gtmpl");
    Application starToolBarPortletApplication = new Application();
    starToolBarPortletApplication.setInstanceId("portal#classic:/exoadmin/StarToolbarPortlet/starportlet");
    starToolBarPortletApplication.setAccessPermissions(new String[] {"Everyone"});
    starToolBarPortletApplication.setShowInfoBar(false);
    starToolBarPortletContainer.getChildren().add(starToolBarPortletApplication);
    toolbarContainer.getChildren().add(starToolBarPortletContainer);
    
    Container userToolBarPortletContainer = new Container();
    userToolBarPortletContainer.setId("UserToolBarPortlet");
    userToolBarPortletContainer.setTemplate("system:/groovy/portal/webui/container/UIContainer.gtmpl");
    Application userToolBarPortletApplication = new Application();
    userToolBarPortletApplication.setInstanceId("portal#classic:/exoadmin/UserToolbarPortlet/userportlet");
    userToolBarPortletApplication.setAccessPermissions(new String[] {"Everyone"});
    userToolBarPortletApplication.setShowInfoBar(false);
    userToolBarPortletContainer.getChildren().add(userToolBarPortletApplication);
    toolbarContainer.getChildren().add(userToolBarPortletContainer);
    
    Container adminToolBarPortletContainer = new Container();
    adminToolBarPortletContainer.setId("AdminToolBarPortlet");
    adminToolBarPortletContainer.setTemplate("system:/groovy/portal/webui/container/UIContainer.gtmpl");
    Application adminToolBarPortletApplication = new Application();
    adminToolBarPortletApplication.setInstanceId("portal#classic:/exoadmin/AdminToolbarPortlet/adminportlet");
    adminToolBarPortletApplication.setAccessPermissions(new String[] {"Everyone"});
    adminToolBarPortletApplication.setShowInfoBar(false);
    adminToolBarPortletContainer.getChildren().add(adminToolBarPortletApplication);
    toolbarContainer.getChildren().add(adminToolBarPortletContainer);
    
    ArrayList<Object> children = sharedLayout.getChildren();
    children.add(toolbarContainer);
    children.add(new SiteBody());
    return sharedLayout;
  }
}

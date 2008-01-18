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
package org.exoplatform.portal.config.jcr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.exoplatform.commons.utils.ObjectPageList;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.config.Query;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Widgets;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SARL
 * Author : Tung Pham
 *          thanhtungty@gmail.com
 * Nov 14, 2007  
 */
public class DataStorageImpl implements DataStorage, Startable {
  
  final private static String PORTAL_DATA = "MainPortalData" ;
  final private static String USER_DATA = "UserPortalData";
  final private static String GROUP_DATA = "SharedPortalData";
  
  final private static String PORTAL_CONFIG_FILE_NAME = "portal-xml" ;
  final private static String NAVIGATION_CONFIG_FILE_NAME = "navigation-xml" ;
  final private static String WIDGETS_CONFIG_FILE_NAME = "widgets-xml" ;
  final private static String PAGE_SET_NODE = "pages" ;
  final private static String PORTLET_PREFERENCES_SET_NODE = "portletPreferences" ;

  private RegistryService regService_ ;
  private DataMapper mapper_ = new DataMapper() ;
  
  public DataStorageImpl(RegistryService service) throws Exception {
    regService_ = service ;
  }

  public PortalConfig getPortalConfig(String portalName) throws Exception {
    String portalPath = getApplicationRegistryPath(PortalConfig.PORTAL_TYPE, portalName)
                        + "/"  + PORTAL_CONFIG_FILE_NAME;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry portalEntry ;
    try {
      portalEntry = regService_.getEntry(sessionProvider, portalPath) ;
    } catch (ItemNotFoundException ie) {
      sessionProvider.close() ;
      return null ;
    }
    PortalConfig config = mapper_.toPortalConfig(portalEntry.getDocument()) ;
    sessionProvider.close() ;
    return config ;
  }
  
  public void create(PortalConfig config) throws Exception {
    String portalAppPath = getApplicationRegistryPath(PortalConfig.PORTAL_TYPE, config.getName()) ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry portalEntry = new RegistryEntry(PORTAL_CONFIG_FILE_NAME) ;
    mapper_.map(portalEntry.getDocument(), config) ;
    regService_.createEntry(sessionProvider, portalAppPath, portalEntry) ;
    sessionProvider.close() ;
  }
  
  public void save(PortalConfig config) throws Exception {
    String portalAppPath = getApplicationRegistryPath(PortalConfig.PORTAL_TYPE, config.getName()) ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry portalEntry = regService_.getEntry(sessionProvider, portalAppPath + "/" + PORTAL_CONFIG_FILE_NAME) ;
    mapper_.map(portalEntry.getDocument(), config) ;
    regService_.recreateEntry(sessionProvider, portalAppPath, portalEntry) ;
    sessionProvider.close() ;
  }

  public void remove(PortalConfig config) throws Exception {
    String portalPath = getApplicationRegistryPath(PortalConfig.PORTAL_TYPE, config.getName())
                        + "/"  + PORTAL_CONFIG_FILE_NAME;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    regService_.removeEntry(sessionProvider, portalPath) ;
    sessionProvider.close() ;
  }

  public Page getPage(String pageId) throws Exception {
    String[] fragments = pageId.split("::") ;
    if(fragments.length < 3) {
      throw new Exception("Invalid PageId: " + "[" + pageId + "]") ;
    }
    String pagePath = getApplicationRegistryPath(fragments[0], fragments[1])
                      + "/" + PAGE_SET_NODE + "/" + fragments[2] ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry pageEntry ;
    try {
      pageEntry = regService_.getEntry(sessionProvider, pagePath) ;      
    } catch (ItemNotFoundException ie) {
      sessionProvider.close() ;
      return null ;
    }
    Page page = mapper_.toPageConfig(pageEntry.getDocument()) ;
    sessionProvider.close() ;
    return page ;
  }
  
  public void create(Page page) throws Exception {
    String[] fragments = page.getPageId().split("::") ;
    String pageSetPath = getApplicationRegistryPath(fragments[0], fragments[1])
                         + "/" + PAGE_SET_NODE ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry pageEntry = new RegistryEntry(page.getName()) ;
    mapper_.map(pageEntry.getDocument(), page) ;
    regService_.createEntry(sessionProvider, pageSetPath, pageEntry) ;
    sessionProvider.close() ;
  }
  

  public void save(Page page) throws Exception {
    String[] fragments = page.getPageId().split("::") ;
    String pageSetPath = getApplicationRegistryPath(fragments[0], fragments[1]) + "/" + PAGE_SET_NODE ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry pageEntry = regService_.getEntry(sessionProvider, pageSetPath + "/" + page.getName()) ;
    mapper_.map(pageEntry.getDocument(), page) ;
    regService_.recreateEntry(sessionProvider, pageSetPath, pageEntry) ;
    sessionProvider.close() ;
  }
  
  public void remove(Page page) throws Exception {
    String[] fragments = page.getPageId().split("::") ;
    String pagePath = getApplicationRegistryPath(fragments[0], fragments[1])
                      + "/" + PAGE_SET_NODE + "/" + page.getName() ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    regService_.removeEntry(sessionProvider, pagePath) ;
    sessionProvider.close() ;
  }
  
  public PageNavigation getPageNavigation(String id) throws Exception {
    String[] fragments = id.split("::") ;
    if(fragments.length < 2) {
      throw new Exception("Invalid PageNavigation Id: " + "[" + id + "]") ;
    }
    String navigationPath = getApplicationRegistryPath(fragments[0], fragments[1])
                            + "/" + NAVIGATION_CONFIG_FILE_NAME ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry navigationEntry ;
    try {      
     navigationEntry = regService_.getEntry(sessionProvider, navigationPath) ;
    } catch (ItemNotFoundException ie) {
      sessionProvider.close() ;
      return null ;
    }
    PageNavigation navigation = mapper_.toPageNavigation(navigationEntry.getDocument()) ;
    sessionProvider.close() ;
    return navigation ;
  }
  
  public void create(PageNavigation navigation) throws Exception {
    String appRegPath = getApplicationRegistryPath(navigation.getOwnerType(), navigation.getOwnerId()) ; 
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry NavigationEntry = new RegistryEntry(NAVIGATION_CONFIG_FILE_NAME) ;
    mapper_.map(NavigationEntry.getDocument(), navigation) ;
    regService_.createEntry(sessionProvider, appRegPath, NavigationEntry) ;
    sessionProvider.close() ;
  }

  public void save(PageNavigation navigation) throws Exception {
    String appRegPath = getApplicationRegistryPath(navigation.getOwnerType(), navigation.getOwnerId()) ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry navigationEntry = regService_.getEntry(sessionProvider, appRegPath + "/" + NAVIGATION_CONFIG_FILE_NAME ) ;
    mapper_.map(navigationEntry.getDocument(), navigation) ;
    regService_.recreateEntry(sessionProvider, appRegPath, navigationEntry) ;
    sessionProvider.close() ;
  }
  
  public void remove(PageNavigation navigation) throws Exception {
    String navigationPath = getApplicationRegistryPath(navigation.getOwnerType(), navigation.getOwnerId())
                            + "/" + NAVIGATION_CONFIG_FILE_NAME ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    regService_.removeEntry(sessionProvider, navigationPath) ;
    sessionProvider.close() ;;
  }
  
  public Widgets getWidgets(String id) throws Exception {
    String[] fragments = id.split("::") ;
    if(fragments.length < 2) {
      throw new Exception("Invalid Widgets Id: " + "[" + id + "]") ;
    }
    String widgetsPath = getApplicationRegistryPath(fragments[0], fragments[1])
                         + "/" + WIDGETS_CONFIG_FILE_NAME ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry widgetsEntry ;
    try {      
     widgetsEntry = regService_.getEntry(sessionProvider, widgetsPath) ;
    } catch (ItemNotFoundException ie) {
      sessionProvider.close() ;
      return null ;
    }
    Widgets widgets = mapper_.toWidgets(widgetsEntry.getDocument()) ;
    sessionProvider.close() ;
    return widgets ;
  }
  
  public void create(Widgets widgets) throws Exception {
    String appRegPath = getApplicationRegistryPath(widgets.getOwnerType(), widgets.getOwnerId()) ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry widgetsEntry = new RegistryEntry(WIDGETS_CONFIG_FILE_NAME) ;
    mapper_.map(widgetsEntry.getDocument(), widgets) ;
    regService_.createEntry(sessionProvider, appRegPath, widgetsEntry) ;
    sessionProvider.close() ;
  }

  public void save(Widgets widgets) throws Exception {
    String appRegPath = getApplicationRegistryPath(widgets.getOwnerType(), widgets.getOwnerId()) ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry widgetsEntry = regService_.getEntry(sessionProvider, appRegPath + "/" + WIDGETS_CONFIG_FILE_NAME) ;
    mapper_.map(widgetsEntry.getDocument(), widgets) ;
    regService_.recreateEntry(sessionProvider, appRegPath, widgetsEntry) ;
    sessionProvider.close() ;    
  }
  
  public void remove(Widgets widgets) throws Exception {
    String widgetsPath = getApplicationRegistryPath(widgets.getOwnerType(), widgets.getOwnerId())
                         + "/" + WIDGETS_CONFIG_FILE_NAME ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    regService_.removeEntry(sessionProvider, widgetsPath) ;
    sessionProvider.close() ;    
  }
  
  public PortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    String[] fragments = windowID.getOwner().split("#") ;
    if(fragments.length < 2) {
      throw new Exception("Invalid WindowID: " + "[" + windowID + "]");
    }
    ExoWindowID exoWindowID = (ExoWindowID) windowID ;
    String name = exoWindowID.getPersistenceId().replace('/', '_').replace(':', '_').replace('#', '_') ;
    String portletPreferencesPath = getApplicationRegistryPath(fragments[0], fragments[1])
                                    + "/" + PORTLET_PREFERENCES_SET_NODE
                                    + "/" + name ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry portletPreferencesEntry ;
    try {
       portletPreferencesEntry = regService_.getEntry(sessionProvider, portletPreferencesPath) ;      
    } catch (ItemNotFoundException ie) {
      sessionProvider.close() ;
      return null ;
    }
    PortletPreferences portletPreferences = mapper_.toPortletPreferences(portletPreferencesEntry.getDocument()) ;
    sessionProvider.close() ;
    return portletPreferences ;
  }
  
  public void save(PortletPreferences portletPreferences) throws Exception {
    String name = portletPreferences.getWindowId().replace('/', '_').replace(':', '_').replace('#', '_') ;
    String portletPreferencesSet = getApplicationRegistryPath(portletPreferences.getOwnerType(), portletPreferences.getOwnerId())
                                    + "/" + PORTLET_PREFERENCES_SET_NODE ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry entry ;
    try {
      entry = regService_.getEntry(sessionProvider, portletPreferencesSet + "/" + name) ;
    } catch (ItemNotFoundException ie) {
      entry = new RegistryEntry(name) ;
      regService_.createEntry(sessionProvider, portletPreferencesSet, entry) ;
    }
    mapper_.map(entry.getDocument(), portletPreferences) ;
    regService_.recreateEntry(sessionProvider, portletPreferencesSet, entry) ;
    sessionProvider.close() ;
  }
  
  public void remove(PortletPreferences portletPreferences) throws Exception {
    String name = portletPreferences.getWindowId().replace('/', '_').replace(':', '_').replace('#', '_') ;
    String portletPreferencesPath = getApplicationRegistryPath(portletPreferences.getOwnerType(), portletPreferences.getOwnerId())
                                    + "/" + PORTLET_PREFERENCES_SET_NODE
                                    + "/" + name ;
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    regService_.removeEntry(sessionProvider, portletPreferencesPath) ;
    sessionProvider.close() ;
  }
  
  @SuppressWarnings("unchecked")
  public PageList find(Query q) throws Exception {
    return find(q, null);
  }

  @SuppressWarnings("unchecked")
  public PageList find(Query q, Comparator sortComparator) throws Exception {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    StringBuilder builder = new StringBuilder("select * from " + DataMapper.EXO_REGISTRYENTRY_NT) ;
    String registryNodePath = regService_.getRegistry(sessionProvider).getNode().getPath() ;
    generateScript(builder, "jcr:path", registryNodePath + "/%") ;
    generateScript(builder, DataMapper.EXO_DATA_TYPE, q.getClassType().getSimpleName()) ;
    generateScript(builder, DataMapper.EXO_NAME, q.getName()) ;
    generateScript(builder, DataMapper.EXO_OWNER_TYPE, q.getOwnerType()) ;
    generateScript(builder, DataMapper.EXO_OWNER_ID, q.getOwnerId()) ;
    Session session = regService_.getRegistry(sessionProvider).getNode().getSession() ;
    QueryManager queryManager = session.getWorkspace().getQueryManager() ;
    javax.jcr.query.Query query = queryManager.createQuery(builder.toString(), "sql") ;
    QueryResult result = query.execute() ;
    ArrayList<Object> list = new ArrayList<Object>() ;
    NodeIterator itr = result.getNodes() ;
    while(itr.hasNext()) {
      Node node = itr.nextNode() ;
      String entryPath = node.getPath().substring(registryNodePath.length() + 1) ;
      RegistryEntry entry = regService_.getEntry(sessionProvider, entryPath) ;
      list.add(mapper_.fromDocument(entry.getDocument(), q.getClassType())) ;
    }
    sessionProvider.close() ;
    if(sortComparator != null) Collections.sort(list, sortComparator) ;
    return new ObjectPageList(list, 10);
  }
  
  public void start() {}

  public void stop() {}

  private void generateScript(StringBuilder sql, String name, String value){
    if(value == null || value.length() < 1) return ;
    if(sql.indexOf(" where") < 0) sql.append(" where "); else sql.append(" and "); 
    value = value.replace('*', '%') ;
    sql.append(name).append(" like '").append(value).append("'");
  }
  
  private String getApplicationRegistryPath(String ownerType, String ownerId) {
    String path = "" ;
    if(PortalConfig.PORTAL_TYPE.equals(ownerType)) {
      path = RegistryService.EXO_APPLICATIONS + "/" + PORTAL_DATA + "/" + ownerId;
    } else if(PortalConfig.USER_TYPE.equals(ownerType)) {
      path = RegistryService.EXO_USERS + "/" + ownerId + "/" + USER_DATA;
    } else if(PortalConfig.GROUP_TYPE.equals(ownerType)) {
      if(ownerId.charAt(0) != '/') ownerId = "/" + ownerId ;
      path = RegistryService.EXO_GROUPS + ownerId + "/" + GROUP_DATA ;
    }
    
    return path ;
  }
  
}

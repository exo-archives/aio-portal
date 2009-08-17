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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ObjectParameter;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.PortletPreferences.PortletPreferencesSet;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.Page.PageSet;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * Created by The eXo Platform SARL Author : Tuan Nguyen
 * tuan08@users.sourceforge.net May 22, 2006
 */

public class NewPortalConfigListener extends BaseComponentPlugin {

  private ConfigurationManager cmanager_;

  private DataStorage          pdcService_;

  private List<?>              configs;

  private PageTemplateConfig   pageTemplateConfig_;

  private String               defaultPortal;
  
  private Log                                log                     = ExoLogger.getLogger("Portal:UserPortalConfigService");


  public NewPortalConfigListener(DataStorage pdcService,
                                 ConfigurationManager cmanager,
                                 InitParams params) throws Exception {
    cmanager_ = cmanager;
    pdcService_ = pdcService;

    ObjectParameter objectParam = params.getObjectParam("page.templates");
    if (objectParam != null)
      pageTemplateConfig_ = (PageTemplateConfig) objectParam.getObject();

    defaultPortal = "classic";
    ValueParam valueParam = params.getValueParam("default.portal");
    if (valueParam != null)
      defaultPortal = valueParam.getValue();
    if (defaultPortal == null || defaultPortal.trim().length() == 0)
      defaultPortal = "classic";
    configs = params.getObjectParamValues(NewPortalConfig.class);
  }

  public void run() throws Exception {
    if (isInitedDB(defaultPortal))
      return;
    for (Object ele : configs) {
      NewPortalConfig portalConfig = (NewPortalConfig) ele;
      initPortalConfigDB(portalConfig);
    }
    for (Object ele : configs) {
      NewPortalConfig portalConfig = (NewPortalConfig) ele;
      initPageDB(portalConfig);
    }
    for (Object ele : configs) {
      NewPortalConfig portalConfig = (NewPortalConfig) ele;
      initPageNavigationDB(portalConfig);
    }
    for (Object ele : configs) {
      NewPortalConfig portalConfig = (NewPortalConfig) ele;
      initPortletPreferencesDB(portalConfig);
    }
    for (Object ele : configs) {
      NewPortalConfig portalConfig = (NewPortalConfig) ele;
      portalConfig.getPredefinedOwner().clear();
    }
  }

  NewPortalConfig getPortalConfig(String ownerType) {
    for (Object object : configs) {
      NewPortalConfig portalConfig = (NewPortalConfig) object;
      if (portalConfig.getOwnerType().equals(ownerType))
        return portalConfig;
    }
    return null;
  }

  private boolean isInitedDB(String user) throws Exception {
    PortalConfig pconfig = pdcService_.getPortalConfig(user);
    return pconfig != null;
  }

  public void initPortalConfigDB(NewPortalConfig config) throws Exception {
    for (String owner : config.getPredefinedOwner()) {
      createPortalConfig(config, owner);
    }
  }

  public void initPageDB(NewPortalConfig config) throws Exception {
    for (String owner : config.getPredefinedOwner()) {
      createPage(config, owner);
    }
  }

  public void initPageNavigationDB(NewPortalConfig config) throws Exception {
    for (String owner : config.getPredefinedOwner()) {
      createPageNavigation(config, owner);
    }
  }

  public void initPortletPreferencesDB(NewPortalConfig config) throws Exception {
    for (String owner : config.getPredefinedOwner()) {
      if  (!config.getOwnerType().equals(PortalConfig.USER_TYPE)) {
        createPortletPreferences(config, owner);
      }
    }
  }

  private static String fixOwnerName(String type, String owner) {
    if (type.equals(PortalConfig.GROUP_TYPE) && !owner.startsWith("/")) {
      return "/" + owner;
    } else {
      return owner;
    }
  }

  private static String fixOwnerName(String persistenceId) {
    int pos1 = persistenceId.indexOf("#");
    String type = persistenceId.substring(0, pos1);
    int pos2 = persistenceId.indexOf(":", pos1 + 1);
    String owner = persistenceId.substring(pos1 + 1, pos2);
    String windowId = persistenceId.substring(pos2 + 1);
    owner = fixOwnerName(type, owner);
    return type + "#" + owner + ":" + windowId;
  }

  private static void fixOwnerName(PortalConfig config) {
    config.setName(fixOwnerName(config.getType(), config.getName()));
    fixOwnerName(config.getPortalLayout());
  }

  private static void fixOwnerName(Container container) {
    for (Object o : container.getChildren()) {
      if (o instanceof Container) {
        fixOwnerName((Container)o);
      } else if (o instanceof Application) {
        fixOwnerName((Application)o);
      }
    }
  }

  private static void fixOwnerName(Application application) {
    String instanceId = application.getInstanceId();
    instanceId = fixOwnerName(instanceId);
    application.setInstanceId(instanceId);
  }

  private static void fixOwnerName(PageNavigation pageNav) {
    pageNav.setOwnerId(fixOwnerName(pageNav.getOwnerType(), pageNav.getOwnerId()));
    for (PageNode pageNode : pageNav.getNodes()) {
      fixOwnerName(pageNode);
    }
  }

  private static void fixOwnerName(PageNode pageNode) {
    if (pageNode.getPageReference() != null) {
      String pageRef = pageNode.getPageReference();
      int pos1 = pageRef.indexOf("::");
      int pos2 = pageRef.indexOf("::", pos1 + 2);
      String type = pageRef.substring(0, pos1);
      String owner = pageRef.substring(pos1 + 2, pos2);
      String name = pageRef.substring(pos2 + 2);
      owner = fixOwnerName(type, owner);
      pageRef = type + "::" + owner + "::" + name;
      pageNode.setPageReference(pageRef);
    }
    if (pageNode.getChildren() != null) {
      for (PageNode childPageNode : pageNode.getChildren()) {
        fixOwnerName(childPageNode);
      }
    }
  }

  private static void fixOwnerName(PortletPreferences prefs) {
    prefs.setOwnerId(fixOwnerName(prefs.getOwnerType(), prefs.getOwnerId()));
    prefs.setWindowId(fixOwnerName(prefs.getWindowId()));
  }

  private static void fixOwnerName(Page page) {
    page.setOwnerId(fixOwnerName(page.getOwnerType(), page.getOwnerId()));
    fixOwnerName((Container)page);
  }

  private void createPortalConfig(NewPortalConfig config, String owner) throws Exception {
    String type = config.getOwnerType();

    //
    if (pdcService_.getPortalConfig(type, owner) != null) {
      return;
    }


    String xml;
    
    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean notTemplate = (config.getTemplateOwner() == null || config.getTemplateOwner()
                                                                      .trim()
                                                                      .length() < 1);
    String path = getPathConfig(config, owner, type, notTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (!notTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }

    PortalConfig pconfig = fromXML(xml, PortalConfig.class);
    pconfig.setType(type);
    fixOwnerName(pconfig);
    pdcService_.create(pconfig);
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path, e);
    }
  }

  private void createPage(NewPortalConfig config, String owner) throws Exception {

    String xml = null;

    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean notTemplate = (config.getTemplateOwner() == null || config.getTemplateOwner()
                                                                      .trim()
                                                                      .length() < 1);
    String path = getPathConfig(config, owner, "pages", notTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (!notTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }

      PageSet pageSet = fromXML(xml, PageSet.class);
      ArrayList<Page> list = pageSet.getPages();
      for (Page page : list) {
        fixOwnerName(page);
        pdcService_.create(page);
      }
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path, e);
    }
  }

  private void createPageNavigation(NewPortalConfig config, String owner) throws Exception {
    String xml = null;

    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean notTemplate = (config.getTemplateOwner() == null || config.getTemplateOwner()
                                                                      .trim()
                                                                      .length() < 1);
    String path = getPathConfig(config, owner, "navigation", notTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (!notTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }
      PageNavigation navigation = fromXML(xml, PageNavigation.class);
      fixOwnerName(navigation);
      if (pdcService_.getPageNavigation(navigation.getOwner()) == null) {
        pdcService_.create(navigation);
      } else {
        pdcService_.save(navigation);
      }
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path, e);
    }
  }

  private void createPortletPreferences(NewPortalConfig config, String owner) throws Exception {
    String xml = null;

    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean notTemplate = (config.getTemplateOwner() == null || config.getTemplateOwner()
                                                                      .trim()
                                                                      .length() < 1);
    String path = getPathConfig(config, owner, "portlet-preferences", notTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (!notTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }

      PortletPreferencesSet portletSet = fromXML(xml, PortletPreferencesSet.class);
      ArrayList<PortletPreferences> list = portletSet.getPortlets();
      for (PortletPreferences portlet : list) {
        fixOwnerName(portlet);
        pdcService_.save(portlet);
      }
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path, e);
    }
  }

  private String getDefaultConfig(String location, String path) throws Exception {
    return IOUtil.getStreamContentAsString(cmanager_.getInputStream(location + path));
  }

  private String getPathConfig(NewPortalConfig portalConfig,
                               String owner,
                               String dataType,
                               boolean notTemplate) {
    String path = "";
    if (!notTemplate) {
      String ownerType = portalConfig.getOwnerType();
      path = "/" + ownerType + "/template/" + portalConfig.getTemplateOwner() + "/" + dataType
          + ".xml";
    } else {
      String ownerType = portalConfig.getOwnerType();
      path = "/" + ownerType + "/" + owner + "/" + dataType + ".xml";
    }
    return path;
  }

  public Page createPageFromTemplate(String temp) throws Exception {
    return fromXML(getTemplateConfig(temp, "page"), Page.class);
  }

  public PortletPreferencesSet createPortletPreferencesFromTemplate(String temp) throws Exception {
    return fromXML(getTemplateConfig(temp, "portlet-preferences"), PortletPreferencesSet.class);
  }

  private String getTemplateConfig(String name, String dataType) throws Exception {
    String path = pageTemplateConfig_.getLocation() + "/" + name + "/" + dataType + ".xml";
    InputStream is = cmanager_.getInputStream(path);
    return IOUtil.getStreamContentAsString(is);
  }

  private <T> T fromXML(String xml, Class<T> clazz) throws Exception {
    ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
    IBindingFactory bfact = BindingDirectory.getFactory(clazz);
    IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
    return clazz.cast(uctx.unmarshalDocument(is, "UTF-8"));
  }

  String getDefaultPortal() {
    return defaultPortal;
  }

}

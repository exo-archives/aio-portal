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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ObjectParameter;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.PortletPreferences.PortletPreferencesSet;
import org.exoplatform.portal.config.model.Gadgets;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Page.PageSet;
import org.exoplatform.services.log.ExoLogger;
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

  private DataStorage          storageServ_;
  
  private UserPortalConfigService portalConfigService_;

  private List<NewPortalConfig>              configs;

  private PageTemplateConfig   pageTemplateConfig_;

  private String               defaultPortal;

  private boolean              isUseTryCatch;
  
  private boolean              isOverwrited = false;

  private Log                  log = ExoLogger.getLogger("Portal:NewPortalConfigListener");

  public NewPortalConfigListener(UserPortalConfigService portalConfigService, DataStorage pdcService,
                                 ConfigurationManager cmanager,
                                 InitParams params) throws Exception {
    cmanager_ = cmanager;
    storageServ_ = pdcService;
    portalConfigService_ = portalConfigService;
    
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

    // get parameter
    valueParam = params.getValueParam("initializing.failure.ignore");
    // determine in the run function, is use try catch or not
    if (valueParam != null) {
      isUseTryCatch = (valueParam.getValue().toLowerCase().equals("true"));
    } else {
      isUseTryCatch = true;
    }

    valueParam = params.getValueParam("overwrite");
    if (valueParam != null) {
      isOverwrited = (valueParam.getValue().toLowerCase().equals("true"));
    }
  }

  public void run() throws Exception {
    if(isOverwrited) {
      for (NewPortalConfig config : configs) {
        Set<String> owners = config.getPredefinedOwner();
        for (String owner : owners) {
            try {
                portalConfigService_.removeUserPortalConfig(owner);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    }
    
    if (storageServ_.getPortalConfig(defaultPortal) != null)
      return;

    if (isUseTryCatch) {
      for (NewPortalConfig portalConfig : configs) {
        try {
          if (portalConfig.getOwnerType().equals(PortalConfig.USER_TYPE)) {
            initUserTypeDB(portalConfig);
          } else if (portalConfig.getOwnerType().equals(PortalConfig.GROUP_TYPE)) {
            initGroupTypeDB(portalConfig);
          } else {
            initPortalTypeDB(portalConfig);
          }
          portalConfig.getPredefinedOwner().clear();
        } catch (Exception e) {
          if (log.isErrorEnabled()) {
            log.error("Can not initialize " + portalConfig.getOwnerType() + " new portal config : "
                + e.getMessage());
          }
        }

      }
    } else {
      for (NewPortalConfig portalConfig : configs) {
        if (portalConfig.getOwnerType().equals(PortalConfig.USER_TYPE)) {
          initUserTypeDB(portalConfig);
        } else if (portalConfig.getOwnerType().equals(PortalConfig.GROUP_TYPE)) {
          initGroupTypeDB(portalConfig);
        } else {
          initPortalTypeDB(portalConfig);
        }
        portalConfig.getPredefinedOwner().clear();
      }
    }

  }

  public NewPortalConfig getPortalConfig(String ownerType) {
    for (NewPortalConfig portalConfig : configs) {
      if (portalConfig.getOwnerType().equals(ownerType))
        return portalConfig;
    }
    return null;
  }

  public void initUserTypeDB(NewPortalConfig config) throws Exception {
    HashSet<String> owners = config.getPredefinedOwner();
    Iterator<String> iter = owners.iterator();
    while (iter.hasNext()) {
      String owner = iter.next();
      createPage(config, owner);
      createPageNavigation(config, owner);
      createGadgets(config, owner);
    }
  }

  public void initGroupTypeDB(NewPortalConfig config) throws Exception {
    HashSet<String> owners = config.getPredefinedOwner();
    Iterator<String> iter = owners.iterator();
    while (iter.hasNext()) {
      String owner = iter.next();
      createPage(config, owner);
      createPageNavigation(config, owner);
      createPortletPreferences(config, owner);
    }
  }

  public void initPortalTypeDB(NewPortalConfig config) throws Exception {
    HashSet<String> owners = config.getPredefinedOwner();
    Iterator<String> iter = owners.iterator();
    while (iter.hasNext()) {
      String owner = iter.next();
      createPortalConfig(config, owner);
      createPage(config, owner);
      createPageNavigation(config, owner);
      createPortletPreferences(config, owner);
    }
  }

  private void createPortalConfig(NewPortalConfig config, String owner) throws Exception {
    String xml = null;
    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean isTemplate = (config.getTemplateOwner() != null && config.getTemplateOwner()
                                                                      .trim().length() > 0);
    String path = getPathConfig(config, owner, "portal", isTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (isTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }
      PortalConfig pconfig = fromXML(xml, PortalConfig.class);
      storageServ_.create(pconfig);
    } catch (Exception e) {
      log.error(e.getMessage() + " file: " + path);
    }
  }

  private void createPage(NewPortalConfig config, String owner) throws Exception {

    String xml = null;

    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean isTemplate = (config.getTemplateOwner() != null && config.getTemplateOwner()
                                                                      .trim().length() > 0);
    String path = getPathConfig(config, owner, "pages", isTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (isTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }

      PageSet pageSet = fromXML(xml, PageSet.class);
      ArrayList<Page> list = pageSet.getPages();
      for (Page page : list) {
        storageServ_.create(page);
      }
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path);
    }
  }

  private void createPageNavigation(NewPortalConfig config, String owner) throws Exception {
    String xml = null;

    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean isTemplate = (config.getTemplateOwner() != null && config.getTemplateOwner()
                                                                      .trim().length() > 0);
    String path = getPathConfig(config, owner, "navigation", isTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (isTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }

      PageNavigation navigation = fromXML(xml, PageNavigation.class);
      if (storageServ_.getPageNavigation(navigation.getOwner()) == null) {
        storageServ_.create(navigation);
      } else {
        storageServ_.save(navigation);
      }
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path);
    }
  }

  private void createGadgets(NewPortalConfig config, String owner) throws Exception {
    String xml = null;
    if (config.getTemplateOwner() == null || config.getTemplateOwner().trim().length() < 1) {
      String ownerType = config.getOwnerType();
      String path = "/" + ownerType + "/" + owner + "/" + "gadgets.xml";
      String location = config.getTemplateLocation();
      URL url = cmanager_.getURL(location + path);
      if (url != null)
        xml = IOUtil.getStreamContentAsString(cmanager_.getInputStream(location + path));
      // xml = getDefaultConfig(config, owner, "gadgets");
    } else {
      String path = getPathConfig(config, owner, "gadgets", true);
      xml = getDefaultConfig(config.getTemplateLocation(), path);
      xml = StringUtils.replace(xml, "@owner@", owner);
    }
    if (xml == null)
      return;
    Gadgets gadgets = fromXML(xml, Gadgets.class);
    if (storageServ_.getGadgets(gadgets.getId()) == null) {
      storageServ_.create(gadgets);
    } else {
      storageServ_.save(gadgets);
    }
  }

  // -------------------------------------------------------------------------------------

  private void createPortletPreferences(NewPortalConfig config, String owner) throws Exception {
    String xml = null;
    // get path of xml file, check if path in template folder and if path not in
    // template folder
    boolean isTemplate = (config.getTemplateOwner() != null && config.getTemplateOwner()
                                                                     .trim().length() > 0);
    String path = getPathConfig(config, owner, "portlet-preferences", isTemplate);

    // get xml content and parse xml content
    try {
      xml = getDefaultConfig(config.getTemplateLocation(), path);

      if (isTemplate) {
        xml = StringUtils.replace(xml, "@owner@", owner);
      }
      PortletPreferencesSet portletSet = fromXML(xml, PortletPreferencesSet.class);
      ArrayList<PortletPreferences> list = portletSet.getPortlets();
      for (PortletPreferences portlet : list) {
        storageServ_.save(portlet);
      }
    } catch (JiBXException e) {
      log.error(e.getMessage() + " file: " + path);
    }
  }

  private String getDefaultConfig(String location, String path) throws Exception {
    return IOUtil.getStreamContentAsString(cmanager_.getInputStream(location + path));
  }

  private String getPathConfig(NewPortalConfig portalConfig,
                               String owner,
                               String dataType,
                               boolean isTemplate) {
    String path = "";
    if (isTemplate) {
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

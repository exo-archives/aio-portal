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
package org.exoplatform.portal.webui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.exoplatform.portal.config.UserPortalConfig;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageBody;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.SiteBody;
import org.exoplatform.portal.webui.application.UIGadget;
import org.exoplatform.portal.webui.application.UIPortlet;
import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.portal.webui.page.UIPage;
import org.exoplatform.portal.webui.page.UIPageBody;
import org.exoplatform.portal.webui.page.UISiteBody;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.core.UIComponent;

import org.gatein.common.net.media.MediaType;
import org.gatein.pc.api.PortletInvoker;
import org.gatein.pc.api.Portlet;
import org.gatein.pc.api.PortletContext;
import org.gatein.pc.api.info.ModeInfo;
import org.gatein.pc.api.info.PortletInfo;
/**
 * Created by The eXo Platform SAS May 4, 2007 TODO: Rename this to
 * PortalDataModelMapper
 */
public class PortalDataMapper {

  @SuppressWarnings("unchecked")
	public static final <T> T buildModelObject(UIComponent uiComponent) {
    Object model = null;
    if (uiComponent instanceof UIPortal) {
      model = toPortal((UIPortal) uiComponent);
    } else if (uiComponent instanceof UIPageBody) {
      model = new PageBody();
    } else if (uiComponent instanceof UIPage) {
      model = toPageModel((UIPage) uiComponent);
    } else if (uiComponent instanceof UIPortlet) {
      model = toPortletModel((UIPortlet) uiComponent);
    } else if (uiComponent instanceof UIContainer) {
      model = toContainer((UIContainer) uiComponent);
    } else if (uiComponent instanceof UIGadget) {
      model = toGadget((UIGadget) uiComponent);
    }
    return (T) model;
  }
  
  private static final Application toGadget(UIGadget uiGadget) {
    Application model = new Application();
    model.setApplicationType(org.exoplatform.web.application.Application.EXO_GAGGET_TYPE);
    model.setInstanceId(uiGadget.getApplicationInstanceId());
    model.setId(uiGadget.getId());
    model.setProperties(uiGadget.getProperties());
    return model;
  }

  private static void toContainer(Container model, UIContainer uiContainer) {
    model.setId(uiContainer.getId());
    model.setName(uiContainer.getName());
    model.setTitle(uiContainer.getTitle());
    model.setIcon(uiContainer.getIcon());
    model.setDescription(uiContainer.getDescription());
    model.setHeight(uiContainer.getHeight());
    model.setWidth(uiContainer.getWidth());
    model.setTemplate(uiContainer.getTemplate());
    model.setFactoryId(uiContainer.getFactoryId());
    model.setAccessPermissions(uiContainer.getAccessPermissions());

    List<UIComponent> uiChildren = uiContainer.getChildren();
    if (uiChildren == null)
      return;
    ArrayList<Object> children = new ArrayList<Object>();
    for (UIComponent child : uiChildren) {
      Object component = buildModelObject(child);
      if (component != null)
        children.add(component);
    }
    model.setChildren(children);
  }

  private static final Application toPortletModel(UIPortlet uiPortlet) {
    Application model = new Application();
    model.setInstanceId(uiPortlet.getWindowId().toString());
    model.setApplicationType(uiPortlet.getFactoryId());
    model.setTitle(uiPortlet.getTitle());
    model.setWidth(uiPortlet.getWidth());
    model.setHeight(uiPortlet.getHeight());
    model.setDescription(uiPortlet.getDescription());
    model.setShowInfoBar(uiPortlet.getShowInfoBar());
    model.setShowApplicationState(uiPortlet.getShowWindowState());
    model.setShowApplicationMode(uiPortlet.getShowPortletMode());
    model.setDescription(uiPortlet.getDescription());
    model.setIcon(uiPortlet.getIcon());
    model.setProperties(uiPortlet.getProperties());
    model.setTheme(uiPortlet.getTheme());
    model.setAccessPermissions(uiPortlet.getAccessPermissions());
    model.setModifiable(uiPortlet.isModifiable());
    return model;
  }

  static final private Container toContainer(UIContainer uiContainer) {
    Container model = new Container();
    toContainer(model, uiContainer);
    return model;
  }

  static final private Page toPageModel(UIPage uiPage) {
    Page model = new Page();
    toContainer(model, uiPage);
    model.setCreator(uiPage.getCreator());
    model.setModifier(uiPage.getModifier());
    model.setOwnerId(uiPage.getOwnerId());
    model.setOwnerType(uiPage.getOwnerType());
    model.setIcon(uiPage.getIcon());
    model.setPageId(uiPage.getPageId());
    model.setTitle(uiPage.getTitle());
    model.setAccessPermissions(uiPage.getAccessPermissions());
    model.setEditPermission(uiPage.getEditPermission());
    model.setFactoryId(uiPage.getFactoryId());
    model.setShowMaxWindow(uiPage.isShowMaxWindow());
    model.setModifiable(uiPage.isModifiable());
    return model;
  }

  static final private PortalConfig toPortal(UIPortal uiPortal) {
    PortalConfig model = new PortalConfig();
    model.setName(uiPortal.getName());
    model.setCreator(uiPortal.getCreator());
    model.setModifier(uiPortal.getModifier());
    model.setAccessPermissions(uiPortal.getAccessPermissions());
    model.setEditPermission(uiPortal.getEditPermission());
    model.setLocale(uiPortal.getLocale());
    model.setSkin(uiPortal.getSkin());
    model.setTitle(uiPortal.getTitle());
    model.setModifiable(uiPortal.isModifiable());
    model.setProperties(uiPortal.getProperties());

    List<UIComponent> children = uiPortal.getChildren();
    if (children == null)
      return model;
    ArrayList<Object> newChildren = new ArrayList<Object>();
    for (UIComponent child : children) {
      Object component = buildModelObject(child);
      if (component != null)
        newChildren.add(component);
    }
    model.getPortalLayout().setChildren(newChildren);
    return model;
  }

  static public void toUIGadget(UIGadget uiGadget, Application model) throws Exception {
    uiGadget.setApplicationInstanceId(model.getInstanceId());
    uiGadget.setId(model.getId());
    uiGadget.setProperties(model.getProperties());
  }

  /**
   * Fill the UI component with both information from the persistent model and
   * some coming from the portlet.xml defined by the JSR 286 specification
   */
  static public void toUIPortlet(UIPortlet uiPortlet, Application model) throws Exception {
    /*
     * Fill UI component object with info from the XML file that persist portlet
     * information
     */
    uiPortlet.setWidth(model.getWidth());
    uiPortlet.setHeight(model.getHeight());
    uiPortlet.setWindowId(model.getInstanceId());
    uiPortlet.setTitle(model.getTitle());
    uiPortlet.setIcon(model.getIcon());
    uiPortlet.setDescription(model.getDescription());
    uiPortlet.setFactoryId(model.getApplicationType());
    uiPortlet.setShowInfoBar(model.getShowInfoBar());
    uiPortlet.setShowWindowState(model.getShowApplicationState());
    uiPortlet.setShowPortletMode(model.getShowApplicationMode());
    uiPortlet.setProperties(model.getProperties());
    uiPortlet.setTheme(model.getTheme());
    if (model.getAccessPermissions() != null)
      uiPortlet.setAccessPermissions(model.getAccessPermissions());
    uiPortlet.setModifiable(model.isModifiable());    
    
    PortletInvoker portletInvoker = uiPortlet.getApplicationComponent(PortletInvoker.class);
    PortletContext portletContext = uiPortlet.getProducerOfferedPortletContext();
    Portlet portlet = portletInvoker.getPortlet(portletContext);
    if (portlet == null || portlet.getInfo() == null) return;
    
    PortletInfo portletInfo = portlet.getInfo();
    
    /*
     * Define which portlet modes the portlet supports and hence should be shown
     * in the portlet info bar
     */
    Set<ModeInfo> modes = portletInfo.getCapabilities().getModes(MediaType.create("text/html"));
    List<String> supportModes = new ArrayList<String>() ;
    for (ModeInfo modeInfo : modes)
    {
    	String modeName = modeInfo.getModeName().toLowerCase();
    	if ("config".equals(modeInfo.getModeName()))
    	{
    		supportModes.add(modeName);
    	} else {
    		supportModes.add(modeName);
    	}    	
    }
    
    if(supportModes.size() > 1) supportModes.remove("view");
    uiPortlet.setSupportModes(supportModes);    
  }
  
  static public void toUIContainer(UIContainer uiContainer, Container model) throws Exception {
    uiContainer.setId(model.getId());
    uiContainer.setWidth(model.getWidth());
    uiContainer.setHeight(model.getHeight());
    uiContainer.setTitle(model.getTitle());
    uiContainer.setIcon(model.getIcon());
    uiContainer.setDescription(model.getDescription());
    uiContainer.setFactoryId(model.getFactoryId());
    uiContainer.setName(model.getName());
    uiContainer.setTemplate(model.getTemplate());
    if (model.getAccessPermissions() != null)
      uiContainer.setAccessPermissions(model.getAccessPermissions());

    List<Object> children = model.getChildren();
    if (children == null)
      return;
    for (Object child : children) {
      buildUIContainer(uiContainer, child);
    }
  }

  static public void toUIPage(UIPage uiPage, Page model) throws Exception {
    toUIContainer(uiPage, model);
    uiPage.setCreator(model.getCreator());
    uiPage.setModifier(model.getModifier());
    uiPage.setOwnerId(model.getOwnerId());
    uiPage.setOwnerType(model.getOwnerType());
    uiPage.setIcon(model.getIcon());
    uiPage.setAccessPermissions(model.getAccessPermissions());
    uiPage.setEditPermission(model.getEditPermission());
    uiPage.setFactoryId(model.getFactoryId());
    uiPage.setPageId(model.getPageId());
    uiPage.setTitle(model.getTitle());
    uiPage.setShowMaxWindow(model.isShowMaxWindow());
    uiPage.setModifiable(model.isModifiable());

    List<UIPortlet> portlets = new ArrayList<UIPortlet>();
    uiPage.findComponentOfType(portlets, UIPortlet.class);
    for (UIPortlet portlet : portlets) {
      portlet.setPortletInPortal(false);
    }
  }

  static public void toUIPortal(UIPortal uiPortal, UserPortalConfig userPortalConfig) throws Exception {
    PortalConfig model = userPortalConfig.getPortalConfig();

    uiPortal.setId("UIPortal");
    uiPortal.setCreator(model.getCreator());
    uiPortal.setModifier(model.getModifier());
    uiPortal.setName(model.getName());
    // uiPortal.setFactoryId(model.getFactoryId());
    uiPortal.setOwner(model.getName());
    uiPortal.setTitle(model.getTitle());
    uiPortal.setModifiable(model.isModifiable());

    uiPortal.setLocale(model.getLocale());
    uiPortal.setSkin(model.getSkin());
    uiPortal.setAccessPermissions(model.getAccessPermissions());
    uiPortal.setEditPermission(model.getEditPermission());
    uiPortal.setProperties(model.getProperties());

    List<Object> children = model.getPortalLayout().getChildren();
    if (children != null) {
      for (Object child : children) {
        buildUIContainer(uiPortal, child);
      }
    }
    uiPortal.setNavigation(userPortalConfig.getNavigations());
  }

  static private void buildUIContainer(UIContainer uiContainer, Object model) throws Exception {
    UIComponent uiComponent = null;
    WebuiRequestContext context = Util.getPortalRequestContext();
    if (model instanceof SiteBody) {
    	uiComponent = uiContainer.createUIComponent(context, UISiteBody.class, null, null);
    } else if (model instanceof PageBody) {
    	uiComponent = uiContainer.createUIComponent(context, UIPageBody.class, null, null);
    } else if (model instanceof Application) {
      Application application = (Application) model;
      String applicationType = application.getApplicationType();
      if (applicationType == null
          || applicationType.equals(org.exoplatform.web.application.Application.EXO_PORTLET_TYPE)) {
        UIPortlet uiPortlet = uiContainer.createUIComponent(context, UIPortlet.class, null, null);
        toUIPortlet(uiPortlet, application);
        uiComponent = uiPortlet;
      } else if (applicationType.equals(org.exoplatform.web.application.Application.EXO_GAGGET_TYPE)) {
        UIGadget uiGadget = uiContainer.createUIComponent(context, UIGadget.class, null, null);
        toUIGadget(uiGadget, application);
        uiComponent = uiGadget;
      }
    } else if (model instanceof Container) {
      Container container = (Container) model;
      UIContainer uiTempContainer = uiContainer.createUIComponent(context,
                                                           UIContainer.class,
                                                           container.getFactoryId(),
                                                           null);
      toUIContainer(uiTempContainer, (Container) model);
      uiComponent = uiTempContainer;
    }
    uiContainer.addChild(uiComponent);
  }
}

/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.applicationregistry.webui.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.jboss.portal.common.i18n.LocalizedString;
import org.jboss.portal.portlet.api.Portlet;
import org.jboss.portal.portlet.api.PortletInvoker;
import org.jboss.portal.portlet.api.info.MetaInfo;
import org.jboss.portal.portlet.api.info.PortletInfo;
import org.jboss.portal.portlet.api.info.PreferencesInfo;

/**
 * Created by The eXo Platform SAS
 * Author : Pham Thanh Tung
 *          thanhtungty@gmail.com
 * Jun 24, 2008  
 */

@ComponentConfig(
    template = "app:/groovy/applicationregistry/webui/component/UIPortletManagement.gtmpl",
    events = {
        @EventConfig(listeners = UIPortletManagement.SelectPortletActionListener.class),
        @EventConfig(listeners = UIPortletManagement.SelectPortletType.class)
    }
)

public class UIPortletManagement extends UIContainer {
  
  static final public String LOCAL = "local";
  static final public String REMOTE = "remote";
  private List<WebApp> webApps;
  private PortletExtra selectedPorlet;
  private String [] portletTypes = new String [] {LOCAL, REMOTE};
  private String selectedType;
  
  public UIPortletManagement() throws Exception {
    setSelectedType(LOCAL);
  }
  
  private void initWebApps(String type) throws Exception {
    webApps = new ArrayList<WebApp>(5) ;
    ExoContainer manager  = ExoContainerContext.getCurrentContainer();
    
    PortletInvoker portletInvoker = (PortletInvoker)manager.getComponentInstance(PortletInvoker.class);
    Set<Portlet> portlets = portletInvoker.getPortlets();
    if (portlets == null || portlets.isEmpty()) return;
    Iterator<Portlet> iterator = portlets.iterator();
    while (iterator.hasNext())
    {
    	Portlet portlet = iterator.next();
    	if (!portlet.isRemote())
    	{
    		String portletName = portlet.getInfo().getName();
    		String appName = portlet.getInfo().getApplicationName();
    		WebApp webApp = getWebApp(appName);
    		if (webApp == null) {
    			webApp = new WebApp(appName);
    			webApps.add(webApp);
    		}
            String portletId = appName + "/" + portletName;
    		webApp.addPortlet(new PortletExtra(portletId, type, portlet.getInfo()));
    	}
    }
    
    Collections.sort(webApps, new WebAppComparator());
    for(WebApp ele : webApps) {
      Collections.sort(ele.getPortlets(), new PortletComparator());
    }    
  }
  
  public WebApp getWebApp(String name) {
    for(WebApp ele : webApps) {
      if(ele.getName().equals(name)) return ele;
    }
    return null;
  }
  public List<WebApp> getWebApps() { return webApps; }
  
  public String getSelectedType() { return selectedType; }
  public void setSelectedType(String type) throws Exception { 
    selectedType = type;
    initWebApps(type);
    if(webApps != null && !webApps.isEmpty()) {
      List<PortletExtra> list = webApps.get(0).getPortlets();
      if(!list.isEmpty()) setSelectedPortlet(list.get(0));
    } else setSelectedPortlet((PortletExtra)null);
  }
  
  public String [] getPortletTypes() { return portletTypes; }
  
  public PortletExtra getSelectedPortlet() { return selectedPorlet; }  
  public void setSelectedPortlet(PortletExtra portlet) throws Exception { 
    selectedPorlet = portlet;
    if(selectedPorlet == null) {
      getChildren().clear();
      UIMessageBoard uiMessage = addChild(UIMessageBoard.class, null, null);
      uiMessage.setMessage(new ApplicationMessage("UIPortletManagement.msg.noPortlet", null));
      return;
    }
    UIPortletInfo uiPortletInfo = getChild(UIPortletInfo.class);
    if(uiPortletInfo == null) {
      getChildren().clear();
      uiPortletInfo = addChild(UIPortletInfo.class, null, null);
    }
    uiPortletInfo.setPortlet(selectedPorlet);
  }
  
  public void setSelectedPortlet(String id) throws Exception {
    String [] fragments = id.split("/");
    WebApp webApp = getWebApp(fragments[0]);
    for(PortletExtra ele : webApp.getPortlets()) {
      if(ele.getName().equals(fragments[1])) {
        setSelectedPortlet(ele) ;
        break ;
      }
    }
  }

  public void processRender(WebuiRequestContext context) throws Exception {
    super.processRender(context);
  }
  
  
  static public class SelectPortletType extends EventListener<UIPortletManagement> {

    public void execute(Event<UIPortletManagement> event) throws Exception {
      UIPortletManagement uiManagement = event.getSource() ;
      String type = event.getRequestContext().getRequestParameter(OBJECTID) ;
      uiManagement.setSelectedType(type) ;
      event.getRequestContext().addUIComponentToUpdateByAjax(uiManagement) ;
    }
    
  }
  
  static public class SelectPortletActionListener extends EventListener<UIPortletManagement> {

    public void execute(Event<UIPortletManagement> event) throws Exception {
      UIPortletManagement uiManagement = event.getSource() ;
      String portletId = event.getRequestContext().getRequestParameter(OBJECTID) ;
      uiManagement.setSelectedPortlet(portletId) ;
      event.getRequestContext().addUIComponentToUpdateByAjax(uiManagement) ;
    }
    
  }
  
  static public class PortletExtra {

    private String id_;
    private String name_;
    private String group_;
    private String type_;
    private PortletInfo portletInfo_;
    
    public PortletExtra(String id, String type, PortletInfo info) {
      id_ = id;
      String [] fragments = id.split("/");
      group_ = fragments[0];
      name_ = fragments[1];
      type_ = type;
      portletInfo_ = info;
    }
    
    public String getId() { return id_; }
    
    public String getName() { return name_; }
    
    public String getPortletGroup() { return group_; }
    
    public String getType() { return type_; }
    
    //public PortletData getPortletData() { return portletData_; }
    
    public String getDisplayName() {
      LocalizedString displayName = portletInfo_.getMeta().getMetaValue(MetaInfo.DISPLAY_NAME);
      if (displayName == null || displayName.getDefaultString() == null) return name_;
      return displayName.getDefaultString();   
    }
    
    public String getDescription() {
      LocalizedString description = portletInfo_.getMeta().getMetaValue(MetaInfo.DESCRIPTION);
      if (description == null || description.getDefaultString() == null) return name_;
      return description.getDefaultString();
    }
    
    public PreferencesInfo getPortletPreferences() {
      return portletInfo_.getPreferences();
    }
    
  }
  
  static public class WebApp {
    
    String name_;
    List<PortletExtra> portlets_;
    
    
    public WebApp(String name) {
      name_ = name;
    }

    public WebApp(String name, List<PortletExtra> portlets) {
      name_ = name;
      portlets_ = portlets;
    }

    public String getName() { return name_; }
    public void setName(String name) { name_ = name; }

    public List<PortletExtra> getPortlets() { return portlets_; }
    public void setPortlets(List<PortletExtra> portlets) { portlets_ = portlets; }
    
    public void addPortlet(PortletExtra portlet) {
      if(portlets_ == null) portlets_ = new ArrayList<PortletExtra>();
      portlets_.add(portlet);
    }
    
  }
  
  static public class WebAppComparator implements Comparator<WebApp> {

    public int compare(WebApp app1, WebApp app2) {
      return app1.getName().compareToIgnoreCase(app2.getName());
    }
    
  }
  
  static public class PortletComparator implements Comparator<PortletExtra> {

    public int compare(PortletExtra portlet1, PortletExtra portlet2) {
      return portlet1.getName().compareToIgnoreCase(portlet2.getName());
    }
    
  }

}
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
package org.exoplatform.applicationregistry.webui.component;

import java.util.ArrayList;
import java.util.Calendar;

import org.exoplatform.application.newregistry.Application;
import org.exoplatform.application.newregistry.ApplicationRegistryService;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIPopupComponent;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.organization.UIListPermissionSelector;

/**
 * Created by The eXo Platform SARL
 * Author : Nguyen Thi Hoa
 *          hoa.nguyen@exoplatform.com
 * Sep 26, 2006  
 */

@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/form/UIForm.gtmpl",
    events = @EventConfig(listeners = UIPermissionForm.SaveActionListener.class)
)
public class UIPermissionForm extends UIForm implements UIPopupComponent {
 
  private Application portlet_;
  
  public UIPermissionForm() throws Exception{
    UIListPermissionSelector selector = addChild(UIListPermissionSelector.class, null, "UIListPermissionSelector") ;
    selector.setName("UIListPermissionSelector") ;
  }
  
  public void setValue(Application portlet) throws Exception {
    portlet_ = portlet;
    ArrayList<String> accessPermissions = portlet_.getAccessPermissions() ;
    String[] per = new String[accessPermissions.size()];
    if (accessPermissions != null && accessPermissions.size() > 0) {
      getChild(UIListPermissionSelector.class).setValue(accessPermissions.toArray(per)) ;
    }
  }
  
  public Application getPortlet() { return portlet_; }
  
  static public class SaveActionListener extends EventListener<UIPermissionForm> {    
    public void execute(Event<UIPermissionForm> event) throws Exception {
      UIPermissionForm  uiPermissionForm = event.getSource();
      Application portlet = uiPermissionForm.getPortlet() ;
      UIListPermissionSelector uiListPermissionSelector = uiPermissionForm.getChild(UIListPermissionSelector.class) ;
      ArrayList<String> pers = new ArrayList<String>();
      if(uiListPermissionSelector.getValue()!= null)
      for(String per: uiListPermissionSelector.getValue()) pers.add(per);
      portlet.setAccessPermissions(pers) ;
      ApplicationRegistryService service = uiPermissionForm.getApplicationComponent(ApplicationRegistryService.class) ;
      portlet.setModifiedDate(Calendar.getInstance().getTime());
      service.update(portlet) ;      
      UIPopupWindow uiPopupWindow = uiPermissionForm.getParent();
      uiPopupWindow.setShow(false);
    }
  }
  public void activate() throws Exception {}
  public void deActivate() throws Exception {}  
}
/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portletregistry.webui.component;

import java.util.Calendar;

import org.exoplatform.application.registry.Application;
import org.exoplatform.application.registry.ApplicationRegistryService;
import org.exoplatform.organization.webui.component.UIAccessGroup;
import org.exoplatform.webui.component.UIForm;
import org.exoplatform.webui.component.UIPopupWindow;
import org.exoplatform.webui.component.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SARL
 * Author : Nguyen Thi Hoa
 *          hoa.nguyen@exoplatform.com
 * Sep 26, 2006  
 */

@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/component/UIFormWithTitle.gtmpl",
    events = @EventConfig(listeners = UIPermissionForm.SaveActionListener.class)
)
public class UIPermissionForm extends UIForm {
 
  private Application portlet_;
  
  public UIPermissionForm() throws Exception{
    addChild(UIAccessGroup.class, null, "Permission");
  }
  
  public void setValue(Application portlet) throws Exception {    
    portlet_ = portlet;
    String[] accessGroup = new String[]{};
    if(portlet.getAccessGroup() != null) { 
      accessGroup = portlet.getAccessGroup();
    }
    getChild(UIAccessGroup.class).setGroups(accessGroup);
  }
  
  public Application getPortlet() { return portlet_; }
  
  static public class SaveActionListener extends EventListener<UIPermissionForm> {    
    public void execute(Event<UIPermissionForm> event) throws Exception {
      UIPermissionForm  uiPermissionForm = event.getSource();
      Application portlet = uiPermissionForm.getPortlet() ;

      UIAccessGroup accessGroupForm = uiPermissionForm.getChild(UIAccessGroup.class);
      portlet.setAccessGroup(accessGroupForm.getAccessGroup());
      ApplicationRegistryService service = uiPermissionForm.getApplicationComponent(ApplicationRegistryService.class) ;
      portlet.setModifiedDate(Calendar.getInstance().getTime());
      service.update(portlet) ;
      
      UIPopupWindow popupWindow = uiPermissionForm.getParent();
      popupWindow.setShow(false);
    }
  }

}

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

import org.exoplatform.application.registry.Application;
import org.exoplatform.application.registry.ApplicationCategory;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SAS
 * Author : Pham Thanh Tung
 *          thanhtungty@gmail.com
 * Jul 4, 2008  
 */

@ComponentConfig(
    template = "app:/groovy/applicationregistry/webui/component/UIApplicationInfo.gtmpl",
    events = {
        @EventConfig(listeners = UIApplicationInfo.EditApplicationActionListener.class)
    }
)

public class UIApplicationInfo extends UIContainer {
  
  private Application application_ ;
  
  public UIApplicationInfo() throws Exception {
    addChild(UIPermissionForm.class, null, null) ;
  }

  public Application getApplication() { return application_ ; }
  
  public void setApplication(Application app) throws Exception { 
    application_ = app ;
    if(application_ == null) return ;
    UIPermissionForm uiPermissionForm = getChild(UIPermissionForm.class) ;
    uiPermissionForm.setValue(application_) ;
  }
  
  public ApplicationCategory getApplicationCategory() {
    UIApplicationOrganizer uiOrganizer = getAncestorOfType(UIApplicationOrganizer.class);
    return uiOrganizer.getSelectedCategory();
  }
  
  public void processRender(WebuiRequestContext context) throws Exception {
    super.processRender(context);
  }

  public static class EditApplicationActionListener extends EventListener<UIApplicationInfo> {

    public void execute(Event<UIApplicationInfo> event) throws Exception {
      UIApplicationOrganizer uiOrganizer = event.getSource().getParent();
      UIApplicationForm uiForm = uiOrganizer.createUIComponent(UIApplicationForm.class, null, null);
      uiForm.setValues(uiOrganizer.getSelectedApplication()) ;
      uiOrganizer.getChildren().clear();
      uiOrganizer.addChild(uiForm);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiOrganizer); 
    }
    
  }

}

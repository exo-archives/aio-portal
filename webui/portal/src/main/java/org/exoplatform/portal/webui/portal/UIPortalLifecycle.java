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
package org.exoplatform.portal.webui.portal;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.Lifecycle;

/**
 * Created by The eXo Platform SAS
 * May 8, 2006
 */
public class UIPortalLifecycle extends Lifecycle { 
  
  public void processRender(UIComponent uicomponent , WebuiRequestContext context) throws Exception { 
    UIPortal uiPortal = (UIPortal) uicomponent;
    if(uiPortal.getMaximizedUIComponent() != null){
      UIComponent uiComponent = uiPortal.getMaximizedUIComponent();
      uiComponent.processRender(context);
      return;
    }
    super.processRender(uicomponent, context);
  }
  
}
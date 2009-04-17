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
package org.exoplatform.gadget.webui.component;

import javax.portlet.PortletPreferences;

import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.webui.application.UIGadget;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * Created by The eXo Platform SARL
 * Author : dang.tung
 *          tungcnw@gmail.com
 * June 27, 2008
 */
@ComponentConfig(
		lifecycle = UIApplicationLifecycle.class,
		template = "app:/groovy/gadget/webui/component/UIGadgetPortlet.gtmpl"
)
public class UIGadgetPortlet extends UIPortletApplication {
  public UIGadgetPortlet() throws Exception {
    UIGadget uiGadget = addChild(UIGadget.class, null, null);
    uiGadget.setId(Integer.toString(uiGadget.hashCode()+1));
    uiGadget.setUrl(getUrl());
    uiGadget.setView("canvas");
    uiGadget.setDecorator(false);
    StringBuilder windowId = new StringBuilder(PortalConfig.PORTAL_TYPE);
    windowId.append("#").append(Util.getUIPortal().getOwner());
    windowId.append(":/gadgetportlet/sample/");
    windowId.append(uiGadget.hashCode());
    uiGadget.setApplicationInstanceId(windowId.toString());    
    addChild(UIGadgetEditMode.class, null, null);
  }
  
  static public String getUrl() {
    PortletRequestContext pcontext = (PortletRequestContext) WebuiRequestContext.getCurrentInstance();
    PortletPreferences pref = pcontext.getRequest().getPreferences();
    return pref.getValue("url", "http://www.google.com/ig/modules/horoscope.xml");
  }

}


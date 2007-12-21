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
package org.exoplatform.portal.webui.component;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SARL
 * Author : Philippe Aristote
 *          philippe.aristote@gmail.com
 * May 23, 2007
 */
@ComponentConfigs({
    @ComponentConfig(
      lifecycle = UIApplicationLifecycle.class,
      template = "app:/groovy/webui/component/UIHelloPortlet.gtmpl",
      events = {
        @EventConfig(listeners = UIHelloPortlet.OpenPopupActionListener.class)
      }
    ),
    @ComponentConfig(
        type = UIContainer.class,
        id = "UIHelloContent",
        template = "app:/groovy/webui/component/UIHelloContent.gtmpl"
    )
})
public class UIHelloPortlet extends UIPortletApplication {

  public UIHelloPortlet() throws Exception {
    UIContainer uiContainer = createUIComponent(UIContainer.class, "UIHelloContent", null); 
    uiContainer.addChild(UIHelloSelector.class, null, null);
    uiContainer.addChild(UIHelloWelcome.class, null, null);
    uiContainer.addChild(UIHelloForm.class, null, null).setRendered(false);
    addChild(uiContainer);
    UIPopupWindow popup = addChild(UIPopupWindow.class, null, null);
    popup.setWindowSize(400, 300);
    UIHelloForm form = createUIComponent(UIHelloForm.class, null, null);
    popup.setUIComponent(form);
    popup.setRendered(false);
  }
  
  static public class OpenPopupActionListener extends EventListener<UIHelloPortlet> {
    public void execute(Event<UIHelloPortlet> event) throws Exception {
      UIHelloPortlet portlet = event.getSource();
      UIPopupWindow popup = portlet.getChild(UIPopupWindow.class);
//      UIHelloForm form = portlet.getChild(UIContainer.class).getChild(UIHelloForm.class);
//      popup.setUIComponent(form);
//      form.setRendered(true);
      popup.setRendered(true);
      popup.setShow(true);
    }
  }
}
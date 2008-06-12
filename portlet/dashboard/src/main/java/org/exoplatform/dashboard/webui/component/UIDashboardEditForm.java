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
package org.exoplatform.dashboard.webui.component;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;

import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.exception.MessageException;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;

/**
 * Created by The eXo Platform SAS
 * Author : Pham Dinh Tan
 *          tan.pham@exoplatform.com
 * Jun 7, 2008  
 */
@ComponentConfigs({
    @ComponentConfig(
        template = "system:/groovy/webui/form/UIForm.gtmpl",
        lifecycle = UIFormLifecycle.class,
        events = {
          @EventConfig(listeners = UIDashboardEditForm.SaveActionListener.class)
        }
    )
})
public class UIDashboardEditForm extends UIForm {
  
  final static public String TOTAL_COLUMNS = "totalColumns";
  
  public UIDashboardEditForm() throws Exception {
    PortletRequestContext pcontext = (PortletRequestContext) WebuiRequestContext.getCurrentInstance();
    PortletPreferences pref = pcontext.getRequest().getPreferences();
    addUIFormInput(new UIFormStringInput(TOTAL_COLUMNS, TOTAL_COLUMNS, pref.getValue("totalColumns", "3" )));
  }
  
  static public class SaveActionListener extends EventListener<UIDashboardEditForm> {
    public void execute(Event<UIDashboardEditForm> event) throws Exception {

      UIDashboardEditForm uiForm = event.getSource();
      UIFormStringInput uiInput = uiForm.getUIStringInput(TOTAL_COLUMNS);
      Object[] args = { TOTAL_COLUMNS, String.valueOf(1), String.valueOf(4)};
      
      PortletRequestContext pcontext = (PortletRequestContext) WebuiRequestContext.getCurrentInstance();
      PortletPreferences pref = pcontext.getRequest().getPreferences();
      String lastValue = pref.getValue(TOTAL_COLUMNS, "3");
      
      if(uiInput.getValue()==null || uiInput.getValue().length()==0){
        uiInput.setValue(lastValue);
        throw new MessageException(new ApplicationMessage("EmptyFieldValidator.msg.empty-input", args));
      }
      
      int totalCols = 0;
      try {
        totalCols = Integer.parseInt(uiInput.getValue());
      } catch (Exception e) {
        uiInput.setValue(lastValue);
        throw new MessageException(new ApplicationMessage("NumberFormatValidator.msg.Invalid-number", args));
      }

      if(totalCols<1 || totalCols>4) {
        uiInput.setValue(lastValue);
        throw new MessageException(new ApplicationMessage("NumberInRangeValidator.msg.Invalid-number", args));
      }
      
      pref.setValue(TOTAL_COLUMNS, String.valueOf(totalCols));
      pref.store();

      UIDashboardContainer uiDashboardContainer = ((UIDashboardPortlet)uiForm.getParent()).getChild(UIDashboardContainer.class);
      uiDashboardContainer.setColumns(totalCols);
      pcontext.setApplicationMode(PortletMode.VIEW);
    }
  }
}
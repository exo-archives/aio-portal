/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
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
package org.exoplatform.portal.webui.container;

import java.util.Collections;
import java.util.List;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.webui.container.UIContainerActionListener.EditContainerActionListener;
import org.exoplatform.portal.webui.portal.UIPortalComponent;
import org.exoplatform.portal.webui.portal.UIPortalComponentActionListener.DeleteComponentActionListener;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIPortalApplication;
import org.exoplatform.portal.webui.workspace.UIWorkingWorkspace;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * Created by The eXo Platform SAS
 * Author : Nguyen Duc Khoi
 *          khoi.nguyen@exoplatform.com
 * Jul 15, 2010  
 */

@ComponentConfig(template = "system:/groovy/portal/webui/container/UIColumnContainer.gtmpl", events = {
    @EventConfig(listeners = UIColumnContainer.InsertColumnActionListener.class),
    @EventConfig(listeners = DeleteComponentActionListener.class, confirm = "UIColumnContainer.deleteColumnContainer"),
    @EventConfig(listeners = EditContainerActionListener.class)
}
)
public class UIColumnContainer extends UIContainer {
  public static final String COLUMN_CONTAINER = "ColumnContainer";

  public static final String INSERT_AFTER     = "insertRight";

  public static final String INSERT_BEFORE    = "insertLeft";

  public UIColumnContainer() {
    super();
  }

  public static class InsertColumnActionListener extends EventListener<UIColumnContainer> {
    @Override
    public void execute(Event<UIColumnContainer> event) throws Exception {
      String insertPosition = event.getRequestContext().getRequestParameter(UIComponent.OBJECTID);
      UIColumnContainer uiSelectedColumn = event.getSource();
      UIPortalComponent uiParent = (UIPortalComponent) uiSelectedColumn.getParent();
      if (insertPosition.equals(INSERT_AFTER)) {
        UIColumnContainer.insertColumn(uiSelectedColumn, true);
      } else if (insertPosition.equals(INSERT_BEFORE)) {
        UIColumnContainer.insertColumn(uiSelectedColumn, false);
      }

      Util.showComponentLayoutMode(uiSelectedColumn.getClass());

      PortalRequestContext pcontext = (PortalRequestContext) event.getRequestContext();
      UIPortalApplication uiPortalApp = uiParent.getAncestorOfType(UIPortalApplication.class);
      UIWorkingWorkspace uiWorkingWS = uiPortalApp.getChildById(UIPortalApplication.UI_WORKING_WS_ID);
      pcontext.addUIComponentToUpdateByAjax(uiWorkingWS);
      pcontext.setFullRender(true);
    }

  }

  private static void insertColumn(UIColumnContainer selectedColumn, boolean isInsertAfter) throws Exception {
    UIContainer uiParent = selectedColumn.getParent();
    UIColumnContainer uiNewColumn = uiParent.addChild(UIColumnContainer.class, null, null);
    
    uiNewColumn.setTemplate(selectedColumn.getTemplate());
    uiNewColumn.setFactoryId(selectedColumn.getFactoryId());
    uiNewColumn.setId(String.valueOf(uiNewColumn.hashCode()));

    List<UIComponent> listColumn = uiParent.getChildren();
    int position = listColumn.indexOf(selectedColumn);
    if (isInsertAfter) {
      position += 1;
    }
    Collections.rotate(listColumn.subList(position, listColumn.size()), 1);
  }

}

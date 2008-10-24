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

import java.util.Hashtable;
import java.util.List;

import org.exoplatform.application.registry.Application;
import org.exoplatform.application.registry.ApplicationCategory;
import org.exoplatform.application.registry.ApplicationRegistryService;
import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

@ComponentConfig(
    template = "classpath:groovy/dashboard/webui/component/UIDashboardSelectContainer.gtmpl", 
    lifecycle = UIFormLifecycle.class
) 
public class UIDashboardSelectContainer extends UIContainer {
  
  private List<ApplicationCategory> categories ;
  
  private ApplicationCategory selectedCategory ;

  private Hashtable<ApplicationCategory, List<Application>> gadgets ;

  private boolean isShow = false ;
  
  private String aggregatorId ;

  public UIDashboardSelectContainer() throws Exception {
    addChild(UIAddGadgetForm.class, null, null) ;
  }
  
  public void setSelectedCategory(ApplicationCategory category) {
    selectedCategory = category ;
  }
  
  public ApplicationCategory getSelectedCategory() {
    return selectedCategory ;
  }

  public final List<ApplicationCategory> getCategories() throws Exception {
    ApplicationRegistryService service = getApplicationComponent(ApplicationRegistryService.class);

    String remoteUser = ((WebuiRequestContext) WebuiRequestContext.getCurrentInstance())
        .getRemoteUser();
    List<ApplicationCategory> listCategories = service.getApplicationCategories(remoteUser,
        org.exoplatform.web.application.Application.EXO_GAGGET_TYPE);

    gadgets = new Hashtable<ApplicationCategory, List<Application>>();

    for (int i = 0; i < listCategories.size(); i++) {
      ApplicationCategory cate = listCategories.get(i);
      List<Application> listGadgets = service.getApplications(cate,
          org.exoplatform.web.application.Application.EXO_GAGGET_TYPE);
      if (listGadgets == null || listGadgets.size() == 0) {
        listCategories.remove(i);
        i--;
      } else {
        gadgets.put(cate, listGadgets);
      }
    }
    categories = listCategories;
    return categories;
  }

  public void setCategories(final List<ApplicationCategory> categories) throws Exception {
    this.categories = categories;
  }

  public List<Application> getGadgetsOfCategory(final ApplicationCategory appCategory)
    throws Exception {
    List<Application> listGadgets = gadgets.get(appCategory);
    if (listGadgets == null || listGadgets.size() == 0) {
      return null;
    }
    return listGadgets;
  }

  public boolean isShow() {
    return isShow;
  }

  public void setShow(final boolean value) {
    this.isShow = value;
  }
  
  public String getAggregatorId() {
    return aggregatorId;
  }

  public void setAggregatorId(String aggregatorId) {
    this.aggregatorId = aggregatorId;
  }

  static public class SetShowSelectContainerActionListener extends EventListener<org.exoplatform.webui.core.UIContainer> {
    public final void execute(final Event<org.exoplatform.webui.core.UIContainer> event) throws Exception {
      org.exoplatform.webui.core.UIContainer uiDashboard = event.getSource();
      if (!((UIDashboard)uiDashboard).canEdit())
        return;
      UIDashboardSelectContainer uiForm = uiDashboard.getChild(UIDashboardSelectContainer.class);
      PortletRequestContext pcontext = (PortletRequestContext) event.getRequestContext();
      boolean isShow = Boolean.parseBoolean(pcontext.getRequestParameter("isShow"));

      uiForm.setShow(isShow);
    }
  }

}

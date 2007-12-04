/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.webui.component;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.application.registry.Application;
import org.exoplatform.application.registry.ApplicationCategory;
import org.exoplatform.application.registry.ApplicationRegistryService;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.webui.application.UIPortlet;
import org.exoplatform.portal.webui.page.UIPage;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.portal.webui.util.PortalDataMapper;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIPortalApplication;
import org.exoplatform.portal.webui.workspace.UIWorkspace;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormInputInfo;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormTabPane;
import org.exoplatform.webui.form.UIFormTableInputSet;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Dec 29, 2006  
 */
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/form/UIFormTabPane.gtmpl",
    events = {
      @EventConfig(listeners = UIAddPortletForm.SaveActionListener.class),    
      @EventConfig(listeners = UIAddPortletForm.RefreshActionListener.class, phase=Phase.DECODE)
    }
)
public class UIAddPortletForm extends UIFormTabPane {   

  final static String [] TABLE_COLUMNS = {"label", "description", "input"};

  public UIAddPortletForm() throws Exception {
    super("UIAddPortletForm");
    super.setInfoBar(false);
    super.setRenderResourceTabName(false) ;

    loadPortlet();
  } 

  @SuppressWarnings("unchecked")
  private void loadPortlet() throws Exception {
    getChildren().clear();    
    ApplicationRegistryService registeryService = getApplicationComponent(ApplicationRegistryService.class) ;
    List<ApplicationCategory> portletCategories = registeryService.getApplicationCategories();

    String tableName = getClass().getSimpleName();
    //boolean selected = true;
    // TODO: dang.tung - set which tab will be display - in this case tab with index = 1; (index inc from 1)
    if(portletCategories.size() > 0) setSelectedTab(1) ;
    
    for(ApplicationCategory category : portletCategories) {      
      UIFormInputSet uiInputSet = new UIFormInputSet(category.getName()) ;
      addUIFormInput(uiInputSet) ;           

      UIFormTableInputSet uiTableInputSet = createUIComponent(UIFormTableInputSet.class, null, null) ;
      uiTableInputSet.setName(tableName);
      uiTableInputSet.setColumns(TABLE_COLUMNS);
      uiInputSet.addChild(uiTableInputSet);

      List<Application> portlets = registeryService.getApplications(category) ; 
      for(Application portlet : portlets){
        String id = portlet.getId();      

        uiInputSet = new UIFormInputSet(id) ;
        UIFormInputInfo uiInfo = new UIFormInputInfo("label", null, portlet.getDisplayName());
        uiInputSet.addChild(uiInfo);
        uiInfo = new UIFormInputInfo("description", null, portlet.getDescription());
        uiInputSet.addChild(uiInfo);

        UIFormCheckBoxInput<String> uiCheckbox = new UIFormCheckBoxInput<String>(id, null, id);       
        uiCheckbox.setValue(id);
        uiCheckbox.setChecked(false);
        uiInputSet.addChild(uiCheckbox);
        uiTableInputSet.addChild(uiInputSet);       
      }
    }   

  } 

  public void processDecode(WebuiRequestContext context) throws Exception {
    super.processDecode(context);
    for(UIComponent child : getChildren())  {
      child.processDecode(context) ;
    }
  }

  @SuppressWarnings("unchecked")
  static public class SaveActionListener extends EventListener<UIAddPortletForm> {
    public void execute(Event<UIAddPortletForm> event) throws Exception {
      List<UIFormCheckBoxInput> listCheckbox =  new ArrayList<UIFormCheckBoxInput>();
      event.getSource().findComponentOfType(listCheckbox, UIFormCheckBoxInput.class);

      UIPortal uiPortal = Util.getUIPortal();        
      UIPage uiPage = uiPortal.findFirstComponentOfType(UIPage.class);

      for(UIFormCheckBoxInput<String> ele : listCheckbox){
        if(!ele.isChecked())continue;    
        UIPortlet uiPortlet =  uiPage.createUIComponent(UIPortlet.class, null, null);
        StringBuilder windowId = new StringBuilder();
        windowId.append(Util.getUIPortal().getOwner()).append(":/");
        windowId.append(ele.getValue()).append('/').append(uiPortlet.hashCode());
        uiPortlet.setWindowId(windowId.toString());
        uiPage.addChild(uiPortlet);
      }      

      Page page = PortalDataMapper.toPageModel(uiPage); 
      UserPortalConfigService configService = uiPortal.getApplicationComponent(UserPortalConfigService.class);     
      configService.update(page);

      PortalRequestContext pcontext = (PortalRequestContext)event.getRequestContext().getParentAppRequestContext();
      UIPortalApplication uiPortalApp = uiPortal.getAncestorOfType(UIPortalApplication.class);
      UIWorkspace uiWorkingWS = uiPortalApp.findComponentById(UIPortalApplication.UI_WORKING_WS_ID);    
      pcontext.addUIComponentToUpdateByAjax(uiWorkingWS) ;
      pcontext.setFullRender(true);
    }  
    
  }

  @SuppressWarnings("unchecked")
  static public class RefreshActionListener  extends EventListener<UIAddPortletForm> {
    public void execute(Event<UIAddPortletForm> event) throws Exception {  
      event.getSource().loadPortlet();
    }  
  }

}
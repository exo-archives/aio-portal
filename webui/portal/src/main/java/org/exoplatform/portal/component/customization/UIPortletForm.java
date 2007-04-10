/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.component.customization;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.component.UIPortalApplication;
import org.exoplatform.portal.component.UIWorkspace;
import org.exoplatform.portal.component.control.UIMaskWorkspace;
import org.exoplatform.portal.component.view.PortalDataModelUtil;
import org.exoplatform.portal.component.view.UIPortlet;
import org.exoplatform.portal.component.view.Util;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.webui.component.UIComponent;
import org.exoplatform.webui.component.UIFormCheckBoxInput;
import org.exoplatform.webui.component.UIFormInputIconSelector;
import org.exoplatform.webui.component.UIFormInputSet;
import org.exoplatform.webui.component.UIFormStringInput;
import org.exoplatform.webui.component.UIFormTabPane;
import org.exoplatform.webui.component.UIFormTextAreaInput;
import org.exoplatform.webui.component.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.component.validator.EmptyFieldValidator;
import org.exoplatform.webui.component.validator.NumberFormatValidator;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
/**
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@yahoo.com
 * Jun 8, 2006
 */
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "system:/groovy/webui/component/UIFormTabPane.gtmpl",
    events = {
      @EventConfig(listeners = UIMaskWorkspace.CloseActionListener.class, phase = Phase.DECODE),
      @EventConfig(listeners = UIPortletForm.SaveActionListener.class )
    }
)   
/*initParams = {
      @ParamConfig(
          name = "PortletDecorator", 
          value = "app:/WEB-INF/conf/uiconf/portal/webui/component/customization/PortletDecorator.groovy"
      ),          
      @ParamConfig(
          name = "PortletTemplate",
          value = "app:/WEB-INF/conf/uiconf/portal/webui/component/customization/PortletTemplate.groovy"
      ),
      @ParamConfig(
          name = "help.UIPortletFormQuickHelp",
          value = "app:/WEB-INF/conf/uiconf/portal/webui/component/customization/UIPortletFormQuickHelp.xhtml"
      )
    },*/
public class UIPortletForm extends UIFormTabPane {	
  
	private UIPortlet uiPortlet_ ;
  private UIComponent backComponent_ ;
  
  @SuppressWarnings("unchecked")
  public UIPortletForm() throws Exception {//InitParams initParams
  	super("UIPortletForm");
    UIFormInputSet uiSettingSet = new UIFormInputSet("PortletSetting") ;
  	uiSettingSet.
      addUIFormInput(new UIFormStringInput("id", "id", null).
                     addValidator(EmptyFieldValidator.class)).
      addUIFormInput(new UIFormStringInput("applicationInstanceId", "applicationInstanceId", null).
                     addValidator(EmptyFieldValidator.class)).               
    	addUIFormInput(new UIFormStringInput("title", "title", null)).
  		addUIFormInput(new UIFormStringInput("width", "width", null).
                     addValidator(NumberFormatValidator.class)).
  		addUIFormInput(new UIFormStringInput("height", "height", null).
                     addValidator(NumberFormatValidator.class)).
  		addUIFormInput(new UIFormCheckBoxInput("showInfoBar", "showInfoBar", false)).
  		addUIFormInput(new UIFormCheckBoxInput("showApplicationMode", "showApplicationMode", false)).
    	addUIFormInput(new UIFormCheckBoxInput("showApplicationState", "showApplicationState", false)).
      addUIFormInput(new UIFormTextAreaInput("description", "description", null));
    addUIFormInput(uiSettingSet);    
    
//    UIFormInputDecoratorSelector uiDecorator = new UIFormInputDecoratorSelector("Decorator", "decorator");
//    uiDecorator.setRendered(false) ;
//  	addUIFormInput(uiDecorator);
    
    UIFormInputIconSelector uiIconSelector = new UIFormInputIconSelector("Icon", "icon") ;
    uiIconSelector.setRendered(false)  ;
    addUIFormInput(uiIconSelector) ;
    
   /* UIFormInputItemSelector uiTemplate = new UIFormInputItemSelector("Template", "template");
    uiTemplate.setTypeValue(String.class);
    uiTemplate.setRendered(false);
    addUIFormInput(uiTemplate);*/

//    if(initParams == null) return ;
//    UIFormInputDecoratorSelector uiDecoratorInput = getChild(UIFormInputDecoratorSelector.class);
//    RequestContext context = RequestContext.getCurrentInstance() ;
//    List<SelectItemOption> options = initParams.getParam("PortletDecorator").getMapGroovyObject(context) ;
//    uiDecoratorInput.setOptions(options);
  }
  
  public UIComponent getBackComponent() { return backComponent_ ; }
  public void setBackComponent(UIComponent uiComp) throws Exception {
    backComponent_ = uiComp;
  }
  
  public UIPortlet getUIPortlet() { return uiPortlet_; }
  
  public void setValues(UIPortlet uiPortlet) throws Exception {
  	uiPortlet_ = uiPortlet;
    Application portlet = PortalDataModelUtil.toPortletModel(uiPortlet) ;
    getUIStringInput("id").setEditable(false);
    getUIStringInput("applicationInstanceId").setEditable(false);
    invokeGetBindingBean(portlet) ;
  }
  
	static public class SaveActionListener extends EventListener<UIPortletForm> {
    public void execute(Event<UIPortletForm> event) throws Exception {      
      UIPortletForm uiPortletForm = event.getSource() ;
      UIPortlet uiPortlet = uiPortletForm.getUIPortlet() ;
      Application portlet = new Application() ;
      UIFormInputIconSelector iconSelector = uiPortletForm.getChild(UIFormInputIconSelector.class);
      portlet.setIcon(iconSelector.getSelectedIcon());
      uiPortletForm.invokeSetBindingBean(portlet) ;
      PortalDataModelUtil.toUIPortlet(uiPortlet, portlet);
      
      UIMaskWorkspace uiMaskWorkspace = uiPortletForm.getParent();
      uiMaskWorkspace.setUIComponent(null);
      
      PortalRequestContext pcontext = (PortalRequestContext)event.getRequestContext();
      pcontext.addUIComponentToUpdateByAjax(uiMaskWorkspace);
      
      UIPortalApplication uiPortalApp = uiPortlet.getAncestorOfType(UIPortalApplication.class);
      UIWorkspace uiWorkingWS = uiPortalApp.findComponentById(UIPortalApplication.UI_WORKING_WS_ID);
      pcontext.addUIComponentToUpdateByAjax(uiWorkingWS);
      pcontext.setFullRender(true);
      Util.showComponentLayoutMode(UIPortlet.class);  
    }
  }
  
}

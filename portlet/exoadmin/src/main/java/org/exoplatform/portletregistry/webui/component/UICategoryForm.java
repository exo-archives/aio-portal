/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portletregistry.webui.component;

import java.util.Date;

import org.exoplatform.application.registry.ApplicationCategory;
import org.exoplatform.application.registry.ApplicationRegistryService;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIPopupWindow;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;
import org.exoplatform.webui.form.validator.EmptyFieldValidator;
import org.exoplatform.webui.form.validator.IdentifierValidator;

/**
 * Created by The eXo Platform SARL
 * Author : Hoa Nguyen
 *          hoa.nguyen@exoplatform.com
 * Jul 4, 2006  
 */
@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template =  "system:/groovy/webui/form/UIForm.gtmpl",
    events = {
      @EventConfig(listeners = UICategoryForm.SaveActionListener.class),
      @EventConfig(phase = Phase.DECODE, listeners = UICategoryForm.CloseActionListener.class)
    }
)
public class UICategoryForm extends UIForm { 

  final static private String FIELD_NAME = "name" ;
  final static private String FIELD_DISPLAY_NAME = "displayName" ;
  final static private String FIELD_DESCRIPTION = "description" ;

  private ApplicationCategory category_ = null ;

  public UICategoryForm() throws Exception {
    addUIFormInput(new UIFormStringInput(FIELD_NAME, FIELD_NAME, null).
                   addValidator(EmptyFieldValidator.class).
                   addValidator(IdentifierValidator.class));
    addUIFormInput(new UIFormStringInput(FIELD_DISPLAY_NAME, FIELD_DISPLAY_NAME, null));
    addUIFormInput(new UIFormTextAreaInput(FIELD_DESCRIPTION, FIELD_DESCRIPTION, null)); 
  } 

  public void setValue(ApplicationCategory category) throws Exception {
    reset();
    if(category == null) {
      category_ = null;
      getUIStringInput(FIELD_NAME).setEditable(UIFormStringInput.ENABLE) ;
      return ;
    }
    getUIStringInput(FIELD_NAME).setEditable(false);    
    category_ = category ;
    invokeGetBindingBean(category);
  }

  public ApplicationCategory getCategory() { return category_ ; }

  static public class SaveActionListener extends EventListener<UICategoryForm> {
    public void execute(Event<UICategoryForm> event) throws Exception{
      //TODO: Tung.Pham replaced
      UICategoryForm uiForm = event.getSource() ;
      UIPopupWindow uiParent = uiForm.getParent();
      ApplicationRegistryControlArea uiRegistryCategory = uiForm.getAncestorOfType(ApplicationRegistryControlArea.class);
      ApplicationRegistryService service = uiForm.getApplicationComponent(ApplicationRegistryService.class);

      ApplicationCategory category = uiForm.getCategory() ;
      if(category == null) category = new ApplicationCategory();
      uiForm.invokeSetBindingBean(category) ;
      if(category == uiForm.getCategory()) {
        category.setModifiedDate(new Date()) ;
      }else {
        ApplicationCategory existCategory = uiRegistryCategory.getCategory(category.getName()) ; 
        if(existCategory != null) {
          UIApplication uiApp = event.getRequestContext().getUIApplication() ;
          uiApp.addMessage(new ApplicationMessage("UICategoryForm.msg.SameName", null)) ;
          return ;
        }
        category.setModifiedDate(new Date()) ;
        category.setCreatedDate(new Date()) ;
      }

      service.save(category) ;
      uiForm.setValue(null) ;
      uiRegistryCategory.initApplicationCategories();
      uiRegistryCategory.setSelectedCategory(category);
      uiParent.setShow(false);
      //---------------------------------------------------------------------
//      UICategoryForm uiForm = event.getSource() ;
//      UIPopupWindow uiParent = uiForm.getParent();
//      uiParent.setShow(false);
//      ApplicationCategory category = uiForm.getCategory() ;
//
//      ApplicationRegistryControlArea uiRegistryCategory = uiForm.getAncestorOfType(ApplicationRegistryControlArea.class);
//      ApplicationRegistryService service = uiForm.getApplicationComponent(ApplicationRegistryService.class);
//
//      if(category == null) {
//        String name = uiForm.getUIStringInput(FIELD_NAME).getValue();
//        category = uiRegistryCategory.getCategory(name);
//      }
//
//      if(category == null) category = new ApplicationCategory();
//
//      uiForm.invokeSetBindingBean(category) ;
//      service.save(category) ; 
//      uiRegistryCategory.initApplicationCategories();
//      uiRegistryCategory.setSelectedCategory(category);
    }
  }

  static public class CloseActionListener extends EventListener<UICategoryForm>{
    public void execute(Event<UICategoryForm> event) throws Exception{
      UICategoryForm uiForm = event.getSource() ;
      uiForm.setValue(null) ;
      UIPopupWindow uiParent = uiForm.getParent();
      uiParent.setShow(false);
    }
  }
  
}

package org.exoplatform.portal.account;

import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIMaskWorkspace;
import org.exoplatform.portal.account.UIAccountSetting;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.config.annotation.ParamConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.EmailAddressValidator;
import org.exoplatform.webui.form.validator.EmptyFieldValidator;
import org.exoplatform.webui.form.validator.IdentifierValidator;
/**
 * Created by The eXo Platform SARL
 * Author : Tungnd
 */

@ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template = "app:/groovy/webui/form/UIForm.gtmpl",

    events = {
        @EventConfig(listeners = UIMaskWorkspace.CloseActionListener.class, phase = Phase.DECODE),
        @EventConfig(listeners = UIAccountProfiles.SaveActionListener.class),
        @EventConfig(listeners = UIAccountProfiles.ResetActionListener.class, phase = Phase.DECODE)
      }
)

public class UIAccountProfiles extends UIForm {
  
  // constructor
  public UIAccountProfiles() throws Exception {
    super();
    //  get information about user
    String username = Util.getPortalRequestContext().getRemoteUser() ;
    OrganizationService service = this.getApplicationComponent(OrganizationService.class);
    User useraccount = service.getUserHandler().findUserByName(username);
    
    // user
    UIFormStringInput userName  = new UIFormStringInput("userName","userName", username) ;
    userName.setEditable(false) ;
    addUIFormInput(userName.
        addValidator(EmptyFieldValidator.class).
        addValidator(IdentifierValidator.class)) ;
    // first name
    addUIFormInput(new UIFormStringInput("firstName", "firstName", useraccount.getFirstName()).
            addValidator(EmptyFieldValidator.class)) ;
    // last name
    addUIFormInput(new UIFormStringInput("lastName", "lastName", useraccount.getLastName()).
            addValidator(EmptyFieldValidator.class)) ;
    // email
    addUIFormInput(new UIFormStringInput("email", "email", useraccount.getEmail()). 
            addValidator(EmptyFieldValidator.class).
            addValidator(EmailAddressValidator.class)) ;
  }
  
  static  public class ResetActionListener extends EventListener<UIAccountProfiles> {
    public void execute(Event<UIAccountProfiles> event) throws Exception {
      UIAccountProfiles uiForm = event.getSource() ;
      String userName = uiForm.getUIStringInput("userName").getValue() ;
      OrganizationService service =  uiForm.getApplicationComponent(OrganizationService.class);
      User user = service.getUserHandler().findUserByName(userName) ; 
      uiForm.getUIStringInput("firstName").setValue(user.getFirstName()) ;
      uiForm.getUIStringInput("lastName").setValue(user.getLastName()) ;
      uiForm.getUIStringInput("email").setValue(user.getEmail()) ;
      event.getRequestContext().addUIComponentToUpdateByAjax(uiForm) ;
    }
  }
  
  static  public class SaveActionListener extends EventListener<UIAccountProfiles> {
    public void execute(Event<UIAccountProfiles> event) throws Exception {
      UIAccountProfiles uiForm = event.getSource();
      OrganizationService service =  uiForm.getApplicationComponent(OrganizationService.class);
      WebuiRequestContext context = WebuiRequestContext.getCurrentInstance() ;
      UIApplication uiApp = context.getUIApplication() ;
     
      // get userName input
      String userName = uiForm.getUIStringInput("userName").getValue() ;
      User user = service.getUserHandler().findUserByName(userName) ;    
      user.setFirstName(uiForm.getUIStringInput("firstName").getValue()) ;
      user.setLastName(uiForm.getUIStringInput("lastName").getValue()) ;
      user.setEmail(uiForm.getUIStringInput("email").getValue()) ;
      uiApp.addMessage(new ApplicationMessage("UIAccountProfiles.msg.update.success", null)) ;
      service.getUserHandler().saveUser(user, true) ;
      context.addUIComponentToUpdateByAjax(uiApp.getUIPopupMessages());
      return;
    }
  }
}

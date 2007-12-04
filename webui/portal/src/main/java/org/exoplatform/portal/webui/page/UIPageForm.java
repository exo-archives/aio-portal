package org.exoplatform.portal.webui.page;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.webui.application.UIPortlet;
import org.exoplatform.portal.webui.container.UIContainer;
import org.exoplatform.portal.webui.navigation.UIPageManagement;
import org.exoplatform.portal.webui.navigation.UIPageNavigationControlBar;
import org.exoplatform.portal.webui.navigation.UIPageNodeSelector;
import org.exoplatform.portal.webui.util.PortalDataMapper;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIControlWorkspace;
import org.exoplatform.portal.webui.workspace.UIMaskWorkspace;
import org.exoplatform.portal.webui.workspace.UIPortalApplication;
import org.exoplatform.portal.webui.workspace.UIPortalToolPanel;
import org.exoplatform.portal.webui.workspace.UIWorkspace;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.InitParams;
import org.exoplatform.webui.config.Param;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.config.annotation.ParamConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemCategory;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIFormCheckBoxInput;
import org.exoplatform.webui.form.UIFormInputItemSelector;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTabPane;
import org.exoplatform.webui.form.validator.EmptyFieldValidator;
import org.exoplatform.webui.form.validator.IdentifierValidator;
import org.exoplatform.webui.form.validator.PageNodeNameValidator;
import org.exoplatform.webui.form.validator.StringLengthValidator;
import org.exoplatform.webui.organization.UIGroupMembershipSelector;
import org.exoplatform.webui.organization.UIListPermissionSelector;
import org.exoplatform.webui.organization.UIPermissionSelector;
import org.exoplatform.webui.organization.UIListPermissionSelector.EmptyIteratorValidator;

@ComponentConfigs({
  @ComponentConfig(
    lifecycle = UIFormLifecycle.class,
    template =  "system:/groovy/webui/form/UIFormTabPane.gtmpl",    
    events = {
      @EventConfig(listeners = UIPageForm.SaveActionListener.class),
      @EventConfig(listeners = UIPageForm.ChangeOwnerTypeActionListener.class, phase = Phase.DECODE),
      @EventConfig(listeners = UIPageForm.SelectMembershipActionListener.class, phase = Phase.DECODE),
      @EventConfig(listeners = UIPageForm.ChangeOwnerIdActionListener.class, phase = Phase.DECODE),
      @EventConfig(listeners = UIMaskWorkspace.CloseActionListener.class, phase = Phase.DECODE)
    },
    initParams = @ParamConfig(
      name = "PageTemplate",
      value = "system:/WEB-INF/conf/uiconf/portal/webui/page/PageTemplate.groovy"
    )
  ),
  @ComponentConfig(
      type = UIFormInputSet.class,
      id = "PermissionSetting",
      template = "system:/groovy/webui/core/UITabSelector.gtmpl"
  )
})
public class UIPageForm extends UIFormTabPane {
  
  private UIPage uiPage_ ;
  protected UIFormInputSet uiPermissionSetting;
  protected UIFormSelectBox groupIdSelectBox = null;
  protected UIFormStringInput ownerIdInput = null;
  
  
  public static final String OWNER_TYPE = "ownerType";
  public static final String OWNER_ID = "ownerId";
  @SuppressWarnings("unchecked")
  public UIPageForm(InitParams initParams) throws Exception  {
    super("UIPageForm");
    PortalRequestContext pcontext = Util.getPortalRequestContext();
    UserPortalConfigService configService = getApplicationComponent(UserPortalConfigService.class);
    List<SelectItemOption<String>> ownerTypes = new ArrayList<SelectItemOption<String>>() ;
    ownerTypes.add(new SelectItemOption<String>(PortalConfig.USER_TYPE)) ;
    if(Util.getUIPortal().isModifiable()) {
      ownerTypes.add(new SelectItemOption<String>(PortalConfig.PORTAL_TYPE)) ;
    }
    ownerIdInput = new UIFormStringInput(OWNER_ID, OWNER_ID, null);
    ownerIdInput.setEditable(false).setValue(pcontext.getRemoteUser());
    
    UIFormSelectBox uiSelectBoxOwnerType = new UIFormSelectBox(OWNER_TYPE, OWNER_TYPE , ownerTypes) ;
    uiSelectBoxOwnerType.setOnChange("ChangeOwnerType");
    
    UIFormInputSet uiSettingSet = new UIFormInputSet("PageSetting");
    uiSettingSet.addUIFormInput(new UIFormStringInput("pageId", "pageId", null).setEditable(false)).
                 addUIFormInput(uiSelectBoxOwnerType).
                 addUIFormInput(ownerIdInput).
                 addUIFormInput(new UIFormStringInput("name", "name", null).
                                addValidator(EmptyFieldValidator.class).
                                addValidator(IdentifierValidator.class).
                                addValidator(StringLengthValidator.class, 3, 30).
                                addValidator(PageNodeNameValidator.class)).
                 addUIFormInput(new UIFormStringInput("title", "title", null)).
                 addUIFormInput(new UIFormCheckBoxInput("showMaxWindow", "showMaxWindow", false));
    addUIFormInput(uiSettingSet) ;
    //TODO: modify by dang.tung
    //      set which form will be display first, in this case is page setting form.         
    setSelectedTab(uiSettingSet.getId()) ;
    
    uiPermissionSetting = createUIComponent(UIFormInputSet.class, "PermissionSetting", null);
    UIListPermissionSelector uiListPermissionSelector = createUIComponent(UIListPermissionSelector.class, null, null);
    uiListPermissionSelector.configure("UIListPermissionSelector", "accessPermissions");
    uiListPermissionSelector.addValidator(EmptyIteratorValidator.class) ;
    uiPermissionSetting.addChild(uiListPermissionSelector);
    UIPermissionSelector uiEditPermission = createUIComponent(UIPermissionSelector.class, null, null);
    uiEditPermission.setRendered(false) ;
    uiEditPermission.addValidator(org.exoplatform.webui.organization.UIPermissionSelector.EmptyFieldValidator.class);
    uiEditPermission.setEditable(false);
    uiEditPermission.configure("UIPermissionSelector", "editPermission");
    uiPermissionSetting.addChild(uiEditPermission);

    WebuiRequestContext context = WebuiRequestContext.getCurrentInstance() ;
    Param param = initParams.getParam("PageTemplate");
    List<SelectItemCategory>  itemCategories =  param.getMapGroovyObject(context) ;
    
    UIFormInputItemSelector uiTemplate = new UIFormInputItemSelector("Template", "template");
    uiTemplate.setItemCategories(itemCategories);
    uiTemplate.setRendered(false);
    addUIFormInput(uiTemplate);
    
    List<String> groups = configService.getMakableNavigations(pcontext.getRemoteUser());
    if(groups.size() > 0){
      ownerTypes.add(new SelectItemOption<String>(PortalConfig.GROUP_TYPE)) ;
      List<SelectItemOption<String>> groupsItem = new ArrayList<SelectItemOption<String>>() ;
      for(String group: groups){
        groupsItem.add(new SelectItemOption<String>(group));
      }
      groupIdSelectBox = new UIFormSelectBox(OWNER_ID, OWNER_ID, groupsItem);
      groupIdSelectBox.setOnChange("ChangeOwnerId");
      groupIdSelectBox.setParent(uiSettingSet);
    }
    setActions(new String[]{"Save", "Close" });
  }
  
  public UIPage getUIPage() { return uiPage_ ; }
  
  @SuppressWarnings("unchecked")
  public void setValues(UIPage uiPage) throws Exception {
    uiPage_ = uiPage;
    Page page = PortalDataMapper.toPageModel(uiPage) ;
    if(uiPage.getOwnerType().equals(PortalConfig.USER_TYPE)) {
      removeChildById("PermissionSetting") ;  
    } else if(getChildById("PermissionSetting") == null) {
      addUIComponentInput(uiPermissionSetting);
    }
    uiPermissionSetting.getChild(UIPermissionSelector.class).setEditable(true);
    invokeGetBindingBean(page) ;
    getUIStringInput("name").setEditable(false) ;
    getUIStringInput("pageId").setValue(uiPage.getPageId());
    getUIFormCheckBoxInput("showMaxWindow").setValue(uiPage.isShowMaxWindow());
    getUIFormSelectBox(OWNER_TYPE).setEnable(false).setValue(uiPage.getOwnerType());
    removeChild(UIPageTemplateOptions.class);
    
    UIFormInputItemSelector uiTemplate = getChild(UIFormInputItemSelector.class);
    if(uiTemplate == null)  return;
    if(page.getFactoryId() == null || page.getFactoryId().trim().length() < 1) {
      uiTemplate.setValue("Default");
      return;
    }
    uiTemplate.setValue(uiPage.getFactoryId());
  }
  
  public void setEditPermission(String per){
    
  }
  
  public  void invokeSetBindingBean(Object bean) throws Exception {
//    super.invokeSetBindingBean(bean);
    Page page = (Page)bean;    
    page.setPageId(getUIStringInput("pageId").getValue());
    page.setOwnerType(getUIFormSelectBox("ownerType").getValue());
    page.setOwnerId(getUIStringInput("ownerId").getValue());
    page.setName(getUIStringInput("name").getValue());
    String title = getUIStringInput("title").getValue() ;
    if(title == null || title.trim().length() < 1) title = page.getName() ;
    page.setTitle(title);
    
    if(!page.isShowMaxWindow()) {
      page.setShowMaxWindow((Boolean) getUIFormCheckBoxInput("showMaxWindow").getValue());      
    }
    if(!PortalConfig.USER_TYPE.equals(page.getOwnerType())) {
      page.setAccessPermissions(uiPermissionSetting.getChild(UIListPermissionSelector.class).getValue());
      page.setEditPermission(uiPermissionSetting.getChild(UIPermissionSelector.class).getValue());
    }
    UserACL userACL = getApplicationComponent(UserACL.class) ;
    String remoteUser = Util.getPortalRequestContext().getRemoteUser() ;
    userACL.hasPermission(page, remoteUser) ;

    UIFormInputItemSelector uiTemplate = getChildById("Template");
    if(uiTemplate != null) {
      SelectItemOption<?> itemOption = uiTemplate.getSelectedItemOption();
      if(itemOption != null){
        page.setFactoryId(itemOption.getIcon());
        page.setTemplate((String)itemOption.getValue());
        page.setShowMaxWindow(page.getFactoryId().equals(Page.DESKTOP_PAGE));
      } 
    } 
    UIPageTemplateOptions uiConfigOptions = getChild(UIPageTemplateOptions.class);
    if(uiConfigOptions == null) return;
    Page selectedPage = uiConfigOptions.getSelectedOption();
    if(selectedPage == null) return ;
    page.setChildren(selectedPage.getChildren());
    page.setFactoryId(selectedPage.getFactoryId());
    if(Page.DESKTOP_PAGE.equals(page.getFactoryId())) page.setShowMaxWindow(true);
    
  }

  @SuppressWarnings("unchecked")
  static public class SaveActionListener  extends EventListener<UIPageForm> {
    public void execute(Event<UIPageForm> event) throws Exception {
      UIPageForm uiPageForm = event.getSource();   
      UIPortalApplication uiPortalApp = uiPageForm.getAncestorOfType(UIPortalApplication.class);
      PortalRequestContext pcontext = Util.getPortalRequestContext();
      UIMaskWorkspace uiMaskWS = uiPortalApp.getChildById(UIPortalApplication.UI_MASK_WS_ID) ;
      uiMaskWS.setUIComponent(null);
      uiMaskWS.setShow(false);
      pcontext.addUIComponentToUpdateByAjax(uiMaskWS) ;
      
      UIPage uiPage = uiPageForm.getUIPage();
      if(uiPage == null)  return;
      Page page = new Page() ;
      page.setPageId(uiPage.getPageId());
      uiPageForm.invokeSetBindingBean(page);
      page.setOwnerType(uiPage.getOwnerType());
      List<UIPortlet> uiPortlets = new ArrayList<UIPortlet>();
      findAllPortlet(uiPortlets, uiPage);
      ArrayList<Object> applications = new ArrayList<Object>();
      for(UIPortlet uiPortlet : uiPortlets) {
        applications.add(PortalDataMapper.toPortletModel(uiPortlet));
      }
      
      UIPageManagement uiManagement = uiPortalApp.findFirstComponentOfType(UIPageManagement.class);
      UIPageEditBar uiEditBar = uiManagement.getChild(UIPageEditBar.class); 
      if(Page.DESKTOP_PAGE.equals(uiPage.getFactoryId()) && !Page.DESKTOP_PAGE.equals(page.getFactoryId())) {
        page.setShowMaxWindow(false);
        uiPage.getChildren().clear();
        page.setChildren(applications);

        page.setModifier(pcontext.getRemoteUser());
        PortalDataMapper.toUIPage(uiPage, page);  
        if(page.getTemplate() == null) page.setTemplate(uiPage.getTemplate()) ;
        if(page.getChildren() == null) page.setChildren(new ArrayList<Object>()); 

        uiEditBar.setUIPage(uiPage);
        Class<?> [] childrenToRender = {UIPageEditBar.class,
            UIPageNodeSelector.class, UIPageNavigationControlBar.class};      
        uiManagement.setRenderedChildrenOfTypes(childrenToRender);
        
        pcontext.setFullRender(true);
        UIControlWorkspace uiControl = uiPortalApp.findComponentById(UIPortalApplication.UI_CONTROL_WS_ID) ;
        pcontext.addUIComponentToUpdateByAjax(uiControl) ;
        UIWorkspace uiWorkingWS = uiPortalApp.findComponentById(UIPortalApplication.UI_WORKING_WS_ID);    
        pcontext.addUIComponentToUpdateByAjax(uiWorkingWS) ;

        return;
      }

      if(Page.DESKTOP_PAGE.equals(page.getFactoryId())) {
        uiPage.getChildren().clear();         
        page.setChildren(applications);         

        page.setModifier(pcontext.getRemoteUser());
        PortalDataMapper.toUIPage(uiPage, page);  
        if(page.getTemplate() == null) page.setTemplate(uiPage.getTemplate()) ;
        if(page.getChildren() == null) page.setChildren(new ArrayList<Object>()); 

        uiEditBar.setUIPage(uiPage);
        Class<?> [] childrenToRender = null;
        if(uiManagement.getChild(UIPageBrowseControlBar.class).isRendered()) {
          childrenToRender = new Class<?>[]{UIPageBrowseControlBar.class};
        } else {
          childrenToRender = new Class<?>[]{UIPageNodeSelector.class, UIPageNavigationControlBar.class};
        }
        uiManagement.setRenderedChildrenOfTypes(childrenToRender);
        pcontext.addUIComponentToUpdateByAjax(uiManagement);
        UIPortalToolPanel toolPanel = Util.getUIPortalToolPanel();
        toolPanel.setShowMaskLayer(true);
        pcontext.setFullRender(true);
        UIWorkspace uiWorkingWS = uiPortalApp.findComponentById(UIPortalApplication.UI_WORKING_WS_ID);    
        pcontext.addUIComponentToUpdateByAjax(uiWorkingWS) ;
        UserPortalConfigService service = uiEditBar.getApplicationComponent(UserPortalConfigService.class);
        service.update(page);
        return;
      } 
      
      List<UIComponent> uiChildren = uiPage.getChildren();
      if(uiChildren == null)  {
        PortalDataMapper.toUIPage(uiPage, page);
        return ;
      }
      ArrayList<Object>  children = new ArrayList<Object>();
      for(UIComponent child : uiChildren){ 
        Object component = PortalDataMapper.buildChild(child);
        if(component != null) children.add(component);
      }
      page.setChildren(children);
      uiPage.getChildren().clear(); 

      page.setModifier(pcontext.getRemoteUser());
      PortalDataMapper.toUIPage(uiPage, page);  
      if(page.getTemplate() == null) page.setTemplate(uiPage.getTemplate()) ;
      if(page.getChildren() == null) page.setChildren(new ArrayList<Object>());
    }
    
    protected void findAllPortlet(List<UIPortlet> list, UIContainer uiContainer) {
      List<UIComponent> children = uiContainer.getChildren();
      for(UIComponent ele : children) {
        if(ele instanceof UIPortlet) list.add((UIPortlet)ele);
        else if(ele instanceof UIContainer) findAllPortlet(list, (UIContainer) ele); 
      }
    }
  }
 
  static public class ChangeOwnerTypeActionListener  extends EventListener<UIPageForm> {
    public void execute(Event<UIPageForm> event) throws Exception {
      UIPageForm uiForm = event.getSource();
      UIFormSelectBox uiSelectBox = uiForm.getUIFormSelectBox(OWNER_TYPE);
      String ownerType = uiSelectBox.getValue();
      PortalRequestContext prContext = Util.getPortalRequestContext();
      UIFormInputSet uiSettingSet = uiForm.getChildById("PageSetting");
      uiForm.setSelectedTab("PageSetting");
      List<UIComponent> list = uiSettingSet.getChildren();
      if(PortalConfig.USER_TYPE.equals(ownerType)){
        uiForm.removeChildById("PermissionSetting") ; 
        list.remove(2);
        list.add(2, uiForm.ownerIdInput);
        uiForm.ownerIdInput.setValue(prContext.getRemoteUser());
      } else {
        if(uiForm.getChildById("PermissionSetting") == null) {
          uiForm.addUIComponentInput(uiForm.uiPermissionSetting);
          
        }
        if(PortalConfig.PORTAL_TYPE.equals(ownerType)){
          list.remove(2);
          list.add(2, uiForm.ownerIdInput);
          uiForm.ownerIdInput.setValue(Util.getUIPortal().getName());
          uiForm.findFirstComponentOfType(UIListPermissionSelector.class).setValue(Util.getUIPortal().getAccessPermissions());
          uiForm.findFirstComponentOfType(UIPermissionSelector.class).setValue(Util.getUIPortal().getEditPermission());
        } else {
          list.remove(2);
          list.add(2, uiForm.groupIdSelectBox);
          String permission = "*:/" + uiForm.groupIdSelectBox.getValue();
          uiForm.findFirstComponentOfType(UIListPermissionSelector.class).setValue(new String[]{permission});
          UserACL userACL = uiForm.getApplicationComponent(UserACL.class); 
          permission = userACL.getMakableMT() + ":/" + uiForm.groupIdSelectBox.getValue();
          uiForm.findFirstComponentOfType(UIPermissionSelector.class).setValue(permission);
        }
      }
      prContext.addUIComponentToUpdateByAjax(uiForm.getParent());
    }
  }

  static  public class ChangeOwnerIdActionListener extends EventListener<UIPageForm> {    
    public void execute(Event<UIPageForm> event) throws Exception {
      UIPageForm uiForm = event.getSource();
      String permission = "*:/" + uiForm.groupIdSelectBox.getValue();
      uiForm.findFirstComponentOfType(UIListPermissionSelector.class).setValue(new String[]{permission});
      UserACL userACL = uiForm.getApplicationComponent(UserACL.class); 
      permission = userACL.getMakableMT() + ":/" + uiForm.groupIdSelectBox.getValue();
      uiForm.findFirstComponentOfType(UIPermissionSelector.class).setValue(permission);
      event.getRequestContext().addUIComponentToUpdateByAjax(uiForm.getParent()) ;
    }
  }
  
  static  public class SelectMembershipActionListener extends EventListener<UIGroupMembershipSelector> {    
    public void execute(Event<UIGroupMembershipSelector> event) throws Exception {
        UIPageForm uiForm = event.getSource().getAncestorOfType(UIPageForm.class);
        if(!uiForm.getUIStringInput(OWNER_TYPE).getValue().equals(PortalConfig.GROUP_TYPE) ) return ;
        String editPer = uiForm.findFirstComponentOfType(UIPermissionSelector.class).getValue();
        if(editPer == null || editPer.length() < 1) return;
        String group = editPer.substring(editPer.indexOf("/") + 1);
        uiForm.ownerIdInput.setValue(group);
        event.getRequestContext().addUIComponentToUpdateByAjax(uiForm.getParent());
    }
  }
}
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
package org.exoplatform.portal.webui.page;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.webui.UIWelcomeComponent;
import org.exoplatform.portal.webui.application.UIPortletOptions;
import org.exoplatform.portal.webui.navigation.UIPageNodeSelector;
import org.exoplatform.portal.webui.portal.PageNodeEvent;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.portal.webui.util.PortalDataMapper;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIControlWorkspace;
import org.exoplatform.portal.webui.workspace.UIExoStart;
import org.exoplatform.portal.webui.workspace.UIPortalApplication;
import org.exoplatform.portal.webui.workspace.UIWorkspace;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.web.application.RequestContext;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponentDecorator;
import org.exoplatform.webui.core.model.SelectItemCategory;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minhdv81@yahoo.com
 * Jun 23, 2006
 */
@ComponentConfig(
    template = "system:/groovy/webui/core/UIWizard.gtmpl" ,
    events = {
        @EventConfig(listeners = UIPageEditWizard.ViewStep1ActionListener.class),
        @EventConfig(listeners = UIPageEditWizard.ViewStep2ActionListener.class),
        @EventConfig(listeners = UIPageEditWizard.ViewStep3ActionListener.class),
        @EventConfig(listeners = UIPageEditWizard.ViewStep4ActionListener.class),
        @EventConfig(listeners = UIPageEditWizard.AbortActionListener.class)
    }
)
public class UIPageEditWizard extends UIPageWizard {
  
  public UIPageEditWizard() throws Exception {
    addChild(UIWizardPageSetInfo.class, null, "EditWizard") ;
    UIWizardPageSelectLayoutForm layoutForm = addChild(UIWizardPageSelectLayoutForm.class, null, null).setRendered(false);
    addChild(UIPagePreview.class, null, null).setRendered(false); 
    setNumberSteps(3);
    setShowWelcomeComponent(false);
    UIPageTemplateOptions templateOption = layoutForm.getChild(UIPageTemplateOptions.class);
    List<SelectItemCategory> categorys = templateOption.getItemCategories();
    SelectItemCategory category = categorys.get(0);
    List<SelectItemOption<?>> options = category.getSelectItemOptions();
    SelectItemOption<Object> curent = new SelectItemOption<Object>("Current Layout", null);
    if(options.get(0).getValue() != null)  options.add(0, curent);
    templateOption.setSelectOptionItem("Current Layout");
  }
  
  private void saveData() throws Exception {
    UserPortalConfigService service = getApplicationComponent(UserPortalConfigService.class);
    
    UIPagePreview uiPagePreview = getChild(UIPagePreview.class);
    UIPage uiPage = (UIPage)uiPagePreview.getUIComponent();
    Page page = PortalDataMapper.toPageModel(uiPage);
    service.update(page); 
    
    UIWizardPageSetInfo uiPageInfo = getChild(UIWizardPageSetInfo.class);  
    UIPageNodeSelector uiNodeSelector = uiPageInfo.getChild(UIPageNodeSelector.class);      
    PageNavigation pageNav =  uiNodeSelector.getSelectedNavigation();
    String uri = pageNav.getId() + "::" + uiPageInfo.getPageNode().getUri();
    pageNav.setModifier(RequestContext.<WebuiRequestContext>getCurrentInstance().getRemoteUser());
    service.update(pageNav);
    
    UIPortal uiPortal = Util.getUIPortal();
    for(PageNavigation editNav : uiNodeSelector.getPageNavigations()) {
      setNavigation(uiPortal.getNavigations(), editNav);
    }
    PageNodeEvent<UIPortal> pnevent = new PageNodeEvent<UIPortal>(uiPortal, PageNodeEvent.CHANGE_PAGE_NODE, null, uri) ;
    uiPortal.broadcast(pnevent, Event.Phase.PROCESS) ;
  }
  
  private void setNavigation(List<PageNavigation> navs, PageNavigation nav) {
    for(int i = 0; i < navs.size(); i++) {
      if(navs.get(i).getId().equals(nav.getId())) {
        navs.set(i, nav);
        return;
      }
    }
  }
  
  static  public class ViewStep1ActionListener extends EventListener<UIPageWizard> {
    public void execute(Event<UIPageWizard> event) throws Exception { 
      UIPageWizard uiWizard = event.getSource();
      uiWizard.setDescriptionWizard(2);
      
      uiWizard.updateWizardComponent();
      uiWizard.viewStep(1);   
    }
  }
  
  static  public class ViewStep2ActionListener extends EventListener<UIPageWizard> {
    public void execute(Event<UIPageWizard> event) throws Exception {
      UIPageWizard uiWizard = event.getSource();
      UIPortalApplication uiPortalApp = uiWizard.getAncestorOfType(UIPortalApplication.class);
      PortalRequestContext pcontext = Util.getPortalRequestContext() ;
      uiWizard.setDescriptionWizard(3);
      
      uiWizard.updateWizardComponent();
      UIWizardPageSetInfo uiPageInfo = uiWizard.getChild(UIWizardPageSetInfo.class); 
      UIPageNodeSelector uiPageNodeSelector = uiPageInfo.getChild(UIPageNodeSelector.class);
      if(uiPageNodeSelector.getSelectedNavigation() == null) {
        uiPortalApp.addMessage(new ApplicationMessage("UIPageEditWizard.msg.notSelectedPageNavigation", new String[]{})) ;;
        pcontext.addUIComponentToUpdateByAjax(uiPortalApp.getUIPopupMessages());
        uiWizard.viewStep(1);
        return ;
      }
      
      PageNode pageNode = uiPageNodeSelector.getSelectedPageNode() ;
      if(pageNode == null) {
        uiPortalApp.addMessage(new ApplicationMessage("UIPageEditWizard.msg.notSelectedPage", null)) ;
        pcontext.addUIComponentToUpdateByAjax(uiPortalApp.getUIPopupMessages()) ;
        uiWizard.viewStep(1);
        return ;
      }
      
      UserPortalConfigService configService = uiWizard.getApplicationComponent(UserPortalConfigService.class) ;
      Page page = configService.getPage(pageNode.getPageReference(), pcontext.getRemoteUser()) ;
      if(page == null) {
        uiWizard.viewStep(1) ;
        return ;
      }

      if(!page.isModifiable()) {
        uiPortalApp.addMessage(new ApplicationMessage("UIPageEditWizard.msg.Invalid-editPermission", null)) ;
        pcontext.addUIComponentToUpdateByAjax(uiPortalApp.getUIPopupMessages()) ;
        uiWizard.viewStep(1);
        return ;
      }
      
      uiWizard.updateWizardComponent();
      uiWizard.viewStep(2);
    }
  }

  static  public class ViewStep3ActionListener extends EventListener<UIPageEditWizard> {
    public void execute(Event<UIPageEditWizard> event) throws Exception {
      UIPageEditWizard uiWizard = event.getSource();
      WebuiRequestContext context = event.getRequestContext() ;
      UIPortalApplication uiPortalApp = uiWizard.getAncestorOfType(UIPortalApplication.class);
      UIWizardPageSetInfo uiPageInfo = uiWizard.getChild(UIWizardPageSetInfo.class); 
      PageNode seletctedPageNode = uiPageInfo.getPageNode() ;
      UserPortalConfigService userService = uiWizard.getApplicationComponent(UserPortalConfigService.class) ;
      Page selectPage = userService.getPage(seletctedPageNode.getPageReference(), context.getRemoteUser()) ;

      if(selectPage == null|| !selectPage.isModifiable()) {
        uiPortalApp.addMessage(new ApplicationMessage("UIPageEditWizard.msg.Invalid-editPermission", null)) ;
        context.addUIComponentToUpdateByAjax(uiPortalApp.getUIPopupMessages()) ;
        uiWizard.viewStep(1);
        return ;
      }
      uiWizard.viewStep(3);      
      if(uiWizard.getSelectedStep() < 3){
        uiWizard.updateWizardComponent();
        uiPortalApp.addMessage(new ApplicationMessage("UIPageEditWizard.msg.selectStep2", null)) ;
        context.addUIComponentToUpdateByAjax(uiPortalApp.getUIPopupMessages() );
        return;
      }
      
      UIExoStart uiExoStart = uiPortalApp.findFirstComponentOfType(UIExoStart.class);      
      uiExoStart.setUIControlWSWorkingComponent(UIWizardPageCreationBar.class);
      UIWizardPageCreationBar uiCreationBar = uiExoStart.getUIControlWSWorkingComponent();
      
      UIPageEditBar uiPageEditBar = uiCreationBar.getChild(UIPageEditBar.class);
      UIWizardPageCreationBar uiParent = uiPageEditBar.getParent();
      
      UIPageTemplateOptions uiPageTemplateOptions = uiWizard.findFirstComponentOfType(UIPageTemplateOptions.class);
      
      Page templatePage = uiPageTemplateOptions.createPageFromSelectedOption(selectPage.getOwnerType(), selectPage.getOwnerId());
      boolean isDesktopPage = false;
      if(templatePage != null ) {
        templatePage.setName(selectPage.getName());
        templatePage.setCreator(selectPage.getCreator()) ;
        templatePage.setAccessPermissions(selectPage.getAccessPermissions()) ;
        templatePage.setEditPermission(selectPage.getEditPermission()) ;
        selectPage  = templatePage;
        isDesktopPage = Page.DESKTOP_PAGE.equals(selectPage.getFactoryId());
        if(isDesktopPage) {
          selectPage.setChildren(new ArrayList<Object>());
          selectPage.setShowMaxWindow(true);
        }
      } else {
        isDesktopPage = Page.DESKTOP_PAGE.equals(selectPage.getFactoryId());
      }
      selectPage.setModifier(context.getRemoteUser());
      selectPage.setTitle(seletctedPageNode.getLabel()) ;
      
      UIPagePreview uiPagePreview = uiWizard.getChild(UIPagePreview.class);
      UIPage uiPage = null;
      if(Page.DEFAULT_PAGE.equals(selectPage.getFactoryId())) {
        uiPage = uiPagePreview.createUIComponent(context, UIPage.class, null, null);
      } else {
        uiPage = uiPagePreview.createUIComponent(context, UIPage.class, selectPage.getFactoryId(), null);
      }
      PortalDataMapper.toUIPage(uiPage, selectPage);
      uiPortalApp.findFirstComponentOfType(UIPageBody.class).setUIComponent(null) ;
      uiPagePreview.setUIComponent(uiPage);
      
      if(isDesktopPage){
        uiWizard.saveData();
        uiWizard.updateUIPortal(uiPortalApp, event);
        return;
      }

      Class<?> [] childrenToRender = {UIPageEditBar.class, UIPortletOptions.class}; 
      uiParent.setRenderedChildrenOfTypes(childrenToRender);
      
      uiPageEditBar.setUIPage(uiPage);      
      uiPageTemplateOptions.setSelectedOption(null);
      uiWizard.updateWizardComponent();
    }
  }

  static  public class ViewStep4ActionListener extends EventListener<UIPageEditWizard> {
    public void execute(Event<UIPageEditWizard> event) throws Exception {
      UIPageEditWizard uiWizard = event.getSource();
      uiWizard.saveData();
      UIPortalApplication uiPortalApp = event.getSource().getAncestorOfType(UIPortalApplication.class);
      uiWizard.updateUIPortal(uiPortalApp, event);
    }
  }  
  
  static public class AbortActionListener extends EventListener<UIPageEditWizard> {
    public void execute(Event<UIPageEditWizard> event) throws Exception {
      UIPortalApplication uiPortalApp = event.getSource().getAncestorOfType(UIPortalApplication.class);
      PortalRequestContext pcontext = (PortalRequestContext)event.getRequestContext();

      UIControlWorkspace uiControl = uiPortalApp.findComponentById(UIPortalApplication.UI_CONTROL_WS_ID);
      UIComponentDecorator uiWorkingArea = uiControl.getChildById(UIControlWorkspace.WORKING_AREA_ID);
      uiWorkingArea.setUIComponent(uiWorkingArea.createUIComponent(UIWelcomeComponent.class, null, null)) ;
      pcontext.addUIComponentToUpdateByAjax(uiControl);  

      UIPortal uiPortal = Util.getUIPortal();
      uiPortal.setMode(UIPortal.COMPONENT_VIEW_MODE);
      uiPortal.setRenderSibbling(UIPortal.class) ;    
      pcontext.setFullRender(true);
      
      UIWorkspace uiWorkingWS = uiPortalApp.findComponentById(UIPortalApplication.UI_WORKING_WS_ID);
      pcontext.addUIComponentToUpdateByAjax(uiWorkingWS);      
    }
  }

}

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
package org.exoplatform.portal.webui.navigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.portal.webui.workspace.UIMaskWorkspace;
import org.exoplatform.portal.webui.workspace.UIPortalApplication;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponentDecorator;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.model.SelectItemOption;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormSelectBox;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.UIFormTextAreaInput;
import org.exoplatform.webui.form.validator.StringLengthValidator;

/**
 * Author : Le Bien Thuy lebienthuy@gmail.com Aug 30, 2007
 */
@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "system:/groovy/webui/form/UIFormWithTitle.gtmpl", events = {
    @EventConfig(listeners = UIPageNavigationForm.SaveActionListener.class),
    @EventConfig(listeners = UIPageNavigationForm.ChangeOwnerTypeActionListener.class),
    @EventConfig(listeners = UIMaskWorkspace.CloseActionListener.class, phase = Phase.DECODE) })
public class UIPageNavigationForm extends UIForm {

  protected PageNavigation pageNav_;

  public UIPageNavigationForm() throws Exception {
    PortalRequestContext pContext = PortalRequestContext.getCurrentInstance();
    UserPortalConfigService dataService = getApplicationComponent(UserPortalConfigService.class);
    List<String> list = dataService.getMakableNavigations(pContext.getRemoteUser());
    List<SelectItemOption<String>> makableGroups = new ArrayList<SelectItemOption<String>>();
    for (String ele : list) {
      makableGroups.add(new SelectItemOption<String>(ele));
    }
    UIFormSelectBox uiSelectBoxOwnerId = new UIFormSelectBox("ownerId", "ownerId", makableGroups);
    List<SelectItemOption<String>> priorties = new ArrayList<SelectItemOption<String>>();
    for (int i = 1; i < 11; i++) {
      priorties.add(new SelectItemOption<String>(String.valueOf(i), String.valueOf(i)));
    }

    // check navigation has user type

    UIPortal uiPortal = Util.getUIPortal();

    UIPortalApplication uiPortalApp = uiPortal.getAncestorOfType(UIPortalApplication.class);
    UIPageNodeSelector uiPageNodeSelector = uiPortalApp.findFirstComponentOfType(UIPageNodeSelector.class);

    List<PageNavigation> navis = uiPageNodeSelector.getPageNavigations();
    List<PageNavigation> pnavigations = getExistedNavigation(navis);

    boolean isHasUserNavigation = false;

    for (PageNavigation nav : pnavigations) {
      if (nav.isModifiable() && nav.getOwnerType().equals(PortalConfig.USER_TYPE))
        isHasUserNavigation = true;
    }

    List<SelectItemOption<String>> ownerType = new ArrayList<SelectItemOption<String>>();
    ownerType.add(new SelectItemOption<String>(PortalConfig.GROUP_TYPE, PortalConfig.GROUP_TYPE));
    if (!isHasUserNavigation) {
      ownerType.add(new SelectItemOption<String>(PortalConfig.USER_TYPE, PortalConfig.USER_TYPE));
    }

    // addUIFormInput(new UIFormStringInput("ownerType", "ownerType",
    // PortalConfig.GROUP_TYPE).setEditable(false)).
    UIFormSelectBox uiOwnerType = new UIFormSelectBox("ownerType", null, ownerType);
    uiOwnerType.setOnChange("ChangeOwnerType");
    addUIFormInput(uiOwnerType).addUIFormInput(uiSelectBoxOwnerId)
                               .addUIFormInput(new UIFormStringInput("creator",
                                                                     "creator",
                                                                     pContext.getRemoteUser()).setEditable(false))
                               .addUIFormInput(new UIFormStringInput("modifier", "modifier", null).setEditable(false))
                               .addUIFormInput(new UIFormTextAreaInput("description",
                                                                       "description",
                                                                       null).addValidator(StringLengthValidator.class,
                                                                                          0,
                                                                                          255))
                               .addUIFormInput(new UIFormSelectBox("priority", null, priorties));
    setActions(new String[] { "Save", "Close" });
  }

  private List<PageNavigation> getExistedNavigation(List<PageNavigation> navis) throws Exception {
    Iterator<PageNavigation> itr = navis.iterator();
    UserPortalConfigService configService = getApplicationComponent(UserPortalConfigService.class);
    while (itr.hasNext()) {
      PageNavigation nav = itr.next();
      if (configService.getPageNavigation(nav.getOwnerType(), nav.getOwnerId()) == null)
        itr.remove();
    }
    return navis;
  }

  public void setValues(PageNavigation pageNavigation) throws Exception {
    pageNav_ = pageNavigation;
    invokeGetBindingBean(pageNavigation);
    getUIStringInput("creator").setValue(pageNavigation.getCreator());
    UIFormStringInput ownerIdStringInput = new UIFormStringInput("ownerId",
                                                                 "ownerId",
                                                                 pageNavigation.getOwnerId());
    ownerIdStringInput.setEditable(false);
    replaceChild("ownerId", ownerIdStringInput);
    
    UIFormStringInput ownerTypeStringInput = new UIFormStringInput("ownerType",
                                                                 "ownerType",
                                                                 pageNavigation.getOwnerType());
    ownerTypeStringInput.setEditable(false);
    replaceChild("ownerType", ownerTypeStringInput);
    
    UIFormSelectBox uiSelectBox = findComponentById("priority");
    uiSelectBox.setValue(String.valueOf(pageNavigation.getPriority()));
  }

  static public class SaveActionListener extends EventListener<UIPageNavigationForm> {
    public void execute(Event<UIPageNavigationForm> event) throws Exception {
      UIPageNavigationForm uiForm = event.getSource();
      PageNavigation pageNav = uiForm.pageNav_;
      PortalRequestContext pcontext = (PortalRequestContext) event.getRequestContext();

      if (pageNav != null) {
        uiForm.invokeSetBindingBean(pageNav);
        UIFormSelectBox uiSelectBox = uiForm.findComponentById("priority");
        int priority = Integer.parseInt(uiSelectBox.getValue());
        pageNav.setPriority(priority);
        pageNav.setModifier(pcontext.getRemoteUser());
        UIComponentDecorator uiFormParent = uiForm.getParent();
        uiFormParent.setUIComponent(null);
        pcontext.addUIComponentToUpdateByAjax(uiFormParent);
        return;
      }

      pageNav = new PageNavigation();
      uiForm.invokeSetBindingBean(pageNav);
      UIFormSelectBox uiSelectBox = uiForm.findComponentById("priority");
      int priority = Integer.parseInt(uiSelectBox.getValue());

      UIFormSelectBox uiOwnerType = uiForm.findComponentById("ownerType");

      pageNav.setOwnerType(uiOwnerType.getValue());
      pageNav.setPriority(priority);
      pageNav.setModifiable(true);
      pageNav.setCreator(pcontext.getRemoteUser());

      UIPortalApplication uiPortalApp = uiForm.getAncestorOfType(UIPortalApplication.class);
      UIPageNodeSelector uiPageNodeSelector = uiPortalApp.findFirstComponentOfType(UIPageNodeSelector.class);

      PageNavigation existingNavi = uiPageNodeSelector.getPageNavigation(pageNav.getId());
      if (existingNavi != null || checkExiting(pageNav)) {
        uiPortalApp.addMessage(new ApplicationMessage("UIPageNavigationForm.msg.existPageNavigation",
                                                      new String[] { pageNav.getOwnerId() }));
        ;
        return;
      }

      uiPageNodeSelector.addPageNavigation(pageNav);
      uiPageNodeSelector.selectNavigation(pageNav.getId());
      pcontext.addUIComponentToUpdateByAjax(uiPageNodeSelector.getParent());

      UIMaskWorkspace uiMaskWS = uiPortalApp.getChildById(UIPortalApplication.UI_MASK_WS_ID);
      uiMaskWS.setUIComponent(null);
      uiMaskWS.setShow(false);
      pcontext.addUIComponentToUpdateByAjax(uiMaskWS);
    }

    private boolean checkExiting(PageNavigation navi) throws Exception {
      UIPortal portal = Util.getUIPortal();
      DataStorage service = portal.getApplicationComponent(DataStorage.class);
      List<PageNavigation> list = portal.getNavigations();
      if (service.getPageNavigation(navi.getOwnerType(), navi.getOwnerId()) == null)
        return false;

      for (PageNavigation nav : list) {
        if (nav.getId() == navi.getId())
          return false;
      }
      return true;
    }
  }

  static public class ChangeOwnerTypeActionListener extends EventListener<UIPageNavigationForm> {
    public void execute(Event<UIPageNavigationForm> event) throws Exception {
      UIPageNavigationForm uiForm = event.getSource();
      PortalRequestContext pcontext = (PortalRequestContext) event.getRequestContext();
      UIFormSelectBox uiOwnerType = uiForm.findComponentById("ownerType");
      UIFormSelectBox uiOwnerId = uiForm.findComponentById("ownerId");
      if (uiOwnerType.getValue().equals(PortalConfig.USER_TYPE)) {
        List<SelectItemOption<String>> ownerId = new ArrayList<SelectItemOption<String>>();
        ownerId.add(new SelectItemOption<String>(pcontext.getRemoteUser(), pcontext.getRemoteUser()));

        uiOwnerId.setOptions(ownerId);
        uiOwnerId.setEditable(false);
      } else {
        UserPortalConfigService dataService = uiForm.getApplicationComponent(UserPortalConfigService.class);
        List<String> list = dataService.getMakableNavigations(pcontext.getRemoteUser());
        List<SelectItemOption<String>> makableGroups = new ArrayList<SelectItemOption<String>>();
        for (String ele : list) {
          makableGroups.add(new SelectItemOption<String>(ele));
        }
        uiOwnerId.setOptions(makableGroups);
        uiOwnerId.setEditable(true);
      }
      pcontext.addUIComponentToUpdateByAjax(uiForm);
    }

  }
}

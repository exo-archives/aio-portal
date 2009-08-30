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

import java.util.List;

import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIRepeater;
import org.exoplatform.webui.core.UIVirtualList;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIForm;
import org.exoplatform.webui.form.UIFormInput;
import org.exoplatform.webui.form.UIFormInputContainer;
import org.exoplatform.webui.form.UIFormInputSet;
import org.exoplatform.webui.form.UIFormPopupWindow;
import org.exoplatform.webui.form.UIFormStringInput;
import org.exoplatform.webui.form.validator.IdentifierValidator;
import org.exoplatform.webui.form.validator.MandatoryValidator;
import org.exoplatform.webui.form.validator.StringLengthValidator;

/**
 * Author : Dang Van Minh minhdv81@yahoo.com Jun 14, 2006
 */
@ComponentConfigs( {
    @ComponentConfig(template = "system:/groovy/portal/webui/page/UIPageSelector2.gtmpl"),
    @ComponentConfig(id = "SelectPage", type = UIPageBrowser.class, template = "system:/groovy/portal/webui/page/UIPageBrowser.gtmpl", events = @EventConfig(listeners = UIPageSelector2.SelectPageActionListener.class)) })
public class UIPageSelector2 extends UIFormInputContainer<String> {

  private Page       page_;

  private static Log logger = ExoLogger.getExoLogger(UIPageSelector2.class);

  public UIPageSelector2() throws Exception {
    super("UIPageSelector2", null);
    UIFormPopupWindow uiPopup = addChild(UIFormPopupWindow.class, null, "PopupPageSelector2");
    uiPopup.setWindowSize(900, 400);
    uiPopup.setRendered(false);
    UIPageBrowser uiPageBrowser = createUIComponent(UIPageBrowser.class, "SelectPage", null);
    uiPopup.setUIComponent(uiPageBrowser);

    UIFormInputSet uiInputSet = new UIFormInputSet("PageNodeSetting") ;

    uiInputSet.addChild(new UIFormStringInput("pageId", "pageId", null));
    uiInputSet.addChild(new UIFormStringInput("pageName", "pageName", null).
             addValidator(StringLengthValidator.class, 3, 30).
             addValidator(IdentifierValidator.class).
             addValidator(MandatoryValidator.class));
    uiInputSet.addChild(new UIFormStringInput("pageTitle", "pageTitle", null).
             addValidator(StringLengthValidator.class, 3, 120));

    addChild(uiInputSet);
    UIVirtualList uiVirtualList = uiPageBrowser.getChild(UIVirtualList.class);
    configureVirtualList(uiVirtualList);
  }

  private static void configureVirtualList(UIVirtualList vList) {
    UIRepeater repeater;
    try {
      repeater = (UIRepeater) vList.getUIComponent();
      repeater.configure("pageId", UIPageBrowser.BEAN_FIELD, new String[] { "SelectPage" });
    } catch (ClassCastException clCastEx) {
      logger.info("Could not upcast to UIRepeater", clCastEx);
    }
  }

  public void configure(String iname, String bfield) {
    setId(iname);
    setName(iname);
    setBindingField(bfield);
  }

  public UIFormInput<?> setValue(String value) throws Exception {
    WebuiRequestContext ctx = WebuiRequestContext.getCurrentInstance();
    UserPortalConfigService service = getApplicationComponent(UserPortalConfigService.class);
    Page page = service.getPage(value, ctx.getRemoteUser());
    page_ = page;
    super.setValue(value);
    return this;
  }

  public Page getPage() {
    return page_;
  }

  public void setPage(Page page) {
    page_ = page;
  }

  public Class<String> getTypeValue() {
    return String.class;
  }

  public void processDecode(WebuiRequestContext context) throws Exception {
    super.processDecode(context);
    UIPageBrowser uiPageBrowser = findFirstComponentOfType(UIPageBrowser.class);
    uiPageBrowser.processDecode(context);

    UIFormInputSet uiInputSet = getChild(UIFormInputSet.class);
    
    List<UIComponent> children = uiInputSet.getChildren(); 
    for(UIComponent ele : children) {
      ele.processDecode(context);
    }
//    UIFormStringInput uiPageId = getChildById("pageId");
//    uiPageId.processDecode(context);
//
//    UIFormStringInput uiPageName = getChildById("pageName");
//    uiPageName.processDecode(context);
//
//    UIFormStringInput uiPageTitle = getChildById("pageTitle");
//    uiPageTitle.processDecode(context);
  }

  static public class SelectPageActionListener extends EventListener<UIPageBrowser> {
    public void execute(Event<UIPageBrowser> event) throws Exception {
      UIPageBrowser uiPageBrowser = event.getSource();
      String id = event.getRequestContext().getRequestParameter(OBJECTID);
      WebuiRequestContext ctx = event.getRequestContext();
      UIApplication uiApp = ctx.getUIApplication();
      UIPageSelector2 uiPageSelector = uiPageBrowser.getAncestorOfType(UIPageSelector2.class);
      UserPortalConfigService service = uiPageBrowser.getApplicationComponent(UserPortalConfigService.class);
      UserACL userACL = uiPageBrowser.getApplicationComponent(UserACL.class);
      if (!userACL.hasPermission(service.getPage(id))) {
        uiApp.addMessage(new ApplicationMessage("UIPageBrowser.msg.NoPermission",
                                                new String[] { id }));
        ;
      }
      uiPageSelector.setValue(id);
      uiPageBrowser.defaultValue(null);

      UIForm uiForm = uiPageSelector.getAncestorOfType(UIForm.class);
      if (uiForm != null) {
        ctx.addUIComponentToUpdateByAjax(uiForm.getParent());
      } else {
        ctx.addUIComponentToUpdateByAjax(uiPageSelector.getParent());
      }
      UIFormPopupWindow uiPopup = uiPageSelector.getChild(UIFormPopupWindow.class);
      uiPopup.setShow(false);
    }
  }

}

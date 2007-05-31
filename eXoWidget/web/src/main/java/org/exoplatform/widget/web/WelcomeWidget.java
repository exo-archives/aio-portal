/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.widget.web;

import java.io.Writer;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.component.view.Util;
import org.exoplatform.web.application.mvc.MVCRequestContext;
import org.exoplatform.web.application.widget.WidgetApplication;

/**
 * Created by The eXo Platform SARL
 * Author : Pham Dung Ha
 *          ha.pham@exoplatform.com
 * May 25, 2007
 */
public class WelcomeWidget extends WidgetApplication {
  
//  public WelcomeWidget() {
//    System.out.println("\n\n\n\n\n\n\n\n\n\n\n INIT WelcomeWidget \n\n\n\n\n\n\n\n\n\n");
//  }
  
  public String getApplicationId() { return "exo.widget.web/WelcomeWidget" ; }

  public String getApplicationName() { return "WelcomeWidget"; }

  public String getApplicationGroup() { return "exo.widget.web"; }
  
  public void processRender(String instanceId, Writer w) throws Exception {
    PortalRequestContext pContext = Util.getPortalRequestContext();
    MVCRequestContext appReqContext = new MVCRequestContext(this, pContext) ;
    
    String applicationId = getApplicationId() ;
//    System.out.println("\n\n\n\n\n\n\n\n\n\n applicationId:     "+applicationId+" \n\n\n\n\n\n\n\n\n\n");
    w.write("<div id = 'UIWelcomeWidget' applicationId = '"+instanceId+"'><span></span></div>") ;
    
    String script = 
      "eXo.portal.UIPortal.createJSApplication('eXo.widget.web.welcome.UIWelcomeWidget','UIWelcomeWidget','"+instanceId+"','/exo.widget.web/javascript/');";
    appReqContext.getJavascriptManager().addCustomizedOnLoadScript(script) ;
  }
}

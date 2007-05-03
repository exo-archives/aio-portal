/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.application;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.portletcontainer.helper.WindowInfosContainer;
import org.exoplatform.web.WebAppController;

/**
 * Created by The eXo Platform SARL        .
 * Date: Jan 25, 2003
 * Time: 5:25:52 PM
 */
public class PortalSessionListener implements HttpSessionListener {
  
  public PortalSessionListener() {
  
  }
  
  public void sessionCreated(HttpSessionEvent event) {
  }

  public void sessionDestroyed(HttpSessionEvent event) {
    System.out.println("A  SESSION IS  DESTROYED");
    try {
      String portalContainerName = event.getSession().getServletContext().getServletContextName() ;
      RootContainer rootContainer = RootContainer.getInstance() ;
      PortalContainer portalContainer = rootContainer.getPortalContainer(portalContainerName) ;
      PortalContainer.setInstance(portalContainer); 
      WebAppController controller = 
        (WebAppController)portalContainer.getComponentInstanceOfType(WebAppController.class) ;
      PortalApplication portalApp =  controller.getApplication(PortalApplication.PORTAL_APPLICATION_ID) ;
      portalApp.getStateManager().expire(event.getSession().getId(), portalApp) ;
      
      WindowInfosContainer.removeInstance(portalContainer, event.getSession().getId());
    } catch(Exception ex) {
      ex.printStackTrace() ;
    } finally {
      PortalContainer.setInstance(null) ;
    }
  }
  
}
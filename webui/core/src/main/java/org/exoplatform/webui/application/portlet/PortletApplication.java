/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SAS         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.application.portlet;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.resolver.ApplicationResourceResolver;
import org.exoplatform.resolver.PortletResourceResolver;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.web.application.ApplicationLifecycle;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIPortletApplication;
/**
 * May 26, 2006
 * 
 * A portlet application
 */
public class PortletApplication extends WebuiApplication {
  
  protected static Log log = ExoLogger.getLogger("portlet:PortletApplication"); 
  
  /**
   * The configuration parameter of this portlet
   */
  private PortletConfig portletConfig_ ;
  
  /**
   * The id of this portlet
   */
  private String applicationId_ ;

  
  /**
   *  This constructor has 2 purposes:
   *     1) recreate the application id
   *     2) configure the resource resolver to look in different UIR scheme (here app: and par:)
   */
  public PortletApplication(PortletConfig config) throws Exception {
    portletConfig_ = config ;
    PortletContext pcontext = config.getPortletContext();
    String contextName = pcontext.getPortletContextName();
    applicationId_ = contextName + "/" + config.getPortletName() ;
    
    ApplicationResourceResolver resolver = new ApplicationResourceResolver() ;
    resolver.addResourceResolver(new PortletResourceResolver(pcontext, "app:")) ;
    resolver.addResourceResolver(new PortletResourceResolver(pcontext, "par:")) ;
    setResourceResolver(resolver) ;
  }
  
  public String getApplicationId() { return applicationId_ ; }
  public String getApplicationName() {  return portletConfig_.getPortletName() ; }
  public String getApplicationGroup() {
    return portletConfig_.getPortletContext().getPortletContextName();
  }

  @Override
  public String getApplicationType() { return JSR168_APPLICATION_TYPE; }


  public ResourceBundle getResourceBundle(Locale locale) throws Exception {    
    return portletConfig_.getResourceBundle(locale) ;
  }
    
  @SuppressWarnings("unused")
  public ResourceBundle getOwnerResourceBundle(String username, Locale locale) throws Exception {
    return null;
  }

  public String getApplicationInitParam(String name) { return  portletConfig_.getInitParameter(name); }
  
  public ExoContainer getApplicationServiceContainer() { return PortalContainer.getInstance() ; }
  
  
  public void processAction(ActionRequest req, ActionResponse res) throws Exception {
    WebuiRequestContext parentAppRequestContext =  WebuiRequestContext.getCurrentInstance() ;
    PortletRequestContext context = createRequestContext(req, res, parentAppRequestContext)  ;
    WebuiRequestContext.setCurrentInstance(context) ;
    try {      
      for(ApplicationLifecycle lifecycle : getApplicationLifecycle())  {
        lifecycle.onStartRequest(this, context) ;
      } 
      UIApplication uiApp = getStateManager().restoreUIRootComponent(context) ;
      context.setUIApplication(uiApp) ;
      processDecode(uiApp, context) ;
//      req.setCharacterEncoding("UTF-8");
      if(!context.isResponseComplete() && !context.getProcessRender()) {
        processAction(uiApp, context) ;
      }
    } finally {
      context.setProcessAction(true) ;
      WebuiRequestContext.setCurrentInstance(parentAppRequestContext) ;
    }
  }
  
  public  void render(RenderRequest req,  RenderResponse res) throws Exception {    
    WebuiRequestContext parentAppRequestContext =  WebuiRequestContext.getCurrentInstance() ;
    PortletRequestContext context = createRequestContext(req, res, parentAppRequestContext)  ;
    WebuiRequestContext.setCurrentInstance(context) ;
    try {
      if(!context.hasProcessAction()) {
        for(ApplicationLifecycle lifecycle : getApplicationLifecycle())  {
          lifecycle.onStartRequest(this, context) ;
        }
      }      
      UIApplication uiApp =  getStateManager().restoreUIRootComponent(context) ;
      context.setUIApplication(uiApp) ;
      if(!context.isResponseComplete()) {
        UIPortletApplication uiPortletApp = (UIPortletApplication)uiApp;
        uiPortletApp.processRender(this, context) ;
      }
      uiApp.setLastAccessApplication(System.currentTimeMillis()) ;
    } finally {
      try {
        for(ApplicationLifecycle lifecycle :  getApplicationLifecycle()) {
          lifecycle.onEndRequest(this, context) ;
        }
      } catch (Exception exception){
    	log.error("Error while trying to call onEndRequest of the portlet ApplicationLifecycle", 
    		exception);
      }
      WebuiRequestContext.setCurrentInstance(parentAppRequestContext) ;
    }
  }
  
  private PortletRequestContext createRequestContext(PortletRequest req, PortletResponse res,
                                                    WebuiRequestContext parentAppRequestContext) throws IOException {
    String attributeName = getApplicationId() + "$PortletRequest" ;
    PortletRequestContext context = 
      (PortletRequestContext) parentAppRequestContext.getAttribute(attributeName) ;
    Writer w  = null ;       
    if(res instanceof  RenderResponse){
      RenderResponse renderRes = (RenderResponse)res;
      renderRes.setContentType("text/html; charset=UTF-8");      
      w = renderRes.getWriter() ; 
    }
    if(context != null) {
      context.init(w, req, res) ;
    } else {
      context =  new PortletRequestContext(this, w, req, res) ;
      parentAppRequestContext.setAttribute(attributeName, context) ;
    }
    context.setParentAppRequestContext(parentAppRequestContext) ;
    return context;
  }  
}
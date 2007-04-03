/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.templates.groovy.ApplicationResourceResolver;
import org.exoplatform.templates.groovy.ResourceResolver;
import org.exoplatform.web.application.Application;
import org.exoplatform.web.application.Parameter;
import org.exoplatform.web.application.RequestContext;
import org.exoplatform.webui.component.UIApplication;
import org.exoplatform.webui.component.UIComponent;
import org.exoplatform.webui.config.Event;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * May 7, 2006
 */
abstract public class WebuiRequestContext extends RequestContext {
  
  protected UIApplication  uiApplication_ ;
  protected String sessionId_ ;
  protected ResourceBundle appRes_ ;
  private StateManager stateManager_ ;
  private boolean  responseComplete_ = false ;
  private boolean  processRender_ =  false ;
  private Throwable executionError_ ;
  private ArrayList<UIComponent>  uicomponentToUpdateByAjax ;
  
  public WebuiRequestContext(Application app) {
    super(app) ;
  }
  
  public String getSessionId() {  return sessionId_  ; }  
  protected void setSessionId(String id) { sessionId_ = id ;}
  
  @SuppressWarnings("unchecked")
  public UIApplication getUIApplication() { return uiApplication_ ; }  
  
  public void  setUIApplication(UIApplication uiApplication) throws Exception { 
    uiApplication_ = uiApplication ;
    appRes_ = getApplication().getResourceBundle(uiApplication.getLocale()) ;   
  }
  
  public Locale getLocale() {  return uiApplication_.getLocale() ;} 
  
  public ResourceBundle getApplicationResourceBundle() {  return appRes_ ; }
  
  public  String getActionParameterName() {  return WebuiRequestContext.ACTION ; }
  
  public  String getUIComponentIdParameterName() {  return UIComponent.UICOMPONENT; }
  
  abstract public String getRequestContextPath() ;
  
  abstract  public <T> T getRequest() throws Exception ;
  
  abstract  public <T> T getResponse() throws Exception ;
  
  public Throwable  getExecutionError()  { return executionError_ ; }
  
  public List<UIComponent>  getUIComponentToUpdateByAjax() {  return uicomponentToUpdateByAjax ; }
  
  public boolean isResponseComplete() { return responseComplete_ ;}
  
  public void    setResponseComplete(boolean b) { responseComplete_ = b ; }
  
  public boolean getProcessRender() { return processRender_ ;}
  
  public void    setProcessRender(boolean b) { processRender_ = b; }
  
  public void addUIComponentToUpdateByAjax(UIComponent uicomponent) {   
    if(uicomponentToUpdateByAjax == null)  {
      uicomponentToUpdateByAjax =  new ArrayList<UIComponent>() ;
    }
    uicomponentToUpdateByAjax.add(uicomponent) ;
  }
 
  public ResourceResolver getResourceResolver(String uri) {
    Application app = getApplication() ;
    while(app != null) {
      ApplicationResourceResolver appResolver = app.getResourceResolver() ;
      ResourceResolver resolver =  appResolver.getResourceResolver(uri) ;
      if(resolver  != null)  return resolver ;  
      RequestContext pcontext = getParentAppRequestContext() ;
      if(pcontext != null) app = pcontext.getApplication() ;
      else app =null ;
    }
    return null ;
  }
  
  public StateManager  getStateManager() { return stateManager_; }
  public void  setStateManager(StateManager manager) { stateManager_ =  manager ; }
}
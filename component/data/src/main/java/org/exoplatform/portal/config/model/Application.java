/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.config.model;

/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Portlet.java,v 1.7 2004/09/30 01:00:05 tuan08 Exp $
 **/
public class Application extends Component {
  
  public final static String TYPE_PORTLET = "portlet";
  public final static String TYPE_WIDGET = "widget";
  public final static String EXO_APPLICATION_TYPE = "eXoApplication";
  
  private String title;
  private String applicationInstanceId ;
  private String icon ; 
  private String description;

  private boolean  showInfoBar = true ;
  private boolean  showApplicationState = true ;
  private boolean  showApplicationMode = true ;
  
  public Application(){
    factoryId = TYPE_PORTLET;
  }
  
  public String getTitle() { return title ; }
  public void   setTitle(String s) { title = s ;}
  
  public String getApplicationInstanceId() { return applicationInstanceId ; }
  public void   setApplicationInstanceId(String s) { applicationInstanceId = s ;}
  
  public String getDescription() {  return  description ; }
  public void   setDescription(String s) { description = s ;}
  
  public boolean getShowInfoBar() { return showInfoBar ; }
  public void    setShowInfoBar(Boolean b) { showInfoBar = b ; }
  
  public boolean getShowApplicationState() { return showApplicationState ; }
  public void    setShowApplicationState(Boolean b) { showApplicationState = b ; }
  
  public boolean getShowApplicationMode() { return showApplicationMode ; }
  public void    setShowApplicationMode(Boolean b) { showApplicationMode = b ; }
  
  public String getIcon() { return icon ; }
  public void setIcon(String s) { icon = s ; }
}
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
package org.exoplatform.portal.webui.application;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.config.model.Properties;
import org.exoplatform.web.application.gadget.GadgetApplication;
import org.exoplatform.web.application.gadget.GadgetRegistryService;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
/**
 * Created by The eXo Platform SAS
 * Author : dang.tung
 *          tungcnw@gmail.com
 * May 06, 2008   
 */
@ComponentConfig(
    template = "system:/groovy/portal/webui/application/UIGadget.gtmpl"
)
/**
 * This class represents user interface gadgets, it using UIGadget.gtmpl for rendering
 * UI in eXo. It mapped to Application model in page or container.
 */
public class UIGadget extends UIComponent {
  
  private String applicationInstanceId_ ;
  private String applicationOwnerType_ ;
  private String applicationOwnerId_ ;
  private String applicationGroup_ ;
  private String applicationName_ ;
  private String applicationInstanceUniqueId_ ;
  private String applicationId_ ;
  private String userPref_ ;
  private Properties properties;
  
  /**
   * Initializes a newly created <code>UIGadget</code> object
   * @throws Exception if can't initialize object
   */
  public UIGadget() throws Exception {
  }
  
  /**
   * Gets instance id of gadget application
   * @return the string represents instance id of gadget application
   */
  public String getApplicationInstanceId() { return applicationInstanceId_ ; }
  
  /**
   * Sets instance id of gadget application
   * @param s an string that is the instance id of gadget application 
   */
  public void setApplicationInstanceId(String s) {  
    applicationInstanceId_ = s ;
    String[]  tmp =  applicationInstanceId_.split("/") ;
    applicationGroup_ = tmp[1] ;
    applicationName_ = tmp[2] ;
    applicationId_ =  applicationGroup_ + "/" + applicationName_ ;
    applicationInstanceUniqueId_ = tmp[3] ;
  }
  
  /**
   * Gets owner type of gadget application
   * @return the string represents owner type of gadget application
   */
  public String getApplicationOwnerType() { return applicationOwnerType_ ;}
  
  /**
   * Sets owner type of gadget application
   * @param ownerType an string that is the owner type of gadget application
   */
  public void setApplicationOwnerType(String ownerType){ applicationOwnerType_ = ownerType;}
  
  /**
   * Gets owner id of gadget application
   * @return the string represents owner id of gadget application
   */
  public String getApplicationOwnerId() { return applicationOwnerId_ ;}
  
  /**
   * Sets owner id of gadget application
   * @param ownerId an string that is the owner id of gadget application
   */
  public void setApplicationOwnerId(String ownerId){ applicationOwnerId_ = ownerId;} 
  
  /**
   * Gets group of gadget application such as eXoGadgets...
   * @return the string represents group of gadget application
   */
  public String getApplicationGroup() { return applicationGroup_ ;}
  
  /**
   * Sets group of gadget application
   * @param group an string that is the group of gadget application
   */
  public void setApplicationGroup(String group){ applicationGroup_ = group;}
  
  /**
   * Gets name of gadget application
   * @return the string represents name of gadget application
   */
  public String getApplicationName() { return applicationName_ ;}
  
  /**
   * Sets name of gadget application
   * @param name an string that is the name of gadget application
   */
  public void setApplicationName(String name) { applicationName_ = name;}
  
  /**
   * Gets Id of gadget application
   * @return gadget application's id
   */
  public String getApplicationId() { return applicationId_ ; }
 
  /**
   * Gets Unique id of instance gadget application
   * @return Id of instance gadget application
   */
  public String getApplicationInstanceUniqueId() { return applicationInstanceUniqueId_ ;}
  
  /**
   * Gets Properties of gadget application such as locationX, locationY in desktop page
   * @return all properties of gadget application
   * @see org.exoplatform.portal.config.model.Application
   * @see org.exoplatform.portal.config.model.Properties
   */
  public Properties getProperties() {
    if(properties == null) properties  = new Properties();
    return properties; 
  }
  
  /**
   * Sets Properties of gadget application such as locationX, locationY in desktop page
   * @param properties Properties that is the properties of gadget application
   * @see org.exoplatform.portal.config.model.Properties
   * @see org.exoplatform.portal.config.model.Application
   */
  public void setProperties(Properties properties) { this.properties = properties; }

  /**
   * Gets GadgetApplication by GadgedRegistryService
   * @return Gadget Application 
   */
  private GadgetApplication getApplication() {
    PortalContainer container = PortalContainer.getInstance() ;
    GadgetRegistryService gadgetService = (GadgetRegistryService) container.getComponentInstanceOfType(GadgetRegistryService.class) ;
    GadgetApplication application = null ;
    try {
      application = gadgetService.getGadget(applicationId_.split("/")[1]) ;
    } catch (Exception e) {}
    return application;
  }

  /**
   * Gets Url of gadget application, it saved before by GadgetRegistryService
   * @return url of gadget application, such as "http://www.google.com/ig/modules/horoscope.xml"
   */
  public String getUrl() {
    GadgetApplication application = getApplication();
    return application.getUrl() ;
  }

  /**
   * Gets Metadata of gadget application, it'll use in client to rendering gadget application
   * @return the string represents Metadata of gadget application
   */
  public String getMetadata() {
    GadgetApplication application = getApplication();
    return application.getMetadata();
  }
  
  /**
   * Gets user preference of gadget application
   * @return the string represents user preference of gadget application
   */
  public String getUserPref() { return userPref_ ;}
  
  /**
   * Sets user preference of gadget application
   * @param userPref an string that is the user preference of gadget application
   */
  public void setUserPref(String userPref) {userPref_ = userPref ;}
}
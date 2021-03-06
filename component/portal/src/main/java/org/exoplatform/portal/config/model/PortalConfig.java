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
package org.exoplatform.portal.config.model;

import java.util.ArrayList;

/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalConfig.java,v 1.7 2004/08/06 03:02:29 tuan08 Exp $
 **/
public class PortalConfig {
  
  final public static String USER_TYPE = "user";
  final public static String GROUP_TYPE = "group";
  final public static String PORTAL_TYPE = "portal";
  
	private String    name ;

  /** Added for new POM . */
  private String    type;

	private String    locale ;
  
  private String[]  accessPermissions ;
  private String editPermission;
  
  private Properties properties ;
  
  private String    skin;
  private String    title;
  
  private Container portalLayout;
  
  private String    creator ;
  private String    modifier ;
  
  private transient boolean modifiable ;
  
  public PortalConfig() {
    portalLayout = new Container();
    type = PORTAL_TYPE;
  }
  
  public PortalConfig(String type) {
    portalLayout = new Container();
    this.type = type;
  }

  public String getType() { return type; }
  public void   setType(String type) { this.type = type; }

  public String getName() { return name ; }
  public void   setName(String s) { name = s  ; } 
 
  public String getLocale() { return locale ; }
  public void   setLocale(String s) { locale = s ; }
  
  public String [] getAccessPermissions() { return accessPermissions ; }
  public void   setAccessPermissions(String[] s) { accessPermissions = s ; }
  
  public String getEditPermission() { return editPermission; }
  public void setEditPermission(String editPermission) { this.editPermission = editPermission; }
  
  public String getSkin() { 
    if(skin == null || skin.length() < 1) return "Default";
    return skin; 
  }
  public void setSkin(String s ) { skin = s; }
  
  public Container   getPortalLayout() { return portalLayout; }
  public void setPortalLayout(Container container) { portalLayout = container; }
  
  public boolean isModifiable() { return modifiable ; }
  public void  setModifiable(boolean b) { modifiable = b ; }
  
//  public String getFactoryId() { 
//    return factoryId; 
//  }
//  public void setFactoryId(String factoryId) { this.factoryId = factoryId; }
  
  public String getCreator()  {  return creator ; }
  public void   setCreator(String s) { creator = s ; }
  
  public String getModifier() { return modifier ; }
  public void   setModifier(String s) { modifier = s ; }
  
  public String getTitle() { return title ; }
  public void   setTitle(String value) { title = value ; }
  
  public Properties getProperties() { return properties ; }
  public void setProperties(Properties props) { properties = props; }
  
  public String getProperty(String name) {
    if(name == null) throw new NullPointerException() ;
    if(properties == null || !properties.containsKey(name)) return null;
    return properties.get(name) ;
  }
  
  public String getProperty(String name, String defaultValue) {
    String value = getProperty(name) ;
    if(value != null) return value ;
    return defaultValue ;
  }
  
  public void setProperty(String name, String value) {
    if(name == null || properties == null) throw new NullPointerException() ;
    if(value == null) properties.remove(name) ;
    else properties.setProperty(name, value) ;
  }
  
  public void removeProperty(String name) {
    if(name == null || properties == null) throw new NullPointerException() ;
    properties.remove(name) ;
  }
  
  public String getSessionAlive() { 
    return getProperty(PortalProperties.SESSION_ALIVE, PortalProperties.SESSION_ON_DEMAND) ;
  }
  public void setSessionAlive(String type) {
    setProperty(PortalProperties.SESSION_ALIVE, type) ;
  }
  static public class PortalConfigSet {
    private ArrayList<PortalConfig> portalConfigs ;
    
    public ArrayList<PortalConfig> getPortalConfigs() { return portalConfigs ; }
    public void setPortalConfigs(ArrayList<PortalConfig> list) { portalConfigs = list ; }
  }

  @Override
  public String toString() {
    return "PortalConfig[name=" + name + ",type=" + type + "]";
  }
}

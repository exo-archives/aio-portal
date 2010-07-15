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
package org.exoplatform.webui.core;

import java.util.List;

import org.exoplatform.webui.config.Event;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minhdv@exoplatform.com
 * Aug 31, 2006  
 * 
 * A component that represents a toolbar
 */

public abstract class UIToolbar extends UIComponent {
  /**
   * The css style
   */
  private String toolbarStyle_ = "LightToolbar" ;
  /**
   * A javascript expression
   */
  private String strJavascript_ = "" ;
  /**
   * A javascript event name
   */
  private String eventName_ = "" ;
  
  public UIToolbar() throws Exception {
  }
  
  public String getToolbarStyle() { return toolbarStyle_ ; }
  public void setToolbarStyle(String toolbarStyle) { toolbarStyle_ = toolbarStyle ;  }
  
  public List<Event> getEvents() { return getComponentConfig().getEvents() ; }
  
  public String getJavascript() { return strJavascript_ ; }
  public String getEventName() { return eventName_ ; }
  public void setJavascript(String eventName, String strJavascript) { 
    strJavascript_ = strJavascript ; 
    eventName_ = eventName ;
  }

}

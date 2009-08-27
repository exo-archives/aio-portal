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
package org.exoplatform.services.config;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 5, 2004
 * @version $Id: ConfigurationDataImpl.java 5799 2006-05-28 17:55:42Z geaz $
 * @hibernate.class table="EXO_SERVICE_CONFIG"
 * @hibernate.cache usage="read-write"
 */
public class ConfigurationData {

  private String serviceType;

  private String data;

  /**
   * @hibernate.id generator-class="assigned" unsaved-value="null"
   ***/
  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String s) {
    serviceType = s;
  }

  /**
   * @hibernate.property length="65535"
   *                     type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getData() {
    return data;
  }

  public String setData(String s) {
    return data = s;
  }

}

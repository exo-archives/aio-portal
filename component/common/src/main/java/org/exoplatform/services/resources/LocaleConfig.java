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
package org.exoplatform.services.resources;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * May 3, 2004
 * 
 * @author: Tuan Nguyen
 * @email: tuan08@users.sourceforge.net
 * @version: $Id: LocaleConfig.java 5799 2006-05-28 17:55:42Z geaz $
 **/
public interface LocaleConfig {

  public String getDescription();

  public void setDescription(String desc);

  public String getOutputEncoding();

  public void setOutputEncoding(String enc);

  public String getInputEncoding();

  public void setInputEncoding(String enc);

  public Locale getLocale();

  public void setLocale(Locale locale);

  public void setLocale(String localeName);

  public String getLanguage();

  public String getLocaleName();

  public ResourceBundle getResourceBundle(String name);

  public ResourceBundle getMergeResourceBundle(String[] names);

  public ResourceBundle getNavigationResourceBundle(String ownerType, String ownerId);

  public void setInput(HttpServletRequest req) throws java.io.UnsupportedEncodingException;

  public void setOutput(HttpServletResponse res);

  /**
   * Returns the orientation of the locale config.
   *
   * @return the orientation
   */
  public Orientation getOrientation();

  /**
   * Updates the orientation of the locale config.
   *
   * @param orientation the new orientation
   */
  public void setOrientation(Orientation orientation);

}

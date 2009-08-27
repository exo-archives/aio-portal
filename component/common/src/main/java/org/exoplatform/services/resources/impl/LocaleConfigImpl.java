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
package org.exoplatform.services.resources.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.ResourceBundleService;
import org.exoplatform.services.resources.Orientation;

/**
 * @author Benjamin Mestrallet benjamin.mestrallet@exoplatform.com
 */
public class LocaleConfigImpl implements LocaleConfig {

  static private Map<String, Locale> predefinedLocaleMap_ = null;

  static {
    predefinedLocaleMap_ = new HashMap<String, Locale>(10);
    predefinedLocaleMap_.put("us", Locale.US);
    predefinedLocaleMap_.put("en", Locale.ENGLISH);
    predefinedLocaleMap_.put("fr", Locale.FRANCE);
    predefinedLocaleMap_.put("zh", Locale.SIMPLIFIED_CHINESE);
  }

  private Locale                     locale_;

  private String                     outputEncoding_;

  private String                     inputEncoding_;

  private String                     description_;

  private String                     localeName_;

  private Orientation                orientation;

  public LocaleConfigImpl() {
  }

  public final String getDescription() {
    return description_;
  }

  public final void setDescription(String desc) {
    description_ = desc;
  }

  public final String getOutputEncoding() {
    return outputEncoding_;
  }

  public final void setOutputEncoding(String enc) {
    outputEncoding_ = enc;
  }

  public final String getInputEncoding() {
    return inputEncoding_;
  }

  public final void setInputEncoding(String enc) {
    inputEncoding_ = enc;
  }

  public final Locale getLocale() {
    return locale_;
  }

  public final void setLocale(Locale locale) {
    locale_ = locale;
    if (localeName_ == null)
      localeName_ = locale.getLanguage();
  }

  public final void setLocale(String localeName) {
    localeName_ = localeName;
    locale_ = predefinedLocaleMap_.get(localeName);
    if (locale_ == null)
      locale_ = new Locale(localeName);
  }

  public final String getLanguage() {
    return locale_.getLanguage();
  }

  public final String getLocaleName() {
    return localeName_;
  }

  public final void setLocaleName(String localeName) {
    localeName_ = localeName;
  }

  public ResourceBundle getResourceBundle(String name) {
    ResourceBundleService service = (ResourceBundleService) ExoContainerContext.getCurrentContainer()
                                                                               .getComponentInstanceOfType(ResourceBundleService.class);
    ResourceBundle res = service.getResourceBundle(name, locale_);
    return res;
  }

  public ResourceBundle getMergeResourceBundle(String[] names) {
    ResourceBundleService service = (ResourceBundleService) ExoContainerContext.getCurrentContainer()
                                                                               .getComponentInstanceOfType(ResourceBundleService.class);
    ResourceBundle res = service.getResourceBundle(names, locale_);
    return res;
  }

  public ResourceBundle getNavigationResourceBundle(String ownerType, String ownerId) {
    return getResourceBundle("locale.navigation." + ownerType + "." + ownerId.replaceAll("/", "."));
  }

  public void setInput(HttpServletRequest req) throws java.io.UnsupportedEncodingException {
    req.setCharacterEncoding(inputEncoding_);
  }

  public void setOutput(HttpServletResponse res) {
    res.setContentType("text/html; charset=" + outputEncoding_);
    res.setLocale(locale_);
  }

  public Orientation getOrientation() {
    return orientation;
  }

  public void setOrientation(Orientation orientation) {
    this.orientation = orientation;
  }

  @Override
  public String toString() {
    return "LocaleConfig[" + "localeName=" + localeName_ + ",locale=" + locale_ + ",description="
        + description_ + ",inputEncoding=" + inputEncoding_ + ",outputEncoding=" + outputEncoding_
        + "]";
  }
}

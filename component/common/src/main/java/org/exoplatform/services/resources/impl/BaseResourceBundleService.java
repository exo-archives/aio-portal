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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.exoplatform.services.log.Log;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.resources.IdentityResourceBundle;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.services.resources.PropertiesClassLoader;
import org.exoplatform.services.resources.Query;
import org.exoplatform.services.resources.ResourceBundleData;
import org.exoplatform.services.resources.ResourceBundleLoader;
import org.exoplatform.services.resources.ResourceBundleService;

/**
 * Created by The eXo Platform SAS Mar 9, 2007
 */
abstract public class BaseResourceBundleService implements ResourceBundleService {

  protected Log                 log_;

  protected List<String>        classpathResources_;

  protected String[]            portalResourceBundleNames_;

  protected LocaleConfigService localeService_;

  protected ExoCache            cache_;

  @SuppressWarnings("unchecked")
  protected void initParams(InitParams params) throws Exception {
    classpathResources_ = params.getValuesParam("classpath.resources").getValues();

    // resources name can use for portlets
    List prnames = params.getValuesParam("portal.resource.names").getValues();
    portalResourceBundleNames_ = new String[prnames.size()];
    for (int i = 0; i < prnames.size(); i++) {
      portalResourceBundleNames_[i] = (String) prnames.get(i);
    }

    PageList pl = findResourceDescriptions(new Query(null, null));
    if (pl.getAvailable() > 0)
      return;

    // init resources
    List<String> initResources = params.getValuesParam("init.resources").getValues();
    for (String resource : initResources) {
      initResources(resource, Thread.currentThread().getContextClassLoader());
    }
  }

  public ResourceBundle getResourceBundle(String[] name, Locale locale) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return getResourceBundle(name, locale, cl);
  }

  public ResourceBundle getResourceBundle(String name, Locale locale) {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    return getResourceBundle(name, locale, cl);
  }

  public String[] getSharedResourceBundleNames() {
    return portalResourceBundleNames_;
  }

  public ResourceBundleData createResourceBundleDataInstance() {
    return new ResourceBundleData();
  }

  protected boolean isClasspathResource(String name) {
    if (classpathResources_ == null)
      return false;
    for (int i = 0; i < classpathResources_.size(); i++) {
      String pack = classpathResources_.get(i);
      if (name.startsWith(pack))
        return true;
    }
    return false;
  }

  protected void initResources(String baseName, ClassLoader cl) {
    String name = baseName.replace('.', '/');
    try {
      Collection<LocaleConfig> localeConfigs = localeService_.getLocalConfigs();
      String defaultLang = localeService_.getDefaultLocaleConfig().getLanguage();
      for (Iterator<LocaleConfig> iter = localeConfigs.iterator(); iter.hasNext();) {
        LocaleConfig localeConfig = iter.next();
        String language = localeConfig.getLanguage();
        String content = getResourceBundleContent(name, language, defaultLang, cl);
        if (content != null) {
          // save the content
          ResourceBundleData data = new ResourceBundleData();
          data.setId(baseName + "_" + language);
          data.setName(baseName);
          data.setLanguage(language);
          data.setData(content);
          saveResourceBundle(data);
        }
      }
    } catch (Exception ex) {
    	log_.error("Error while reading the resource bundle : " + baseName, ex);
    }
  }

  protected String getResourceBundleContent(String name, String language,
			String defaultLang, ClassLoader cl) throws Exception {
		String fileName = null;
		try {
		    cl = new PropertiesClassLoader(cl, true);
			fileName = name + "_" + language + ".properties";
			URL url = cl.getResource(fileName);
			if (url == null && defaultLang.equals(language)) {
				url = cl.getResource(name + ".properties");
			}
			if (url != null) {
				InputStream is = url.openStream();
				try {
					byte[] buf = IOUtil.getStreamContentAsBytes(is);
					return new String(buf, "UTF-8");
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						// Do nothing
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Error while reading the file: " + fileName, e);
		}
		return null;
	}

  
  public ResourceBundle getResourceBundle(String name, Locale locale, ClassLoader cl) {
    if (IdentityResourceBundle.MAGIC_LANGUAGE.equals(locale.getLanguage())) {
      return IdentityResourceBundle.getInstance();
    }
    if (isClasspathResource(name))
      return ResourceBundleLoader.load(name, locale, cl);
    String id = name + "_" + locale.getLanguage();
    try {
      Object obj = cache_.get(id);
      if (obj != null)
        return (ResourceBundle) obj;
    } catch (Exception ex) {
    }

    try {
      ResourceBundle res = null;
      String rootId = name + "_" + localeService_.getDefaultLocaleConfig().getLanguage();
      ResourceBundle parent = getResourceBundleFromDb(rootId, null, locale);
      if (parent != null) {
        res = getResourceBundleFromDb(id, parent, locale);
        if (res == null)
          res = parent;
        cache_.put(id, res);
        return res;
      }
    } catch (Exception ex) {
      log_.error("Error: " + id, ex);
    }
    return null;
  }

  public ResourceBundle getResourceBundle(String[] name, Locale locale, ClassLoader cl) {
    if (IdentityResourceBundle.MAGIC_LANGUAGE.equals(locale.getLanguage())) {
      return IdentityResourceBundle.getInstance();
    }
    StringBuilder idBuf = new StringBuilder("merge:");
    for (String n : name)
      idBuf.append(n).append("_");
    idBuf.append(locale);
    String id = idBuf.toString();
    try {
      ResourceBundle res = (ResourceBundle) cache_.get(id);
      if (res != null)
        return res;
      MapResourceBundle outputBundled = new MapResourceBundle(locale);
      for (int i = 0; i < name.length; i++) {
        ResourceBundle temp = getResourceBundle(name[i], locale, cl);
        if (temp != null) {
          outputBundled.merge(temp);
          continue;
        }
        log_.warn("Cannot load and merge the bundle: " + name[i]);
      }
      outputBundled.resolveDependencies();
      cache_.put(id, outputBundled);
      return outputBundled;
    } catch (Exception ex) {
      log_.error("Cannot load and merge the bundle: " + id, ex);
    }
    return null;
  }

  abstract protected ResourceBundle getResourceBundleFromDb(String id,
                                                            ResourceBundle parent,
                                                            Locale locale) throws Exception;

}

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

import org.exoplatform.services.database.HibernateService;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 5, 2004
 * @version $Id: ConfigurationServiceImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class ConfigurationServiceImpl implements ConfigurationService {

  private HibernateService hservice_;

  private XStream          xstream_;

  public ConfigurationServiceImpl(HibernateService service) {
    hservice_ = service;
    xstream_ = new XStream(new XppDriver());
  }

  public Object getServiceConfiguration(Class serviceType) throws Exception {
    ConfigurationData impl = (ConfigurationData) hservice_.findOne(ConfigurationData.class,
                                                                   serviceType.getName());
    Object obj = null;
    if (impl == null) {
      obj = loadDefaultConfig(serviceType);
      saveServiceConfiguration(serviceType, obj);
    } else {
      obj = xstream_.fromXML(impl.getData());
    }
    return obj;
  }

  public void saveServiceConfiguration(Class serviceType, Object config) throws Exception {
    ConfigurationData configData = (ConfigurationData) hservice_.findOne(ConfigurationData.class,
                                                                         serviceType.getName());
    String xml = xstream_.toXML(config);
    if (configData == null) {
      configData = new ConfigurationData();
      configData.setServiceType(serviceType.getName());
      configData.setData(xml);
      hservice_.create(configData);
    } else {
      configData.setData(xml);
      hservice_.update(configData);
    }
  }

  public void removeServiceConfiguration(Class serviceType) throws Exception {
    hservice_.remove(serviceType, serviceType.getName());
  }

  @SuppressWarnings("unused")
  private Object loadDefaultConfig(Class serviceType) throws Exception {
    // ServiceConfiguration sconf =
    // manager_.getServiceConfiguration(serviceType) ;
    // Iterator i = sconf.values().iterator() ;
    // ObjectParam param = (ObjectParam) i.next() ;
    // return param.getObject() ;
    return null;
  }

}

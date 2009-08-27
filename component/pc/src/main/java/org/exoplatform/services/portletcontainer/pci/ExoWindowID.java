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
package org.exoplatform.services.portletcontainer.pci;

import org.apache.commons.lang.StringUtils;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoWindowID implements WindowID {

  /**
   * Constant for default portal source.
   */
  public static final String DEFAULT_PORTAL_CONFIG = "default-portal-config";

  /**
   * Constant for mobile portal source.
   */
  public static final String MOBILE_PORTAL_CONFIG = "default-portal-config";

  /**
   * Owner.
   */
  private String owner;

  /**
   * Portlet application name.
   */
  private String portletApplicationName;

  /**
   * Portlet name.
   */
  private String portletName;

  /**
   * Unique id.
   */
  private String uniqueID;

  /**
   * Persistence id.
   */
  private String persistenceId;

  /**
   * Configuration source.
   */
  private String configurationSource = DEFAULT_PORTAL_CONFIG;

  /**
   * Default constructor.
   */
  public ExoWindowID() {
  }

  /**
   * @param persistenceId persistence id
   */
  public ExoWindowID(final String persistenceId) {
    this.persistenceId = persistenceId;
    int idx = persistenceId.indexOf(":/");
    this.owner = persistenceId.substring(0, idx);
    String persistenceId1 = persistenceId.substring(idx + 2, persistenceId.length());
    String[] keys = StringUtils.split(persistenceId1, "/");
    this.portletApplicationName = keys[0];
    this.portletName = keys[1];
    this.uniqueID = keys[2];
  }

  /**
   * Overridden method.
   *
   * @return owner
   * @see org.exoplatform.services.portletcontainer.pci.WindowID#getOwner()
   */
  public final String getOwner() {
    return owner;
  }

  /**
   * @param owner owner
   */
  public final void setOwner(final String owner) {
    this.owner = owner;
  }

  /**
   * Overridden method.
   *
   * @return app name
   * @see org.exoplatform.services.portletcontainer.pci.WindowID#getPortletApplicationName()
   */
  public final String getPortletApplicationName() {
    return portletApplicationName;
  }

  /**
   * @param portletApplicationName app name
   */
  public final void setPortletApplicationName(final String portletApplicationName) {
    this.portletApplicationName = portletApplicationName;
  }

  /**
   * Overridden method.
   *
   * @return portlet name
   * @see org.exoplatform.services.portletcontainer.pci.WindowID#getPortletName()
   */
  public final String getPortletName() {
    return portletName;
  }

  /**
   * @param portletName portlet name
   */
  public final void setPortletName(final String portletName) {
    this.portletName = portletName;
  }

  /**
   * Overridden method.
   *
   * @return unique id
   * @see org.exoplatform.services.portletcontainer.pci.WindowID#getUniqueID()
   */
  public final String getUniqueID() {
    return uniqueID;
  }

  /**
   * @param uniqueID unique id
   */
  public final void setUniqueID(final String uniqueID) {
    this.uniqueID = uniqueID;
  }

  /**
   * @return persistence id
   */
  public final String getPersistenceId() {
    return this.persistenceId;
  }

  /**
   * @param id persistence id
   */
  public final void setPersistenceId(final String id) {
    persistenceId = id;
  }

  /**
   * @return generated persistence id = owner + ":/" + portletApplicationName + "/" + portletName + "/" + uniqueID
   */
  public final String generatePersistenceId() {
    return owner + ":/" + portletApplicationName + "/" + portletName + "/" + uniqueID;
  }

  /**
   * Overridden method.
   *
   * @deprecated
   * @return generated key
   * @see org.exoplatform.services.portletcontainer.pci.WindowID#generateKey()
   */
  public final String generateKey() {
    return uniqueID;
  }

  /**
   * The configuration source can be from default portal config layout, mobile
   * portal config layout or page config.
   *
   * @return source
   */
  public final String getConfigurationSource() {
    return configurationSource;
  }

  /**
   * @param source source
   */
  public final void setConfigurationSource(final String source) {
    configurationSource = source;
  }
}

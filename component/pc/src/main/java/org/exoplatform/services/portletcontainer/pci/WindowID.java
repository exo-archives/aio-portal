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

import java.io.Serializable;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface WindowID extends Serializable {

  /**
   * @return owner
   */
  String getOwner();

  /**
   * @return portlet app name
   */
  String getPortletApplicationName();

  /**
   * @return portlet name
   */
  String getPortletName();

  /**
   * @return unique key id
   */
  String getUniqueID();

  /**
   * @deprecated
   * @return key
   */
  String generateKey();

  /**
   * @return persistence Id
   */
  String getPersistenceId();

}

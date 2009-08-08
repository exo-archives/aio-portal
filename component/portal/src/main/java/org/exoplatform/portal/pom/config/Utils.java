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
package org.exoplatform.portal.pom.config;

import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.config.model.PortalConfig;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Utils {

  public static String getOwnerType(ObjectType<? extends Site> siteType) {
    if (siteType == ObjectType.PORTAL) {
      return PortalConfig.PORTAL_TYPE;
    } else if (siteType == ObjectType.GROUP) {
      return PortalConfig.GROUP_TYPE;
    } else if (siteType == ObjectType.USER) {
      return PortalConfig.USER_TYPE;
    } else {
      throw new IllegalArgumentException("Invalid site type " + siteType);
    }
  }

  public static ObjectType<? extends Site> parseSiteType(String ownerType) {
    if (ownerType.equals(PortalConfig.PORTAL_TYPE)) {
      return ObjectType.PORTAL;
    } else if (ownerType.equals(PortalConfig.GROUP_TYPE)) {
      return ObjectType.GROUP;
    } else if (ownerType.equals(PortalConfig.USER_TYPE)) {
      return ObjectType.USER;
    } else {
      throw new IllegalArgumentException("Invalid owner type " + ownerType);
    }
  }

  public static String join(String separator, String... strings) {
    if (strings == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0;i < strings.length;i++) {
      Object o = strings[i];
      if (i > 0) {
        sb.append(separator);
      }
      sb.append(o);
    }
    return sb.toString();
  }

  public static String[] split(String separator, String s) {
    if (s == null) {
      return null;
    }
    return split(s, 0, 0, separator);
  }

  private static String[] split(String s, int fromIndex, int index, String separator) {
    int toIndex = s.indexOf(separator, fromIndex);
    String[] chunks;
    if (toIndex == -1) {
      chunks = new String[index + 1];
      toIndex = s.length();
    } else {
      chunks = split(s, toIndex + separator.length(), index + 1, separator);
    }
    chunks[index] = s.substring(fromIndex, toIndex);
    return chunks;
  }
}

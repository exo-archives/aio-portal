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

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Utils {

  public static String join(String... strings) {
    if (strings == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0;i < strings.length;i++) {
      Object o = strings[i];
      if (i > 0) {
        sb.append('|');
      }
      sb.append(o);
    }
    return sb.toString();
  }

  public static String[] split(String s) {
    if (s == null) {
      return null;
    }
    return s.split("|");
  }

  public static String[] splitId(String id) {
    int index1 = id.indexOf("::");
    if (index1 == -1) {
      throw new IllegalArgumentException("Invalid id: [" + id + "]");
    }
    int index2 = id.indexOf("::", index1 + 2);
    if (index2 == -1) {
      throw new IllegalArgumentException("Invalid id: [" + id + "]");
    }
    return new String[]{
      id.substring(0, index1),
      id.substring(index1 + 2, index2),
      id.substring(index2 + 2)
    };
  }

}

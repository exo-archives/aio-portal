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
package org.exoplatform.commons.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * A class that contains utility method that make the caller not worry much about the unexpectable expected such as
 * argument nullity or the control flow due to exceptions.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Safe {

  private Safe() {
  }

  /**
   * Return true if both objects are null or both are non null and the equals method of one object returns true when
   * it is invoked with the other object as argument.
   *
   * @param o1 the first object
   * @param o2 the second object
   * @return true if string are safely equal
   */
  public static boolean equals(Object o1, Object o2) {
    if (o1 == null) {
      return o2 == null;
    } else {
      return o2 != null && o1.equals(o2);
    }
  }

  /**
   * Close a closable object. The provided object may be null or thrown an IOException or a runtime exception during the
   * invocation of the close method without changing the control flow of the method caller. If the closeable was
   * succesfully closed the method returns true.
   *
   * @param closeable the closeable
   * @return true if the object was closed
   */
  public static boolean close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
        return true;
      }
      catch (IOException ignore) {
      }
      catch (RuntimeException ignore) {
      }
    }
    return false;
  }
}

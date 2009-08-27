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
package org.exoplatform.services.portletcontainer;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PortletContainerException extends Exception {

  /**
   * @param cause cause
   */
  public PortletContainerException(final Throwable cause) {
    super(cause);
    if (cause != null)
      cause.printStackTrace();
  }

  /**
   * @param message message
   */
  public PortletContainerException(final String message) {
    super(message);
  }

  /**
   * @param message message
   * @param cause cause
   */
  public PortletContainerException(final String message, final Throwable cause) {
    super(message, cause);
    if (cause != null)
      cause.printStackTrace();
  }

}

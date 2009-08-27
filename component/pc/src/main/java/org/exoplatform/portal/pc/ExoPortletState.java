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
package org.exoplatform.portal.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoPortletState implements Serializable {

  /** . */
  private final String portletId;

  /** . */
  private final HashMap<String, List<String>> state;

  public ExoPortletState(String portletId) {
    this.portletId = portletId;
    this.state = new HashMap<String, List<String>>();
  }

  public String getPortletId() {
    return portletId;
  }

  public Map<String, List<String>> getState() {
    return state;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ExoPortletState) {
      ExoPortletState that = (ExoPortletState)obj;
      return portletId.equals(that.portletId) && state.equals(that.state);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return portletId.hashCode() ^ state.hashCode();
  }

  @Override
  public String toString() {
    return "ExoPortletState[portletId=" + portletId + ",state=" + state + "]";
  }
}

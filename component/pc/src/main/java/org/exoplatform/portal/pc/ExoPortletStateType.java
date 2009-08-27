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

import org.gatein.pc.api.PortletStateType;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoPortletStateType extends PortletStateType<ExoPortletState> {

  /** . */
  private static final ExoPortletStateType instance = new ExoPortletStateType();

  public static ExoPortletStateType getInstance() {
    return instance;
  }

  public Class<ExoPortletState> getJavaType() {
    return ExoPortletState.class;
  }

  public boolean equals(ExoPortletState state1, ExoPortletState state2) {
    return state1.equals(state2);
  }

  public int hashCode(ExoPortletState state) {
    return state.hashCode();
  }

  public String toString(ExoPortletState state) {
    return state.toString();
  }
}

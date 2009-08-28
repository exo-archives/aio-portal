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
package org.exoplatform.services.resources;

/**
 * An orientation.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public enum Orientation {

  LT(0), // Western Europe

  RT(1), // Middle East (Arabic, Hebrew)

  TL(2), // Japanese, Chinese, Korean

  TR(3); // Mongolian

  private final int ordinal;

  Orientation(int ordinal) {
    this.ordinal = ordinal;
  }

  /**
   * Indicates whether the Locale correspond to an LT orientation.
   *  
   * @return true if the orientation is LT
   */
  public boolean isLT() {
    return ordinal == 0;
  }

  /**
   * Indicates whether the Locale correspond to an RT orientation.
   * 
   * @return true if the orientation is RT
   */
  public boolean isRT() {
    return ordinal == 1;
  }

  /**
   * Indicates whether the Locale correspond to an TL orientation.
   * 
   * @return true if the orientation is TL
   */
  public boolean isTL() {
    return ordinal == 2;
  }

  /**
   * Indicates whether the Locale correspond to an TR orientation.
   *  
   * @return true if the orientation is TR
   */
  public boolean isTR() {
    return ordinal == 3;
  }

}
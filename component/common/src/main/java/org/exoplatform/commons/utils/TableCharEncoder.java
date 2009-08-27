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

/**
 * A char encoder that use a table to cache the result produced by a delegate char encoder. This encoder
 * is stateless and should only be composed with stateless char encoder otherwise an unexpected result
 * may happen.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class TableCharEncoder implements CharEncoder {

  private static final char MAX = (char)0x10FFFD;
  
  private byte[][] table;
  private final CharEncoder charEncoder;

  /**
   * Creates a new table based char encoder.
   *
   * @param charEncoder the delegate char encoder
   */
  public TableCharEncoder(CharEncoder charEncoder) {
    this.charEncoder = charEncoder;
    this.table = new byte[MAX + 1][];
  }

  public byte[] encode(char c) {
    byte[] bytes = table[c];
    if (bytes == null) {
      bytes = charEncoder.encode(c);
      table[c] = bytes;
    }
    return bytes;
  }
}

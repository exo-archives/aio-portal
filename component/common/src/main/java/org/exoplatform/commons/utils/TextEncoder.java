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

import java.io.OutputStream;
import java.io.IOException;

/**
 * A text encoder that encodes text to an output stream. No assumptions must be made about the
 * statefullness nature of an encoder as some encoder may be statefull and some encoder may be stateless.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public interface TextEncoder {

  void encode(char c, OutputStream out) throws IOException;

  void encode(char[] chars, int off, int len, OutputStream out) throws IOException;

  void encode(String str, int off, int len, OutputStream out) throws IOException;
}

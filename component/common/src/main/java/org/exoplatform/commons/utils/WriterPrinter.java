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

import java.io.Writer;
import java.io.IOException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class WriterPrinter extends Printer {

  private final Writer delegate;

  public WriterPrinter(Writer delegate) {
    this.delegate = delegate;
  }

  public void write(int c) throws IOException {
    delegate.write(c);
  }

  public void write(char cbuf[], int off, int len) throws IOException {
    delegate.write(cbuf, off, len);
  }

  public void write(String str, int off, int len) throws IOException {
    delegate.write(str, off, len);
  }

  public void flush() throws IOException {
    delegate.flush();
  }

  public void close() throws IOException {
    delegate.close();
  }
}

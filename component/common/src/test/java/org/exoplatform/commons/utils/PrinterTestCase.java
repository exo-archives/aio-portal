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

import junit.framework.TestCase;

import java.io.IOException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PrinterTestCase extends TestCase {

  private MyPrinter printer;

  @Override
  protected void setUp() throws Exception {
    printer = new MyPrinter();
  }

  @Override
  protected void tearDown() throws Exception {
    printer = null;
  }

  public void testPrintNull() {
    printer.print(null);
    assertEquals("null", printer.buffer.toString());
  }

  public void testPrint() {
    printer.print("foo");
    assertEquals("foo", printer.buffer.toString());
  }

  public void testPrintlnNull() {
    printer.println(null);
    assertEquals("null\n", printer.buffer.toString());
  }

  public void testPrintln() {
    printer.println("foo");
    assertEquals("foo\n", printer.buffer.toString());
  }

  public void testPrintln2() {
    printer.println();
    assertEquals("\n", printer.buffer.toString());
  }

  public void testWriteNull() throws IOException {
    try {
      printer.write((String)null);
      fail();
    }
    catch (Exception ignore) {
      assertEquals("", printer.buffer.toString());
    }
    try {
      printer.write((String)null, 0, 10);
      fail();
    }
    catch (Exception ignore) {
      assertEquals("", printer.buffer.toString());
    }
    try {
      printer.write((char[])null, 0, 10);
      fail();
    }
    catch (Exception ignore) {
      assertEquals("", printer.buffer.toString());
    }
    try {
      printer.write((char[])null);
      fail();
    }
    catch (Exception ignore) {
      assertEquals("", printer.buffer.toString());
    }
  }

  private static class MyPrinter extends Printer {

    private StringBuffer buffer = new StringBuffer();

    public void write(char[] cbuf, int off, int len) throws IOException {
      buffer.append(cbuf, off, len);
    }

    public void flush() throws IOException {
    }

    public void close() throws IOException {
    }
  }
}

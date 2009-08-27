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
import junit.framework.AssertionFailedError;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class SafeTestCase extends TestCase {

  public SafeTestCase() {
  }

  public SafeTestCase(String s) {
    super(s);
  }

  public void testClose() {
    assertFalse(Safe.close(null));
    assertTrue(Safe.close(new ByteArrayOutputStream()));
    assertFalse(Safe.close(new Closeable() {
      public void close() throws IOException {
        throw new IOException();
      }
    }));
    assertFalse(Safe.close(new Closeable() {
      public void close() throws IOException {
        throw new RuntimeException();
      }
    }));
    final Error expectedError = new Error();
    try {
      Safe.close(new Closeable() {
        public void close() throws IOException {
          throw expectedError;
        }
      });
      fail();
    }
    catch (Error error) {
      assertSame(expectedError, error);
    }
  }

  private static class Thrower {

    private final Throwable t;

    private Thrower(Throwable t) {
      this.t = t;
    }

    @Override
    public boolean equals(Object obj) {
      if (t instanceof Error) {
        throw ((Error)t);
      }
      if (t instanceof RuntimeException) {
        throw ((RuntimeException)t);
      }
      throw new AssertionFailedError();
    }
  }

  public void testEquals() {
    Object o = new Object();
    assertTrue(Safe.equals(o, o));
    assertTrue(Safe.equals(null, null));
    assertFalse(Safe.equals(new Object(), null));
    assertFalse(Safe.equals(null, new Object()));
    assertFalse(Safe.equals(new Object(), new Object()));
    assertFalse(Safe.equals(new Thrower(new RuntimeException()), null));
    assertFalse(Safe.equals(null, new Thrower(new RuntimeException())));
    assertFalse(Safe.equals(new Object(), new Thrower(new RuntimeException())));
    assertFalse(Safe.equals(new Thrower(new Error()), null));
    assertFalse(Safe.equals(null, new Thrower(new Error())));
    assertFalse(Safe.equals(new Object(), new Thrower(new Error())));
    RuntimeException re = new RuntimeException();
    Error er = new Error();
    try {
      Safe.equals(new Thrower(er), new Object());
      fail();
    }
    catch (Error e) {
      assertSame(er, e);
    }
    try {
      Safe.equals(new Thrower(re), new Object());
      fail();
    }
    catch (RuntimeException e) {
      assertSame(re, e);
    }
  }

}

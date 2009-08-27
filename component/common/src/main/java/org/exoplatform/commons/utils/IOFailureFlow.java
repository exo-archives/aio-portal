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
 * Enumerates the behavior that a joinpoint can have uppon an i/o exception instead of just letting
 * the exception happen.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public enum IOFailureFlow {

  /**
   * Rethrows the exception.
   */
  RETHROW,

  /**
   * Ignores the exception.
   */
  IGNORE,

  /**
   * Wraps and rethrows the exception with an instance of {@link org.exoplatform.commons.utils.UndeclaredIOException}.
   */
  THROW_UNDECLARED

}

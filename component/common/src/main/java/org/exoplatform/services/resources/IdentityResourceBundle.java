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

import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Locale;

/**
 * A resource bundle that returns the queried key. It returns an empty enumeration when the keys are queried.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class IdentityResourceBundle extends ResourceBundle {

  public static final String MAGIC_LANGUAGE = "ma".intern();
  public static final Locale MAGIC_LOCALE = new Locale(MAGIC_LANGUAGE);
  private static final Vector<String> EMPTY_KEYS = new Vector<String>();
  private static final IdentityResourceBundle instance = new IdentityResourceBundle();

  public static ResourceBundle getInstance() {
    return instance;
  }

  protected Object handleGetObject(String key) {
    return key;
  }

  public Enumeration<String> getKeys() {
    return EMPTY_KEYS.elements();
  }
}

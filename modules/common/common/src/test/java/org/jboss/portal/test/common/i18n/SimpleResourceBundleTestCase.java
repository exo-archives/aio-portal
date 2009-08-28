/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.jboss.portal.test.common.i18n;

import junit.framework.TestCase;
import org.jboss.portal.common.i18n.SimpleResourceBundleFactory;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class SimpleResourceBundleTestCase extends TestCase
{

   /** . */
   private Locale defaultLocale;

   public SimpleResourceBundleTestCase()
   {
   }

   public SimpleResourceBundleTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      defaultLocale = Locale.getDefault();
      Locale.setDefault(Locale.JAPAN);
   }

   protected void tearDown() throws Exception
   {
      Locale.setDefault(defaultLocale);
   }

   public void testNormal()
   {
      SimpleResourceBundleFactory factory = new SimpleResourceBundleFactory("a", BundleClassLoader.assertExists());
      assertNull(factory.getBundle(Locale.UK));
      ResourceBundle bundle = factory.getBundle(new Locale("fr", "FR", "ab"));
      assertNotNull(bundle);
      assertEquals(Locale.FRANCE, bundle.getLocale());
      bundle = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(bundle);
      assertEquals(Locale.FRANCE, bundle.getLocale());
      bundle = factory.getBundle(new Locale("fr"));
      assertNotNull(bundle);
      assertEquals(Locale.FRENCH, bundle.getLocale());
   }

   public void testCtorThrowsIAE()
   {
      try
      {
         new SimpleResourceBundleFactory(null, BundleClassLoader.assertExists());
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         new SimpleResourceBundleFactory("a", null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testGetBundleWithNullLocaleThrowsIAE()
   {
      SimpleResourceBundleFactory factory = new SimpleResourceBundleFactory("a", BundleClassLoader.assertExists());
      try
      {
         factory.getBundle(null);
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }
}

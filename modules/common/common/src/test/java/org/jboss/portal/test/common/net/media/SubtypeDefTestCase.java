/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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
package org.jboss.portal.test.common.net.media;

import junit.framework.TestCase;
import org.jboss.portal.common.net.media.SubtypeDef;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class SubtypeDefTestCase extends TestCase
{

   public void testCaseSensitivity()
   {
      assertEquals(SubtypeDef.HTML, SubtypeDef.create("html"));
      assertEquals(SubtypeDef.HTML, SubtypeDef.create("hTml"));
      assertEquals(SubtypeDef.HTML, SubtypeDef.create("HTML"));

      //
      SubtypeDef foo = SubtypeDef.create("foo");
      assertEquals(foo, SubtypeDef.create("foo"));
      assertEquals(foo, SubtypeDef.create("fOo"));
      assertEquals(foo, SubtypeDef.create("FOO"));
   }

   public void testIAE()
   {
      try
      {
         SubtypeDef.create(null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
      try
      {
         SubtypeDef.create("");
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }
}

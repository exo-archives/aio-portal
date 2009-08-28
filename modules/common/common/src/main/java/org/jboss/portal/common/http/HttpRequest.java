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
package org.jboss.portal.common.http;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Just used to define the request body.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class HttpRequest implements Serializable
{

   public abstract static class Body implements Serializable
   {
   }

   public static class Raw extends Body
   {

      /** . */
      private byte[] bytes;

      public byte[] getBytes()
      {
         return bytes;
      }

      public void setBytes(byte[] bytes)
      {
         this.bytes = bytes;
      }
   }

   public static class Form extends Body
   {

      /** . */
      private Map parameters = new HashMap();

      public void addParameter(String name, String[] values)
      {
         if (name == null)
         {
            throw new IllegalStateException();
         }
         if (values == null)
         {
            throw new IllegalStateException();
         }
         for (int i = 0; i < values.length; i++)
         {
            String value = values[i];
            if (value == null)
            {
               throw new IllegalStateException();
            }
         }
         parameters.put(name, values.clone());
      }

      public void removeParameter(String name)
      {
         if (name == null)
         {
            throw new IllegalStateException();
         }
         parameters.remove(name);
      }

      public Set getParameterNames()
      {
         return Collections.unmodifiableSet(parameters.keySet());
      }

      public String[] getParameterValues(String name)
      {
         if (name == null)
         {
            throw new IllegalStateException();
         }
         String[] values = (String[])parameters.get(name);
         return (String[])values.clone();
      }
   }
}

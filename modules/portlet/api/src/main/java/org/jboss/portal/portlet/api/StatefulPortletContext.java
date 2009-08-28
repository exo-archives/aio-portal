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
package org.jboss.portal.portlet.api;

import org.jboss.portal.common.util.ParameterValidation;

import java.util.Arrays;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 1.1 $
 * @since 2.6
 */
public class StatefulPortletContext extends PortletContext
{

   /** . */
   private final byte[] marshalledState;

   StatefulPortletContext(String id, byte[] marshalledState) throws IllegalArgumentException
   {
      super(id);

      ParameterValidation.throwIllegalArgExceptionIfNull(marshalledState, "Portlet state");

      this.marshalledState = marshalledState;
   }


   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o == null || getClass() != o.getClass())
      {
         return false;
      }
      if (!super.equals(o))
      {
         return false;
      }

      StatefulPortletContext that = (StatefulPortletContext)o;

      return Arrays.equals(marshalledState, that.marshalledState);
   }

   public int hashCode()
   {
      int result = super.hashCode();
      result = 31 * result + (marshalledState != null ? marshalledState.hashCode() : 0);
      return result;
   }

   public byte[] getState()
   {
      return marshalledState;
   }

   public String toString()
   {
      return "StatefulPortletContext[" + id + "," + marshalledState.length + "]";
   }
}

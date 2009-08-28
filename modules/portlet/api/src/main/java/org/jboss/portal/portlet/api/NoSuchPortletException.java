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

/**
 * Thrown by the portlet invoker when a portlet id does not exist.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5989 $
 */
public class NoSuchPortletException extends InvalidPortletIdException
{

   public NoSuchPortletException(String portletId)
   {
      super("No such portlet " + portletId, portletId);
   }

   public NoSuchPortletException(String message, String portletId)
   {
      super(message, portletId);
   }

   public NoSuchPortletException(Throwable cause, String portletId)
   {
      super(cause, portletId);
   }

   public NoSuchPortletException(String message, Throwable cause, String portletId)
   {
      super(message, cause, portletId);
   }
}

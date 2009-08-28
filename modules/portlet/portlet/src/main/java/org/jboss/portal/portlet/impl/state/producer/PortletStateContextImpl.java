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
package org.jboss.portal.portlet.impl.state.producer;

import org.jboss.portal.portlet.state.producer.PortletStateContext;
import org.jboss.portal.portlet.state.producer.PortletState;
import org.jboss.portal.portlet.api.state.PropertyMap;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class PortletStateContextImpl implements PortletStateContext
{

   /** . */
   private final String id;

   /** . */
   private PortletState state;

   public PortletStateContextImpl(String id, String portletId, PropertyMap propertyMap)
   {
      this.id = id;
      this.state = new PortletState(portletId, propertyMap);
   }

   public String getId()
   {
      return id;
   }

   public PortletState getState()
   {
      return state;
   }
}

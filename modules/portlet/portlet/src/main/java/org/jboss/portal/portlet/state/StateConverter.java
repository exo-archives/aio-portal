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
package org.jboss.portal.portlet.state;

import org.jboss.portal.portlet.state.producer.PortletState;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5873 $
 */
public interface StateConverter
{
   /**
    * Marshall the producer state as a byte array.
    *
    * @param state the producer state
    * @return the marshalled state
    * @throws StateConversionException if marshalling cannot be performed
    * @throws IllegalArgumentException if the state is null
    */
   byte[] marshall(PortletState state) throws StateConversionException, IllegalArgumentException;

   /**
    * Unmarshall the producer state from a byte array.
    *
    * @param marshalledState the marshalled state
    * @return the producer state
    * @throws StateConversionException if unmarshalling cannot be performed
    * @throws IllegalArgumentException if the argument is null
    */
   PortletState unmarshall(byte[] marshalledState) throws StateConversionException, IllegalArgumentException;
}

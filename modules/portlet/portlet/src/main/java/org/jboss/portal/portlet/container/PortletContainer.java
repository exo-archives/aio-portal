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
package org.jboss.portal.portlet.container;

import org.gatein.common.invocation.InvocationException;
import org.jboss.portal.portlet.api.PortletInvokerException;
import org.jboss.portal.portlet.api.info.PortletInfo;
import org.jboss.portal.portlet.api.invocation.PortletInvocation;
import org.jboss.portal.portlet.api.invocation.response.PortletInvocationResponse;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7226 $
 */
public interface PortletContainer
{

   /**
    * Return the portlet container id, unique within the context of the parent application.
    *
    * @return the id
    */
   String getId();

   /**
    * Returns the runtime meta data of the container.
    *
    * @return the info
    */
   PortletInfo getInfo();

   /**
    * Invoke the portlet container.
    *
    * @param invocation the portlet invocation
    * @return the portlet invocation response
    * @throws org.jboss.portal.portlet.api.PortletInvokerException a portlet invoker exception
    * @throws InvocationException an invocation exception
    */
   PortletInvocationResponse dispatch(PortletInvocation invocation) throws PortletInvokerException, InvocationException;

   /**
    * Returns the wired application.
    *
    * @return the application
    */
   PortletApplication getPortletApplication();

   /**
    * Returns the context of the portlet container.
    *
    * @return the context
    */
   PortletContainerContext getContext();
}

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
package org.jboss.portal.portlet.aspects.portlet;

import org.jboss.portal.portlet.api.invocation.PortletInvocation;
import org.jboss.portal.portlet.api.invocation.ActionInvocation;
import org.jboss.portal.portlet.api.invocation.EventInvocation;
import org.jboss.portal.portlet.PortletInvokerInterceptor;
import org.jboss.portal.portlet.api.invocation.response.PortletInvocationResponse;
import org.jboss.portal.portlet.api.spi.InstanceContext;
import org.jboss.portal.portlet.api.spi.UserContext;
import org.jboss.portal.portlet.api.state.AccessMode;
import org.jboss.portal.portlet.api.StateEvent;
import org.jboss.portal.portlet.api.PortletContext;
import org.jboss.portal.portlet.api.PortletInvokerException;

/**
 * <p>This interceptor takes in charge the management of portlet customization when the invocation carries
 * an read only access mode. The customizations will be stored in the principal scope of the portlet
 * invocation for security reasons.</p>
 *
 * <p>This interceptor must not be used in a production environment.</p>
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class PortletCustomizationInterceptor extends PortletInvokerInterceptor
{
   public PortletInvocationResponse invoke(PortletInvocation invocation) throws IllegalArgumentException, PortletInvokerException
   {
      InstanceContext oldContext = invocation.getInstanceContext();
      if ((invocation instanceof ActionInvocation || invocation instanceof EventInvocation) && oldContext.getAccessMode() == AccessMode.READ_ONLY)
      {
         PortletContext oldTarget = invocation.getTarget();
         try
         {
            UserContext userContext = invocation.getUserContext();
            PortletInstanceContext newContext = new PortletInstanceContext(userContext, oldTarget);

            //
            invocation.setInstanceContext(newContext);
            invocation.setTarget(newContext.getTarget());

            //
            return super.invoke(invocation);
         }
         finally
         {
            invocation.setTarget(oldTarget);
            invocation.setInstanceContext(oldContext);
         }
      }
      else
      {
         return super.invoke(invocation);
      }
   }

   private static class PortletInstanceContext implements InstanceContext
   {

      /** . */
      private UserContext userContext;

      /** . */
      private PortletContext target;

      /** . */
      private boolean useClone;

      /** . */
      private String id;

      public PortletInstanceContext(
         UserContext userContext,
         PortletContext portletContext)
      {
         String id = portletContext.getId();
         PortletContext target = portletContext;
         boolean useClone = false;
         PortletContext clone = (PortletContext)userContext.getAttribute("clone." + id);
         if (clone != null)
         {
            target = clone;
            useClone = true;
         }

         //
         this.userContext = userContext;
         this.useClone = useClone;
         this.target = target;
         this.id = id;
      }

      public PortletContext getTarget()
      {
         return target;
      }

      public String getId()
      {
         return id;
      }

      public AccessMode getAccessMode()
      {
         return useClone ? AccessMode.READ_WRITE : AccessMode.CLONE_BEFORE_WRITE;
      }

      public void onStateEvent(StateEvent event)
      {
         target = event.getPortletContext();
         useClone = true;
         userContext.setAttribute("clone." + id, target);
      }
   }
}

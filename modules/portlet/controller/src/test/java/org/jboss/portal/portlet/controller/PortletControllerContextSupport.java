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
package org.jboss.portal.portlet.controller;

import org.jboss.portal.portlet.api.info.PortletInfo;
import org.jboss.portal.portlet.api.spi.PortletInvocationContext;
import org.jboss.portal.portlet.controller.state.PortletPageNavigationalState;
import org.jboss.portal.portlet.controller.state.StateControllerContext;
import org.jboss.portal.portlet.controller.state.PortletWindowNavigationalState;
import org.jboss.portal.portlet.controller.impl.state.StateControllerContextImpl;
import org.jboss.portal.portlet.controller.event.EventControllerContext;
import org.jboss.portal.portlet.controller.request.ControllerRequest;
import org.jboss.portal.portlet.controller.request.PortletActionRequest;
import org.jboss.portal.portlet.api.invocation.response.PortletInvocationResponse;
import org.jboss.portal.portlet.api.invocation.ActionInvocation;
import org.jboss.portal.portlet.api.invocation.EventInvocation;
import org.jboss.portal.portlet.api.invocation.ResourceInvocation;
import org.jboss.portal.portlet.api.invocation.PortletInvocation;
import org.jboss.portal.portlet.api.invocation.RenderInvocation;
import org.jboss.portal.portlet.api.PortletInvokerException;
import org.jboss.portal.portlet.api.PortletContext;
import org.jboss.portal.portlet.api.OpaqueStateString;
import org.jboss.portal.portlet.support.PortletInvokerSupport;
import org.jboss.portal.portlet.support.PortletSupport;
import org.gatein.common.util.ParameterMap;

import javax.servlet.http.Cookie;
import java.util.List;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public final class PortletControllerContextSupport implements PortletControllerContext
{

   /** . */
   private final StateControllerContext stateControllerContext = new StateControllerContextImpl(this);

   /** . */
   private EventControllerContext eventControllerContext;

   /** . */
   private final PortletInvokerSupport invoker = new PortletInvokerSupport();

   public PortletInvokerSupport getInvoker()
   {
      return invoker;
   }

   public PortletInfo getPortletInfo(String windowId)
   {
      if (windowId == null)
      {
         throw new IllegalArgumentException();
      }

      //
      PortletSupport portlet = invoker.getPortlet(windowId);

      //
      return portlet != null ? portlet.getInfo() : null;
   }

   //
   public PortletInvocationContext createPortletInvocationContext(String windowId, PortletPageNavigationalState pageNavigationalState)
   {
      if (windowId == null)
      {
         throw new IllegalArgumentException();
      }

      //
      return new PortletInvocationContextSupport(windowId, pageNavigationalState);
   }

   private PortletInvocationResponse invoke(PortletInvocation invocation) throws PortletInvokerException
   {
      PortletInvocationContextSupport context = (PortletInvocationContextSupport)invocation.getContext();

      //
      PortletContext target = PortletContext.createPortletContext(context.getWindowId());

      //
      invocation.setTarget(target);

      //
      return invoker.invoke(invocation);
   }

   public PortletInvocationResponse invoke(ActionInvocation actionInvocation) throws PortletInvokerException
   {
      return invoke((PortletInvocation)actionInvocation);
   }

   public PortletInvocationResponse invoke(List<Cookie> requestCookies, EventInvocation eventInvocation) throws PortletInvokerException
   {
      return invoke(eventInvocation);
   }

   public PortletInvocationResponse invoke(List<Cookie> requestCookies, RenderInvocation renderInvocation) throws PortletInvokerException
   {
      return invoke((PortletInvocation)renderInvocation);
   }

   public PortletInvocationResponse invoke(ResourceInvocation resourceInvocation) throws PortletInvokerException
   {
      return invoke((PortletInvocation)resourceInvocation);
   }

   public EventControllerContext getEventControllerContext()
   {
      return eventControllerContext;
   }

   public void setEventControllerContext(EventControllerContext eventControllerContext)
   {
      this.eventControllerContext = eventControllerContext;
   }

   public StateControllerContext getStateControllerContext()
   {
      return stateControllerContext;
   }

   public ControllerRequest createActionRequest(String windowId)
   {
      return new PortletActionRequest(
         windowId,
         new OpaqueStateString(""),
         new ParameterMap(),
         new PortletWindowNavigationalState(),
         getStateControllerContext().createPortletPageNavigationalState(false)
      );
   }
}

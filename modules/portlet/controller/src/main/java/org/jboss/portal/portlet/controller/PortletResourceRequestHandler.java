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

import org.jboss.portal.Mode;
import org.jboss.portal.WindowState;
import org.jboss.portal.common.util.ParameterMap;
import org.jboss.portal.portlet.api.PortletInvokerException;
import org.jboss.portal.portlet.api.StateString;
import org.jboss.portal.portlet.api.spi.PortletInvocationContext;
import org.jboss.portal.portlet.api.cache.CacheLevel;
import org.jboss.portal.portlet.api.invocation.ResourceInvocation;
import org.jboss.portal.portlet.api.invocation.response.PortletInvocationResponse;
import org.jboss.portal.portlet.controller.request.PortletResourceRequest;
import org.jboss.portal.portlet.controller.response.ControllerResponse;
import org.jboss.portal.portlet.controller.response.ResourceResponse;
import org.jboss.portal.portlet.controller.state.PortletPageNavigationalState;
import org.jboss.portal.portlet.controller.state.PortletWindowNavigationalState;

import java.util.Map;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 10580 $
 */
class PortletResourceRequestHandler extends RequestHandler<PortletResourceRequest>
{

   public PortletResourceRequestHandler(PortletController controller)
   {
      super(PortletResourceRequest.class, controller);
   }

   ControllerResponse processResponse(PortletControllerContext controllerContext, PortletResourceRequest request, PortletInvocationResponse response) throws PortletInvokerException
   {
      return new ResourceResponse(response);
   }

   PortletInvocationResponse invoke(PortletControllerContext context, PortletResourceRequest portletResourceRequest) throws PortletInvokerException
   {

      //
      Mode mode = null;
      WindowState windowState = null;
      PortletPageNavigationalState pageNavigationalState = null;
      Map<String, String[]> publicNS = null;
      StateString portletNS = null;
      CacheLevel cacheability;

      PortletResourceRequest.Scope scope = portletResourceRequest.getScope();

      //
      if (scope instanceof PortletResourceRequest.PortletScope)
      {
         PortletResourceRequest.PortletScope portletScope = (PortletResourceRequest.PortletScope)scope;
         PortletWindowNavigationalState navigationalState = portletScope.getWindowNavigationalState();

         // 
         if (navigationalState != null)
         {
            mode = navigationalState.getMode();
            windowState = navigationalState.getWindowState();
            portletNS = navigationalState.getPortletNavigationalState();
         }

         //
         if (scope instanceof PortletResourceRequest.PageScope)
         {
            PortletResourceRequest.PageScope pageScope = (PortletResourceRequest.PageScope)scope;
            pageNavigationalState = pageScope.getPageNavigationalState();
            cacheability = CacheLevel.PAGE;

            //
            if (pageNavigationalState != null)
            {
               publicNS = pageNavigationalState.getPortletPublicNavigationalState(portletResourceRequest.getWindowId());
            }
         }
         else
         {
            cacheability = CacheLevel.PORTLET;
         }
      }
      else
      {
         cacheability = CacheLevel.FULL;
      }

      //
      if (mode == null)
      {
         mode = Mode.VIEW;
      }
      if (windowState == null)
      {
         windowState = WindowState.NORMAL;
      }

      //
      PortletInvocationContext portletInvocationContext = context.createPortletInvocationContext(portletResourceRequest.getWindowId(), pageNavigationalState);
      ResourceInvocation resourceInvocation = new ResourceInvocation(portletInvocationContext);

      //
      resourceInvocation.setResourceId(portletResourceRequest.getResourceId());
      resourceInvocation.setCacheLevel(cacheability);
      resourceInvocation.setMode(mode);
      resourceInvocation.setWindowState(windowState);
      resourceInvocation.setNavigationalState(portletNS);
      resourceInvocation.setPublicNavigationalState(publicNS);
      resourceInvocation.setResourceState(portletResourceRequest.getResourceState());
      resourceInvocation.setForm(portletResourceRequest.getBodyParameters() != null ? ParameterMap.clone(portletResourceRequest.getBodyParameters()) : null);

      //
      try
      {
         return context.invoke(resourceInvocation);
      }
      catch (PortletInvokerException e)
      {
         return null;
      }
   }
}

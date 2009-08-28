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
package org.jboss.portal.test.portlet.jsr286.tck.dispatcher;

import org.jboss.portal.unit.PortletTestCase;
import org.jboss.portal.unit.PortletTestContext;
import org.jboss.portal.unit.base.AbstractUniversalTestPortlet;
import org.jboss.portal.unit.actions.PortletRenderTestAction;
import org.jboss.portal.unit.actions.ServletServiceTestAction;
import org.jboss.portal.unit.actions.PortletActionTestAction;
import org.jboss.portal.unit.actions.PortletEventTestAction;
import org.jboss.portal.unit.actions.PortletResourceTestAction;
import org.jboss.portal.test.portlet.framework.UTP1;
import org.jboss.portal.test.portlet.framework.UTS1;
import org.jboss.portal.unit.annotations.TestCase;
import org.jboss.portal.unit.Assertion;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import static org.jboss.unit.api.Assert.assertNotNull;
import static org.jboss.unit.api.Assert.assertEquals;
import org.jboss.unit.remote.driver.handler.http.response.InvokeGetResponse;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@TestCase({Assertion.JSR168_128})
public class IncludedRequestAttributesTestCase
{

   /** . */
   private Object _config;

   /** . */
   private Object _request;

   /** . */
   private Object _response;

   public IncludedRequestAttributesTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            return new InvokeGetResponse(response.createActionURL().toString());
         }
      });
      seq.bindAction(1, UTP1.ACTION_JOIN_POINT, new PortletActionTestAction()
      {
         protected void run(Portlet portlet, ActionRequest request, ActionResponse response, PortletTestContext context) throws PortletException, IOException
         {
            dispatch(portlet, request, response);

            //
            response.setEvent("Event", null);
         }
      });
      seq.bindAction(1, UTP1.EVENT_JOIN_POINT, new PortletEventTestAction()
      {
         protected void run(Portlet portlet, EventRequest request, EventResponse response, PortletTestContext context) throws PortletException, IOException
         {
            dispatch(portlet, request, response);
         }
      });
      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            dispatch(portlet, request, response);

            //
            return new InvokeGetResponse(response.createResourceURL().toString());
         }
      });
      seq.bindAction(1, UTS1.SERVICE_JOIN_POINT, service);
      seq.bindAction(2, UTP1.RESOURCE_JOIN_POINT, new PortletResourceTestAction()
      {
         protected DriverResponse run(Portlet portlet, ResourceRequest request, ResourceResponse response, PortletTestContext context) throws PortletException, IOException
         {
            dispatch(portlet, request, response);

            //
            return new EndTestResponse();
         }
      });
      seq.bindAction(2, UTS1.SERVICE_JOIN_POINT, service);
   }

   private ServletServiceTestAction service = new ServletServiceTestAction()
   {
      protected DriverResponse run(Servlet servlet, HttpServletRequest request, HttpServletResponse response, PortletTestContext context) throws ServletException, IOException
      {
         _config = request.getAttribute("javax.portlet.config");
         _request = request.getAttribute("javax.portlet.request");
         _response = request.getAttribute("javax.portlet.response");
         return null;
      }
   };

   private void dispatch(Portlet portlet, PortletRequest request, PortletResponse response) throws PortletException, IOException
   {
      _config = null;
      _request = null;
      _response = null;

      //
      PortletRequestDispatcher dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().getRequestDispatcher("/universalServletA");
      dispatcher.include(request, response);

      assertEquals(((AbstractUniversalTestPortlet)portlet).getPortletConfig(), _config);
      assertEquals(request, _request);
      assertEquals(response, _response);

      //
      dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().getNamedDispatcher("UniversalServletA");
      assertNotNull(dispatcher);
      dispatcher.include(request, response);

      //
      assertEquals(((AbstractUniversalTestPortlet)portlet).getPortletConfig(), _config);
      assertEquals(request, _request);
      assertEquals(response, _response);

      //
      _config = null;
      _request = null;
      _response = null;
   }
}
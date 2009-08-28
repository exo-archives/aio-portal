/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.jboss.portal.test.portlet.jsr168.ext.taglib;

import org.jboss.portal.unit.PortletTestCase;
import org.jboss.portal.unit.PortletTestContext;
import org.jboss.portal.unit.Assertion;
import org.jboss.portal.unit.annotations.TestCase;
import org.jboss.portal.unit.base.AbstractUniversalTestPortlet;
import org.jboss.portal.unit.actions.PortletRenderTestAction;
import org.jboss.portal.test.portlet.framework.UTP1;
import org.jboss.portal.test.portlet.jsr286.tck.taglib.*;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import org.jboss.unit.remote.driver.handler.http.response.InvokeGetResponse;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.WindowState;
import javax.portlet.PortletMode;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author <a href="mailto:boleslaw dot dawidowicz at redhat anotherdot com">Boleslaw Dawidowicz</a>
 * @version : 0.1 $
 */
@TestCase({
   Assertion.EXT_TAGLIB_4
   })

public class RenderURLTestCase extends TaglibTestCase
{
   public RenderURLTestCase(PortletTestCase seq)
   {

      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            response.setContentType("text/html");
            PortletRequestDispatcher dispatcher = ((AbstractUniversalTestPortlet)portlet).getPortletContext().
               getRequestDispatcher("/renderURL.jsp");

            PortletURL pu1 = response.createRenderURL();

            pu1.setWindowState(WindowState.NORMAL);
            pu1.setPortletMode(PortletMode.EDIT);
            pu1.setSecure(true);

            PortletURL pu2 = response.createRenderURL();
            pu2.setSecure(false);

            pu2.setParameter("testParam", new String[] {"testParamValue", "testParamValue2"});
            pu2.setParameter("secondParam", "testParamValue");

            // Create session
            request.getPortletSession();

            // escapeXml=true
            String url1 = response.encodeURL(pu1.toString());


            // escapeXml=false
            String url2 = response.encodeURL(pu2.toString());


            expectedResults = new String[]{ url2 };

            include(dispatcher, request, response);


            return new InvokeGetResponse(response.createRenderURL().toString());


         }
      });

      seq.bindAction(1, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws IOException, PortletException
         {
            response.setContentType("text/html");

            assertResults(context);

            return new EndTestResponse();
         }
      });


   }
}
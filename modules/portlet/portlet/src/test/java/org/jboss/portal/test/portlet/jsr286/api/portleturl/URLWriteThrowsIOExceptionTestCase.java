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
package org.jboss.portal.test.portlet.jsr286.api.portleturl;

import org.jboss.portal.unit.annotations.TestCase;
import org.jboss.portal.unit.Assertion;
import org.jboss.portal.unit.PortletTestCase;
import org.jboss.portal.unit.PortletTestContext;
import org.jboss.portal.unit.actions.PortletRenderTestAction;
import org.jboss.portal.test.portlet.framework.UTP1;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import static org.jboss.unit.api.Assert.*;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletException;
import javax.portlet.BaseURL;
import java.io.IOException;
import java.io.Writer;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase(Assertion.API286_BASE_URL_5)
public class URLWriteThrowsIOExceptionTestCase
{
   public URLWriteThrowsIOExceptionTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP1.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context) throws PortletException, IOException
         {
            BaseURL url = response.createRenderURL();

            //
            try
            {
               url.write(new Writer()
               {
                  public void write(char cbuf[], int off, int len) throws IOException
                  {
                     throw new IOException();
                  }

                  public void flush() throws IOException
                  {
                  }

                  public void close() throws IOException
                  {
                  }
               });
               fail("Was expecting an IOException to be thrown");
            }
            catch (IOException ignore)
            {
            }

            //
            return new EndTestResponse();
         }
      });
   }
}

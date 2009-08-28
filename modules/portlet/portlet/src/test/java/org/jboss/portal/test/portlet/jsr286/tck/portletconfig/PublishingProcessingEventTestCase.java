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
package org.jboss.portal.test.portlet.jsr286.tck.portletconfig;

import org.jboss.portal.unit.PortletTestCase;
import org.jboss.portal.unit.PortletTestContext;
import org.jboss.portal.unit.Assertion;
import org.jboss.portal.unit.annotations.TestCase;
import org.jboss.portal.unit.base.AbstractUniversalTestPortlet;
import org.jboss.portal.unit.actions.PortletRenderTestAction;
import org.jboss.portal.test.portlet.framework.UTP4;
import org.gatein.common.util.Tools;
import org.jboss.unit.driver.DriverResponse;
import org.jboss.unit.driver.response.EndTestResponse;
import static org.jboss.unit.api.Assert.assertNotNull;
import static org.jboss.unit.api.Assert.assertFalse;
import static org.jboss.unit.api.Assert.assertEquals;

import javax.portlet.Portlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletConfig;
import javax.xml.namespace.QName;
import java.util.Enumeration;
import java.util.List;
import java.util.HashSet;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
@TestCase({
   Assertion.JSR286_30,
   Assertion.JSR286_31,
   Assertion.JSR286_32,
   Assertion.JSR286_33
   })
public class PublishingProcessingEventTestCase
{
   public PublishingProcessingEventTestCase(PortletTestCase seq)
   {
      seq.bindAction(0, UTP4.RENDER_JOIN_POINT, new PortletRenderTestAction()
      {
         protected DriverResponse run(Portlet portlet, RenderRequest request, RenderResponse response, PortletTestContext context)
         {
            PortletConfig cfg = ((AbstractUniversalTestPortlet)portlet).getPortletConfig();

            //
            Enumeration<QName> publishingEvents = cfg.getPublishingEventQNames();
            assertNotNull(publishingEvents);
            List<QName> publishingEventList = Tools.toList(publishingEvents);
            assertEquals(PublishingEventTestCase.EVENT_NAMES.size(), publishingEventList.size());
            assertEquals(PublishingEventTestCase.EVENT_NAMES, new HashSet<QName>(publishingEventList));

            //
            Enumeration<QName> processingEvents = cfg.getProcessingEventQNames();
            assertNotNull(processingEvents);
            List<QName> processingEventList = Tools.toList(processingEvents);
            assertEquals(PublishingEventTestCase.EVENT_NAMES.size(), processingEventList.size());
            assertEquals(PublishingEventTestCase.EVENT_NAMES, new HashSet<QName>(processingEventList));

            //
            return new EndTestResponse();
         }
      });
   }
}
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
package org.jboss.portal.test.portlet.federation;

import org.jboss.portal.portlet.api.PortletInvokerException;
import org.jboss.portal.portlet.support.PortletInvokerSupport;
import org.jboss.portal.portlet.federation.FederatingPortletInvoker;
import org.jboss.portal.portlet.federation.FederatedPortletInvoker;
import org.jboss.portal.portlet.federation.impl.FederatingPortletInvokerService;

import java.util.HashSet;
import java.util.Collection;

import org.jboss.unit.api.pojo.annotations.Test;

import static org.jboss.unit.api.Assert.*;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class OneInvokerNoPortletsTestCase
{

   @Test
   public void testOneFederatedWithNoPortlets() throws PortletInvokerException
   {
      FederatingPortletInvoker federating = new FederatingPortletInvokerService();
      PortletInvokerSupport support = new PortletInvokerSupport();
      federating.registerInvoker("foo", support);

      //
      assertEquals(new HashSet(), federating.getPortlets());

      //
      Collection federateds = federating.getFederatedInvokers();
      assertNotNull(federateds);
      assertEquals(1, federateds.size());
      FederatedPortletInvoker federated = (FederatedPortletInvoker)federateds.iterator().next();
      assertEquals("foo", federated.getId());
      assertEquals(support, federated.getPortletInvoker());
   }
}

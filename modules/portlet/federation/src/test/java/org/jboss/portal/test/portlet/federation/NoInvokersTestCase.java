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
import org.jboss.portal.portlet.federation.FederatingPortletInvoker;
import org.jboss.portal.portlet.federation.impl.FederatingPortletInvokerService;

import java.util.HashSet;
import java.util.ArrayList;

import static org.jboss.unit.api.Assert.*;
import org.jboss.unit.api.pojo.annotations.Test;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class NoInvokersTestCase
{
   @Test
   public void testNoFederated() throws PortletInvokerException
   {
      FederatingPortletInvoker federating = new FederatingPortletInvokerService();

      //
      assertEquals(new HashSet(), federating.getPortlets());

      //
      assertEquals(new ArrayList(), new ArrayList(federating.getFederatedInvokers()));
   }
}

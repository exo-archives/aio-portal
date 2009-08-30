/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.portletcontainer.impl.servlet;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * <p>This class just do nothing. This class exists to provide backwards compatiblity for portlets that have
 * been setup to be deployable on versions of eXo portal.</p>
 *
 * <p>When the context is initiliazed it prints a message on the system logging to explain that this
 * class should not be used anymore.</p>
 *
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class PortletApplicationListener implements ServletContextListener
{
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // julien todo : print log that say that the legacy exo servlet should be replaced by GateIn servlet
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    
  }
}

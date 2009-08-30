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
package org.exoplatform.portal.pc;

import org.gatein.pc.mc.PortletApplicationDeployer;
import org.gatein.pc.portlet.impl.metadata.PortletApplication10MetaData;
import org.gatein.wci.WebApp;

/**
 * Extends the {@link org.gatein.pc.mc.PortletApplicationDeployer} to configure the resource bundle factory
 * of deployed portlet applications. The resource bundle factory used is
 * {@link org.exoplatform.portal.pc.ExoResourceBundleFactory}.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoPortletApplicationDeployer extends PortletApplicationDeployer {

  @Override
  protected PortletApplication10MetaData buildPortletApplicationMetaData(WebApp webApp) {
    PortletApplication10MetaData md = super.buildPortletApplicationMetaData(webApp);
    if (md != null)
    {
      md.setResourceBundleFactoryName(ExoResourceBundleFactory.class.getName());
    }
    return md;
  }
}

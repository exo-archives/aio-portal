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
package org.exoplatform.portal.application;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.web.application.ApplicationLifecycle;
import org.exoplatform.web.application.Application;
import org.exoplatform.portal.pom.config.POMSessionManager;
import org.exoplatform.container.ExoContainer;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class MOPSessionLifeCycle implements ApplicationLifecycle<WebuiRequestContext> {

  /** . */
  private final ThreadLocal<POMSessionManager> currentMgr = new ThreadLocal<POMSessionManager>();

  public void onInit(Application app) throws Exception {
  }

  public void onStartRequest(Application app, WebuiRequestContext context) throws Exception {
    ExoContainer container = context.getApplication().getApplicationServiceContainer();
    POMSessionManager mgr = (POMSessionManager)container.getComponentInstanceOfType(POMSessionManager.class);
    mgr.openSession();
    currentMgr.set(mgr);
  }

  public void onEndRequest(Application app, WebuiRequestContext context) throws Exception {
    POMSessionManager mgr = currentMgr.get();
    currentMgr.remove();

    // Need to see if saving untouched session has an impact or not on performances
    mgr.closeSession(true);
  }

  public void onDestroy(Application app) throws Exception {
  }
}

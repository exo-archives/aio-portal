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
package org.exoplatform.portal.pom.config;

import org.exoplatform.portal.model.impl.api.POM;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;
import org.exoplatform.services.jcr.ext.common.SessionProvider;

import javax.jcr.Session;
import javax.jcr.RepositoryException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class POMSession {

  /** . */
  final POMSessionManager mgr;

  /** . */
  private POM model;

  /** . */
  private boolean isInTask;

  public POMSession(POMSessionManager mgr) {
    this.mgr = mgr;
    this.isInTask = false;
  }

  private POM getModel() {
    if (model == null) {
      model = mgr.pomService.getModel();
    }
    return model;
  }

  public Workspace getWorkspace() {
    return getModel().getWorkspace();
  }

  public void execute(POMTask task) throws RepositoryException {
    if (isInTask) {
      throw new IllegalStateException();
    }

    //
    try {
      isInTask = true;
      task.run(this);
    } finally {
      model.close();
      isInTask = false;
    }
  }

  public void save() {
    if (!isInTask) {
      throw new IllegalStateException();
    }
    model.save();
  }
}

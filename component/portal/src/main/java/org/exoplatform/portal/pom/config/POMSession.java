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

import org.gatein.mop.core.impl.api.POM;
import org.gatein.mop.core.impl.api.POMFormatter;
import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.workspace.WorkspaceObject;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Page;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.content.ContentManager;

import java.util.Iterator;

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
      model = mgr.getPOMService().getModel();
    }
    return model;
  }

  public Workspace getWorkspace() {
    return getModel().getWorkspace();
  }

  public ContentManager getContentManager() {
    return getModel().getContentManager();
  }

  public <O extends WorkspaceObject> Iterator<O> findObject(ObjectType<O> ownerType, String statement) {
    return model.findObject(ownerType, statement);
  }

  public Iterator<Page> findPages(ObjectType<? extends Site> siteType, String ownerId, String title) {
    Workspace workspace = getWorkspace();
    String workspacePath = model.getPath(workspace);
    String statement;
    if (siteType != null) {
      try {
        String pathPrefix;
        if (siteType == ObjectType.PORTAL_SITE) {
          pathPrefix = workspacePath + "/portalsites";
        } else if (siteType == ObjectType.GROUP_SITE) {
          pathPrefix = workspacePath + "/groupsites";
        } else {
          pathPrefix = workspacePath + "/usersites";
        }
        statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + pathPrefix + "/%/root/pages/pages/pages/%'";
      }
      catch (IllegalArgumentException e) {
        statement = "SELECT * FROM mop:page WHERE jcr:path LIKE ''";
      }
    } else {
      if (ownerId != null) {
        statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspacePath + "/%/" + new POMFormatter().encodeNodeName(null, ownerId) + "/root/pages/pages/pages/%'";
      } else {
        if (title != null) {
          throw new UnsupportedOperationException("Julien : todo");
        } else {
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspacePath + "/%/%/root/pages/pages/pages/%'";
        }
      }
    }

    //
    return model.findObject(ObjectType.PAGE, statement);
  }

  public void execute(POMTask task) throws Exception {
    if (isInTask) {
      throw new IllegalStateException();
    }

    //
    try {
      isInTask = true;
      task.run(this);
    } finally {
      isInTask = false;
    }
  }

  public void save() {
    if (model != null) {
      model.save();
    }
  }

  void close() {
    if (model != null) {
      model.close();
    }
  }
}

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

import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.workspace.WorkspaceObject;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.Model;
import org.gatein.mop.core.api.ModelImpl;
import org.gatein.mop.core.api.MOPFormatter;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.pom.config.tasks.Mapper;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class POMSession {

  /** . */
  final POMSessionManager mgr;

  /** . */
  private ModelImpl model;

  /** . */
  private boolean isInTask;

  /** Hack. */
  private final Map<String, PortletPreferences> pendingPrefs = new HashMap<String, PortletPreferences>();

  public POMSession(POMSessionManager mgr) {
    this.mgr = mgr;
    this.isInTask = false;
  }

  private Model getModel() {
    if (model == null) {
      model = mgr.getPOMService().getModel();
    }
    return model;
  }

  public Workspace getWorkspace() {
    return getModel().getWorkspace();
  }

  public <O extends WorkspaceObject> Iterator<O> findObject(ObjectType<O> ownerType, String statement) {
    return model.findObject(ownerType, statement);
  }

  public void addPortletPreferences(PortletPreferences prefs) {
    pendingPrefs.put(prefs.getWindowId(), prefs);
  }

  public PortletPreferences getPortletPreferences(String id) {
    return pendingPrefs.get(id);
  }

  public Set<PortletPreferences> getPortletPreferences(Site site) {
    Set<PortletPreferences> prefs = new HashSet<PortletPreferences>();
    for (Iterator<Map.Entry<String, PortletPreferences>> i = pendingPrefs.entrySet().iterator();i.hasNext();) {
      Map.Entry<String, PortletPreferences> entry = i.next();
      String prefix = Mapper.getOwnerType(site.getObjectType()) + "#" + site.getName() + ":/";
      if (entry.getKey().startsWith(prefix)) {
        prefs.add(entry.getValue());
        i.remove();
      }
    }
    return prefs;
  }

  public <O extends WorkspaceObject> Iterator<O> findObjects(
    ObjectType<O> type,
    ObjectType<? extends Site> siteType,
    String ownerId,
    String title) {

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
        if (type == ObjectType.PAGE) {
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + pathPrefix + "/%/rootpage/children/pages/children/%'";
        } else {
          statement = "SELECT * FROM mop:navigation WHERE jcr:path LIKE '" + pathPrefix + "/%/rootnavigation/children/default'";
        }
      }
      catch (IllegalArgumentException e) {
        if (type == ObjectType.PAGE) {
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE ''";
        } else {
          statement = "SELECT * FROM mop:navigation WHERE jcr:path LIKE ''";
        }
      }
    } else {
      if (ownerId != null) {
        if (type == ObjectType.PAGE) {
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspacePath + "/%/" + new MOPFormatter().encodeNodeName(null, ownerId) + "/rootpage/chldren/pages/children/%'";
        } else {
          statement = "SELECT * FROM mop:navigation WHERE jcr:path LIKE '" + workspacePath + "/%/" + new MOPFormatter().encodeNodeName(null, ownerId) + "/rootnavigation/children/default'";
        }
      } else {
        if (title != null) {
          if (type == ObjectType.PAGE) {
            statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspacePath + "/%/%/rootpage/children/pages/children/%' AND mop:title='" + title + "'";
          } else {
            throw new UnsupportedOperationException();
          }
        } else {
          if (type == ObjectType.PAGE) {
            statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspacePath + "/%/%/rootpage/children/pages/children/%'";
          } else {
            statement = "SELECT * FROM mop:navigation WHERE jcr:path LIKE '" + workspacePath + "/%/%/rootnavigation/children/default'";
          }
        }
      }
    }

    //
    return model.findObject(type, statement);
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

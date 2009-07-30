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

import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.Portal;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.config.model.PortalConfig;

import static org.exoplatform.portal.pom.config.Utils.join;
import static org.exoplatform.portal.pom.config.Utils.split;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PortalConfigTask extends AbstractPOMTask {

  protected static void update(PortalConfig config, Portal portal)  {
    Attributes attrs = portal.getAttributes();
    attrs.setString("locale", config.getLocale());
    attrs.setString("access-permissions", join(config.getAccessPermissions()));
  }

  public static class Remove extends PortalConfigTask {

    /** . */
    private final String name;

    public Remove(String name) {
      this.name = name;
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Portal portal = workspace.getSite(ObjectType.PORTAL, name);
      if (portal == null) {
        throw new NullPointerException("Could not remove non existing portal " + name);
      } else {
        portal.destroy();
      }
      session.save();
    }
  }

  public static class Persist extends PortalConfigTask {

    /** . */
    private final PortalConfig config;

    /** . */
    private boolean create;

    public Persist(PortalConfig config, boolean create) {
      this.config = config;
      this.create = create;
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Portal portal;
      if (create) {
        portal = workspace.createSite(ObjectType.PORTAL, config.getName());
      } else {
        portal = workspace.getSite(ObjectType.PORTAL, config.getName());;
      }
      update(config, portal);
      session.save();
    }
  }

  public static class Get extends PortalConfigTask {

    /** . */
    protected final String name;

    /** . */
    private PortalConfig config;

    public Get(String name) {
      this.name = name;
    }

    public PortalConfig getConfig() {
      return config;
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Portal portal = workspace.getSite(ObjectType.PORTAL, name);
      if (portal != null) {
        Attributes attrs = portal.getAttributes();
        PortalConfig config = new PortalConfig();
        config.setName(portal.getName());
        config.setLocale(attrs.getString("locale"));
        config.setAccessPermissions(split(attrs.getString("access-permissions")));

        //
        this.config = config;
      }
    }
  }

}

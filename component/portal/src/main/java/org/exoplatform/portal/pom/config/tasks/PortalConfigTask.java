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
package org.exoplatform.portal.pom.config.tasks;

import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.config.model.PortalConfig;

import static org.exoplatform.portal.pom.config.Utils.join;
import static org.exoplatform.portal.pom.config.Utils.split;
import static org.exoplatform.portal.pom.config.Utils.parseSiteType;
import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PortalConfigTask extends AbstractPOMTask {

  protected static void update(PortalConfig config, Site site)  {
    Attributes attrs = site.getAttributes();
    attrs.setString("locale", config.getLocale());
    attrs.setString("access-permissions", join("|", config.getAccessPermissions()));
  }

  /** . */
  protected final String name;

  /** . */
  protected final ObjectType<? extends Site> type;

  protected PortalConfigTask(String type, String name) {
    this.type = parseSiteType(type);
    this.name = name;
  }

  public static class Remove extends PortalConfigTask {

    public Remove(String type, String name) {
      super(type, name);
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(type, name);
      if (site == null) {
        throw new NullPointerException("Could not remove non existing portal " + name);
      } else {
        site.destroy();
      }
    }
  }

  public static class Save extends PortalConfigTask {

    /** . */
    private final PortalConfig config;

    /** . */
    private boolean overwrite;



    public Save(PortalConfig config, boolean overwrite) {
      super(config.getType(), config.getName());

      //
      this.config = config;
      this.overwrite = overwrite;
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(type, name);
      if (site != null) {
        if (!overwrite) {
          throw new IllegalArgumentException("Cannot create portal " + config.getName() + " that already exist");
        }
      } else {
        site = workspace.addSite(type, config.getName());
      }
      update(config, site);
    }
  }

  public static class Load extends PortalConfigTask {

    /** . */
    private PortalConfig config;

    public Load(String type, String name) {
      super(type, name);
    }

    public PortalConfig getConfig() {
      return config;
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(type, name);
      if (site != null) {
        Attributes attrs = site.getAttributes();
        PortalConfig config = new PortalConfig();
        config.setName(site.getName());
        config.setLocale(attrs.getString("locale"));
        config.setAccessPermissions(split("|", attrs.getString("access-permissions")));

        //
        this.config = config;
      }
    }
  }
}

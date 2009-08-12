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

import org.exoplatform.portal.config.model.PortalConfig;

import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.workspace.Page;
import org.gatein.mop.api.Attributes;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PortalConfigTask extends AbstractPOMTask {

  /** . */
  protected final String name;

  /** . */
  protected final ObjectType<? extends Site> type;

  protected PortalConfigTask(String type, String name) {
    this.type = Mapper.parseSiteType(type);
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
        Page root = site.getRootPage();
        root.addChild("pages");
        root.addChild("templates");
      }
      new Mapper(session.getContentManager()).save(config, site);
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

        //
        new Mapper(session.getContentManager()).load(site, config);

        //
        this.config = config;
      }
    }
  }
}

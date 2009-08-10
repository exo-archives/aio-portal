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
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.config.model.Page;
import static org.exoplatform.portal.pom.config.Utils.split;
import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PageTask extends AbstractPOMTask {

  /** . */
  protected final String pageId;

  /** . */
  protected final String ownerType;

  /** . */
  protected final String ownerId;

  /** . */
  protected final String name;

  /** . */
  protected final ObjectType<? extends Site> siteType;

  protected PageTask(String pageId) {
    String[] chunks = split("::", pageId);

    //
    if (chunks.length != 3) {
      throw new IllegalArgumentException("Wrong pageId format should be ownerType::ownerId:name was " + pageId);
    }

    //
    String ownerType = chunks[0];
    String ownerId = chunks[1];
    String name = chunks[2];

    //
    this.pageId = pageId;
    this.ownerType = ownerType;
    this.ownerId = ownerId;
    this.name = name;
    this.siteType = Mapper.parseSiteType(ownerType);
  }

  public static class Remove extends PageTask {

    public Remove(Page page) {
      super(page.getPageId());
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Could not remove page " + name + "of non existing site of type " + ownerType + " with id " + ownerId);
      } else {
        org.exoplatform.portal.model.api.workspace.Page page = site.getRootPage().getChild(name);
        if (page == null) {
          throw new IllegalArgumentException("Could not remove non existing page " + name + "of site of type " + ownerType + " with id " + ownerId);
        }
        page.destroy();
      }
    }
  }

  public static class Save extends PageTask {

    /** . */
    private final Page page;

    public Save(Page page) {
      super(page.getPageId());

      //
      this.page = page;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot insert page " + pageId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      org.exoplatform.portal.model.api.workspace.Page page = site.getRootPage().getChild(name);

      //
      if (page == null) {
        page = site.getRootPage().addChild(name);
      }

      //
      Mapper mapper = new Mapper(session.getContentManager());
      mapper.save(this.page, page);
    }
  }

  public static class Load extends PageTask {

    /** . */
    private Page page;

    public Load(String pageId) {
      super(pageId);
    }

    public Page getPage() {
      return page;
    }

    public void run(POMSession session) {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site != null) {
        org.exoplatform.portal.model.api.workspace.Page root = site.getRootPage();
        org.exoplatform.portal.model.api.workspace.Page page = root.getChild(name);
        if (page != null) {
          Mapper mapper = new Mapper(session.getContentManager());
          this.page = mapper.load(page);
        }
      }
    }
  }
}

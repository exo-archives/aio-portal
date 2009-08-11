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

import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;
import static org.exoplatform.portal.pom.config.Utils.split;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.Navigation;
import org.exoplatform.portal.config.model.PageNavigation;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PageNavigationTask extends AbstractPOMTask {

  /** . */
  protected final String owner;

  /** . */
  protected final String ownerType;

  /** . */
  protected final String ownerId;

  /** . */
  protected final ObjectType<? extends Site> siteType;

  protected PageNavigationTask(String owner) {
    String[] chunks = split("::", owner);
    if (chunks.length != 2) {
      throw new IllegalArgumentException("Wrong owner format should be ownerType::ownerId was " + owner);
    }

    //
    this.ownerType = chunks[0];
    this.ownerId = chunks[1];
    this.siteType = Mapper.parseSiteType(ownerType);
    this.owner = owner;
  }

  public static class Load extends PageNavigationTask {

    /** . */
    private PageNavigation pageNav;

    public Load(String owner) {
      super(owner);
    }

    public PageNavigation getPageNavigation() {
      return pageNav;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site != null) {
        Navigation nav = site.getRootNavigation();
        pageNav = new PageNavigation();
        new Mapper(session.getContentManager()).load(nav, this.pageNav);
      } else {
        System.out.println("Cannot load page navigation " + owner +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }
    }
  }

  public static class Save extends PageNavigationTask {

    /** . */
    private final PageNavigation pageNav;

    /** . */
    private final boolean overwrite;

    public Save(PageNavigation pageNav, boolean overwrite) {
      super(pageNav.getOwner());

      //
      this.pageNav = pageNav;
      this.overwrite = overwrite;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot insert page navigation " + owner +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      // Delete node descendants first
      Navigation nav = site.getRootNavigation();
      nav.getChildren().clear();

      //
      new Mapper(session.getContentManager()).save(pageNav, nav);
    }

  }

  public static class Remove extends PageNavigationTask {

    public Remove(PageNavigation pageNav) {
      super(pageNav.getOwner());
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot insert page navigation " + owner +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      // Delete descendants
      Navigation nav = site.getRootNavigation();
      nav.getChildren().clear();
    }
  }
}
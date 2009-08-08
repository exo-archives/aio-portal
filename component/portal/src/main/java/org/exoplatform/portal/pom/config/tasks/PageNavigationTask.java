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
import static org.exoplatform.portal.pom.config.Utils.parseSiteType;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.Navigation;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;

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
    this.siteType = parseSiteType(ownerType);
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
      if (site == null) {
        throw new IllegalArgumentException("Cannot load page navigation " + owner +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Navigation nav = site.getRootNavigation();

      //
      pageNav = loadPageNavigation(nav);
    }

    private PageNavigation loadPageNavigation(Navigation nav) {
      PageNavigation pageNav = new PageNavigation();
      pageNav.setOwnerId(ownerId);
      pageNav.setOwnerType(ownerType);
      Attributes attrs = nav.getAttributes();
      pageNav.setCreator(attrs.getString("creator"));
      pageNav.setModifier(attrs.getString("modifier"));
      pageNav.setDescription(attrs.getString("description"));
      Integer priority = attrs.getInteger("priority");
      if (priority != null) {
        pageNav.setPriority(priority);
      }
      for (Navigation childNav : nav.getChildren()) {
        PageNode node = loadPageNode(childNav);
        pageNav.addNode(node);
      }
      return pageNav;
    }

    private PageNode loadPageNode(Navigation nav) {
      PageNode node = new PageNode();
      Attributes attrs = nav.getAttributes();
      node.setLabel(attrs.getString("label"));
      node.setIcon(attrs.getString("icon"));
      node.setUri(attrs.getString("uri"));
      node.setStartPublicationDate(attrs.getDate("start-publication-date"));
      node.setEndPublicationDate(attrs.getDate("end-publication-date"));
      node.setShowPublicationDate(attrs.getBoolean("show-publication-date"));
      node.setVisible(attrs.getBoolean("visible"));
      node.setPageReference(attrs.getString("page-reference"));
      for (Navigation childNav : nav.getChildren()) {
        PageNode childNode = loadPageNode(childNav);
        pageNav.addNode(childNode);
      }
      return node;
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
      save(pageNav, nav);
    }

    private void save(PageNavigation pageNav, Navigation nav) {
      Attributes attrs = nav.getAttributes();
      attrs.setInteger("priority", pageNav.getPriority());
      attrs.setString("creator", pageNav.getCreator());
      attrs.setString("modifier", pageNav.getModifier());
      attrs.setString("description", pageNav.getDescription());
      for (PageNode node : pageNav.getNodes()) {
        Navigation childNav = nav.addChild(node.getName());
        save(node, childNav);
      }
    }

    private void save(PageNode node, Navigation nav) {
      Attributes attrs = nav.getAttributes();
      attrs.setString("label", node.getLabel());
      attrs.setString("icon", node.getIcon());
      attrs.setString("uri", node.getUri());
      attrs.setDate("start-publication-date", node.getStartPublicationDate());
      attrs.setDate("end-publication-date", node.getEndPublicationDate());
      attrs.setBoolean("show-publication-date", node.isShowPublicationDate());
      attrs.setBoolean("visible", node.isVisible());
      attrs.setString("page-reference", node.getPageReference());
      if (node.getChildren() != null) {
        for (PageNode childNode : node.getChildren()) {
          Navigation childNav = nav.addChild(node.getName());
          save(childNode, childNav);
        }
      }
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

      // Delete node descendants
      Navigation nav = site.getRootNavigation();
      nav.getChildren().clear();
    }
  }
}
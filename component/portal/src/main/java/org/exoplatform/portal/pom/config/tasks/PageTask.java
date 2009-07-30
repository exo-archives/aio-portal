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
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PortalConfig;
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
    String[] strings = split(pageId);

    //
    String ownerType = strings[0];
    String ownerId = strings[1];
    String name = strings[2];

    //
    ObjectType<? extends Site> siteType;
    if (ownerType.equals(PortalConfig.PORTAL_TYPE)) {
      siteType = ObjectType.PORTAL;
    } else if (ownerType.equals(PortalConfig.GROUP_TYPE)) {
      siteType = ObjectType.GROUP;
    } else if (ownerType.equals(PortalConfig.USER_TYPE)) {
      siteType = ObjectType.USER;
    } else {
      throw new IllegalArgumentException("Invalid owner type " + ownerType);
    }

    //
    this.pageId = pageId;
    this.ownerType = ownerType;
    this.ownerId = ownerId;
    this.name = name;
    this.siteType = siteType;
  }

  public static class Get extends PageTask {

    /** . */
    private Page page;

    public Get(String pageId) {
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
          Attributes attrs = page.getAttributes();
          Page bilto = new Page();
          bilto.setId(pageId);
          bilto.setOwnerId(ownerId);
          bilto.setOwnerType(ownerType);
          bilto.setName(name);
          bilto.setTitle(attrs.getString("title"));
          bilto.setShowMaxWindow(attrs.getBoolean("show-max-window"));
          bilto.setCreator(attrs.getString("creator"));
          bilto.setModifier(attrs.getString("modifier"));
          bilto.setAccessPermissions(split(attrs.getString("access-permissions")));
          bilto.setEditPermission(attrs.getString("edit-permission"));

          // Need to do components

          //
          this.page = bilto;
        }
      }
    }

  }

}

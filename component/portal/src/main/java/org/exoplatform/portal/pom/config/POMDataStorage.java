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

import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.config.Query;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.commons.utils.LazyPageList;

import javax.jcr.RepositoryException;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import static org.exoplatform.portal.pom.config.Utils.join;
import static org.exoplatform.portal.pom.config.Utils.split;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class POMDataStorage implements DataStorage {

  /** . */
  private final POMSessionManager pomMgr;

  public POMDataStorage(POMSessionManager pomMgr) {
    this.pomMgr = pomMgr;
  }

  private <T extends AbstractPOMTask> T execute(T task) throws RepositoryException {
    pomMgr.execute(task);
    return task;
  }

  public PortalConfig getPortalConfig(final String portalName) throws Exception {
    return execute(new PortalConfigTask.Get(portalName)).getConfig();
  }

  public void create(final PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Persist(config, true));
  }

  public void save(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Persist(config, false));
  }

  public void remove(final PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Remove(config.getName()));
  }

  private String[] getNames(String pageId) {
    int index1 = pageId.indexOf("::");
    if (index1 == -1) {
      throw new IllegalArgumentException("Invalid page id: [" + pageId + "]");
    }
    int index2 = pageId.indexOf("::", index1 + 2);
    if (index2 == -1) {
      throw new IllegalArgumentException("Invalid page id: [" + pageId + "]");
    }
    return new String[]{
      pageId.substring(0, index1),
      pageId.substring(index1 + 2, index2),
      pageId.substring(index2 + 2)
    };
  }

  public Page getPage(final String pageId) throws Exception {

    final String[] fragments = getNames(pageId);
    final String ownerType = fragments[0];
    final String ownerId = fragments[1];
    final String name = fragments[2];

    final ObjectType<? extends Site> siteType;
    if (ownerType.equals(PortalConfig.PORTAL_TYPE)) {
      siteType = ObjectType.PORTAL;
    } else if (ownerType.equals(PortalConfig.GROUP_TYPE)) {
      siteType = ObjectType.GROUP;
    } else if (ownerType.equals(PortalConfig.USER_TYPE)) {
      siteType = ObjectType.USER;
    } else {
      throw new IllegalArgumentException("Invalid owner type " + ownerType);
    }

    final AtomicReference<Page> ref = new AtomicReference<Page>();

    pomMgr.execute(new POMTask() {
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
          }
        }
      }
    });

    return ref.get();
  }

  public void remove(Page page) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void create(Page page) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void save(Page page) throws Exception {
    throw new UnsupportedOperationException();
  }

  public PageNavigation getPageNavigation(String fullId) throws Exception {
    throw new UnsupportedOperationException();
  }

  public PageNavigation getPageNavigation(String ownerType, String id) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void save(PageNavigation navigation) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void create(PageNavigation navigation) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void remove(PageNavigation navigation) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void save(PortletPreferences portletPreferences) throws Exception {
    throw new UnsupportedOperationException();
  }

  public PortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void remove(PortletPreferences portletPreferences) throws Exception {
    throw new UnsupportedOperationException();
  }

  public LazyPageList find(Query<?> q) throws Exception {
    throw new UnsupportedOperationException();
  }

  public LazyPageList find(Query<?> q, Comparator<?> sortComparator) throws Exception {
    throw new UnsupportedOperationException();
  }
}

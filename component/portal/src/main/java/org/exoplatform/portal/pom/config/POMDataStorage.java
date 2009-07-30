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
import org.exoplatform.portal.model.api.workspace.Portal;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.commons.utils.LazyPageList;

import javax.jcr.RepositoryException;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

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

  public PortalConfig getPortalConfig(final String portalName) throws Exception {

    final AtomicReference<PortalConfig> ref = new AtomicReference<PortalConfig>();

    pomMgr.execute(new POMTask() {
      public void run(POMSession session) {
        Workspace workspace = session.getWorkspace();
        Portal portal = workspace.getSite(ObjectType.PORTAL, portalName);
        if (portal != null) {
          Attributes attrs = portal.getAttributes();
          PortalConfig config = new PortalConfig();
          config.setName(portal.getName());
          config.setLocale(attrs.getString("locale"));
          config.setAccessPermissions(split(attrs.getString("accessPermissions")));
          ref.set(config);
        }
      }
    });

    return ref.get();
  }

  private static String join(String... strings) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0;i < strings.length;i++) {
      Object o = strings[i];
      if (i > 0) {
        sb.append('|');
      }
      sb.append(o);
    }
    return sb.toString();
  }

  public String[] split(String s) {
    return s.split("|");
  }

  public void create(final PortalConfig config) throws Exception {

    pomMgr.execute(new POMTask() {
      public void run(POMSession session) {
        Workspace workspace = session.getWorkspace();
        Portal portal = workspace.createSite(config.getName(), ObjectType.PORTAL);
        Attributes attrs = portal.getAttributes();
        attrs.setString("locale", config.getLocale());
        attrs.setString("accessPermissions", join(config.getAccessPermissions()));
        session.save();
      }
    });

  }

  public void save(PortalConfig config) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void remove(PortalConfig config) throws Exception {
    throw new UnsupportedOperationException();
  }

  public Page getPage(String pageId) throws Exception {
    throw new UnsupportedOperationException();
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

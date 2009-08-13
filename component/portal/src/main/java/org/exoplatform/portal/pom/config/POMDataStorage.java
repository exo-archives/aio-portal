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
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.commons.utils.LazyPageList;
import org.exoplatform.commons.utils.ListAccess;

import java.util.Comparator;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import org.exoplatform.portal.pom.config.tasks.PageTask;
import org.exoplatform.portal.pom.config.tasks.PortalConfigTask;
import org.exoplatform.portal.pom.config.tasks.PageNavigationTask;
import org.exoplatform.portal.pom.config.tasks.PortletPreferencesTask;
import org.exoplatform.portal.pom.config.tasks.Mapper;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Portal;
import org.gatein.mop.core.impl.api.POMFormatter;
import org.chromattic.api.ChromatticSession;
import org.chromattic.api.query.QueryLanguage;

import javax.jcr.Node;

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

  private <T extends AbstractPOMTask> T execute(T task) throws Exception {
    pomMgr.execute(task);
    return task;
  }

  public POMSessionManager getPOMSessionManager() {
    return pomMgr;
  }

  public PortalConfig getPortalConfig(String portalName) throws Exception {
    return execute(new PortalConfigTask.Load(PortalConfig.PORTAL_TYPE, portalName)).getConfig();
  }

  public PortalConfig getPortalConfig(String ownerType, String portalName) throws Exception {
    return execute(new PortalConfigTask.Load(ownerType, portalName)).getConfig();
  }

  public void create(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Save(config, false));
  }

  public void save(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Save(config, true));
  }

  public void remove(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Remove(config.getType(), config.getName()));
  }

  public Page getPage(String pageId) throws Exception {
    return execute(new PageTask.Load(pageId)).getPage();
  }

  public void remove(Page page) throws Exception {
    execute(new PageTask.Remove(page));
  }

  public void create(Page page) throws Exception {
    execute(new PageTask.Save(page));
  }

  public void save(Page page) throws Exception {
    execute(new PageTask.Save(page));
  }

  public PageNavigation getPageNavigation(String fullId) throws Exception {
    return execute(new PageNavigationTask.Load(fullId)).getPageNavigation();
  }

  public PageNavigation getPageNavigation(String ownerType, String id) throws Exception {
    return execute(new PageNavigationTask.Load(ownerType + "::" + id)).getPageNavigation();
  }

  public void save(PageNavigation navigation) throws Exception {
    execute(new PageNavigationTask.Save(navigation, true));
  }

  public void create(PageNavigation navigation) throws Exception {
    execute(new PageNavigationTask.Save(navigation, false));
  }

  public void remove(PageNavigation navigation) throws Exception {
    execute(new PageNavigationTask.Remove(navigation));
  }

  public void save(PortletPreferences portletPreferences) throws Exception {
    execute(new PortletPreferencesTask.Save(portletPreferences));
  }

  public PortletPreferences getPortletPreferences(WindowID windowID) throws Exception {
    return execute(new PortletPreferencesTask.Load(windowID)).getPreferences();
  }

  public void remove(PortletPreferences portletPreferences) throws Exception {
    execute(new PortletPreferencesTask.Remove(portletPreferences));
  }

  public LazyPageList find(Query<?> q) throws Exception {
    return find(q, null);
  }

/*
new Query<Page>(PortalConfig.GROUP_TYPE, groupId,  Page.class);
new Query<Page>(PortalConfig.GROUP_TYPE, groupId,  Page.class);
new Query<Page>(PortalConfig.USER_TYPE, userName, Page.class);
new Query<Page>(PortalConfig.USER_TYPE, userName, Page.class);
new Query<Page>(PortalConfig.PORTAL_TYPE, portalName, null, null, Page.class);
new Query<Page>(null, null, null, null, Page.class);

new Query<PortletPreferences>(PortalConfig.GROUP_TYPE, groupId, PortletPreferences.class);
new Query<PortletPreferences>(PortalConfig.GROUP_TYPE, groupId, PortletPreferences.class);
new Query<PortletPreferences>(PortalConfig.USER_TYPE, userName, PortletPreferences.class);
new Query<PortletPreferences>(PortalConfig.PORTAL_TYPE, portalName, null, null, PortletPreferences.class);

new Query<PageNavigation>(PortalConfig.GROUP_TYPE, null, PageNavigation.class);
new Query<PageNavigation>(PortalConfig.GROUP_TYPE, null, PageNavigation.class);
new Query<PageNavigation>(PortalConfig.GROUP_TYPE, null, PageNavigation.class);

new Query<PortalConfig>(null, null, null, null, PortalConfig.class);
new Query<PortalConfig>(null, null, null, null, PortalConfig.class);
new Query<PortalConfig>(null, null, null, null, PortalConfig.class);
new Query<PortalConfig>(null, null, null, null, PortalConfig.class);
*/

  public LazyPageList find(Query<?> q, Comparator<?> sortComparator) throws Exception {

    if (PortalConfig.class.equals(q.getClassType())) {
      final POMSession session = pomMgr.getSession();
      Workspace workspace = session.getWorkspace();
      final Collection<? extends Portal> portals = workspace.getSites(ObjectType.PORTAL);

      ListAccess<PortalConfig> la = new ListAccess<PortalConfig>() {
        public PortalConfig[] load(int index, int length) throws Exception, IllegalArgumentException {
          Iterator<? extends Portal> iterator = portals.iterator();
          Mapper mapper = new Mapper(session.getContentManager());
          PortalConfig[] result = new PortalConfig[length];
          for (int i = 0;i < length;i++) {
            PortalConfig config = new PortalConfig();
            mapper.load(iterator.next(), config);
            result[i] = config;
          }
          return result;
        }
        public int getSize() throws Exception {
          return portals.size();
        }
      };
      return new LazyPageList<PortalConfig>(la, 10);
    } else if (Page.class.equals(q.getClassType())) {
      final POMSession session = pomMgr.getSession();

      // That code should be moved to the MOP project somehow
      ChromatticSession tmp = session.getChromatticSession();
      Workspace workspace = session.getWorkspace();
      String id = tmp.getId(workspace);
      Node workspaceNode = tmp.getJCRSession().getNodeByUUID(id);
      String statement;
      String ownerType = q.getOwnerType();
      if (ownerType != null) {
        try {
          ObjectType<? extends Site> siteType = Mapper.parseSiteType(ownerType);
          Node containerNode;
          if (siteType == ObjectType.PORTAL) {
            containerNode = workspaceNode.getNode("portals");
          } else if (siteType == ObjectType.GROUP) {
            containerNode = workspaceNode.getNode("groups");
          } else {
            containerNode = workspaceNode.getNode("users");
          }
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + containerNode.getPath() + "/%/root/pages/pages/pages/%'";
        }
        catch (IllegalArgumentException e) {
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE ''";
        }
      } else {
        String ownerId = q.getOwnerId();
        if (ownerId != null) {
          statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspaceNode.getPath() + "/%/" + new POMFormatter().encodeNodeName(null, ownerId) + "/root/pages/pages/pages/%'";
        } else {
          String title = q.getTitle();
          if (title != null) {
            throw new UnsupportedOperationException("Julien : todo");
          } else {
            statement = "SELECT * FROM mop:page WHERE jcr:path LIKE '" + workspaceNode.getPath() + "/%/%/root/pages/pages/pages/%'";
          }
        }
      }

      //
      org.chromattic.api.query.Query query = tmp.createQuery(QueryLanguage.SQL, statement);
      Iterator<org.gatein.mop.api.workspace.Page> ite = query.execute(org.gatein.mop.api.workspace.Page.class);
      final ArrayList<org.gatein.mop.api.workspace.Page> array = new ArrayList<org.gatein.mop.api.workspace.Page>();
      while (ite.hasNext()) {
        array.add(ite.next());
      }

      //
      final Iterator<? extends org.gatein.mop.api.workspace.Page> it = array.iterator();
      ListAccess<Page> la = new ListAccess<Page>() {
        public Page[] load(int index, int length) throws Exception, IllegalArgumentException {
          Mapper mapper = new Mapper(session.getContentManager());
          Page[] result = new Page[length];
          for (int i = 0;i < length;i++) {
            Page page = new Page();
            mapper.load(it.next(), page);
            result[i] = page;
          }
          return result;
        }
        public int getSize() throws Exception {
          return array.size();
        }
      };

      return new LazyPageList<Page>(la, 10);
    } else if (PortletPreferences.class.equals(q.getClassType())) {

      // We return empty on purpose at it is used when preferences are deleted by the UserPortalConfigService
      // and the prefs are deleted transitively when an entity is removed
      return new LazyPageList<PortletPreferences>(new ListAccess<PortletPreferences>() {
        public PortletPreferences[] load(int index, int length) throws Exception, IllegalArgumentException {
          throw new AssertionError();
        }
        public int getSize() throws Exception {
          return 0;
        }
      }, 10);
    } else {
      throw new UnsupportedOperationException("Cannot query data with type " + q.getClassType());
    }

  }
}

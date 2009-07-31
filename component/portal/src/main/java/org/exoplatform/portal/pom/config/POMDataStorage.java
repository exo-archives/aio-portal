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

import java.util.Comparator;

import org.exoplatform.portal.pom.config.tasks.PageTask;
import org.exoplatform.portal.pom.config.tasks.PortalConfigTask;

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

  public PortalConfig getPortalConfig(String portalName) throws Exception {
    return execute(new PortalConfigTask.Get(portalName)).getConfig();
  }

  public void create(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Persist(config, true));
  }

  public void save(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Persist(config, false));
  }

  public void remove(PortalConfig config) throws Exception {
    execute(new PortalConfigTask.Remove(config.getName()));
  }

  public Page getPage(String pageId) throws Exception {
    return execute(new PageTask.Get(pageId)).getPage();
  }

  public void remove(Page page) throws Exception {
    throw new UnsupportedOperationException();
  }

  public void create(Page page) throws Exception {
    execute(new PageTask.Create(page));
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

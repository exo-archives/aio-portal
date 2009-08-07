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

import org.exoplatform.portal.model.impl.api.POMService;
import org.exoplatform.portal.model.portlet.Preferences;
import org.exoplatform.portal.model.spi.content.ContentProvider;
import org.exoplatform.portal.model.spi.content.GetState;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.core.nodetype.ExtendedNodeTypeManager;
import org.chromattic.api.ChromatticBuilder;
import org.chromattic.apt.InstrumentorImpl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Repository;
import javax.jcr.Credentials;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class POMSessionManager {

  /** . */
  private static final ThreadLocal<POMSession> current = new ThreadLocal<POMSession>();

  /** . */
  final RepositoryService repositoryService;

  /** . */
  final POMService pomService;

  /** . */
  final String repositoryName = "repository";

  /** . */
  final String workspaceName = "pom";

  public POMSessionManager(RepositoryService repositoryService) throws Exception {
    POMService pomService = new POMService();
    pomService.setOption(ChromatticBuilder.SESSION_PROVIDER_CLASSNAME, PortalSessionLifeCycle.class.getName());
    pomService.setOption(ChromatticBuilder.INSTRUMENTOR_CLASSNAME, InstrumentorImpl.class.getName());
    pomService.start();

    // Register no op PC for now
    pomService.getContentManagerRegistry().register(Preferences.CONTENT_TYPE, new ContentProvider() {
      public GetState getState(String contentId) {
        return null;
      }
      public Object combine(List states) {
        throw new UnsupportedOperationException();
      }
    });

    //
    ExtendedNodeTypeManager nodeTypeMgr = repositoryService.getCurrentRepository().getNodeTypeManager();
    try {
      nodeTypeMgr.getNodeType("exo:workspace");
    }
    catch (NoSuchNodeTypeException e) {
      InputStream in = POMService.class.getClassLoader().getResourceAsStream("conf/standalone/nodetypes.xml");
      nodeTypeMgr.registerNodeTypes(in, ExtendedNodeTypeManager.IGNORE_IF_EXISTS);
    }

    //
    this.repositoryService = repositoryService;
    this.pomService = pomService;
  }

  public Session login() throws RepositoryException {
    ManageableRepository repo = repositoryService.getCurrentRepository();
    return repo.login();
  }

  public Session login(String workspace) throws RepositoryException {
    Repository repo = repositoryService.getCurrentRepository();
    return repo.login(workspace);
  }

  public Session login(Credentials credentials, String workspace) throws RepositoryException {
    Repository repo = repositoryService.getCurrentRepository();
    return repo.login(credentials, workspace);
  }

  public Session login(Credentials credentials) throws RepositoryException {
    Repository repo = repositoryService.getCurrentRepository();
    return repo.login(credentials);
  }

  public static POMSession getSession() {
    return current.get();
  }

  public POMSession openSession() {
    POMSession session = current.get();
    if (session == null) {
      session = new POMSession(this);
      current.set(session);
    }
    return session;
  }

  public boolean closeSession() {
    POMSession session = current.get();
    if (session == null) {
      // Should warn
      return false;
    } else {
      current.set(null);
      session.close();
      return true;
    }
  }

  public void execute(POMTask task) throws Exception {
    POMSession session = openSession();
    session.execute(task);
  }


}

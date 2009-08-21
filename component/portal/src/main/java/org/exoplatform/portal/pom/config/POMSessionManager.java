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

import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.registry.RegistryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.chromattic.api.ChromatticBuilder;
import org.chromattic.apt.InstrumentorImpl;
import org.gatein.mop.core.api.MOPService;
import org.gatein.mop.core.content.portlet.Preferences;
import org.gatein.mop.spi.content.ContentProvider;
import org.gatein.mop.spi.content.GetState;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Repository;
import javax.jcr.Credentials;
import java.util.List;
import java.lang.reflect.UndeclaredThrowableException;

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
  private MOPService pomService;

  /** . */
  final String repositoryName = "repository";

  /** . */
  final String workspaceName = "portal-system";

  public POMSessionManager(RegistryService service) throws Exception {
    RepositoryService repositoryService = service.getRepositoryService();
    
    //
    this.repositoryService = repositoryService;
    this.pomService = null;
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

  /*
   * todo : use better than the synchronized block  
   */
  public synchronized MOPService getPOMService() {
    if (pomService == null) {
      try {
        MOPService pomService = new MOPService();
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
/*
        ExtendedNodeTypeManager nodeTypeMgr = repositoryService.getCurrentRepository().getNodeTypeManager();
        try {
          nodeTypeMgr.getNodeType("exo:workspace");
        }
        catch (NoSuchNodeTypeException e) {
          InputStream in = POMService.class.getClassLoader().getResourceAsStream("conf/standalone/nodetypes.xml");
          nodeTypeMgr.registerNodeTypes(in, ExtendedNodeTypeManager.IGNORE_IF_EXISTS);
        }
*/

        //
        this.pomService = pomService;
      }
      catch (Exception e) {
        throw new UndeclaredThrowableException(e);
      }
    }
    return pomService;
  }

  /**
   * <p>Returns the session currently associated with the current thread of execution.</p>
   *
   * @return the current session
   */
  public static POMSession getSession() {
    return current.get();
  }

  /**
   * <p>Open and returns a session to the model. When the current thread is already associated with a previously
   * opened session the method will throw an <tt>IllegalStateException</tt>.</p>
   *
   * @return a session to the model.
   */
  public POMSession openSession() {
    POMSession session = current.get();
    if (session == null) {
      session = new POMSession(this);
      current.set(session);
    } else {
      throw new IllegalStateException("A session is already opened.");
    }
    return session;
  }

  /**
   * <p>Closes the current session and discard the changes done during the session.</p>
   *
   * @return a boolean indicating if the session was closed
   * @see #closeSession(boolean)
   */
  public boolean closeSession() {
    return closeSession(false);
  }

  /**
   * <p>Closes the current session and optionally saves its content. If no session is associated
   * then this method has no effects and returns false.</p>
   *
   * @param save if the session must be saved
   * @return a boolean indicating if the session was closed
   */
  public boolean closeSession(boolean save) {
    POMSession session = current.get();
    if (session == null) {
      // Should warn
      return false;
    } else {
      current.set(null);
      try {
        if (save) {
          session.save();
        }
      }
      finally {
        session.close();
      }
      return true;
    }
  }

  /**
   * <p>Execute the task with a session. The method attempts first to get a current session and if no such session
   * is found then a session will be created for the scope of the method.</p>
   *
   * @param task the task to execute
   * @throws Exception any exception thrown by the task
   */
  public void execute(POMTask task) throws Exception {
    POMSession session = getSession();
    if (session == null) {
      session = openSession();
      try {
        session.execute(task);
      } finally {
        closeSession(true);
      }
    } else {
      session.execute(task);
    }
  }
}

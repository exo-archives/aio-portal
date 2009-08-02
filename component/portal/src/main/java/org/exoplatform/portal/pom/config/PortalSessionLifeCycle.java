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

import org.chromattic.spi.jcr.SessionLifeCycle;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;

import javax.jcr.Session;
import javax.jcr.RepositoryException;
import javax.jcr.Credentials;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class PortalSessionLifeCycle implements SessionLifeCycle {

  private static class Entry {

    /** . */
    private final SessionProvider provider;

    /** . */
    private final ManageableRepository repo;

    /** . */
    private int count;

    /** . */
    private final String workspaceName;

    private Entry(POMSession session) throws RepositoryException {
      ManageableRepository repo;
      try {
        repo = session.mgr.repositoryService.getRepository(session.mgr.repositoryName);
      }
      catch (RepositoryConfigurationException e) {
        throw new RepositoryException(e);
      }

      //
      this.provider = SessionProvider.createSystemProvider();
      this.count = 0;
      this.repo = repo;
      this.workspaceName = session.mgr.workspaceName;
    }

    Session openSession() throws RepositoryException {
      Session session = provider.getSession(workspaceName, repo);
      count++;
      return session;
    }
  }

  /** . */
  private ThreadLocal<Entry> current = new ThreadLocal<Entry>();

  public Session login() throws RepositoryException {
    POMSession session = POMSessionManager.getSession();

    //
    Entry entry = current.get();
    if (entry == null) {
      entry = new Entry(session);
      current.set(entry);
    }

    //
    return entry.openSession();
  }

  public Session login(String workspace) throws RepositoryException {
    throw new UnsupportedOperationException();
  }

  public Session login(Credentials credentials, String workspace) throws RepositoryException {
    throw new UnsupportedOperationException();
  }

  public Session login(Credentials credentials) throws RepositoryException {
    throw new UnsupportedOperationException();
  }

  public void save(Session session) throws RepositoryException {
    session.save();
  }

  public void close(Session session) {
    session.logout();
    Entry entry = current.get();
    if (--entry.count == 0) {
      current.remove();
      entry.provider.close();
    }
  }
}

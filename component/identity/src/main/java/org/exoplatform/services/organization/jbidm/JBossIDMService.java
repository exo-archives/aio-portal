package org.exoplatform.services.organization.jbidm;

import org.jboss.identity.idm.api.IdentitySession;
import org.jboss.identity.idm.api.IdentitySessionFactory;

public interface JBossIDMService {

    IdentitySessionFactory getIdentitySessionFactory();

    IdentitySession getIdentitySession() throws Exception;

    IdentitySession getIdentitySession(String realm) throws Exception;
    
}

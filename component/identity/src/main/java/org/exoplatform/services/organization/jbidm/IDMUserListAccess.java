package org.exoplatform.services.organization.jbidm;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.organization.User;

import org.jboss.identity.idm.api.query.UserQuery;
import org.jboss.identity.idm.api.query.UserQueryBuilder;

import java.util.List;


public class IDMUserListAccess
   implements ListAccess<User>
{
   private final UserDAOImpl userDAO;

   private final JBossIDMService idmService;

   private final UserQueryBuilder userQueryBuilder;

   private final int pageSize;

   private final boolean countAll;

   public IDMUserListAccess(UserDAOImpl userDAO,
                            JBossIDMService idmService,
                            UserQueryBuilder userQueryBuilder,
                            int pageSize,
                            boolean countAll)
   {
      this.userDAO = userDAO;
      this.idmService = idmService;
      this.userQueryBuilder = userQueryBuilder;
      this.pageSize = pageSize;
      this.countAll = countAll;
   }

   public User[] load(int index, int length) throws Exception, IllegalArgumentException
   {
      userQueryBuilder.page(index, length);
      UserQuery query = userQueryBuilder.createQuery();
      List<org.jboss.identity.idm.api.User> users = idmService.getIdentitySession().list(query);

      User[] exoUsers = new User[users.size()];

      for (int i = 0; i < users.size(); i++)
      {
         org.jboss.identity.idm.api.User user = users.get(i);

         exoUsers[i] = UserDAOImpl.getPopulatedUser(user.getId(), idmService.getIdentitySession());
      }
      
      return exoUsers;
   }

   public int getSize() throws Exception
   {
      if(countAll)
      {
         return idmService.getIdentitySession().getPersistenceManager().getUserCount();
      }
      else
      {
         userQueryBuilder.page(0, 0);
         UserQuery query = userQueryBuilder.createQuery();
         return idmService.getIdentitySession().execute(query).size();
      }

   }
}

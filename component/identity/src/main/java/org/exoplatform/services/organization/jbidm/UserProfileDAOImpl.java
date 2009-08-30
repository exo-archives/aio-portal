package org.exoplatform.services.organization.jbidm;

import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.services.organization.UserProfileEventListener;
import org.exoplatform.services.organization.UserProfileHandler;
import org.exoplatform.services.organization.impl.UserProfileImpl;

import org.jboss.identity.idm.api.Attribute;
import org.jboss.identity.idm.api.IdentitySession;
import org.jboss.identity.idm.impl.api.SimpleAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserProfileDAOImpl
   implements UserProfileHandler
{

   static private UserProfile NOT_FOUND = new UserProfileImpl();

   private JBossIDMService service_;

   private ExoCache cache_;

   private List<UserProfileEventListener> listeners_;

   private JBossIDMOrganizationServiceImpl orgService;

   public UserProfileDAOImpl(JBossIDMOrganizationServiceImpl orgService, JBossIDMService service, CacheService cservice) throws Exception
   {
      service_ = service;
      cache_ = cservice.getCacheInstance(getClass().getName());
      listeners_ = new ArrayList<UserProfileEventListener>(3);
      this.orgService = orgService;
   }

   public void addUserProfileEventListener(UserProfileEventListener listener)
   {
      listeners_.add(listener);
   }

   final public UserProfile createUserProfileInstance()
   {
      return new UserProfileImpl();
   }

   public UserProfile createUserProfileInstance(String userName)
   {
      return new UserProfileImpl(userName);
   }

//   void createUserProfileEntry(UserProfile up, IdentitySession session) throws Exception
//   {
//      UserProfileData upd = new UserProfileData();
//      upd.setUserProfile(up);
//      session.save(upd);
//      session.flush();
//      cache_.remove(up.getUserName());
//   }

   public void saveUserProfile(UserProfile profile, boolean broadcast) throws Exception
   {

      if (broadcast)
      {
         preSave(profile, true);
      }

      setProfile(profile.getUserName(), profile);

      if (broadcast)
      {
         postSave(profile, true);
      }


      cache_.put(profile.getUserName(), profile);

   }

   public UserProfile removeUserProfile(String userName, boolean broadcast) throws Exception
   {
      UserProfile profile = getProfile(userName);

      if (profile != null)
      {
         try
         {
            if (broadcast)
            {
               preDelete(profile);
            }

            removeProfile(userName, profile);

            if (broadcast)
            {
               postDelete(profile);
            }
            cache_.remove(userName);
            return profile;
         }
         catch (Exception exp)
         {
            return null;
         }
      }
      cache_.remove(userName);
      return null;
   }

   public UserProfile findUserProfileByName(String userName) throws Exception
   {
      
      org.jboss.identity.idm.api.User foundUser = getIdentitySession().getPersistenceManager().findUser(userName);

      if (foundUser == null)
      {
         return null;
      }


      UserProfile up = (UserProfile) cache_.get(userName);
      if (up == null)
      {
         up = getProfile(userName);
      }

      //
      if (up == null)
      {
         up = NOT_FOUND;
      }

      //
      cache_.put(userName, up);

      // Just to avoid to return a shared object between many threads
      // that would not be thread safe nor corrct
      if (up == NOT_FOUND)
      {
         // julien : integration bug fix
         // Return an empty profile to avoid NPE in portal
         // Should clarify what do do (maybe portal should care about returned value)
         return new UserProfileImpl();
      }
      else
      {
         return up;
      }
   }


   public Collection findUserProfiles() throws Exception
   {
      return null; 
   }

   private void preSave(UserProfile profile, boolean isNew) throws Exception
   {
      for (UserProfileEventListener listener : listeners_)
      {
         listener.preSave(profile, isNew);
      }
   }

   private void postSave(UserProfile profile, boolean isNew) throws Exception
   {
      for (UserProfileEventListener listener : listeners_)
      {
         listener.postSave(profile, isNew);
      }
   }

   private void preDelete(UserProfile profile) throws Exception
   {
      for (UserProfileEventListener listener : listeners_)
      {
         listener.preDelete(profile);
      }
   }

   private void postDelete(UserProfile profile) throws Exception
   {
      for (UserProfileEventListener listener : listeners_)
      {
         listener.postDelete(profile);
      }
   }

   public UserProfile getProfile(String userName) throws Exception
   {
      if (getIdentitySession().getPersistenceManager().findUser(userName) == null)
      {
         return null;
      }

      Map<String, Attribute> attrs = getIdentitySession().getAttributesManager().getAttributes(userName);

      if (attrs == null || attrs.isEmpty())
      {
         return null;
      }

      Map<String, String> filteredAttrs = new HashMap<String, String>();

      for (String key : attrs.keySet())
      {
         // Check if attribute is part of User interface data
         if (!UserDAOImpl.USER_NON_PROFILE_KEYS.contains(key))
         {
            filteredAttrs.put(key, attrs.get(key).getValue().toString());
         }

      }

      if (filteredAttrs.isEmpty())
      {
         return null;
      }


      UserProfile profile = new UserProfileImpl(userName, filteredAttrs);

      return profile;

   }

   public void setProfile(String userName, UserProfile profile) throws Exception
   {

      Map<String, String> profileAttrs = profile.getUserInfoMap();

      Set<Attribute> attrs = new HashSet<Attribute>();

      for (Map.Entry<String, String> entry : profileAttrs.entrySet())
      {
         attrs.add(new SimpleAttribute(entry.getKey(), entry.getValue()));
      }

      Attribute[] attrArray = new Attribute[attrs.size()];
      attrArray = attrs.toArray(attrArray);

      getIdentitySession().getAttributesManager().updateAttributes(userName, attrArray);

   }

   public void removeProfile(String userName, UserProfile profile) throws Exception
   {
      Map<String, String> profileAttrs = profile.getUserInfoMap();

      String[] attrKeys = new String[profileAttrs.keySet().size()];

      attrKeys = profileAttrs.keySet().toArray(attrKeys);

      getIdentitySession().getAttributesManager().removeAttributes(userName, attrKeys);
   }

   private IdentitySession getIdentitySession() throws Exception
   {
      return service_.getIdentitySession();
   }
}


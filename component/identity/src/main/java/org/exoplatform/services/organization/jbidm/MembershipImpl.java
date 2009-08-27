package org.exoplatform.services.organization.jbidm;

import org.exoplatform.services.organization.Membership;

public class MembershipImpl implements Membership
{
   private String membershipType = "member";

   private String userName       = null;

   private String groupId        = null;

   public MembershipImpl()
   {
   }

   public MembershipImpl(String id)
   {
      String[] fields = id.split(":");

      // Id can be pure "//" in some cases
      if (fields[0] != null)
      {
         membershipType = fields[0];
      }
      if (fields[1] != null )
      {
         userName = fields[1];
      }
      if (fields[2] != null)
      {
         groupId = fields[2];
      }
   }

   public String getId()
   {
      StringBuffer id = new StringBuffer();


      if (membershipType != null)
      {
         id.append(membershipType);
      }
      id.append(":");
      if (userName != null)
      {
         id.append(userName);
      }
      id.append(":");
      if (groupId != null)
      {
         id.append(groupId);
      }

      return id.toString();
   }





   public String getMembershipType()
   {
      return membershipType;
   }

   public void setMembershipType(String membershipType)
   {
      this.membershipType = membershipType;
   }

   public String getUserName()
   {
      return userName;
   }

   public void setUserName(String userName)
   {
      this.userName = userName;
   }

   public String getGroupId()
   {
      return groupId;
   }

   public void setGroupId(String groupId)
   {
      this.groupId = groupId;
   }
}

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
package org.exoplatform.portal.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.OrganizationService;
/**
 * Jun 27, 2006
 */
public class UserACL {
  public final static String EVERYONE = "Everyone" ;
  protected static Log log = ExoLogger.getLogger("organization:UserACL");
  
  private OrganizationService orgService_ ;
  
  private String superUser_;
  private String guestGroup_ ;
  private List<String> portalCreatorGroups_;
  private List<String> accessControlWorkspaceGroups_;
  private String navigationCreatorMembershipType_;

  public UserACL(InitParams params, OrganizationService orgService) throws Exception {
    this.orgService_ = orgService;
    
    ValueParam superUserParam = params.getValueParam("super.user");
    if(superUserParam != null) superUser_ = superUserParam.getValue();
    if(superUser_ == null || superUser_.trim().length() == 0) superUser_= "root";
    
    String accessControlWorkspace = "";
    ValueParam accessControlWorkspaceParam = params.getValueParam("access.control.workspace");
    if(accessControlWorkspaceParam != null) accessControlWorkspace = accessControlWorkspaceParam.getValue();
    accessControlWorkspaceGroups_ = defragmentPermission(accessControlWorkspace);    
    
    ValueParam guestGroupParam = params.getValueParam("guests.group") ;
    if(guestGroupParam != null) guestGroup_ = guestGroupParam.getValue() ;
    if(guestGroup_ == null || guestGroup_.trim().length() < 1) guestGroup_ = "/platform/guests" ; 
    
    ValueParam navCretorParam = params.getValueParam("navigation.creator.membership.type");
    if(navCretorParam != null) navigationCreatorMembershipType_ = navCretorParam.getValue();
    if(navigationCreatorMembershipType_ == null || 
       navigationCreatorMembershipType_.trim().length() == 0) navigationCreatorMembershipType_= "owner";
    
    String allGroups = "";
    ValueParam portalCretorGroupsParam = params.getValueParam("portal.creator.groups");
    if(portalCretorGroupsParam != null) allGroups = portalCretorGroupsParam.getValue();
    portalCreatorGroups_ = defragmentPermission(allGroups);
  }
  
  private List<String> defragmentPermission(String permission) {
    List<String> result = new ArrayList<String>();
    if(permission !=null ) {
      if(permission.contains(",")){
        String[] groups = permission.split(",");
        for(String group: groups) {  result.add(group.trim());  }
      } else {
        result.add(permission);
      }
    }
    return result;
  }
  
  public String getMakableMT() { return navigationCreatorMembershipType_; }
  public List<String> getPortalCreatorGroups() { return portalCreatorGroups_;  }
  public List<String> getAccessControlWorkspaceGroups() { return accessControlWorkspaceGroups_;  }
  public String getSuperUser() { return superUser_ ; }
  public String getGuestsGroup() { return guestGroup_ ; }
  public boolean hasPermission(PortalConfig pconfig, String remoteUser) throws Exception {
    if( hasEditPermission(pconfig, remoteUser) == true) {
      pconfig.setModifiable(true);
      return true;
    }
    pconfig.setModifiable(false);
    String[] accessPerms = (pconfig.getAccessPermissions());
    for(String per: accessPerms){
      if(hasPermission(remoteUser, per)) return true;
    }
    return false;
  }

  public boolean hasPermission(Page page, String remoteUser) throws Exception {
    if(PortalConfig.USER_TYPE.equals(page.getOwnerType())){
      if( remoteUser.equals(page.getOwnerId())){
        page.setModifiable(true);
        return true;
      } 
      return false;
    }
    if(superUser_.equals(remoteUser)){
      page.setModifiable(true);
      return true;
    }
    if(hasEditPermission(page, remoteUser)){
      page.setModifiable(true);
      return true;
    }
    page.setModifiable(false);
    String[] accessPerms = page.getAccessPermissions();    
    for(String per: accessPerms){
      if(hasPermission(remoteUser, per)) return true;
    }
    return false;
  }

  public boolean hasPermission(String remoteUser, String expPerm) throws Exception {
    if(log.isDebugEnabled())
      log.debug("------CheckPermission of User "  + remoteUser + " with membership " + expPerm);
    if(superUser_.equals(remoteUser)) return true;
    if(expPerm == null) return false ;
    if(UserACL.EVERYONE.equals(expPerm)) return true ;
    Permission permission = new Permission();
    permission.setPermissionExpression(expPerm);
    String groupId = permission.getGroupId();
    if(remoteUser == null && groupId.equals(guestGroup_)) return true;

    String membership = permission.getMembership() ;
    MembershipHandler handler = orgService_.getMembershipHandler();
    if(membership == null || "*".equals(membership)) {
      Collection<?> c = handler.findMembershipsByUserAndGroup(remoteUser, groupId) ;
      if(c == null) return false ;
      return c.size() > 0 ;
    } 
    return handler.findMembershipByUserGroupAndType(remoteUser, groupId, membership) != null;
  }
  
  public boolean hasCreatePortalPermission(String remoteUser) throws Exception {
    if(superUser_.equals(remoteUser)) return true;
    if( portalCreatorGroups_ == null || portalCreatorGroups_.size() < 1) return false;
    for(String ele: portalCreatorGroups_){
      if(hasPermission(remoteUser, ele)) return true;
    }
    return false;
  }
  
  public boolean hasEditPermission(PortalConfig pconfig, String remoteUser) throws Exception {
    if(superUser_.equals(remoteUser)) return true;
    return hasPermission(remoteUser, pconfig.getEditPermission());
  }
  
  public boolean hasEditPermission(PageNavigation pageNav, String remoteUser) throws Exception {
    if(superUser_.equals(remoteUser)) {
      pageNav.setModifiable(true);
      return true;
    }
    String ownerType= pageNav.getOwnerType();
    if( PortalConfig.GROUP_TYPE.equals(ownerType)) {
      String expPerm = navigationCreatorMembershipType_+ ":/" + pageNav.getOwnerId();
      return hasPermission(remoteUser, expPerm);
    } else if ( PortalConfig.USER_TYPE.equals(ownerType)){
      return remoteUser.equals(pageNav.getOwnerId());
    }
    return false;
  }
 
  public boolean hasEditPermission(Page page, String remoteUser)  throws Exception {
    if(PortalConfig.USER_TYPE.equals(page.getOwnerType())){
      if( remoteUser.equals(page.getOwnerId())){
        page.setModifiable(true);
        return true;
      } 
      return false;
    }
    if(hasPermission(remoteUser, page.getEditPermission())) {
      page.setModifiable(true);
      return true;
    }
    page.setModifiable(false);
    return false;
  }
  
  public boolean hasAccessControlWorkspacePermission(String remoteUser) throws Exception {
    if(superUser_.equals(remoteUser)) return true;
    if( accessControlWorkspaceGroups_ == null || accessControlWorkspaceGroups_.size() < 1) return false;
    for(String ele: accessControlWorkspaceGroups_){
      if(hasPermission(remoteUser, ele)) return true;
    }
    return false;
  }
  
  static public class Permission {

    private String name_ ;

    private String groupId_ = ""  ;  
    private String membership_ = "" ;
    private String expression;
    
    private boolean selected_  = false;

    public void setPermissionExpression(String exp) {
      if(exp == null || exp.length() == 0) return;
      String[] temp = exp.split(":") ;
      if(temp.length < 2) return;
      expression = exp;
      membership_ = temp[0].trim() ;
      groupId_ = temp[1].trim() ;
    }

    public String getGroupId(){ return groupId_; }
    public void setGroupId(String groupId) { groupId_ = groupId; }

    public String getName(){ return name_; }
    public void setName(String name) { name_ = name; }

    public String getValue(){
      if(membership_ .length() == 0 || groupId_.length() == 0) return null;
      return membership_+":"+groupId_;
    }
    
    public String getMembership(){ return membership_; }
    public void setMembership(String membership) { membership_ = membership; }

    public boolean isSelected(){ return selected_; }
    public void setSelected(boolean selected){ selected_ = selected; }

    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
  }
}
/*
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
package org.exoplatform.portal.initializer.organization;

import java.util.List;

import org.exoplatform.container.component.BaseComponentPlugin;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.portal.initializer.organization.OrganizationConfig.GroupsConfig;
import org.exoplatform.services.organization.OrganizationConfig.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.OrganizationServiceInitializer;

/**
 * Created by The eXo Platform SAS
 * Author : Pham Thanh Tung
 *          thanhtungty@gmail.com
 * Mar 4, 2009  
 */
public class OrganizationInitializer extends BaseComponentPlugin implements OrganizationServiceInitializer {

  private OrganizationConfig orgConfig;

  public OrganizationInitializer(InitParams initParams) {
    orgConfig = (OrganizationConfig) initParams.getObjectParamValues(OrganizationConfig.class).get(0);
  }

  public void init(OrganizationService service) throws Exception {
    initGroups(orgConfig.getGroups(), service);
  }

  private void initGroups(List<GroupsConfig> configs, OrganizationService orgService) throws Exception {
    for(GroupsConfig ele : configs) {
      createGroups(ele, orgService);
    }
  }
//
//  private void createGroupRecursively(GroupsConfig config, OrganizationService orgService) throws Exception {
//    createGroupEntry(config.getGroup(), orgService);
//    GroupsConfig children = config.getChildren();
//    if(children == null) return;
//    String parentId = config.getParentId();
//    if(parentId == null || parentId.trim().length() < 1){
//      children.setParentId("/" + config.getName());
//    } else {
//      children.setParentId(config.getParentId() + "/" + config.getName());
//    }
//    createGroups(children, orgService);
//  }

  private void createGroups(GroupsConfig groupsConfig, OrganizationService service) throws Exception {
    String str = groupsConfig.getNumberOfGroups();
    Group group = groupsConfig.getGroup() ;
    if(str != null && str.trim().length() > 0) {
      int number = Integer.parseInt(str);
      for(int i = 0; i < number; i++){
        Group newGroup = new Group();
        newGroup.setName(group.getName() + i);
        newGroup.setDescription(group.getDescription() + " #" +i);
        newGroup.setLabel(group.getLabel() + " #" +i);
        newGroup.setParentId(group.getParentId());
        createGroupEntry(newGroup, service);
      }
    } else {
    	createGroupEntry(group, service);
    }
    
    GroupsConfig children = groupsConfig.getChildren();
    if(children == null) return;
    String parentId = groupsConfig.getParentId();
    if(parentId == null || parentId.trim().length() < 1){
      children.setParentId("/" + groupsConfig.getName());
    } else {
      children.setParentId(groupsConfig.getParentId() + "/" + groupsConfig.getName());
    }
    createGroups(children, service);
    
    
    //createGroupRecursively(groupsConfig.getChildren(), service);
  }

  private void createGroupEntry(Group config, OrganizationService orgService) throws Exception {
    String groupId = null;
    String parentId = config.getParentId();
    if (parentId == null || parentId.length() == 0){
      groupId = "/" + config.getName();
    } else {
      groupId = config.getParentId() + "/" + config.getName();
    }
    if (orgService.getGroupHandler().findGroupById(groupId) == null) {
      org.exoplatform.services.organization.Group group = orgService.getGroupHandler().createGroupInstance();
      group.setGroupName(config.getName());
      group.setDescription(config.getDescription());
      group.setLabel(config.getLabel());
      if (parentId == null || parentId.length() == 0) {
        orgService.getGroupHandler().addChild(null, group, true);
      } else {
        org.exoplatform.services.organization.Group parentGroup = orgService.getGroupHandler().findGroupById(parentId);
        orgService.getGroupHandler().addChild(parentGroup, group, true);
      }
      System.out.println("    Create Group " + groupId);
    }
  }

}

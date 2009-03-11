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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.portal.initializer.organization.OrganizationConfig.GroupsConfig;
import org.exoplatform.portal.initializer.organization.OrganizationConfig.UsersConfig;
import org.exoplatform.services.organization.MembershipHandler;
import org.exoplatform.services.organization.MembershipType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.OrganizationConfig.Group;
import org.exoplatform.services.organization.OrganizationConfig.User;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SAS thanhtungty@gmail.com Mar 4, 2009
 */
public class OrganizationInitializer implements Startable {

  private OrganizationConfig  orgConfig;

  private OrganizationService orgService;

  public OrganizationInitializer(InitParams initParams, OrganizationService service) {
    orgConfig = (OrganizationConfig) initParams.getObjectParamValues(OrganizationConfig.class)
                                               .get(0);
    orgService = service;
  }

  public void start() {
    try {
      System.out.println("\n\n===============>Start Organization Injector[" + (new Date()).toString() + "]\n");
      initGroups(orgConfig.getGroups(), orgService);
      initUsers(orgConfig.getUsers(), orgService);
      System.out.println("\n\n===============>Finish Organization Injector[" + (new Date()).toString() + "]\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initGroups(List<GroupsConfig> configs, OrganizationService orgService) throws Exception {
    for (GroupsConfig ele : configs) {
      prepare(ele.getParentId(), orgService);
      createGroups(ele, orgService);
    }
  }

  private void prepare(String path, OrganizationService orgService) throws Exception {
    String[] nodes = path.split("/");
    String parent = "";
    for (int i = 1; i < nodes.length; i++) {
      Group group = new Group();
      group.setName(nodes[i]);
      group.setDescription(nodes[i]);
      group.setLabel(nodes[i]);
      if (i < 2) {
        group.setParentId(null);
        createGroupEntry(group, orgService);
      } else {
        parent = parent + "/" + nodes[i - 1];
        group.setParentId(parent);
        createGroupEntry(group, orgService);
      }
    }
  }

  private void createGroups(GroupsConfig config, OrganizationService orgService) throws Exception {
    int from = Integer.parseInt(config.getFrom());
    int to = Integer.parseInt(config.getTo());
    Group group = config.getGroup();
    for (int i = from; i <= to; i++) {
      Group newGroup = new Group();
      newGroup.setName(group.getName() + i);
      newGroup.setDescription(group.getDescription() + " #" + i);
      newGroup.setLabel(group.getLabel() + " #" + i);
      newGroup.setParentId(group.getParentId());
      createGroupEntry(newGroup, orgService);
    }
  }

  private void createGroupEntry(Group config, OrganizationService orgService) throws Exception {
    String groupId = null;
    String parentId = config.getParentId();
    if (parentId == null || parentId.length() == 0) {
      groupId = "/" + config.getName();
    } else {
      groupId = config.getParentId() + "/" + config.getName();
    }
    if (orgService.getGroupHandler().findGroupById(groupId) == null) {
      org.exoplatform.services.organization.Group group = orgService.getGroupHandler()
                                                                    .createGroupInstance();
      group.setGroupName(config.getName());
      group.setDescription(config.getDescription());
      group.setLabel(config.getLabel());
      if (parentId == null || parentId.length() == 0) {
        orgService.getGroupHandler().addChild(null, group, true);
      } else {
        org.exoplatform.services.organization.Group parentGroup = orgService.getGroupHandler()
                                                                            .findGroupById(parentId);
        orgService.getGroupHandler().addChild(parentGroup, group, true);
      }
      //System.out.println("    Create Group " + groupId);
    }
    else {
      //System.out.println("    Group " + groupId + " already exists, ignoring the entry");
    }
  }

  private void initUsers(List<UsersConfig> configs, OrganizationService service) throws Exception {
    for (UsersConfig config : configs) {
      int from = Integer.parseInt(config.getFrom());
      int to = Integer.parseInt(config.getTo());
      User u = config.getUser();
      for (int i = from; i <= to; i++) {
        User user = new User();
        user.setUserName(u.getUserName() + i);
        user.setPassword(u.getPassword());
        user.setFirstName(u.getFirstName() + "-" + i);
        user.setLastName(u.getLastName() + "-" + i);
        user.setEmail(u.getEmail());    
        user.setGroups(u.getGroups());
        createUserEntry(user, service);
      }
    }
  }

  private void createUserEntry(User config, OrganizationService service) throws Exception {
    MembershipHandler mhandler = service.getMembershipHandler();
    org.exoplatform.services.organization.User user = service.getUserHandler()
                                                             .createUserInstance(config.getUserName());
    user.setPassword(config.getPassword());
    user.setFirstName(config.getFirstName());
    user.setLastName(config.getLastName());
    user.setEmail(config.getEmail());
    if (service.getUserHandler().findUserByName(config.getUserName()) == null) {
      service.getUserHandler().createUser(user, true);
      //System.out.println("    Created user " + config.getUserName());
    } else {
      //System.out.println("    User " + config.getUserName() + " already exists, ignoring the entry");
    }

    String groups = config.getGroups();
    String[] entry = groups.split(",");
    for (int j = 0; j < entry.length; j++) {
      String[] temp = entry[j].trim().split(":");
      String membership = temp[0];
      String groupId = temp[1];
      if (mhandler.findMembershipByUserGroupAndType(config.getUserName(), groupId, membership) == null) {
        org.exoplatform.services.organization.Group group = service.getGroupHandler()
                                                                   .findGroupById(groupId);
        MembershipType mt = service.getMembershipTypeHandler().createMembershipTypeInstance();
        mt.setName(membership);
        mhandler.linkMembership(user, group, mt, true);
        //System.out.println("    Created membership " + config.getUserName() + ", " + groupId + ", "
        //    + membership);
      } else {
        //System.out.println("    Ignored membership " + config.getUserName() + ", " + groupId + ", "
        //    + membership);
      }
    }
  }

  public void stop() {
  }

}

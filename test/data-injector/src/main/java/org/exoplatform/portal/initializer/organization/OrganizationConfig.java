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

/**
 * Created by The eXo Platform SAS
 * Author : Pham Thanh Tung
 *          thanhtungty@gmail.com
 * Mar 6, 2009  
 */
public class OrganizationConfig {
  
  List<GroupsConfig> groups;
  List<UsersConfig> users;
  
  public List<GroupsConfig> getGroups() {
    return groups;
  }
  public void setGroups(List<GroupsConfig> groups) {
    this.groups = groups;
  }
  
  public List<UsersConfig> getUsers() {
    return users;
  }
  public void setUsers(List<UsersConfig> users) {
    this.users = users;
  }
  
  static public class GroupsConfig {

    private String name;

    private String description;

    private String parentId;

    private String label;

    private String numberOfGroups;
    
    private GroupsConfig children;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getParentId() {
      return parentId;
    }

    public void setParentId(String parentId) {
      this.parentId = parentId;
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public String getNumberOfGroups() {
      return numberOfGroups;
    }

    public void setNumberOfGroups(String number) {
      numberOfGroups = number;
    }
    
    public GroupsConfig getChildren() {
      return children;
    }
    
    public void setChildren(GroupsConfig children) {
      this.children = children;
    }

  }
  
  static public class UsersConfig {
    
  }

}

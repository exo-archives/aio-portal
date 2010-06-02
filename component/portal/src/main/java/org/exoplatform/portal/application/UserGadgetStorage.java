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
package org.exoplatform.portal.application;

import java.util.Set;
import java.util.Map;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Aug 8, 2007  
 */
public interface UserGadgetStorage {

  /**
   * save gadget with property provided (a key/value pair)
   * @param userName - gadget node will be saved to this user's node
   * @param gadgetType - gadget'll be saved to this gadgetType's node
   * @param instanceId 
   * @param key - key of property that will be save with gadget node
   * @param value - value of property that will be save with gadget node
  */
  public void save(String userName, String gadgetType, String instanceId, String key, String value) throws Exception ;

  /**
   * save gadget with property provided (Map)
   * @param userName - gadget node will be saved to this user's node
   * @param gadgetType - gadget'll be saved to this gadgetType's node
   * @param instanceId 
   * @param values - a Map that it's keys, values are used to save to node's property
   */
  public void save(String userName, String gadgetType, String instanceId, Map<String, String> values) throws Exception ;
  
  /**
   * Return value of gadget node's property with key provided
   * @param userName - user node that gadget node belongs to
   * @param gadgetType - gadgetType node that gadget node belongs to
   * @param instanceId - gadget's id
   * @param key - the key of property that want to get value
   */
  public String get(String userName, String gadgetType, String instanceId, String key) throws Exception ;

  /**
   * Return a Map<String, String>, key, value of it's entries are key, value of properties of the gadget
   * @param userName - user node that gadget node belongs to
   * @param gadgetType - gadgetType node that gadget node belongs to
   * @param instanceId - gadget's id
   */
  public Map<String, String> get(String userName, String gadgetType, String instanceId) throws Exception ;

  /**
   * Return a Map<String, String>, key, value of it's entries are key, value of properties of the gadget
   * @param userName - user node that gadget node belongs to
   * @param gadgetType - gadgetType node that gadget node belongs to
   * @param instanceId - gadget's id
   * @param keys - set of keys to find out properties of user gadget node
   */
  public Map<String,String> get(String userName, String gadgetType, String instanceId, Set<String> key) throws Exception ;
  
  /**
   * Delete gadget node from an user node
   * @param userName - used to find user node
   * @param gadgetType 
   * @param instanceId
   */
  public void delete(String userName, String gadgetType, String instanceId) throws Exception ;

  /**
   * Delete gadget properties  
   * @param userName - used to find user node
   * @param gadgetType 
   * @param instanceId
   * @param keys - Set of property's key
   */
  public void delete(String userName, String gadgetType, String instanceId, Set<String> keys) throws Exception ;
  
}

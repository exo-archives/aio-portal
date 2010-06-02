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

package org.exoplatform.application.registry;

import java.util.Comparator;
import java.util.List;

import org.exoplatform.container.component.ComponentPlugin;

/**
 * Created y the eXo platform team
 * User: Tuan Nguyen
 * Date: 20 april 2007
 */
public interface ApplicationRegistryService {
  
  /**
   * get list of application categories (and applications in each category) 
   * @param accessUser - username of user that his access permissions is used to filter app categories and apps
   * @param appTypes - array of application type, used to filter applications in each application category
  */
  public List<ApplicationCategory> getApplicationCategories(String accessUser, String ... appTypes) throws Exception;  
  public void initListener(ComponentPlugin com) throws Exception;
  /**
   * get list of all current application categories (unsorted)
   */
  public List<ApplicationCategory> getApplicationCategories() throws Exception;
  /**
   * get list of all current application categories (sorted) 
   * @param sortComparator - used to sort application category list
   */
  public List<ApplicationCategory> getApplicationCategories(Comparator<ApplicationCategory> sortComparator) throws Exception;
  /**
   * get ApplicationCategory with name provided
   * @param name - ApplicationCategory's name
   */
  public ApplicationCategory getApplicationCategory(String name) throws Exception;
  /**
   * save an application category to database
   * @param category - application category that will be saved
   */
  public void save(ApplicationCategory category) throws Exception;
  /**
   * remove application category (and all application in it) from database
   * @param category - application category that will be removed
   */
  public void remove(ApplicationCategory category) throws Exception;
  /**
   * get list of applications (unsorted) in specific category and have specific type
   * @param category - ApplicationCategory that you want to list applications
   * @param appTypes - array of application type
   */
  public List<Application> getApplications(ApplicationCategory category, String...appTypes) throws Exception;
  /**
   * get list of applications (sorted) in specific category and have specific type
   * @param category - ApplicationCategory that you want to list applications
   * @param sortComparator - comparator used to sort application list
   * @param appTypes - array of application type
   */
  public List<Application> getApplications(ApplicationCategory category, Comparator<Application> sortComparator, String...appTypes) throws Exception;
  /**
   * get list of all current applications
   */
  public List<Application> getAllApplications() throws Exception;
  /**
   * get application with id provided
   * @param id - must be valid applicationId (catgoryname/applicationName), if not, this will throw exception
   */
  public Application getApplication(String id) throws Exception;
  /**
   * get application in specific category and have name provided in param
   * @param category - name of application category
   * @param name - name of application   
   */
  public Application getApplication(String category, String name) throws Exception;
  /**
   * Save Application in ApplicationCategory
   * If ApplicationCategory or Application don't exist, they'll be created
   * @param category - ApplicationCategory that your application'll be saved to
   * @param application - Application that will be saved 
   */
  public void save(ApplicationCategory category, Application application) throws Exception;
  /**
   * Update an Application
   * @param application - Application that you want to update
   */
  public void update(Application application) throws Exception;
  /**
   * Remove an Application from database
   * @param app - Application that you want to remove
   */
  public void remove(Application app) throws Exception;
  /**
   * Get all deployed portlet, add to portlet's ApplicationCategory
   * If ApplicationCategory currently doesn't exist, it'll be created
   * If Application've already existed, it'll be ignored 
   */
  public void importAllPortlets() throws Exception;  
  /**
   * Get all Gadget, add to eXoGadgets application category
   * When first added, it's access permission will be Everyone
   * If ApplicationCategory currently doesn't exist, it'll be created
   * Gadget that has been imported before will be ignored  
   */
  //TODO: dang.tung
  public void importExoGadgets() throws Exception;
  /**
   * remove all ApplicationCategory and Application in it
   */
  public void clearAllRegistries() throws Exception;
}
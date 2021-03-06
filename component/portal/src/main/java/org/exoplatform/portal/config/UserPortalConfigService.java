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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.exoplatform.commons.utils.LazyPageList;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.component.ComponentPlugin;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.pom.config.POMDataStorage;
import org.exoplatform.portal.pom.config.POMSession;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.cache.ExpireKeyStartWithSelector;
import org.exoplatform.services.listener.ListenerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.services.portletcontainer.pci.Input;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SAS Apr 19, 2007 This service is used to load the
 * PortalConfig, Page config and Navigation config for a given user.
 */
public class UserPortalConfigService implements Startable {

	public final static String CREATE_PAGE_EVENT = "UserPortalConfigService.page.onCreate"
		.intern();

	public final static String REMOVE_PAGE_EVENT = "UserPortalConfigService.page.onRemove"
		.intern();

	public final static String UPDATE_PAGE_EVENT = "UserPortalConfigService.page.onUpdate"
		.intern();

	public final static String CREATE_NAVIGATION_EVENT = "UserPortalConfigService.navigation.onCreate"
		.intern();

	public final static String REMOVE_NAVIGATION_EVENT = "UserPortalConfigService.navigation.onRemove"
		.intern();

	public final static String UPDATE_NAVIGATION_EVENT = "UserPortalConfigService.navigation.onUpdate"
		.intern();

	private DataStorage storage_;

	private UserACL userACL_;

	private OrganizationService orgService_;

	private ListenerService listenerService;

	protected ExoCache<String, PortalConfig> portalConfigCache_;

	protected ExoCache<String, Page> pageConfigCache_;

	protected ExoCache<String, PageNavigation> pageNavigationCache_;

	private NewPortalConfigListener newPortalConfigListener_;

	private Log log = ExoLogger.getLogger("Portal:UserPortalConfigService");

	/**
	 *The constructor should create the DataStorage object and broadcast
	 * "the UserPortalConfigService.onInit" event
	 */
	public UserPortalConfigService(UserACL userACL, DataStorage storage,
			CacheService cacheService, OrganizationService orgService,
			ListenerService listenerService) throws Exception {
		storage_ = storage;
		orgService_ = orgService;
		this.listenerService = listenerService;
		userACL_ = userACL;

		portalConfigCache_ = cacheService.getCacheInstance(PortalConfig.class
				.getName());
		pageConfigCache_ = cacheService.getCacheInstance(Page.class.getName());
		pageNavigationCache_ = cacheService.getCacheInstance(PageNavigation.class
				.getName());
	}

	/**
	 * This method should load the PortalConfig object according to the
	 * portalName, set the view and edit permission according to the accessUser,
	 * load Naviagtion of the portal according to the portalName, find all the
	 * navigation of the groups that the user is belong to
	 * 
	 * @param userName
	 * @return a UserPortalConfig object that contain the PortalConfig and a list
	 *         of the PageNavigation objects
	 */
	public UserPortalConfig getUserPortalConfig(String portalName,
			String accessUser) throws Exception {
		PortalConfig portal = portalConfigCache_.get(portalName);
		if (portal == null) {
			portal = storage_.getPortalConfig(portalName);
			if (portal != null)
				portalConfigCache_.put(portalName, portal);
		}
		if (portal == null || !userACL_.hasPermission(portal))
			return null;

		List<PageNavigation> navigations = new ArrayList<PageNavigation>();
		PageNavigation navigation = getPageNavigation(PortalConfig.PORTAL_TYPE,
				portalName);
		if (navigation != null) {
			navigation.setModifiable(userACL_.hasPermission(portal
					.getEditPermission()));
			navigations.add(navigation);
		}

		if (accessUser == null) {
			navigation = getPageNavigation(PortalConfig.GROUP_TYPE, userACL_
					.getGuestsGroup());
			if (navigation != null)
				navigations.add(navigation);
		} else {
			navigation = getPageNavigation(PortalConfig.USER_TYPE, accessUser);
			if (navigation != null) {
				navigation.setModifiable(true);
				navigations.add(navigation);
			}

			Collection<?> groups = null;
			if (userACL_.getSuperUser().equals(accessUser))
				groups = orgService_.getGroupHandler().getAllGroups();
			else
				groups = orgService_.getGroupHandler().findGroupsOfUser(accessUser);
			Iterator<?> iterator = groups.iterator();
			while (iterator.hasNext()) {
				Group m = (Group) iterator.next();
				String groupId = m.getId().trim();
				if(groupId.equals(userACL_.getGuestsGroup())) continue;
				navigation = getPageNavigation(PortalConfig.GROUP_TYPE, groupId);
				if (navigation == null)
					continue;
				navigation.setModifiable(userACL_.hasEditPermission(navigation));
				navigations.add(navigation);
			}
		}
		Collections.sort(navigations, new Comparator<PageNavigation>() {
			public int compare(PageNavigation nav1, PageNavigation nav2) {
				return nav1.getPriority() - nav2.getPriority();
			}
		});

		return new UserPortalConfig(portal, navigations);
	}

	public List<String> getMakableNavigations(String remoteUser) throws Exception {
		List<String> list = new ArrayList<String>();
		Collection<?> groups = null;
		if (remoteUser.equals(userACL_.getSuperUser()))
			groups = orgService_.getGroupHandler().getAllGroups();
		else
			groups = orgService_.getGroupHandler().findGroupByMembership(remoteUser,
					userACL_.getMakableMT());
		if (groups != null) {
			Iterator<?> iterator = groups.iterator();
			while (iterator.hasNext()) {
				Group m = (Group) iterator.next();
				String groupId = m.getId().trim();
				list.add(groupId);
			}
		}
		return list;
	}

	/**
	 * This method should create a the portal config, pages and nRavigation
	 * according to the template name
	 * 
	 * @param portalName
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public void createUserPortalConfig(String portalName, String template)
	throws Exception {
		NewPortalConfig portalConfig = newPortalConfigListener_.getPortalConfig(PortalConfig.PORTAL_TYPE);

    //
		portalConfig.setTemplateOwner(template);
		portalConfig.getPredefinedOwner().clear();
		portalConfig.getPredefinedOwner().add(portalName);

    //
    newPortalConfigListener_.initPortletPreferencesDB(portalConfig);
    newPortalConfigListener_.initPortalConfigDB(portalConfig);
    newPortalConfigListener_.initPageDB(portalConfig);
    newPortalConfigListener_.initPageNavigationDB(portalConfig);
	}

	/**
	 * This method should remove the PortalConfig, Page and PageNavigation that
	 * belong to the portal in the database. The method should broadcast the event
	 * UserPortalConfigService.portal.onRemove
	 * 
	 * @param config
	 * @throws Exception
	 */
	public void removeUserPortalConfig(String portalName) throws Exception {
		Query<Page> query = new Query<Page>(PortalConfig.PORTAL_TYPE, portalName,
				null, null, Page.class);
		LazyPageList pageList = storage_.find(query);

		List<?> listPage = pageList.getAll();
		int lenPage = listPage.size() - 1;
		while (lenPage >= 0) {
			Page page = (Page) listPage.get(lenPage);
			remove(page);
			lenPage--;
		}

		PageNavigation navigation = storage_.getPageNavigation(
				PortalConfig.PORTAL_TYPE, portalName);
		if (navigation != null)
			remove(navigation);

		Query<PortletPreferences> portletPrefQuery = new Query<PortletPreferences>(
				PortalConfig.PORTAL_TYPE, portalName, null, null,
				PortletPreferences.class);
		pageList = storage_.find(portletPrefQuery);
		List<?> list = pageList.getAll();
		int len = list.size() - 1;
		while (len >= 0) {
			PortletPreferences portletPreferences = (PortletPreferences) list
			.get(len);
			storage_.remove(portletPreferences);
			len--;
		}
		//		pageList.setPageSize(10);
		//		i = 1;
		//		while (i <= pageList.getAvailablePage()) {
		//		List<?> list = pageList.getPage(i);
		//		int len = list.size()-1;
		//		while (len >= 0) {
		//		PortletPreferences portletPreferences = (PortletPreferences) list.get(len);
		//		storage_.remove(portletPreferences);
		//		len--;
		//		}
		//		i++;
		//		}

		PortalConfig config = storage_.getPortalConfig(portalName);
		portalConfigCache_.remove(config.getName());
		if (config != null)
			storage_.remove(config);
	}

	/**
	 * This method should update the PortalConfig object
	 * 
	 * @param portal
	 * @throws Exception
	 */
	public void update(PortalConfig portal) throws Exception {
		storage_.save(portal);
		portalConfigCache_
		.select(new ExpireKeyStartWithSelector<String, PortalConfig>(portal
				.getName()));
	}

	//****************************************************************************
	// **********************

	/**
	 * This method will load the page according to the pageId
	 * 
	 * Caller of this method should check the value of parsed parameter before invocation
	 * 
	 * @param pageId
	 * @return
	 * @throws Exception
	 */

	public Page getPage(String pageId) throws Exception {
		if (pageId == null)
			return null;
		Page page = (Page) pageConfigCache_.get(pageId);
		if (page == null)
			page = storage_.getPage(pageId);  //TODO: pageConfigCache_ needs to be updated
		return page;
	}

	/**
	 * This method should load the page according to the pageId, set view and edit
	 * permission for the Page object according to the accessUser.
	 * 
	 * @param accessUser
	 * @return
	 * @throws Exception
	 */
	public Page getPage(String pageId, String accessUser) throws Exception {
		Page page = getPage(pageId);
		if (page != null) { //Add a check on page value before put it into the cache
			pageConfigCache_.put(pageId, page);
		}			
		if (page == null || !userACL_.hasPermission(page)) {
			return null;
		}
		return page;
	}

	/**
	 * This method should remove the page object in the database and broadcast the
	 * event UserPortalConfigService.page.onRemove
	 * 
	 * @param config
	 * @throws Exception
	 */
	public void remove(Page page) throws Exception {
		storage_.remove(page);
		pageConfigCache_.remove(page.getPageId());
		listenerService.broadcast(REMOVE_PAGE_EVENT, this, page);
	}

	/**
	 * This method should create the given page object
	 * 
	 * @param page
	 * @throws Exception
	 */
	public void create(Page page) throws Exception {
		storage_.create(page);
		pageConfigCache_.put(page.getPageId(), page);
		listenerService.broadcast(CREATE_PAGE_EVENT, this, page);
	}

	/**
	 * This method should update the given page object
	 * 
	 * @param page
	 * @throws Exception
	 */
	public void update(Page page) throws Exception {
		storage_.save(page);
		pageConfigCache_.select(new ExpireKeyStartWithSelector<String, Page>(page
				.getPageId()));
		listenerService.broadcast(UPDATE_PAGE_EVENT, this, page);
	}

	//****************************************************************************
	// **********************

	public void create(PageNavigation navigation) throws Exception {
		storage_.create(navigation);
		pageNavigationCache_.put(navigation.getOwner(), navigation);
		listenerService.broadcast(CREATE_NAVIGATION_EVENT, this, navigation);
	}

	/**
	 * This method should create or update the navigation object in the database
	 * 
	 * @param navigation
	 * @throws Exception
	 */
	public void update(PageNavigation navigation) throws Exception {
		storage_.save(navigation);
		pageNavigationCache_
		.select(new ExpireKeyStartWithSelector<String, PageNavigation>(
				navigation.getOwner()));
		listenerService.broadcast(UPDATE_NAVIGATION_EVENT, this, navigation);
	}

	/**
	 * This method should remove the navigation object from the database
	 * 
	 * @param navigation
	 * @throws Exception
	 */
	public void remove(PageNavigation navigation) throws Exception {
		storage_.remove(navigation);
		pageNavigationCache_.remove(navigation.getOwner());
		listenerService.broadcast(REMOVE_NAVIGATION_EVENT, this, navigation);
	}

	public PageNavigation getPageNavigation(String ownerType, String id)
	throws Exception {
		PageNavigation navigation = pageNavigationCache_.get(ownerType + "::" + id);
		if (navigation == null)
			navigation = storage_.getPageNavigation(ownerType, id);
		return navigation;
	}

	/**
	 * This method creates new page from an existing page and links new page to a
	 * PageNode
	 * 
	 * @return PageNode
	 * @throws Exception
	 */
	public PageNode createNodeFromPageTemplate(String nodeName, String nodeLabel,
			String pageId, String ownerType, String ownerId,
			javax.portlet.PortletPreferences portletPreferences) throws Exception {

		Page page = renewPage(pageId, nodeName, ownerType, ownerId,
				portletPreferences);
		PageNode pageNode = new PageNode();
		if (nodeLabel == null || nodeLabel.trim().length() < 1)
			nodeLabel = nodeName;
		pageNode.setName(nodeName);
		pageNode.setLabel(nodeLabel);
		pageNode.setPageReference(page.getPageId());
		return pageNode;
	}

	public Page renewPage(String pageId, String pageName, String ownerType,
			String ownerId, javax.portlet.PortletPreferences portletPreferences)
	throws Exception {

		Page page = storage_.getPage(pageId);
		page.setName(pageName);
		page.setPageId(ownerType + "::" + ownerId + "::" + pageName);
		List<Application> apps = new ArrayList<Application>();
		getApplications(apps, page);
		for (Application ele : apps) {
			String appType = ele.getApplicationType();
			if (appType == null
					|| org.exoplatform.web.application.Application.EXO_PORTLET_TYPE
					.equals(appType)) {
				javax.portlet.PortletPreferences mergedPreferences = getPreferences(ele);
				if (portletPreferences != null) {
					addPreferrences(mergedPreferences, portletPreferences);
				}
				renewInstanceId(ele, ownerType, ownerId);
				setPreferences(ele, mergedPreferences);
			} else {
				renewInstanceId(ele, ownerType, ownerId);
			}
		}
		create(page);
		return page;
	}

	private void getApplications(List<Application> apps, Object component) {
		if (component instanceof Application) {
			apps.add((Application) component);
		} else if (component instanceof Container) {
			Container container = (Container) component;
			List<Object> children = container.getChildren();
			if (children != null)
				for (Object ele : children)
					getApplications(apps, ele);
		}
	}

	private void addPreferrences(javax.portlet.PortletPreferences to,
			javax.portlet.PortletPreferences from) throws Exception {
		Iterator<Entry<String, String[]>> itr = from.getMap().entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String[]> entry = itr.next();
			to.setValues(entry.getKey(), entry.getValue());
		}
	}

	private javax.portlet.PortletPreferences getPreferences(Application app)
	throws Exception {
		ExoWindowID windowID = new ExoWindowID(app.getInstanceId());
		Input input = new Input();
		input.setInternalWindowID(windowID);
		ExoContainer container = ExoContainerContext.getCurrentContainer();
		PortletContainerService pcServ = (PortletContainerService) container
		.getComponentInstanceOfType(PortletContainerService.class);
		return pcServ.getPortletPreferences(input);
	}

	private void setPreferences(Application portlet,
			javax.portlet.PortletPreferences portletPreferences) throws Exception {
		ExoWindowID windowID = new ExoWindowID(portlet.getInstanceId());
		Input input = new Input();
		input.setInternalWindowID(windowID);
		ExoContainer container = ExoContainerContext.getCurrentContainer();
		PortletContainerService pcServ = (PortletContainerService) container
		.getComponentInstanceOfType(PortletContainerService.class);
		pcServ.setPortletPreferences(input, portletPreferences);
	}

	private void renewInstanceId(Application app, String ownerType, String ownerId) {
		ExoWindowID newExoWindowID = new ExoWindowID(app.getInstanceId());
		newExoWindowID.setOwner(ownerType + "#" + ownerId);
		newExoWindowID.setUniqueID(String.valueOf(newExoWindowID.hashCode()));
		app.setInstanceId(newExoWindowID.generatePersistenceId());
	}

	public Page createPageTemplate(String temp, String ownerType, String ownerId)
	throws Exception {
		Page page = newPortalConfigListener_.createPageFromTemplate(temp);
		page.setOwnerType(ownerType);
		page.setOwnerId(ownerId);
		List<Application> apps = new ArrayList<Application>(3);
		getApplications(apps, page);
		if (!apps.isEmpty()) {
			for (Application ele : apps) {
				makeInstanceId(ele, ownerType, ownerId);
			}
			createPortletPreferences(apps, temp);
		}
		return page;
	}

	private void makeInstanceId(Application app, String ownerType, String ownerId) {
		StringBuilder builder = new StringBuilder(20);
		builder.append(ownerType + "#" + ownerId + ":").append(app.getInstanceId())
		.append("/" + builder.hashCode());
		app.setInstanceId(builder.toString());
	}

	private void createPortletPreferences(List<Application> apps, String temp)
	throws Exception {
		List<PortletPreferences> preferencesSet = newPortalConfigListener_
		.createPortletPreferencesFromTemplate(temp).getPortlets();
		if (preferencesSet == null || preferencesSet.size() < 1)
			return;
		for (Application ele : apps) {
			String appType = ele.getApplicationType();
			if (appType == null
					|| org.exoplatform.web.application.Application.EXO_PORTLET_TYPE
					.equals(appType)) {
				savePortletPreferences(ele, preferencesSet);
			}
		}
	}

	private void savePortletPreferences(Application app,
			List<PortletPreferences> preferencesSet) throws Exception {
		ExoWindowID windowID = new ExoWindowID(app.getInstanceId());
		String tmp = "/" + windowID.getPortletApplicationName() + "/"
		+ windowID.getPortletName();
		for (PortletPreferences preferences : preferencesSet) {
			if (tmp.equals(preferences.getWindowId())) {
				String[] fragments = windowID.getOwner().split("#");
				preferences.setOwnerType(fragments[0]);
				preferences.setOwnerId(fragments[1]);
				preferences.setWindowId(windowID.getPersistenceId());
				storage_.save(preferences);
				break;
			}
		}
	}

	public void initListener(ComponentPlugin listener) {
		if (listener instanceof NewPortalConfigListener) {
			newPortalConfigListener_ = (NewPortalConfigListener) listener;
		}
	}

	public void start() {
		try {
			if (newPortalConfigListener_ == null)
				return;

      //
      if (storage_ instanceof POMDataStorage) {
        ((POMDataStorage)storage_).getPOMSessionManager().openSession();
      }

			newPortalConfigListener_.run();
		} catch (Exception e) {
			log.error("", e);
		} finally {
      if (storage_ instanceof POMDataStorage) {
        ((POMDataStorage)storage_).getPOMSessionManager().closeSession(true);
      }
    }
	}

	public void stop() {
	}

	public String getDefaultPortal() {
		return newPortalConfigListener_.getDefaultPortal();
	}

}

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

import org.exoplatform.container.PortalContainer;
import org.exoplatform.portal.pom.config.POMSessionManager;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.test.BasicTestCase;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;

import java.util.Arrays;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by The eXo Platform SARL Author : Tung Pham thanhtungty@gmail.com Nov
 * 13, 2007
 */
public class TestLoadedPOM extends BasicTestCase {

  /** . */
  private UserPortalConfigService portalConfigService;

  /** . */
  private DataStorage storage;

  /** . */
  private POMSessionManager mgr;

  public TestLoadedPOM(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    super.setUp();

    //
    PortalContainer container = PortalContainer.getInstance();
    portalConfigService = (UserPortalConfigService) container.getComponentInstanceOfType(UserPortalConfigService.class);
    storage = (DataStorage) container.getComponentInstanceOfType(DataStorage.class);
    mgr = (POMSessionManager)container.getComponentInstanceOfType(POMSessionManager.class);
  }

  protected void tearDown() throws Exception {
    mgr.closeSession();
    storage = null;
  }

  public void testLegacyGroupWithNormalizedName() throws Exception {
    PageNavigation nav = storage.getPageNavigation("group::/platform/guests");
    assertNotNull(nav);
    assertEquals("/platform/guests", nav.getOwnerId());
    PageNode node = nav.getNodes().get(0);
    assertEquals("group::/platform/guests::register", node.getPageReference());

    Page page = storage.getPage("group::/platform/guests::register");
    assertNotNull(page);
    assertEquals("group::/platform/guests::register", page.getPageId());
    assertEquals("/platform/guests", page.getOwnerId());
    Application app = (Application)page.getChildren().get(0);
    assertEquals("group#/platform/guests:/exoadmin/AccountPortlet/Account", app.getInstanceId());

    PortletPreferences prefs = storage.getPortletPreferences(new ExoWindowID("group#/platform/guests:/web/IFramePortlet/blog"));
    assertNotNull(prefs);
    assertEquals("/platform/guests", prefs.getOwnerId());
    assertEquals("group#/platform/guests:/web/IFramePortlet/blog", prefs.getWindowId());
  }

  public void testGroupWithNormalizedName() throws Exception {
    PageNavigation nav = storage.getPageNavigation("group::/platform/users");
    assertNotNull(nav);
    assertEquals("/platform/users", nav.getOwnerId());
    PageNode node = nav.getNodes().get(0);
    assertEquals("group::/platform/users::dashboard", node.getPageReference());

    Page page = storage.getPage("group::/platform/users::mylink-blog");
    assertNotNull(page);
    assertEquals("group::/platform/users::mylink-blog", page.getPageId());
    assertEquals("/platform/users", page.getOwnerId());
    Application app = (Application)page.getChildren().get(0);
    assertEquals("group#/platform/users:/web/IFramePortlet/blog", app.getInstanceId());

    PortletPreferences prefs = storage.getPortletPreferences(new ExoWindowID("group#/platform/users:/web/IFramePortlet/blog"));
    assertNotNull(prefs);
    assertEquals("/platform/users", prefs.getOwnerId());
    assertEquals("group#/platform/users:/web/IFramePortlet/blog", prefs.getWindowId());
  }

  public void testNavigation() throws Exception {
    PageNavigation nav = storage.getPageNavigation("portal::test");
    assertNotNull(nav);

    //
    assertEquals(1, nav.getPriority());
    assertEquals("navigation_creator", nav.getCreator());
    assertEquals("navigation_modifier", nav.getModifier());
    assertEquals("navigation_description", nav.getDescription());

    //
    assertEquals(1, nav.getNodes().size());

    //
    PageNode nodeNavigation = nav.getNodes().get(0);
    assertEquals(0, nodeNavigation.getChildren().size());
    assertEquals("node_name", nodeNavigation.getName());
    assertEquals("node_uri", nodeNavigation.getUri());
    assertEquals("node_label", nodeNavigation.getLabel());
    assertEquals("node_icon", nodeNavigation.getIcon());
    GregorianCalendar start = new GregorianCalendar(2000, 2, 21, 1, 33, 0);
    start.setTimeZone(TimeZone.getTimeZone("UTC"));
    assertEquals(start.getTime(), nodeNavigation.getStartPublicationDate());
    GregorianCalendar end = new GregorianCalendar(2009, 2, 21, 1, 33, 0);
    end.setTimeZone(TimeZone.getTimeZone("UTC"));
    assertEquals(end.getTime(), nodeNavigation.getEndPublicationDate());
    assertEquals(true, nodeNavigation.isShowPublicationDate());
    assertEquals(true, nodeNavigation.isVisible());
  }

  public void testPortal() throws Exception {
    PortalConfig portal = storage.getPortalConfig("test");
    assertNotNull(portal);

    assertEquals("test", portal.getName());
    assertEquals("en", portal.getLocale());
    assertTrue(Arrays.equals(new String[]{"test_access_permissions"}, portal.getAccessPermissions()));
    assertEquals("test_edit_permission", portal.getEditPermission());
    assertEquals("test_skin", portal.getSkin());
    assertEquals("test_title", portal.getTitle());
    assertEquals("test_creator", portal.getCreator());
    assertEquals("test_modifier", portal.getModifier());
    assertEquals("test_prop_value", portal.getProperty("prop_key"));
  }

  public void testPage() throws Exception {
    Page page = storage.getPage("portal::test::test");
    assertNotNull(page);

    //
    assertEquals("test_title", page.getTitle());
    assertEquals("test_factory_id", page.getFactoryId());
    assertTrue(Arrays.equals(new String[]{"test_access_permissions"}, page.getAccessPermissions()));
    assertEquals("test_edit_permission", page.getEditPermission());
    assertEquals(true, page.isShowMaxWindow());
    assertEquals("test_creator", page.getCreator());
    assertEquals("test_modifier", page.getModifier());

    //
    List<Object> children = page.getChildren();
    assertEquals(2, children.size());

    //
    Container container1 = (Container)children.get(0);
    assertEquals("container_1", container1.getName());
    assertEquals("container_1_title", container1.getTitle());
    assertEquals("container_1_icon", container1.getIcon());
    assertEquals("container_1_template", container1.getTemplate());
    assertTrue(Arrays.equals(new String[]{"container_1_access_permissions"}, container1.getAccessPermissions()));
    assertEquals("container_1_factory_id", container1.getFactoryId());
    assertEquals("container_1_decorator", container1.getDecorator());
    assertEquals("container_1_description", container1.getDescription());
    assertEquals("container_1_width", container1.getWidth());
    assertEquals("container_1_height", container1.getHeight());

    //
    Application application1 = (Application)children.get(1);
    assertEquals("application_1_type", application1.getApplicationType());
    assertEquals("application_1_theme", application1.getTheme());
    assertEquals("application_1_title", application1.getTitle());
    assertTrue(Arrays.equals(new String[]{"application_1_access_permissions"},application1.getAccessPermissions()));
    assertEquals(true, application1.getShowInfoBar());
    assertEquals(true, application1.getShowApplicationState());
    assertEquals(true, application1.getShowApplicationMode());
    assertEquals("application_1_description", application1.getDescription());
    assertEquals("application_1_icon", application1.getIcon());
    assertEquals("application_1_width", application1.getWidth());
    assertEquals("application_1_height", application1.getHeight());
    assertEquals("application_1_prop_value", application1.getProperties().get("prop_key"));
    assertEquals("portal#test:/web/BannerPortlet/banner", application1.getInstanceId());
  }





}
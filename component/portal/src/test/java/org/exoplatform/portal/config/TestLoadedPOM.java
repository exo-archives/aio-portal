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
import org.exoplatform.portal.pom.config.tasks.Mapper;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.Preference;
import org.exoplatform.test.BasicTestCase;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.exoplatform.commons.utils.LazyPageList;

import java.util.Arrays;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

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
    PageNavigation nav = storage.getPageNavigation("group::/platform/test/legacy");
    assertNotNull(nav);
    assertEquals("/platform/test/legacy", nav.getOwnerId());
    PageNode node = nav.getNodes().get(0);
    assertEquals("group::/platform/test/legacy::register", node.getPageReference());

    Page page = storage.getPage("group::/platform/test/legacy::register");
    assertNotNull(page);
    assertEquals("group::/platform/test/legacy::register", page.getPageId());
    assertEquals("/platform/test/legacy", page.getOwnerId());
    Application app = (Application)page.getChildren().get(0);
    assertEquals("group#/platform/test/legacy:/web/IFramePortlet/blog", app.getInstanceId());

    PortletPreferences prefs = storage.getPortletPreferences(new ExoWindowID("group#/platform/test/legacy:/web/IFramePortlet/blog"));
    assertNotNull(prefs);
    assertEquals("/platform/test/legacy", prefs.getOwnerId());
    assertEquals("group#/platform/test/legacy:/web/IFramePortlet/blog", prefs.getWindowId());
  }

  public void testGroupWithNormalizedName() throws Exception {
    PageNavigation nav = storage.getPageNavigation("group::/platform/test/normalized");
    assertNotNull(nav);
    assertEquals("/platform/test/normalized", nav.getOwnerId());
    PageNode node = nav.getNodes().get(0);
    assertEquals("group::/platform/test/normalized::register", node.getPageReference());

    Page page = storage.getPage("group::/platform/test/normalized::register");
    assertNotNull(page);
    assertEquals("group::/platform/test/normalized::register", page.getPageId());
    assertEquals("/platform/test/normalized", page.getOwnerId());
    Application app = (Application)page.getChildren().get(0);
    assertEquals("group#/platform/test/normalized:/exoadmin/AccountPortlet/Account", app.getInstanceId());

    PortletPreferences prefs = storage.getPortletPreferences(new ExoWindowID("group#/platform/test/normalized:/exoadmin/AccountPortlet/Account"));
    assertNotNull(prefs);
    assertEquals("/platform/test/normalized", prefs.getOwnerId());
    assertEquals("group#/platform/test/normalized:/exoadmin/AccountPortlet/Account", prefs.getWindowId());
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
    assertEquals(2, nav.getNodes().size());

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

  public void testPageWithoutPageId() throws Exception {
    Page page = storage.getPage("portal::test::test2");
    assertNotNull(page);
    assertEquals("portal::test::test2", page.getPageId());
    assertEquals("test", page.getOwnerId());
    assertEquals("portal", page.getOwnerType());
    assertEquals("test2", page.getName());
  }

  public void testPage() throws Exception {
    Page page = storage.getPage("portal::test::test1");
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

  public void testFindPageByTitle() throws Exception {
    Query<Page> query = new Query<Page>(null, null, null, "TestTitle", Page.class);
    List<Page> list = storage.find(query).getAll();
    assertEquals("Expected two result instead of " + list, 2, list.size());
    Set<String> ids = new HashSet<String>(Arrays.asList(
      list.get(0).getPageId(),
      list.get(1).getPageId()));
    HashSet<String> expectedIds = new HashSet<String>(Arrays.asList(
      "group::/platform/test/legacy::register",
      "group::/platform/test/normalized::register"));
    assertEquals(expectedIds, ids);
  }

  public void testFindNavigation() throws Exception {
    Query<PageNavigation> query = new Query<PageNavigation>("group", null, null, null, PageNavigation.class);
    List<PageNavigation> list = storage.find(query).getAll();
    assertEquals("Expected 6 results instead of " + list, 6, list.size());
    Set<String> names = new HashSet<String>();
    for (PageNavigation navigation : list) {
      assertEquals("group", navigation.getOwnerType());
      names.add(navigation.getOwnerId());
    }
    HashSet<String> expectedNames = new HashSet<String>(Arrays.asList(
      "/platform/test/legacy",
      "/platform/test/normalized",
      "/platform/administrators",
      "/platform/guests",
      "/platform/users",
      "/organization/management/executive-board"));
    assertEquals(expectedNames, names);
  }

  public void testAnonymousPreference() throws Exception {
    Page page = storage.getPage("portal::test::test3");
    Application app = (Application)page.getChildren().get(0);
    String instanceId = app.getInstanceId();

    // Check instance id
    String[] chunks = Mapper.parseWindowId(instanceId);
    assertEquals("portal", chunks[0]);
    assertEquals("test", chunks[1]);
    assertEquals("web", chunks[2]);
    assertEquals("BannerPortlet", chunks[3]);
    assertTrue(chunks[4].startsWith("@"));

    // Check initial state
    PortletPreferences prefs = storage.getPortletPreferences(new ExoWindowID(instanceId));
    assertEquals(0, prefs.getPreferences().size());

    // Save state
    Preference pref = new Preference();
    pref.setName("foo");
    pref.setValues(new ArrayList<String>(Arrays.asList("foo1")));
    pref.setReadOnly(false);
    prefs.getPreferences().add(pref);
    storage.save(prefs);

    // Now save the page
    storage.save(page);

    // Check we have the same instance id
    page = storage.getPage(page.getPageId());
    app = (Application)page.getChildren().get(0);
//    assertEquals(instanceId, app.getInstanceId());

    // Now check state
    prefs = storage.getPortletPreferences(new ExoWindowID(app.getInstanceId()));
    assertEquals(1, prefs.getPreferences().size());
    assertEquals("foo", prefs.getPreferences().get(0).getName());
    assertEquals(1, prefs.getPreferences().get(0).getValues().size());
    assertEquals("foo1", prefs.getPreferences().get(0).getValues().get(0));
  }

  public void testSitePreferences() throws Exception {
    Page page = storage.getPage("portal::test::test4");
    Application app = (Application)page.getChildren().get(0);
    String instanceId = app.getInstanceId();

    // Check instance id
    String[] chunks = Mapper.parseWindowId(instanceId);
    assertEquals("portal", chunks[0]);
    assertEquals("test", chunks[1]);
    assertEquals("web", chunks[2]);
    assertEquals("BannerPortlet", chunks[3]);
    assertEquals("banner", chunks[4]);

    // Check initial state
    PortletPreferences prefs = storage.getPortletPreferences(new ExoWindowID(instanceId));
    assertEquals(1, prefs.getPreferences().size());
    assertEquals("template", prefs.getPreferences().get(0).getName());
    assertEquals(1, prefs.getPreferences().get(0).getValues().size());
    assertEquals("par:/groovy/groovy/webui/component/UIBannerPortlet.gtmpl", prefs.getPreferences().get(0).getValues().get(0));

    // Save state
    prefs.getPreferences().get(0).setValues(new ArrayList<String>(Arrays.asList("foo")));
    storage.save(prefs);

    // Now save the page
    storage.save(page);

    // Check we have the same instance id
    page = storage.getPage(page.getPageId());
    app = (Application)page.getChildren().get(0);
    assertEquals(instanceId, app.getInstanceId());

    // Now check state
    prefs = storage.getPortletPreferences(new ExoWindowID(instanceId));
    assertEquals(1, prefs.getPreferences().size());
    assertEquals("template", prefs.getPreferences().get(0).getName());
    assertEquals(1, prefs.getPreferences().get(0).getValues().size());
    assertEquals("foo", prefs.getPreferences().get(0).getValues().get(0));
  }
}
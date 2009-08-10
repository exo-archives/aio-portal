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
import org.exoplatform.portal.pom.config.POMSession;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Portal;
import org.exoplatform.portal.model.api.workspace.Page;
import org.exoplatform.portal.model.api.workspace.Navigation;
import org.exoplatform.portal.model.api.workspace.NavigationLink;
import org.exoplatform.portal.model.api.workspace.ui.UIContainer;
import org.exoplatform.portal.model.api.workspace.ui.UIComponent;
import org.exoplatform.portal.model.api.workspace.ui.UIWindow;
import org.exoplatform.portal.model.api.content.Content;
import org.exoplatform.portal.model.api.content.Customization;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.test.BasicTestCase;

import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by The eXo Platform SARL Author : Tung Pham thanhtungty@gmail.com Nov
 * 13, 2007
 */
public class TestSavedPOM extends BasicTestCase {

  /** . */
  private UserPortalConfigService portalConfigService;

  /** . */
  private DataStorage storage;

  /** . */
  private POMSessionManager mgr;

  public TestSavedPOM(String name) {
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

  public void testNavigation() throws Exception {
    POMSession session = mgr.openSession();
    Portal portal = session.getWorkspace().getSite(ObjectType.PORTAL, "test");
    assertNotNull(portal);

    //
    Navigation rootNavigation = portal.getRootNavigation();
    assertNotNull(rootNavigation);

    //
    Attributes rootAttrs = rootNavigation.getAttributes();
    assertEquals(1, (int)rootAttrs.getInteger("priority"));
    assertEquals("navigation_creator", rootAttrs.getString("creator"));
    assertEquals("navigation_modifier", rootAttrs.getString("modifier"));
    assertEquals("navigation_description", rootAttrs.getString("description"));

    //
    Collection<? extends Navigation> childrenNavigations = rootNavigation.getChildren();
    assertNotNull(childrenNavigations);
    assertEquals(1, childrenNavigations.size());
    Iterator<? extends Navigation> i = childrenNavigations.iterator();

    //
    assertTrue(i.hasNext());
    Navigation nodeNavigation = i.next();
    assertEquals(0, nodeNavigation.getChildren().size());
    assertNotNull(nodeNavigation);
    assertEquals("node_name", nodeNavigation.getName());
    Attributes nodeAttrs = nodeNavigation.getAttributes();
    assertEquals("node_uri", nodeAttrs.getString("uri"));
    assertEquals("node_label", nodeAttrs.getString("label"));
    assertEquals("node_icon", nodeAttrs.getString("icon"));
    GregorianCalendar start = new GregorianCalendar(2000, 2, 21, 1, 33, 0);
    start.setTimeZone(TimeZone.getTimeZone("UTC"));
    assertEquals(start.getTime(), nodeAttrs.getDate("start-publication-date"));
    GregorianCalendar end = new GregorianCalendar(2009, 2, 21, 1, 33, 0);
    end.setTimeZone(TimeZone.getTimeZone("UTC"));
    assertEquals(end.getTime(), nodeAttrs.getDate("end-publication-date"));
    assertEquals(true, (boolean)nodeAttrs.getBoolean("show-publication-date"));
    assertEquals(true, (boolean)nodeAttrs.getBoolean("visible"));

    //
    NavigationLink link = nodeNavigation.getLink();
//    assertNotNull(link);
  }

  public void testPortal() throws Exception {
    POMSession session = mgr.openSession();
    Portal portal = session.getWorkspace().getSite(ObjectType.PORTAL, "test");
    assertNotNull(portal);

    assertEquals("test", portal.getName());
    Attributes attrs = portal.getAttributes();
    assertEquals("en", attrs.getString("locale"));
    assertEquals("test_access_permissions", attrs.getString("access-permissions"));
    assertEquals("test_edit_permission", attrs.getString("edit-permission"));
    assertEquals("test_skin", attrs.getString("skin"));
    assertEquals("test_title", attrs.getString("title"));
    assertEquals("test_creator", attrs.getString("creator"));
    assertEquals("test_modifier", attrs.getString("modifier"));
    assertEquals("test_prop_value", attrs.getString("prop_key"));

    //
    mgr.closeSession();
  }

  public void testPage() throws Exception {
    POMSession session = mgr.openSession();
    Portal testPortal = session.getWorkspace().getSite(ObjectType.PORTAL, "test");
    assertNotNull(testPortal);

    //
    Page testRootPage = testPortal.getRootPage();
    assertNotNull(testRootPage);

    //
    Page testPage = testRootPage.getChild("test");
    assertNotNull(testPage);

    //
    Attributes testPageAttrs = testPage.getAttributes();
    assertEquals("test_title", testPageAttrs.getString("title"));
    assertEquals("test_factory_id", testPageAttrs.getString("factory-id"));
    assertEquals("test_access_permissions", testPageAttrs.getString("access-permissions"));
    assertEquals("test_edit_permission", testPageAttrs.getString("edit-permission"));
    assertEquals(true, (boolean)testPageAttrs.getBoolean("show-max-window"));
    assertEquals("test_creator", testPageAttrs.getString("creator"));
    assertEquals("test_modifier", testPageAttrs.getString("modifier"));

    //
    UIContainer c = testPage.getLayout();
    assertNotNull(c);
    Collection<? extends UIComponent> t = c.getComponents();
    assertNotNull(t);
    assertEquals(2, t.size());
    Iterator<? extends UIComponent> it =  t.iterator();

    //
    UIContainer container1 = (UIContainer)it.next();
    assertEquals("container_1", container1.getName());
    Attributes container1Attrs = container1.getAttributes();
    assertEquals("container_1_title", container1Attrs.getString("title"));
    assertEquals("container_1_icon", container1Attrs.getString("icon"));
    assertEquals("container_1_template", container1Attrs.getString("template"));
    assertEquals("container_1_access_permissions", container1Attrs.getString("access-permissions"));
    assertEquals("container_1_factory_id", container1Attrs.getString("factory-id"));
    assertEquals("container_1_decorator", container1Attrs.getString("decorator"));
    assertEquals("container_1_description", container1Attrs.getString("description"));
    assertEquals("container_1_width", container1Attrs.getString("width"));
    assertEquals("container_1_height", container1Attrs.getString("height"));

    //
    UIWindow application1 = (UIWindow)it.next();
    Attributes application1Attrs = application1.getAttributes();
    assertEquals("application_1_type", application1Attrs.getString("type"));
    assertEquals("application_1_theme", application1Attrs.getString("theme"));
    assertEquals("application_1_title", application1Attrs.getString("title"));
    assertEquals("application_1_access_permissions", application1Attrs.getString("access-permissions"));
    assertEquals(true, (boolean)application1Attrs.getBoolean("show-info-bar"));
    assertEquals(true, (boolean)application1Attrs.getBoolean("show-state"));
    assertEquals(true, (boolean)application1Attrs.getBoolean("show-mode"));
    assertEquals("application_1_description", application1Attrs.getString("description"));
    assertEquals("application_1_icon", application1Attrs.getString("icon"));
    assertEquals("application_1_width", application1Attrs.getString("width"));
    assertEquals("application_1_height", application1Attrs.getString("height"));
    assertEquals("application_1_prop_value", application1Attrs.getString("prop_key"));

    //
    Content content = application1.getContent();
    assertNotNull(content);
    assertEquals("application/portlet", content.getType().getMimeType());
    assertEquals("web/BannerPortlet/banner", content.getId());

    //
    mgr.closeSession();
  }





}
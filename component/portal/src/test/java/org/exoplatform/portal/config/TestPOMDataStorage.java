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
import org.exoplatform.portal.model.api.workspace.ui.UIContainer;
import org.exoplatform.portal.model.api.workspace.ui.UIComponent;
import org.exoplatform.portal.model.api.workspace.ui.UIWindow;
import org.exoplatform.portal.model.api.content.Content;
import org.exoplatform.portal.model.api.content.Customization;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.test.BasicTestCase;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by The eXo Platform SARL Author : Tung Pham thanhtungty@gmail.com Nov
 * 13, 2007
 */
public class TestPOMDataStorage extends BasicTestCase {

  /** . */
  private UserPortalConfigService portalConfigService;

  /** . */
  private DataStorage storage;

  /** . */
  private POMSessionManager mgr;

  public TestPOMDataStorage(String name) {
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

  public void testPage() throws Exception {

    POMSession session = mgr.openSession();
    Portal testPortal = session.getWorkspace().getSite(ObjectType.PORTAL, "test");
    assertNotNull(testPortal);
    Page testRootPage = testPortal.getRootPage();
    assertNotNull(testRootPage);
    Page testPage = testRootPage.getChild("test");
    assertNotNull(testPage);

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
    assertEquals("container_1_access_permission", container1Attrs.getString("access-permissions"));
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
    assertEquals("application_1_access_permission", application1Attrs.getString("access-permissions"));
    assertEquals(true, (boolean)application1Attrs.getBoolean("show-info-bar"));
    assertEquals(true, (boolean)application1Attrs.getBoolean("show-state"));
    assertEquals(true, (boolean)application1Attrs.getBoolean("show-mode"));
    assertEquals("application_1_description", application1Attrs.getString("description"));
    assertEquals("application_1_icon", application1Attrs.getString("icon"));
    assertEquals("application_1_width", application1Attrs.getString("width"));
    assertEquals("application_1_height", application1Attrs.getString("height"));
    assertEquals("application_1_prop_value", application1Attrs.getString("prop_key"));
    Content content = application1.getContent();
    assertNotNull(content);
    assertEquals("application/portlet", content.getType().getMimeType());
    assertEquals("web/BannerPortlet/banner", content.getId());


  }


}
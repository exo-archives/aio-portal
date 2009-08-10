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
package org.exoplatform.portal.pom.config.tasks;

import org.exoplatform.portal.config.model.Container;
import org.exoplatform.portal.config.model.Application;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.config.model.Properties;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.model.api.workspace.ui.UIContainer;
import org.exoplatform.portal.model.api.workspace.ui.UIWindow;
import org.exoplatform.portal.model.api.workspace.ui.UIComponent;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.api.workspace.Navigation;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.navigation.PageLink;
import org.exoplatform.portal.model.api.content.ContentManager;
import org.exoplatform.portal.model.api.content.FetchCondition;
import org.exoplatform.portal.model.api.content.Content;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.model.portlet.Preferences;
import static org.exoplatform.portal.pom.config.Utils.join;
import static org.exoplatform.portal.pom.config.Utils.split;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Mapper {

  /** . */
  private static final Set<String> portalPropertiesBlackList = new HashSet<String>(Arrays.asList(
    "locale", "access-permissions", "edit-permission", "skin", "title", "creator", "modifier"
  ));

  /** . */
  private static final Set<String> windowPropertiesBlackList = new HashSet<String>(Arrays.asList(
    "type", "theme", "title", "access-permissions", "show-info-bar", "show-state", "show-mode", "description",
    "icon", "width", "height"
  ));

  /** . */
  private final ContentManager contentManager;

  public Mapper(ContentManager contentManager) {
    this.contentManager = contentManager;
  }

  public void load(Navigation src, PageNavigation dst) {
    Site site = src.getSite();
    String ownerType = getOwnerType(site.getObjectType());
    String ownerId = site.getName();
    dst.setOwnerId(ownerId);
    dst.setOwnerType(ownerType);
    Attributes attrs = src.getAttributes();
    dst.setCreator(attrs.getString("creator"));
    dst.setModifier(attrs.getString("modifier"));
    dst.setDescription(attrs.getString("description"));
    Integer priority = attrs.getInteger("priority");
    if (priority != null) {
      dst.setPriority(priority);
    }
    Collection<? extends Navigation> children = src.getChildren();
    for (Navigation srcChild : children) {
      PageNode dstChild = new PageNode();
      load(srcChild, dstChild);
      dst.addNode(dstChild);
    }
  }

  private PageNode load(Navigation src, PageNode dst) {
    Attributes attrs = src.getAttributes();
    dst.setName(src.getName());
    dst.setLabel(attrs.getString("label"));
    dst.setIcon(attrs.getString("icon"));
    dst.setUri(attrs.getString("uri"));
    dst.setStartPublicationDate(attrs.getDate("start-publication-date"));
    dst.setEndPublicationDate(attrs.getDate("end-publication-date"));
    dst.setShowPublicationDate(attrs.getBoolean("show-publication-date"));
    dst.setVisible(attrs.getBoolean("visible"));
    dst.setPageReference(attrs.getString("page-reference"));
    dst.setChildren(new ArrayList<PageNode>());
    for (Navigation srcChild : src.getChildren()) {
      PageNode dstChild = new PageNode();
      load(srcChild, dstChild);
      dst.getChildren().add(dstChild);
    }
    return dst;
  }

  public static void save(PageNavigation pageNav, Navigation nav) {
    Attributes attrs = nav.getAttributes();
    attrs.setInteger("priority", pageNav.getPriority());
    attrs.setString("creator", pageNav.getCreator());
    attrs.setString("modifier", pageNav.getModifier());
    attrs.setString("description", pageNav.getDescription());
    for (PageNode node : pageNav.getNodes()) {
      Navigation childNav = nav.addChild(node.getName());
      save(node, childNav);
    }
  }

  public static void save(PageNode node, Navigation nav) {
    Workspace workspace = nav.getSite().getWorkspace();
    String reference = node.getPageReference();
    if (reference != null) {
      String[] pageChunks = split("::", reference);
      ObjectType<? extends Site> siteType = parseSiteType(pageChunks[0]);
      Site site = workspace.getSite(siteType, pageChunks[1]);
      org.exoplatform.portal.model.api.workspace.Page target = site.getRootPage().getChild(pageChunks[2]);
      PageLink link = nav.link(ObjectType.PAGE_LINK);
      link.setPage(target);
    }

    //
    Attributes attrs = nav.getAttributes();
    attrs.setString("uri", node.getUri());
    attrs.setString("label", node.getLabel());
    attrs.setString("icon", node.getIcon());
    attrs.setDate("start-publication-date", node.getStartPublicationDate());
    attrs.setDate("end-publication-date", node.getEndPublicationDate());
    attrs.setBoolean("show-publication-date", node.isShowPublicationDate());
    attrs.setBoolean("visible", node.isVisible());
    attrs.setString("page-reference", node.getPageReference());
    if (node.getChildren() != null) {
      for (PageNode childNode : node.getChildren()) {
        Navigation childNav = nav.addChild(node.getName());
        save(childNode, childNav);
      }
    }
  }

  public void load(Site src, PortalConfig dst) {
    dst.setName(src.getName());
    Attributes attrs = src.getAttributes();
    dst.setLocale(attrs.getString("locale"));
    dst.setAccessPermissions(split("|", attrs.getString("access-permissions")));
    dst.setEditPermission(attrs.getString("edit-permission"));
    dst.setSkin(attrs.getString("skin"));
    dst.setTitle(attrs.getString("title"));
    dst.setCreator(attrs.getString("creator"));
    dst.setModifier(attrs.getString("modifier"));
    Properties properties = new Properties();
    load(attrs, properties, portalPropertiesBlackList);
    dst.setProperties(properties);
  }

  public void save(PortalConfig src, Site dst) {
    Attributes attrs = dst.getAttributes();
    attrs.setString("locale", src.getLocale());
    attrs.setString("access-permissions", join("|", src.getAccessPermissions()));
    attrs.setString("edit-permission", src.getEditPermission());
    attrs.setString("skin", src.getSkin());
    attrs.setString("title", src.getTitle());
    attrs.setString("creator", src.getCreator());
    attrs.setString("modifier", src.getModifier());
    if (src.getProperties() != null) {
      save(src.getProperties(), attrs);
    }
  }

  public void load(org.exoplatform.portal.model.api.workspace.Page src, Page dst) {
    Site site = src.getSite();
    String ownerType = getOwnerType(site.getObjectType());
    String ownerId = site.getName();
    String name = src.getName();
    String pageId = join("::", ownerType, ownerId, name);

    //
    Attributes attrs = src.getAttributes();
    dst.setId(pageId);
    dst.setOwnerId(ownerId);
    dst.setOwnerType(ownerType);
    dst.setName(name);
    dst.setTitle(attrs.getString("title"));
    dst.setShowMaxWindow(attrs.getBoolean("show-max-window"));
    dst.setCreator(attrs.getString("creator"));
    dst.setModifier(attrs.getString("modifier"));
    dst.setAccessPermissions(split("|", attrs.getString("access-permissions")));
    dst.setEditPermission(attrs.getString("edit-permission"));
    dst.setFactoryId(attrs.getString("factory-id"));

    //
    loadChildren(src.getLayout(), dst);
  }

  public void load(UIContainer src, Container dst) {
    dst.setName(src.getName());
    Attributes attrs = src.getAttributes();
    dst.setTitle(attrs.getString("title"));
    dst.setIcon(attrs.getString("icon"));
    dst.setTemplate(attrs.getString("template"));
    dst.setAccessPermissions(split("|", attrs.getString("access-permissions")));
    dst.setFactoryId(attrs.getString("factory-id"));
    dst.setDecorator(attrs.getString("decorator"));
    dst.setDescription(attrs.getString("description"));
    dst.setWidth(attrs.getString("width"));
    dst.setHeight(attrs.getString("height"));

    //
    loadChildren(src, dst);
  }

  public void loadChildren(UIContainer src, Container dst) {
    for (UIComponent component : src.getComponents()) {
      if (component instanceof UIContainer) {
        UIContainer srcContainer = (UIContainer)component;
        Container dstContainer = new Container();
        load(srcContainer, dstContainer);
        dst.getChildren().add(dstContainer);
      } else if (component instanceof UIWindow) {
        UIWindow window = (UIWindow)component;
        Application application = new Application();
        load(window, application);
        dst.getChildren().add(application);
      } else {
        throw new AssertionError();
      }
    }
  }

  public void load(UIWindow src, Application dst) {
    Attributes attrs = src.getAttributes();
    dst.setApplicationType(attrs.getString("type"));
    dst.setTheme(attrs.getString("theme"));
    dst.setTitle(attrs.getString("title"));
    dst.setAccessPermissions(split("|", attrs.getString("access-permissions")));
    dst.setShowInfoBar(attrs.getBoolean("show-info-bar"));
    dst.setShowApplicationState(attrs.getBoolean("show-state"));
    dst.setShowApplicationMode(attrs.getBoolean("show-mode"));
    dst.setDescription(attrs.getString("description"));
    dst.setIcon(attrs.getString("icon"));
    dst.setWidth(attrs.getString("width"));
    dst.setHeight(attrs.getString("height"));
    load(attrs, dst.getProperties(), windowPropertiesBlackList);

    //
    Content content = src.getContent();
    if (content != null) {
      Site site = src.getPage().getSite();
      String instanceId = getOwnerType(site.getObjectType()) + "#" + site.getName() + ":/" + content.getId();
      dst.setInstanceId(instanceId);
    } else {
      dst.setInstanceId(null);
    }
  }

  public void save(Page src, org.exoplatform.portal.model.api.workspace.Page dst) {
    Attributes attrs = dst.getAttributes();
    attrs.setString("title", src.getTitle());
    attrs.setString("factory-id", src.getFactoryId());
    attrs.setString("access-permissions", join("|", src.getAccessPermissions()));
    attrs.setString("edit-permission", src.getEditPermission());
    attrs.setBoolean("show-max-window", src.isShowMaxWindow());
    attrs.setString("creator", src.getCreator());
    attrs.setString("modifier", src.getModifier());
    
    //
    saveChildren(src, dst.getLayout());
  }

  public void save(Container src, UIContainer dst) {
    Attributes dstAttrs = dst.getAttributes();
    dstAttrs.setString("title", src.getTitle());
    dstAttrs.setString("icon", src.getIcon());
    dstAttrs.setString("template", src.getTemplate());
    dstAttrs.setString("access-permissions", join("|", src.getAccessPermissions()));
    dstAttrs.setString("factory-id", src.getFactoryId());
    dstAttrs.setString("decorator", src.getDecorator());
    dstAttrs.setString("description", src.getDescription());
    dstAttrs.setString("width", src.getWidth());
    dstAttrs.setString("height", src.getHeight());

    //
    saveChildren(src, dst);
  }

  private void saveChildren(Container src, UIContainer dst) {
    dst.getComponents().clear();
    ArrayList<Object> srcChildren = src.getChildren();
    if (srcChildren != null) {
      for (Object srcChild : srcChildren) {
        if (srcChild instanceof Container) {
          Container srcChildContainer = (Container)srcChild;
          UIContainer dstChildContainer = dst.addComponent(ObjectType.CONTAINER, srcChildContainer.getName());
          save(srcChildContainer, dstChildContainer);
        } else if (srcChild instanceof Application) {
          Application application = (Application)srcChild;
          String id = UUID.randomUUID().toString();
          UIWindow dstChildWindow = dst.addComponent(ObjectType.WINDOW, id);
          save(application, dstChildWindow);
        } else {
          throw new AssertionError("Was not expecting child " + srcChild);
        }
      }
    }
  }

  public void save(Application src, UIWindow dst) {
    Attributes attrs = dst.getAttributes();
    attrs.setString("type", src.getApplicationType());
    attrs.setString("theme", src.getTheme());
    attrs.setString("title", src.getTitle());
    attrs.setString("access-permissions", join("|", src.getAccessPermissions()));
    attrs.setBoolean("show-info-bar", src.getShowInfoBar());
    attrs.setBoolean("show-state", src.getShowApplicationState());
    attrs.setBoolean("show-mode", src.getShowApplicationMode());
    attrs.setString("description", src.getDescription());
    attrs.setString("icon", src.getIcon());
    attrs.setString("width", src.getWidth());
    attrs.setString("height", src.getHeight());
    save(src.getProperties(), attrs);

    //
    String instanceId = src.getInstanceId();
    String contentId = parseContentId(instanceId);
    Content content = contentManager.getContent(Preferences.CONTENT_TYPE, contentId, FetchCondition.ALWAYS);
    dst.setContent(content);
  }

  static String parseContentId(String windowId) {
    String[] persistenceChunks = org.exoplatform.portal.pom.config.Utils.split(":/", windowId);
    return persistenceChunks[persistenceChunks.length - 1];
  }

  public static void load(Attributes src, Properties dst, Set<String> blackList) {
    for (String name : src.getKeys()) {
      if (!blackList.contains(name)) {
        Object value = src.getObject(name);
        if (value instanceof String) {
          dst.setProperty(name, (String)value);
        }
      }
    }
  }

  public static void save(Properties src, Attributes dst) {
    for (Map.Entry<String, String> property : src.entrySet()) {
      dst.setString(property.getKey(), property.getValue());
    }
  }

  public static String getOwnerType(ObjectType<? extends Site> siteType) {
    if (siteType == ObjectType.PORTAL) {
      return PortalConfig.PORTAL_TYPE;
    } else if (siteType == ObjectType.GROUP) {
      return PortalConfig.GROUP_TYPE;
    } else if (siteType == ObjectType.USER) {
      return PortalConfig.USER_TYPE;
    } else {
      throw new IllegalArgumentException("Invalid site type " + siteType);
    }
  }

  public static ObjectType<? extends Site> parseSiteType(String ownerType) {
    if (ownerType.equals(PortalConfig.PORTAL_TYPE)) {
      return ObjectType.PORTAL;
    } else if (ownerType.equals(PortalConfig.GROUP_TYPE)) {
      return ObjectType.GROUP;
    } else if (ownerType.equals(PortalConfig.USER_TYPE)) {
      return ObjectType.USER;
    } else {
      throw new IllegalArgumentException("Invalid owner type " + ownerType);
    }
  }
}

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
import org.exoplatform.portal.config.model.PageBody;
import org.exoplatform.portal.model.api.workspace.ui.UIContainer;
import org.exoplatform.portal.model.api.workspace.ui.UIWindow;
import org.exoplatform.portal.model.api.workspace.ui.UIComponent;
import org.exoplatform.portal.model.api.workspace.ui.UIInsertion;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.api.workspace.Navigation;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.workspace.navigation.PageLink;
import org.exoplatform.portal.model.api.content.ContentManager;
import org.exoplatform.portal.model.api.content.FetchCondition;
import org.exoplatform.portal.model.api.content.Content;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.model.util.Key;
import org.exoplatform.portal.model.util.ValueType;
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
import java.util.Date;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Mapper {

  /** . */
  public static final Key<String> NAME = Key.create("name", ValueType.STRING);

  /** . */
  public static final Key<Boolean> SHOW_MAX_WINDOW = Key.create("show-max-window", ValueType.BOOLEAN);

  /** . */
  public static final Key<String> TITLE = Key.create("title", ValueType.STRING);

  /** . */
  public static final Key<String> FACTORY_ID = Key.create("factory-id", ValueType.STRING);

  /** . */
  public static final Key<String> ACCESS_PERMISSIONS = Key.create("access-permissions", ValueType.STRING);

  /** . */
  public static final Key<String> EDIT_PERMISSION = Key.create("edit-permission", ValueType.STRING);

  /** . */
  public static final Key<String> CREATOR = Key.create("creator", ValueType.STRING);

  /** . */
  public static final Key<String> MODIFIER = Key.create("modifier", ValueType.STRING);

  /** . */
  public static final Key<String> DESCRIPTION = Key.create("description", ValueType.STRING);

  /** . */
  public static final Key<String> DECORATOR = Key.create("decorator", ValueType.STRING);

  /** . */
  public static final Key<Integer> PRIORITY = Key.create("priority", ValueType.INTEGER);

  /** . */
  public static final Key<String> LABEL = Key.create("label", ValueType.STRING);

  /** . */
  public static final Key<String> ICON = Key.create("icon", ValueType.STRING);

  /** . */
  public static final Key<String> URI = Key.create("uri", ValueType.STRING);

  /** . */
  public static final Key<Date> START_PUBLICATION_DATE = Key.create("start-publication-date", ValueType.DATE);

  /** . */
  public static final Key<Date> END_PUBLICATION_DATE = Key.create("end-publication-date", ValueType.DATE);

  /** . */
  public static final Key<Boolean> VISIBLE = Key.create("visible", ValueType.BOOLEAN);

  /** . */
  public static final Key<String> PAGE_REFERENCE = Key.create("page-reference", ValueType.STRING);

  /** . */
  public static final Key<String> TEMPLATE = Key.create("template", ValueType.STRING);

  /** . */
  public static final Key<Boolean> SHOW_PUBLICATION_DATE = Key.create("show-publication-date", ValueType.BOOLEAN);

  /** . */
  public static final Key<Boolean> SHOW_INFO_BAR = Key.create("show-info-bar", ValueType.BOOLEAN);

  /** . */
  public static final Key<Boolean> SHOW_STATE = Key.create("show-state", ValueType.BOOLEAN);

  /** . */
  public static final Key<Boolean> SHOW_MODE = Key.create("show-mode", ValueType.BOOLEAN);

  /** . */
  public static final Key<String> LOCALE = Key.create("locale", ValueType.STRING);

  /** . */
  public static final Key<String> SKIN = Key.create("skin", ValueType.STRING);

  /** . */
  public static final Key<String> WIDTH = Key.create("width", ValueType.STRING);

  /** . */
  public static final Key<String> HEIGHT = Key.create("height", ValueType.STRING);

  /** . */
  public static final Key<String> TYPE = Key.create("type", ValueType.STRING);

  /** . */
  public static final Key<String> THEME = Key.create("theme", ValueType.STRING);

  /** . */
  private static final Set<String> portalPropertiesBlackList = new HashSet<String>(Arrays.asList(
    "jcr:uuid", "jcr:primaryType",
    LOCALE.getName(), ACCESS_PERMISSIONS.getName(), EDIT_PERMISSION.getName(), SKIN.getName(),
    TITLE.getName(), CREATOR.getName(), MODIFIER.getName()
  ));

  /** . */
  private static final Set<String> windowPropertiesBlackList = new HashSet<String>(Arrays.asList(
    "jcr:uuid", "jcr:primaryType",
    TYPE.getName(), THEME.getName(), TITLE.getName(), ACCESS_PERMISSIONS.getName(), SHOW_INFO_BAR.getName(),
    SHOW_STATE.getName(), SHOW_MODE.getName(), DESCRIPTION.getName(),
    ICON.getName(), WIDTH.getName(), HEIGHT.getName()
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
    dst.setCreator(attrs.getValue(CREATOR));
    dst.setModifier(attrs.getValue(MODIFIER));
    dst.setDescription(attrs.getValue(DESCRIPTION));
    Integer priority = attrs.getValue(PRIORITY);
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
    dst.setLabel(attrs.getValue(LABEL));
    dst.setIcon(attrs.getValue(ICON));
    dst.setUri(attrs.getValue(URI));
    dst.setStartPublicationDate(attrs.getValue(START_PUBLICATION_DATE));
    dst.setEndPublicationDate(attrs.getValue(END_PUBLICATION_DATE));
    dst.setShowPublicationDate(attrs.getValue(SHOW_PUBLICATION_DATE));
    dst.setVisible(attrs.getValue(VISIBLE));
    dst.setPageReference(attrs.getValue(PAGE_REFERENCE));
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
    attrs.setValue(PRIORITY, pageNav.getPriority());
    attrs.setValue(CREATOR, pageNav.getCreator());
    attrs.setValue(MODIFIER, pageNav.getModifier());
    attrs.setValue(DESCRIPTION, pageNav.getDescription());
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
    attrs.setValue(URI, node.getUri());
    attrs.setValue(LABEL, node.getLabel());
    attrs.setValue(ICON, node.getIcon());
    attrs.setValue(START_PUBLICATION_DATE, node.getStartPublicationDate());
    attrs.setValue(END_PUBLICATION_DATE, node.getEndPublicationDate());
    attrs.setValue(SHOW_PUBLICATION_DATE, node.isShowPublicationDate());
    attrs.setValue(VISIBLE, node.isVisible());
    attrs.setValue(PAGE_REFERENCE, node.getPageReference());
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
    dst.setLocale(attrs.getValue(LOCALE));
    dst.setAccessPermissions(split("|", attrs.getValue(ACCESS_PERMISSIONS)));
    dst.setEditPermission(attrs.getValue(EDIT_PERMISSION));
    dst.setSkin(attrs.getValue(SKIN));
    dst.setTitle(attrs.getValue(TITLE));
    dst.setCreator(attrs.getValue(CREATOR));
    dst.setModifier(attrs.getValue(MODIFIER));
    Properties properties = new Properties();
    load(attrs, properties, portalPropertiesBlackList);
    dst.setProperties(properties);

    //
    if (src.getObjectType() == ObjectType.PORTAL) {
      org.exoplatform.portal.model.api.workspace.Page template = src.getRootNavigation().getTemplate();
      load(template.getLayout(), dst.getPortalLayout());
    }
  }

  public void save(PortalConfig src, Site dst) {
    Attributes attrs = dst.getAttributes();
    attrs.setValue(LOCALE, src.getLocale());
    attrs.setValue(ACCESS_PERMISSIONS, join("|", src.getAccessPermissions()));
    attrs.setValue(EDIT_PERMISSION, src.getEditPermission());
    attrs.setValue(SKIN, src.getSkin());
    attrs.setValue(TITLE, src.getTitle());
    attrs.setValue(CREATOR, src.getCreator());
    attrs.setValue(MODIFIER, src.getModifier());
    if (src.getProperties() != null) {
      save(src.getProperties(), attrs);
    }

    //
    if (dst.getObjectType() == ObjectType.PORTAL) {
      org.exoplatform.portal.model.api.workspace.Page templates = dst.getRootPage().getChild("templates");
      org.exoplatform.portal.model.api.workspace.Page template = templates.addChild("default");
      save(src.getPortalLayout(), template.getLayout());

      //
      dst.getRootNavigation().setTemplate(template);
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
    dst.setTitle(attrs.getValue(TITLE));
    dst.setShowMaxWindow(attrs.getValue(SHOW_MAX_WINDOW, false));
    dst.setCreator(attrs.getValue(CREATOR));
    dst.setModifier(attrs.getValue(MODIFIER));
    dst.setAccessPermissions(split("|", attrs.getValue(ACCESS_PERMISSIONS)));
    dst.setEditPermission(attrs.getValue(EDIT_PERMISSION));
    dst.setFactoryId(attrs.getValue(FACTORY_ID));

    //
    loadChildren(src.getLayout(), dst);
  }

  public void load(UIContainer src, Container dst) {
    Attributes attrs = src.getAttributes();
    dst.setName(attrs.getValue(NAME));
    dst.setTitle(attrs.getValue(TITLE));
    dst.setIcon(attrs.getValue(ICON));
    dst.setTemplate(attrs.getValue(TEMPLATE));
    dst.setAccessPermissions(split("|", attrs.getValue(ACCESS_PERMISSIONS)));
    dst.setFactoryId(attrs.getValue(FACTORY_ID));
    dst.setDecorator(attrs.getValue(DECORATOR));
    dst.setDescription(attrs.getValue(DESCRIPTION));
    dst.setWidth(attrs.getValue(WIDTH));
    dst.setHeight(attrs.getValue(HEIGHT));

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
      } else if (component instanceof UIInsertion) {
        dst.getChildren().add(new PageBody());
      } else {
        throw new AssertionError();
      }
    }
  }

  public void load(UIWindow src, Application dst) {
    Attributes attrs = src.getAttributes();
    dst.setApplicationType(attrs.getValue(TYPE));
    dst.setTheme(attrs.getValue(THEME));
    dst.setTitle(attrs.getValue(TITLE));
    dst.setAccessPermissions(split("|", attrs.getValue(ACCESS_PERMISSIONS)));
    dst.setShowInfoBar(attrs.getValue(SHOW_INFO_BAR));
    dst.setShowApplicationState(attrs.getValue(SHOW_STATE));
    dst.setShowApplicationMode(attrs.getValue(SHOW_MODE));
    dst.setDescription(attrs.getValue(DESCRIPTION));
    dst.setIcon(attrs.getValue(ICON));
    dst.setWidth(attrs.getValue(WIDTH));
    dst.setHeight(attrs.getValue(HEIGHT));
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
    attrs.setValue(TITLE, src.getTitle());
    attrs.setValue(FACTORY_ID, src.getFactoryId());
    attrs.setValue(ACCESS_PERMISSIONS, join("|", src.getAccessPermissions()));
    attrs.setValue(EDIT_PERMISSION, src.getEditPermission());
    attrs.setValue(SHOW_MAX_WINDOW, src.isShowMaxWindow());
    attrs.setValue(CREATOR, src.getCreator());
    attrs.setValue(MODIFIER, src.getModifier());
    
    //
    saveChildren(src, dst.getLayout());
  }

  public void save(Container src, UIContainer dst) {
    Attributes dstAttrs = dst.getAttributes();
    dstAttrs.setValue(TITLE, src.getTitle());
    dstAttrs.setValue(ICON, src.getIcon());
    dstAttrs.setValue(TEMPLATE, src.getTemplate());
    dstAttrs.setValue(ACCESS_PERMISSIONS, join("|", src.getAccessPermissions()));
    dstAttrs.setValue(FACTORY_ID, src.getFactoryId());
    dstAttrs.setValue(DECORATOR, src.getDecorator());
    dstAttrs.setValue(DESCRIPTION, src.getDescription());
    dstAttrs.setValue(WIDTH, src.getWidth());
    dstAttrs.setValue(HEIGHT, src.getHeight());
    dstAttrs.setValue(NAME, src.getName());

    //
    saveChildren(src, dst);
  }

  private void saveChildren(Container src, UIContainer dst) {
    dst.getComponents().clear();
    ArrayList<Object> srcChildren = src.getChildren();
    if (srcChildren != null) {
      for (Object srcChild : srcChildren) {
        // Perhaps use instead an ordinal ?????
        String id = UUID.randomUUID().toString();
        if (srcChild instanceof Container) {
          Container srcChildContainer = (Container)srcChild;
          UIContainer dstChildContainer = dst.addComponent(ObjectType.CONTAINER, id);
          save(srcChildContainer, dstChildContainer);
        } else if (srcChild instanceof Application) {
          Application application = (Application)srcChild;
          UIWindow dstChildWindow = dst.addComponent(ObjectType.WINDOW, id);
          save(application, dstChildWindow);
        } else if (srcChild instanceof PageBody) {
          dst.addComponent(ObjectType.INSERTION, id);
        } else {
          throw new AssertionError("Was not expecting child " + srcChild);
        }
      }
    }
  }

  public void save(Application src, UIWindow dst) {
    Attributes attrs = dst.getAttributes();
    attrs.setValue(TYPE, src.getApplicationType());
    attrs.setValue(THEME, src.getTheme());
    attrs.setValue(TITLE, src.getTitle());
    attrs.setValue(ACCESS_PERMISSIONS, join("|", src.getAccessPermissions()));
    attrs.setValue(SHOW_INFO_BAR, src.getShowInfoBar());
    attrs.setValue(SHOW_STATE, src.getShowApplicationState());
    attrs.setValue(SHOW_MODE, src.getShowApplicationMode());
    attrs.setValue(DESCRIPTION, src.getDescription());
    attrs.setValue(ICON, src.getIcon());
    attrs.setValue(WIDTH, src.getWidth());
    attrs.setValue(HEIGHT, src.getHeight());
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

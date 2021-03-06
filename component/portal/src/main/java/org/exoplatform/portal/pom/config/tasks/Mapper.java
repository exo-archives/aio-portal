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
import static org.exoplatform.portal.pom.config.Utils.join;
import static org.exoplatform.portal.pom.config.Utils.split;
import org.exoplatform.portal.pom.config.POMSession;
import org.gatein.mop.api.content.Customization;
import org.gatein.mop.api.Key;
import org.gatein.mop.api.Attributes;
import org.gatein.mop.api.ValueType;
import org.gatein.mop.api.workspace.Navigation;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.workspace.link.Link;
import org.gatein.mop.api.workspace.link.PageLink;
import org.gatein.mop.api.workspace.ui.UIContainer;
import org.gatein.mop.api.workspace.ui.UIComponent;
import org.gatein.mop.api.workspace.ui.UIWindow;
import org.gatein.mop.api.workspace.ui.UIBody;
import org.gatein.mop.core.content.portlet.Preferences;
import org.gatein.mop.core.content.portlet.PreferencesBuilder;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Collections;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Mapper {

  /** . */
  public static final Key<String> ID = Key.create("id", ValueType.STRING);
  
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
  private final POMSession session;

  public Mapper(POMSession session) {
    this.session = session;
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
    dst.setChildren(new ArrayList<PageNode>());

    //
    Link link = src.getLink();
    if (link instanceof PageLink) {
      PageLink pageLink = (PageLink)link;
      org.gatein.mop.api.workspace.Page target = pageLink.getPage();
      if (target != null) {
        Site site = target.getSite();
        ObjectType<? extends Site> type = site.getObjectType();
        String pageRef = getOwnerType(type) + "::" + site.getName() + "::" + target.getName();
        dst.setPageReference(pageRef);
      }
    }

    //
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
      org.gatein.mop.api.workspace.Page target = site.getRootPage().getChild("pages").getChild(pageChunks[2]);
      PageLink link = nav.linkTo(ObjectType.PAGE_LINK);
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
    if (node.getChildren() != null) {
      for (PageNode childNode : node.getChildren()) {
        Navigation childNav = nav.addChild(childNode.getName());
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
    if (src.getObjectType() == ObjectType.PORTAL_SITE) {
      org.gatein.mop.api.workspace.Page template = src.getRootNavigation().getTemplate();
      load(template.getRootComponent(), dst.getPortalLayout());
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
    if (dst.getObjectType() == ObjectType.PORTAL_SITE) {
      org.gatein.mop.api.workspace.Page templates = dst.getRootPage().getChild("templates");
      org.gatein.mop.api.workspace.Page template = templates.addChild("default");

      //
      save(src.getPortalLayout(), template.getRootComponent(), Collections.<String, Preferences>emptyMap());

      //
      dst.getRootNavigation().setTemplate(template);
    }
  }

  public void load(org.gatein.mop.api.workspace.Page src, Page dst) {
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
    loadChildren(src.getRootComponent(), dst);
  }

  public void load(UIContainer src, Container dst) {
    Attributes attrs = src.getAttributes();
    dst.setId(attrs.getValue(ID));
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
    for (UIComponent component : src.getChildren()) {
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
      } else if (component instanceof UIBody) {
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
    Customization<?> customization = src.getCustomization();
    Site site = src.getPage().getSite();
    String ownerType = getOwnerType(site.getObjectType());
    String ownerId = site.getName();
    String contentId = customization.getContentId();
    String instanceName = customization.getName() != null ? customization.getName() : "@" +src.getObjectId();

    //
    String instanceId = ownerType + "#" + ownerId + ":/" + contentId + "/" + instanceName;
    dst.setInstanceId(instanceId);
  }

  private static Map<String, Preferences> collectPreferences(UIContainer container) {
    Map<String, Preferences> preferencesMap = new HashMap<String, Preferences>();
    collectPrefs(container, preferencesMap);
    return preferencesMap;
  }

  private static void collectPrefs(UIContainer container, Map<String, Preferences> preferencesMap) {
    for (UIComponent component : container.getChildren()) {
      if (component instanceof UIContainer) {
        collectPrefs((UIContainer)component, preferencesMap);
      } else if (component instanceof UIWindow) {
        UIWindow window = (UIWindow)component;
        Preferences preferences = (Preferences)window.getCustomization().getState();
        if (preferences != null) {
          if (preferences.getEntries().size() > 0) {
            preferencesMap.put(window.getObjectId(), preferences);
          }
        }
      }
    }
  }

  public void save(Page src, org.gatein.mop.api.workspace.Page dst) {
    Attributes attrs = dst.getAttributes();
    attrs.setValue(TITLE, src.getTitle());
    attrs.setValue(FACTORY_ID, src.getFactoryId());
    attrs.setValue(ACCESS_PERMISSIONS, join("|", src.getAccessPermissions()));
    attrs.setValue(EDIT_PERMISSION, src.getEditPermission());
    attrs.setValue(SHOW_MAX_WINDOW, src.isShowMaxWindow());
    attrs.setValue(CREATOR, src.getCreator());
    attrs.setValue(MODIFIER, src.getModifier());

    //
    Map<String, Preferences> preferencesMap = collectPreferences(dst.getRootComponent());

    //
    saveChildren(src, dst.getRootComponent(), preferencesMap);
  }

  public void save(Container src, UIContainer dst, Map<String, Preferences> preferencesMap) {
    Attributes dstAttrs = dst.getAttributes();
    dstAttrs.setValue(ID, src.getId());
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
    saveChildren(src, dst, preferencesMap);
  }

  private void saveChildren(Container src, UIContainer dst, Map<String, Preferences> preferencesMap) {
    // Erase all children
    dst.getChildren().clear();

    // Create new children
    ArrayList<Object> srcChildren = src.getChildren();
    if (srcChildren != null) {
      for (Object srcChild : srcChildren) {
        // Perhaps use instead an ordinal ?????
        String id = UUID.randomUUID().toString();
        if (srcChild instanceof Container) {
          Container srcChildContainer = (Container)srcChild;
          UIContainer dstChildContainer = dst.addChild(ObjectType.CONTAINER, id);
          save(srcChildContainer, dstChildContainer, preferencesMap);
        } else if (srcChild instanceof Application) {
          Application application = (Application)srcChild;
          UIWindow dstChildWindow = dst.addChild(ObjectType.WINDOW, id);
          save(application, dstChildWindow, preferencesMap);
        } else if (srcChild instanceof PageBody) {
          dst.addChild(ObjectType.BODY, id);
        } else {
          throw new AssertionError("Was not expecting child " + srcChild);
        }
      }
    }
  }

  public void save(Application src, UIWindow dst, Map<String, Preferences> preferencesMap) {
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
    String[] chunks = parseWindowId(instanceId);

    //
    if (chunks[4].startsWith("@")) {
      String id = chunks[4].substring(1);
      Preferences prefs = preferencesMap.get(id);
      if (prefs != null) {
        dst.customize(Preferences.CONTENT_TYPE, chunks[2] + "/" + chunks[3], prefs);
      } else {
        dst.customize(Preferences.CONTENT_TYPE, chunks[2] + "/" + chunks[3], null);
      }
    } else {
      ObjectType siteType = parseSiteType(chunks[0]);
      Site site = session.getWorkspace().getSite(siteType, chunks[1]);

      //
      Customization<?> customization = null;
      if (site == null) {
        System.out.println("Could not configure window because corresponding site does not exist " + site);
      } else {
        customization = site.getCustomization(chunks[4]);
      }

      //
      if (customization != null) {
        dst.customize(customization);
      } else {
        System.out.println("Could not configure the window " + dst.getName() + " with portlet " + instanceId +
          " that is not available");
        // We set null on purpose, this way, the PortletPreferencesTask.Load will return null
        // to the caller that will use the default portlet prefs
        // this behavior is for the eXo PC, when JBoss PC is integrated we need to revise that option
        dst.customize(Preferences.CONTENT_TYPE, chunks[2] + "/" + chunks[3], null);
      }
    }
  }

  public static String[] parseWindowId(String windowId) {
    int i0 = windowId.indexOf("#");
    int i1 = windowId.indexOf(":/", i0 + 1);
    String ownerType = windowId.substring(0, i0);
    String ownerId = windowId.substring(i0 + 1, i1);
    String persistenceid = windowId.substring(i1 + 2);
    String[] persistenceChunks = split("/", persistenceid);
    return new String[]{
      ownerType,
      ownerId,
      persistenceChunks[0],
      persistenceChunks[1],
      persistenceChunks[2]
    };
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
    if (siteType == ObjectType.PORTAL_SITE) {
      return PortalConfig.PORTAL_TYPE;
    } else if (siteType == ObjectType.GROUP_SITE) {
      return PortalConfig.GROUP_TYPE;
    } else if (siteType == ObjectType.USER_SITE) {
      return PortalConfig.USER_TYPE;
    } else {
      throw new IllegalArgumentException("Invalid site type " + siteType);
    }
  }

  public static ObjectType<? extends Site> parseSiteType(String ownerType) {
    if (ownerType.equals(PortalConfig.PORTAL_TYPE)) {
      return ObjectType.PORTAL_SITE;
    } else if (ownerType.equals(PortalConfig.GROUP_TYPE)) {
      return ObjectType.GROUP_SITE;
    } else if (ownerType.equals(PortalConfig.USER_TYPE)) {
      return ObjectType.USER_SITE;
    } else {
      throw new IllegalArgumentException("Invalid owner type " + ownerType);
    }
  }
}

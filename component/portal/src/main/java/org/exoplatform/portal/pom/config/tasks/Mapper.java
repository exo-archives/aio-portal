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
import org.exoplatform.portal.model.api.workspace.ui.UIContainer;
import org.exoplatform.portal.model.api.workspace.ui.UIWindow;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.content.ContentManager;
import org.exoplatform.portal.model.api.content.FetchCondition;
import org.exoplatform.portal.model.api.content.Content;
import org.exoplatform.portal.model.util.Attributes;
import org.exoplatform.portal.model.portlet.Preferences;
import static org.exoplatform.portal.pom.config.Utils.join;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Mapper {

  /** . */
  private final ContentManager contentManager;

  public Mapper(ContentManager contentManager) {
    this.contentManager = contentManager;
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
    save(src, dst.getLayout());
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
    Attributes dstAttrs = dst.getAttributes();
    dstAttrs.setString("type", src.getApplicationType());
    dstAttrs.setString("theme", src.getTheme());
    dstAttrs.setString("title", src.getTitle());
    dstAttrs.setString("access-permissions", join("|", src.getAccessPermissions()));
    dstAttrs.setBoolean("show-info-bar", src.getShowInfoBar());
    dstAttrs.setBoolean("show-state", src.getShowApplicationState());
    dstAttrs.setBoolean("show-mode", src.getShowApplicationMode());
    dstAttrs.setString("description", src.getDescription());
    dstAttrs.setString("icon", src.getIcon());
    dstAttrs.setString("width", src.getWidth());
    dstAttrs.setString("height", src.getHeight());
    for (Map.Entry<String, String> property : src.getProperties().entrySet()) {
      dstAttrs.setString(property.getKey(), property.getValue());
    }

    //
    String instanceId = src.getInstanceId();
    String contentId = parseContentId(instanceId);
    Content content = contentManager.getContent(Preferences.CONTENT_TYPE, contentId, FetchCondition.ALWAYS);
    dst.setContent(content);
  }

  public void load(UIContainer src, Container container) {


  }

  static String parseContentId(String windowId) {
    String[] persistenceChunks = org.exoplatform.portal.pom.config.Utils.split(":/", windowId);
    return persistenceChunks[persistenceChunks.length - 1];
  }
}

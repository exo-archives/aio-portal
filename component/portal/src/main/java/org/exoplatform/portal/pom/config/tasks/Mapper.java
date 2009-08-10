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

  public void save(Container src, UIContainer dst) {

    Attributes attrs = dst.getAttributes();

    String title = src.getTitle();
    attrs.setString("title", title);
    attrs.setString("description", src.getDescription());
    attrs.setString("decorator", src.getDecorator());
    attrs.setString("width", src.getWidth());
    attrs.setString("height", src.getHeight());
    attrs.setString("factory-id", src.getFactoryId());
    attrs.setString("icon", src.getIcon());
    attrs.setString("access-permissions", join("|", src.getAccessPermissions()));

    //
    dst.getComponents().clear();
    ArrayList<Object> srcChildren = src.getChildren();
    if (srcChildren != null) {
      for (Object srcChild : srcChildren) {
        if (srcChild instanceof Container) {
          Container srcChildContainer = (Container)srcChild;
          UIContainer dstChildContainer = dst.addComponent(ObjectType.CONTAINER, srcChildContainer.getName());

          //
          Attributes dstAttrs = dstChildContainer.getAttributes();
          dstAttrs.setString("title", srcChildContainer.getTitle());
          dstAttrs.setString("icon", srcChildContainer.getIcon());
          dstAttrs.setString("template", srcChildContainer.getTemplate());
          dstAttrs.setString("access-permissions", join("|", srcChildContainer.getAccessPermissions()));
          dstAttrs.setString("factory-id", srcChildContainer.getFactoryId());
          dstAttrs.setString("decorator", srcChildContainer.getDecorator());
          dstAttrs.setString("description", srcChildContainer.getDescription());
          dstAttrs.setString("width", srcChildContainer.getWidth());
          dstAttrs.setString("height", srcChildContainer.getHeight());

          //
          save(srcChildContainer, dstChildContainer);
        } else if (srcChild instanceof Application) {
          Application srcChildApplication = (Application)srcChild;

          // Generate an id if necessary
          String id = srcChildApplication.getId();
          if (id == null) {
            id = UUID.randomUUID().toString();
          }
          UIWindow dstChildWindow = dst.addComponent(ObjectType.WINDOW, id);

          //
          Attributes dstAttrs = dstChildWindow.getAttributes();
          dstAttrs.setString("type", srcChildApplication.getApplicationType());
          dstAttrs.setString("theme", srcChildApplication.getTheme());
          dstAttrs.setString("title", srcChildApplication.getTitle());
          dstAttrs.setString("access-permissions", join("|", srcChildApplication.getAccessPermissions()));
          dstAttrs.setBoolean("show-info-bar", srcChildApplication.getShowInfoBar());
          dstAttrs.setBoolean("show-state", srcChildApplication.getShowApplicationState());
          dstAttrs.setBoolean("show-mode", srcChildApplication.getShowApplicationMode());
          dstAttrs.setString("description", srcChildApplication.getDescription());
          dstAttrs.setString("icon", srcChildApplication.getIcon());
          dstAttrs.setString("width", srcChildApplication.getWidth());
          dstAttrs.setString("height", srcChildApplication.getHeight());
          for (Map.Entry<String, String> property : srcChildApplication.getProperties().entrySet()) {
            dstAttrs.setString(property.getKey(), property.getValue());
          }

          //
          String instanceId = srcChildApplication.getInstanceId();
          String contentId = parseContentId(instanceId);
          Content content = contentManager.getContent(Preferences.CONTENT_TYPE, contentId, FetchCondition.ALWAYS);
          dstChildWindow.setContent(content);
        } else {
          throw new AssertionError("Was not expecting child " + srcChild);
        }
      }
    }
  }

  public Container load(UIContainer src) {


    return null;
  }

  static String parseContentId(String windowId) {
    String[] persistenceChunks = org.exoplatform.portal.pom.config.Utils.split(":/", windowId);
    return persistenceChunks[persistenceChunks.length - 1];
  }
}

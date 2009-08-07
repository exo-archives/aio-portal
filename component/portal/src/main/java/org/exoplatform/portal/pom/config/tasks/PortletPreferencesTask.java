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

import static org.exoplatform.portal.pom.config.Utils.parseSiteType;
import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;
import org.exoplatform.portal.model.api.workspace.Site;
import org.exoplatform.portal.model.api.workspace.ObjectType;
import org.exoplatform.portal.model.api.workspace.Workspace;
import org.exoplatform.portal.model.api.content.ContentManager;
import org.exoplatform.portal.model.api.content.Content;
import org.exoplatform.portal.model.api.content.CustomizationContext;
import org.exoplatform.portal.model.api.content.FetchCondition;
import org.exoplatform.portal.model.api.content.Customization;
import org.exoplatform.portal.model.api.content.customization.CustomizationMode;
import org.exoplatform.portal.model.portlet.Preferences;
import org.exoplatform.portal.model.portlet.PreferencesBuilder;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.Preference;

import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PortletPreferencesTask extends AbstractPOMTask {

  /** . */
  protected final String windowId;

  /** . */
  protected final ObjectType<? extends Site> siteType;

  /** . */
  protected final String ownerType;

  /** . */
  protected final String ownerId;

  /** . */
  protected final String portletName;

  protected PortletPreferencesTask(String windowId) {
    int i1 = windowId.indexOf('#');
    if (i1 == -1) {
      throw new IllegalArgumentException("Wrong window id format" + windowId);
    }
    String ownerType = windowId.substring(0, i1);

    //
    int i2 = windowId.indexOf(':', i1 + 1);
    if (i2 == -1) {
      throw new IllegalArgumentException("Wrong window id format" + windowId);
    }
    String ownerId = windowId.substring(i1 + 1, i2);
    String portletName = windowId.substring(i2 + 1);

    //
    this.windowId = windowId;
    this.ownerType = ownerType;
    this.ownerId = ownerId;
    this.portletName = portletName;
    this.siteType = parseSiteType(ownerType);
  }

  public static class Save extends PortletPreferencesTask {

    /** . */
    private final PortletPreferences prefs;

    public Save(PortletPreferences prefs) {
      super(prefs.getWindowId());

      //
      this.prefs = prefs;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot save portlet preferences " + windowId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Set<CustomizationContext> context = Collections.<CustomizationContext>singleton(site);

      //
      ContentManager contentManager = session.getContentManager();
      Content<Preferences> content = contentManager.getContent(Preferences.CONTENT_TYPE, portletName, FetchCondition.ALWAYS);
      Customization<Preferences> root = content.getCustomization();
      Customization<Preferences> customization = root.customize(CustomizationMode.CLONE, context);

      //
      PreferencesBuilder builder = new PreferencesBuilder();
      for (Preference pref : prefs.getPreferences()) {
        builder.add(pref.getName(), pref.getValues(), pref.isReadOnly());
      }
      customization.setState(builder.build());
    }
  }

  public static class Load extends PortletPreferencesTask {

    /** . */
    private PortletPreferences prefs;

    public Load(String windowId) {
      super(windowId);
    }

    public PortletPreferences getPreferences() {
      return prefs;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot save portlet preferences " + windowId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Set<CustomizationContext> context = Collections.<CustomizationContext>singleton(site);

      //
      ContentManager contentManager = session.getContentManager();
      Content<Preferences> content = contentManager.getContent(Preferences.CONTENT_TYPE, portletName, FetchCondition.PERSISTED);

      //
      if (content != null) {
        Customization<Preferences> customization = content.getCustomization().getCustomization(context);
        if (customization != null) {
          ArrayList<Preference> list = new ArrayList<Preference>();
          for (org.exoplatform.portal.model.portlet.Preference preference : customization.getState().getEntries()) {
            Preference pref = new Preference();
            pref.setName(preference.getName());
            pref.setValues(new ArrayList<String>(preference.getValues()));
            pref.setReadOnly(preference.isReadOnly());
            list.add(pref);
          }
          PortletPreferences prefs = new PortletPreferences();
          prefs.setWindowId(windowId);
          prefs.setPreferences(list);
          this.prefs = prefs;
        }
      }
    }
  }

  public static class Remove extends PortletPreferencesTask {

    public Remove(String windowId) {
      super(windowId);
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot save portlet preferences " + windowId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Set<CustomizationContext> context = Collections.<CustomizationContext>singleton(site);

      //
      ContentManager contentManager = session.getContentManager();
      Content<Preferences> content = contentManager.getContent(Preferences.CONTENT_TYPE, portletName, FetchCondition.PERSISTED);

      //
      if (content != null) {
        Customization<Preferences> customization = content.getCustomization().getCustomization(context);
        if (customization != null) {
          customization.destroy();
        }
      }
    }
  }
}

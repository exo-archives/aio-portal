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

import static org.exoplatform.portal.pom.config.Utils.split;
import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.Preference;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.content.Customization;
import org.gatein.mop.api.content.FetchCondition;
import org.gatein.mop.api.content.Content;
import org.gatein.mop.api.content.ContentManager;
import org.gatein.mop.api.content.CustomizationContext;
import org.gatein.mop.api.content.customization.CustomizationMode;
import org.gatein.mop.core.portlet.Preferences;
import org.gatein.mop.core.portlet.PreferencesBuilder;

import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class PortletPreferencesTask extends AbstractPOMTask {

/*

WindowID:
- persistenceId : portal#classic:/web/BannerPortlet/testPortletPreferences
- owner : portal#classic
- portletApplicationName : web
- portletName: BannerPortlet
- uniqueID : testPortletPreferences

*/

  /** . */
  protected final ObjectType<? extends Site> siteType;

  /** . */
  protected final String ownerType;

  /** . */
  protected final String ownerId;

  /** . */
  protected final String applicationName;

  /** . */
  protected final String portletName;

  /** . */
  protected final String uniqueId;

  protected PortletPreferencesTask(WindowID windowID) {
    String[] chunks = Mapper.parsePersistenceId(windowID.getPersistenceId());

    //
    this.ownerType = chunks[0];
    this.siteType = Mapper.parseSiteType(chunks[0]);
    this.ownerId = chunks[1];
    this.applicationName = chunks[2];
    this.portletName = chunks[3];
    this.uniqueId = chunks[4];
  }

  protected PortletPreferencesTask(String ownerType, String ownerId, String windowId) {
    String[] windowIdChunks = Mapper.parseWindowId(windowId);

    //
    this.ownerType = ownerType;
    this.ownerId = ownerId;
    this.siteType = Mapper.parseSiteType(ownerType);
    this.applicationName = windowIdChunks[0];
    this.portletName = windowIdChunks[1];
    this.uniqueId = windowIdChunks[2];
  }

  public static class Save extends PortletPreferencesTask {

    /** . */
    private final PortletPreferences prefs;

    public Save(PortletPreferences prefs) {
      super(prefs.getOwnerType(), prefs.getOwnerId(), prefs.getWindowId());

      //
      this.prefs = prefs;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot save portlet preferences " + applicationName + "/" + portletName + "/" + uniqueId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Set<CustomizationContext> context = Collections.<CustomizationContext>singleton(site);

      //
      ContentManager contentManager = session.getContentManager();
      Content<Preferences> content = contentManager.getContent(Preferences.CONTENT_TYPE, contentId, FetchCondition.ALWAYS);
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

    public Load(WindowID windowId) {
      super(windowId);
    }

    public PortletPreferences getPreferences() {
      return prefs;
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot save portlet preferences " + applicationName + "/" + portletName + "/" + uniqueId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Set<CustomizationContext> context = Collections.<CustomizationContext>singleton(site);

      //
      ContentManager contentManager = session.getContentManager();
      Content<Preferences> content = contentManager.getContent(Preferences.CONTENT_TYPE, contentId, FetchCondition.PERSISTED);

      //
      if (content != null) {
        Customization<Preferences> customization = content.getCustomization().getCustomization(context);
        if (customization != null) {
          ArrayList<Preference> list = new ArrayList<Preference>();
          for (org.gatein.mop.core.portlet.Preference preference : customization.getState().getEntries()) {
            Preference pref = new Preference();
            pref.setName(preference.getName());
            pref.setValues(new ArrayList<String>(preference.getValues()));
            pref.setReadOnly(preference.isReadOnly());
            list.add(pref);
          }
          PortletPreferences prefs = new PortletPreferences();
          prefs.setOwnerId(ownerId);
          prefs.setOwnerType(ownerType);
          prefs.setWindowId(windowId);
          prefs.setPreferences(list);
          this.prefs = prefs;
        }
      }
    }
  }

  public static class Remove extends PortletPreferencesTask {

    public Remove(PortletPreferences prefs) {
      super(prefs.getOwnerType(), prefs.getOwnerId(), prefs.getWindowId());
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot save portlet preferences " + applicationName + "/" + portletName + "/" + uniqueId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Set<CustomizationContext> context = Collections.<CustomizationContext>singleton(site);

      //
      ContentManager contentManager = session.getContentManager();
      Content<Preferences> content = contentManager.getContent(Preferences.CONTENT_TYPE, contentId, FetchCondition.PERSISTED);

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

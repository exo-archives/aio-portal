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

import org.exoplatform.portal.pom.config.AbstractPOMTask;
import org.exoplatform.portal.pom.config.POMSession;
import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.Preference;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.gatein.mop.api.workspace.Site;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Workspace;
import org.gatein.mop.api.workspace.ui.UIWindow;
import org.gatein.mop.api.content.Customization;
import org.gatein.mop.core.content.portlet.Preferences;
import org.gatein.mop.core.content.portlet.PreferencesBuilder;

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
  protected final String instanceName;

  /** . */
  protected final String windowId;

  protected PortletPreferencesTask(WindowID windowID) {
    String[] chunks = Mapper.parseWindowId(windowID.getPersistenceId()) ;
    if(chunks.length != 5) {
      throw new IllegalArgumentException("Invalid WindowID: " + "[" + windowID + "]");
    }

    //
    this.ownerType = chunks[0];
    this.siteType = Mapper.parseSiteType(chunks[0]);
    this.ownerId = chunks[1];
    this.applicationName = chunks[2];
    this.portletName = chunks[3];
    this.instanceName = chunks[4];
    this.windowId = windowID.getPersistenceId();
  }

  protected PortletPreferencesTask(String windowId) {
    String[] chunks = Mapper.parseWindowId(windowId) ;
    if(chunks.length != 5) {
      throw new IllegalArgumentException("Invalid WindowID: " + "[" + windowId + "]");
    }

    //
    this.ownerType = chunks[0];
    this.siteType = Mapper.parseSiteType(chunks[0]);
    this.ownerId = chunks[1];
    this.applicationName = chunks[2];
    this.portletName = chunks[3];
    this.instanceName = chunks[4];
    this.windowId = windowId;
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

      Customization customization = null;
      if (instanceName.startsWith("@")) {
        UIWindow window = workspace.getObject(ObjectType.WINDOW, instanceName.substring(1));
        customization = window.getCustomization();
      } else {
        Site site = workspace.getSite(siteType, ownerId);
        if (site != null) {
          customization = site.customize(instanceName, Preferences.CONTENT_TYPE, applicationName + "/" + portletName, new PreferencesBuilder().build());
        }
      }

      //
      if (customization != null) {
        PreferencesBuilder builder = new PreferencesBuilder();
        for (Preference pref : prefs.getPreferences()) {
          builder.add(pref.getName(), pref.getValues(), pref.isReadOnly());
        }
        customization.setState(builder.build());
      } else {
        session.addPortletPreferences(prefs);
      }
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
        throw new IllegalArgumentException("Cannot load portlet preferences " + windowId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Customization<Preferences> customization = null;
      if (instanceName.startsWith("@")) {
        UIWindow window = workspace.getObject(ObjectType.WINDOW, instanceName.substring(1));
        customization = (Customization<Preferences>)window.getCustomization();
      } else {
        customization = (Customization<Preferences>)site.getCustomization(instanceName);
      }

      //
      if (customization != null) {
        Preferences state = customization.getState();
        if (state != null) {
          ArrayList<Preference> list = new ArrayList<Preference>();
          for (org.gatein.mop.core.content.portlet.Preference preference : state.getEntries()) {
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
      super(prefs.getWindowId());
    }

    public void run(POMSession session) throws Exception {
      Workspace workspace = session.getWorkspace();
      Site site = workspace.getSite(siteType, ownerId);
      if (site == null) {
        throw new IllegalArgumentException("Cannot remove portlet preferences " + windowId +
          " as the corresponding portal " + ownerId + " with type " + siteType + " does not exist");
      }

      //
      Customization customization;
      if (instanceName.startsWith("@")) {
        UIWindow window = workspace.getObject(ObjectType.WINDOW, instanceName.substring(1));
        customization = window.getCustomization();
      } else {
        customization = site.getCustomization(instanceName);
      }

      //
      if (customization != null) {
        customization.destroy();
      }
    }
  }
}

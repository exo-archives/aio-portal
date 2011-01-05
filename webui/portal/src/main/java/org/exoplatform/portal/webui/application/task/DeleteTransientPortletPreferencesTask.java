
package org.exoplatform.portal.webui.application.task;

import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;

public class DeleteTransientPortletPreferencesTask implements PortletPreferencesTask<DataStorage>
{
	private ExoWindowID exoWindowID;

	public DeleteTransientPortletPreferencesTask(ExoWindowID exoWindowID)
	{
	  if (exoWindowID == null) {
	    throw new IllegalArgumentException("exoWindowID must not be null");
	  }
		this.exoWindowID = exoWindowID;
	}

	public void run(DataStorage persistentService) throws Exception {
	  try {
      PortletPreferences targetedPP = persistentService.getPortletPreferences(exoWindowID);
      if(targetedPP != null) {
        persistentService.remove(targetedPP);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }		
	}
}

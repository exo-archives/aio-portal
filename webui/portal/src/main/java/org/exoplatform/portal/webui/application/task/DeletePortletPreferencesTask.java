
package org.exoplatform.portal.webui.application.task;

import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.config.DataStorage;

/**
 * @author Minh Hoang TO
 *
 * hoang281283@gmail.com
 */

public class DeletePortletPreferencesTask implements PortletPreferencesTask<DataStorage>
{
	private PortletPreferences deletedPortletPreferences;

	public DeletePortletPreferencesTask(PortletPreferences _deletedPortletPreferences)
	{
		this.deletedPortletPreferences = _deletedPortletPreferences;
	}

	public void run(DataStorage persistentService) throws Exception {
		persistentService.remove(this.deletedPortletPreferences);
	}
}

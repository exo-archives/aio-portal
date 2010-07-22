/**
 * 
 */
package org.exoplatform.portal.webui.application.task;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.portal.config.DataStorage;

/**
 * @author Minh Hoang TO
 *
 * hoang281283@gmail.com
 */
public class PortletPreferencesTaskCollection{

	private List<PortletPreferencesTask<DataStorage>> cache;
	
	public PortletPreferencesTaskCollection()
	{
		this.cache = new ArrayList<PortletPreferencesTask<DataStorage>>();
	}
	
	public void addTask(PortletPreferencesTask<DataStorage> task)
	{
		this.cache.add(task);
	}
	
	public synchronized void clearTasks()
	{
		this.cache.clear();
	}
	
	public void executeTasks(DataStorage persistentService) throws Exception
	{
		for(PortletPreferencesTask<DataStorage> task : cache)
		{
			task.run(persistentService);
		}
	}
}

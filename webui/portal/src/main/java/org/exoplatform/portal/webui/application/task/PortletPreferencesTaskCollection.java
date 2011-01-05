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
	
	private List<PortletPreferencesTask<DataStorage>> rollbackCaches;
	
	public PortletPreferencesTaskCollection()
	{
		this.cache = new ArrayList<PortletPreferencesTask<DataStorage>>();
		this.rollbackCaches = new ArrayList<PortletPreferencesTask<DataStorage>>();
	}
	
	public void addTask(PortletPreferencesTask<DataStorage> task, boolean isFinishAction)
	{
	  if (isFinishAction) {
	    this.cache.add(task);
	  } else {
	    this.rollbackCaches.add(task);
	  }
	}
	
	public synchronized void clearTasks()
	{
		this.cache.clear();
		this.rollbackCaches.clear();
	}
	
	public void executeTasks(DataStorage persistentService, boolean isFinishAction) throws Exception
	{
	  List<PortletPreferencesTask<DataStorage>> tempCache;
	  if (isFinishAction) {
	    tempCache = cache;	     
	  } else {
	    tempCache = rollbackCaches;
	  }
	  for(PortletPreferencesTask<DataStorage> task : tempCache)
    {
      task.run(persistentService);
    }
	}
}

/**
 * 
 */
package org.exoplatform.portal.webui.application.task;

/**
 * 
 * Generic is used here to avoid hardcoded dependence of PortletPreferenceTask on persistent service type
 * 
 * @author Minh Hoang TO
 *
 * hoang281283@gmail.com
 */
public interface PortletPreferencesTask <S>{

	public void run(S persistentService) throws Exception;
	
}

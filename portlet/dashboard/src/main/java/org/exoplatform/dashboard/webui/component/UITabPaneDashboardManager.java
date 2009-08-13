package org.exoplatform.dashboard.webui.component;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;

/**
 * 
 * Created by eXoPlatform SAS
 *
 * Author: Minh Hoang TO - hoang281283@gmail.com
 *
 *      Aug 10, 2009
 */
@ComponentConfig(
		template="app:/groovy/dashboard/webui/component/UITabPaneDashboardManager.gtmpl",
		events={
				@EventConfig(listeners=UITabPaneDashboardManager.AddDashboardActionListener.class),
				@EventConfig(listeners=UITabPaneDashboardManager.SwitchShowedTabRangeActionListener.class)
		}
)
public class UITabPaneDashboardManager extends UIContainer {
	
	private UITabPaneDashboard associatedTabPane;
	
	public UITabPaneDashboardManager()throws Exception{
		super();
	}
	
	public void setAssociatedTabPane(UITabPaneDashboard _associatedTabPane){
		associatedTabPane=_associatedTabPane;
	}
	
	public UITabPaneDashboard getAssociatedTabPane(){
		return associatedTabPane;
	}
	
	public void resetShowedTabs(){
		
	}
	
	static public class AddDashboardActionListener extends EventListener<UITabPaneDashboardManager>{
		public void execute(Event<UITabPaneDashboardManager> event) throws Exception {
			UITabPaneDashboardManager source=event.getSource();
			UITabPaneDashboard tabPane=source.getAssociatedTabPane();
//			if(newTab!=null){
//				WebuiRequestContext context=event.getRequestContext();
//				context.addUIComponentToUpdateByAjax(tabPane);
//				tabPane.setSelectedTabDashboard(newTab);
//			}
		}
	}
	
	static public class SwitchShowedTabRangeActionListener extends EventListener<UITabPaneDashboardManager>{
		public void execute(Event<UITabPaneDashboardManager> event) throws Exception {
		}
	}
}

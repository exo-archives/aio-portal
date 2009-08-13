package org.exoplatform.dashboard.webui.component;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * 
 * Created by eXoPlatform SAS
 *
 * Author: Minh Hoang TO - hoang281283@gmail.com
 *
 *      Aug 10, 2009
 */
@ComponentConfig(
		lifecycle = UIApplicationLifecycle.class,
		template = "app:/groovy/dashboard/webui/component/UITabbedDashboardPortlet.gtmpl"
)
public class UITabbedDashboardPortlet extends UIPortletApplication {
	public UITabbedDashboardPortlet()throws Exception{
		UITabPaneDashboard tabPane = addChild(UITabPaneDashboard.class,null,"UITabPaneDashboard");
		UITabPaneDashboardManager tabManager = addChild(UITabPaneDashboardManager.class,null,"UITabPaneDashboardManager");
		tabPane.setRendered(true);
		tabManager.setRendered(true);
		tabManager.setAssociatedTabPane(tabPane);
		setId("UITabbedDashboardPortlet");
	}
}

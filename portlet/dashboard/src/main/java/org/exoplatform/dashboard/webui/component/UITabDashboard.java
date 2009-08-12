package org.exoplatform.dashboard.webui.component;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.webui.portal.PageNodeEvent;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;

/**
 * 
 * Created by eXoPlatform SAS
 *
 * Author: Minh Hoang TO - hoang281283@gmail.com
 *
 *      Aug 10, 2009
 */
@ComponentConfig(
		template = "app:/groovy/dashboard/webui/component/UITabDashboard.gtmpl",
		events={
			@EventConfig(name="DeleteTabDashboard",listeners=UITabDashboard.DeleteTabDashboardActionListener.class),
			@EventConfig(name="SelectTabDashboard",listeners=UITabDashboard.SelectTabDashboardActionListener.class)
		}
)
public class UITabDashboard extends UIContainer {
	
	private static Log logger=ExoLogger.getExoLogger(UITabDashboard.class);
	
	private String tabLabel;
	
	private Page page;
	private PageNode pageNode;
	
	final public static String PAGE_TEMPLATE="dashboard";
	
	public String getTabLabel(){
		return tabLabel;
	}
	
	public void createPageNode(UserPortalConfigService service,PageNavigation pageNavigation){
		if(tabLabel==null || tabLabel.length()<1){
			return;
		}
		try{
			page=service.createPageTemplate(UITabDashboard.PAGE_TEMPLATE,pageNavigation.getOwnerType(),pageNavigation.getOwnerId());
			page.setTitle(tabLabel);
			page.setName(tabLabel);
			
			pageNode=new PageNode();
			pageNode.setName(tabLabel);
			pageNode.setLabel(tabLabel);
	    pageNode.setUri(tabLabel);
			pageNode.setPageReference(page.getPageId());
				
			pageNavigation.addNode(pageNode);
			service.create(page);
			service.update(pageNavigation);
		}catch(Exception ex){
			logger.warn("Could not create page template",ex);
		}
	}

	public void cleanData(UserPortalConfigService service,PageNavigation pageNavigation){
		try{
			service.remove(page);
			removePageNode(pageNavigation);
			service.update(pageNavigation);
		}catch(Exception ex){
			logger.info("Could not clean the data",ex);
		}
	}
	
	private void removePageNode(PageNavigation pageNavigation){
		pageNavigation.getNodes().remove(pageNode);
	}
	
	public void setTabLabel(String _tabLabel){
		tabLabel=_tabLabel;
	}
	
	public PageNode getPageNode(){
		return pageNode;
	}
		
	static public class DeleteTabDashboardActionListener extends EventListener<UITabDashboard>{
		@Override
		public void execute(Event<UITabDashboard> event) throws Exception {
			UITabDashboard source=event.getSource();
			UITabPaneDashboard tabPane= source.getParent();
			if(tabPane.removeTabDashboard(source.getTabLabel())!=null){
				WebuiRequestContext context=event.getRequestContext();
				context.addUIComponentToUpdateByAjax(tabPane);
			}
		}
	}
	
	static public class SelectTabDashboardActionListener extends EventListener<UITabDashboard>{
		@Override
		public void execute(Event<UITabDashboard> event) throws Exception {
			// TODO Auto-generated method stub
			UITabDashboard source=event.getSource();
			UITabPaneDashboard tabPane=source.getParent();
			UIPortal uiPortal=Util.getUIPortal();
			PageNode pageNode=source.getPageNode();
      PageNavigation pageNavi=tabPane.getPageNavigation();
      String uri=pageNavi.getId()+"::"+pageNode.getUri();
      PageNodeEvent<UIPortal> pnevent=new PageNodeEvent<UIPortal>(uiPortal,PageNodeEvent.CHANGE_PAGE_NODE,uri);
		  uiPortal.broadcast(pnevent, Phase.PROCESS);
		}
	}
}

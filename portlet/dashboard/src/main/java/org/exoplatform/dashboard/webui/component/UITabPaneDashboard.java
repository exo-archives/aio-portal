package org.exoplatform.dashboard.webui.component;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIContainer;
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
		template =  "app:/groovy/dashboard/webui/component/UITabPaneDashboard.gtmpl",
		events = {
				@EventConfig(confirm = UITabPaneDashboard.DELETE_DASHBOARD_CONFIRM_PROMPT, name = "DeleteTab", listeners = UITabPaneDashboard.DeleteTabActionListener.class),
				@EventConfig(name = "AddDashboard", listeners = UITabPaneDashboard.AddDashboardActionListener.class),
				@EventConfig(name = "SwitchShowedTabRange", listeners = UITabPaneDashboard.SwitchShowedTabRangeActionListener.class)
		}
)
public class UITabPaneDashboard extends UIContainer{
	
	private static Log logger = ExoLogger.getExoLogger(UITabPaneDashboard.class);
	
	private int startShowIndex;
	private int endShowIndex;
	private int tabNbs;

	private UserPortalConfigService configService;
	private PageNavigation pageNavigation;
	private UIPortal uiPortal;
	
	final private static int MAX_SHOWED_TAB_NUMBER=6;
	final public static String PAGE_TEMPLATE = "dashboard";
	final public static String DELETE_DASHBOARD_CONFIRM_PROMPT = "Really want to remove this dashboard?";
	
	public UITabPaneDashboard()throws Exception{
		configService = getApplicationComponent(UserPortalConfigService.class);
		uiPortal = Util.getUIPortal();
		initPageNavigation();
	}
	
	private void initPageNavigation(){
		String remoteUser = Util.getPortalRequestContext().getRemoteUser();
		pageNavigation = getPageNavigation(PortalConfig.USER_TYPE + "::" + remoteUser);
	}
	
	private PageNavigation getPageNavigation(String owner){
    List<PageNavigation> allNavigations = uiPortal.getNavigations();
    for(PageNavigation nav: allNavigations){
      if(nav.getOwner().equals(owner)) return nav;
    }
    return null;
  }
	
	public int getCurrentNumberOfTabs(){
		return pageNavigation.getNodes().size();
	}
	
	public int getStartShowIndex(){
		return this.startShowIndex;
	}
	
	public int getEndShowIndex(){
		if(this.endShowIndex > 0){
			return this.endShowIndex;
		}
		else{
			return Math.min(this.tabNbs, this.startShowIndex + MAX_SHOWED_TAB_NUMBER);
		}
	}
	
	public PageNavigation getPageNavigation(){
		if(pageNavigation == null){
			initPageNavigation();
		}
		return pageNavigation;
	}
			
	/**
	 * Remove node specified by nodeIndex and returns the node to switch to
	 * @param nodeIndex
	 * @return
	 */
	public PageNode removePageNode(int nodeIndex){
		try{
			ArrayList<PageNode> nodes = pageNavigation.getNodes();
			if(nodes.size() < 2){
				return null; // At the moment, we prevent user from deleting all the dashboard
			}
			PageNode tobeRemoved = nodes.get(nodeIndex);
			PageNode selectedNode = uiPortal.getSelectedNode();
			
			nodes.remove(nodeIndex);
			
			if(tobeRemoved.getUri().equals(selectedNode.getUri())){
				selectedNode = nodes.get(Math.max(0, nodeIndex - 1));
				uiPortal.setSelectedNode(selectedNode);
				configService.update(pageNavigation);
				return selectedNode;
			}
			
			configService.update(pageNavigation);
			return null; //Return null as there is no need to switch to new node
		}catch(Exception ex){
			return null;
		}
	}
	
	public String createNewPageNode(String nodeName){
		try{
			if(nodeName == null){
				nodeName = "Tab_"+getCurrentNumberOfTabs();
			}
			Page page = configService.createPageTemplate(UITabPaneDashboard.PAGE_TEMPLATE,pageNavigation.getOwnerType(),pageNavigation.getOwnerId());
			page.setTitle(nodeName);
			page.setName(nodeName);
			
			PageNode pageNode = new PageNode();
			pageNode.setName(nodeName);
			pageNode.setLabel(nodeName);
	    pageNode.setUri(nodeName);
			pageNode.setPageReference(page.getPageId());
				
			pageNavigation.addNode(pageNode);
			uiPortal.setSelectedNode(pageNode);
			
			configService.create(page);
			configService.update(pageNavigation);
			
			return nodeName;
		}catch(Exception ex){
			logger.info("Could not create page template",ex);
			return null;
		}
	}
	
	static public class DeleteTabActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
			UITabPaneDashboard source = event.getSource();
			WebuiRequestContext context = event.getRequestContext();
			int removedNodeIndex = Integer.parseInt(context.getRequestParameter(UIComponent.OBJECTID));	
			PageNode selectedNode = source.removePageNode(removedNodeIndex);
			if(selectedNode != null){
				PortalRequestContext prContext = Util.getPortalRequestContext();
				prContext.getResponse().sendRedirect(prContext.getPortalURI() + selectedNode.getName());
			}
		}
	}
	
	static public class AddDashboardActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
			UITabPaneDashboard tabPane = event.getSource();
			String newNodeName = tabPane.createNewPageNode(null);
			if(newNodeName != null){
				PortalRequestContext prContext = Util.getPortalRequestContext();
				prContext.getResponse().sendRedirect(prContext.getPortalURI() + newNodeName);
			}
		}
	}
	
	static public class SwitchShowedTabRangeActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
		}
	}

		
}

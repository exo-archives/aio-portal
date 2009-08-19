package org.exoplatform.dashboard.webui.component;

import java.util.List;

import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.Page;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.portal.webui.portal.PageNodeEvent;
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
		template =  "app:/groovy/dashboard/webui/component/UITabPaneDashboard.gtmpl",
		events = {
				@EventConfig(name = "SelectTab", listeners = UITabPaneDashboard.SelectTabActionListener.class),
				@EventConfig(name = "DeleteTab", listeners = UITabPaneDashboard.DeleteTabActionListener.class),
				@EventConfig(name = "AddDashboard", listeners = UITabPaneDashboard.AddDashboardActionListener.class),
				@EventConfig(name = "SwitchShowedTabRange", listeners = UITabPaneDashboard.SwitchShowedTabRangeActionListener.class)
		}
)
public class UITabPaneDashboard extends UIContainer{
	
	private static Log logger = ExoLogger.getExoLogger(UITabPaneDashboard.class);
	
	private /*volatile*/ int selectedIndex;
	
	private int startShowIndex;
	private int endShowIndex;
	private int tabNbs;

	private UserPortalConfigService configService;
	private PageNavigation pageNavigation;
	private UIPortal uiPortal;
	
	final private static int MAX_SHOWED_TAB_NUMBER=6;
	final public static String PAGE_TEMPLATE = "dashboard";
	
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
			
	public int getSelectedIndex(){
		return selectedIndex;
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
		
	synchronized public void setSelectedIndex(int _selectedIndex){
		selectedIndex = _selectedIndex;
	}
	
	public boolean removePageNode(int nodeIndex){
		try{
			pageNavigation.getNodes().remove(nodeIndex);
			configService.update(pageNavigation);
			if(nodeIndex <= selectedIndex){
				setSelectedIndex(Math.max(0,selectedIndex - 1));
			}
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	public boolean createNewPageNode(String nodeName){
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
			configService.create(page);
			configService.update(pageNavigation);
			setSelectedIndex(pageNavigation.getNodes().size()-1);
			
			String uri = pageNavigation.getId() + "::" + pageNode.getUri();
			PageNodeEvent<UIPortal> pnevent = new PageNodeEvent<UIPortal>(uiPortal,PageNodeEvent.CHANGE_PAGE_NODE,uri);
			uiPortal.broadcast(pnevent, Phase.PROCESS);
			return true;
		}catch(Exception ex){
			logger.info("Could not create page template",ex);
			return false;
		}
	}
	
	static public class SelectTabActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
			UITabPaneDashboard source = event.getSource();
			PageNavigation pageNavigation = source.getPageNavigation();
			WebuiRequestContext context = event.getRequestContext();
			int selectedIndex = Integer.parseInt(context.getRequestParameter(UIComponent.OBJECTID));	
			source.setSelectedIndex(selectedIndex);
			PageNode pageNode = pageNavigation.getNodes().get(selectedIndex);
			UIPortal uiPortal = Util.getUIPortal(); //Better with source.getUIPortal();
			
			String uri = pageNavigation.getId() + "::" + pageNode.getUri();
			PageNodeEvent<UIPortal> pnevent = new PageNodeEvent<UIPortal>(uiPortal,PageNodeEvent.CHANGE_PAGE_NODE,uri);
			uiPortal.broadcast(pnevent, Phase.PROCESS);
			
			context.addUIComponentToUpdateByAjax(source);
		}
	}
	
	static public class DeleteTabActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
			UITabPaneDashboard source = event.getSource();
			WebuiRequestContext context = event.getRequestContext();
			int selectedIndex = Integer.parseInt(context.getRequestParameter(UIComponent.OBJECTID));	
			if(source.removePageNode(selectedIndex)){
				PageNavigation pageNavigation = source.getPageNavigation();
				PageNode pageNode = pageNavigation.getNodes().get(source.getSelectedIndex());
				UIPortal uiPortal = Util.getUIPortal(); //Better with source.getUIPortal();
			
				String uri = pageNavigation.getId() + "::" + pageNode.getUri();
				PageNodeEvent<UIPortal> pnevent = new PageNodeEvent<UIPortal>(uiPortal,PageNodeEvent.CHANGE_PAGE_NODE,uri);
				uiPortal.broadcast(pnevent, Phase.PROCESS);
				
				context.addUIComponentToUpdateByAjax(source);
			}
		}
	}
	
	static public class AddDashboardActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
			UITabPaneDashboard tabPane = event.getSource();
			if(tabPane.createNewPageNode(null)){
				WebuiRequestContext context = event.getRequestContext();
				context.addUIComponentToUpdateByAjax(tabPane);
			}
		}
	}
	
	static public class SwitchShowedTabRangeActionListener extends EventListener<UITabPaneDashboard>{
		public void execute(Event<UITabPaneDashboard> event) throws Exception {
		}
	}

		
}

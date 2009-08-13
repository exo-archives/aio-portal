package org.exoplatform.dashboard.webui.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.PageNavigation;
import org.exoplatform.portal.config.model.PageNode;
import org.exoplatform.portal.webui.portal.PageNodeEvent;
import org.exoplatform.portal.webui.portal.UIPortal;
import org.exoplatform.portal.webui.util.Util;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIContainer;
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
		template =  "app:/groovy/dashboard/webui/component/UITabPaneDashboard.gtmpl"
)
public class UITabPaneDashboard extends UIContainer{
	
	private static Log logger=ExoLogger.getExoLogger(UITabPaneDashboard.class);
	//private volatile int selectedIndex;//Declare volatile to avoid using synchronize on read/write method
	private int startShowIndex;
	private int endShowIndex;
	private int tabNbs;

	private UserPortalConfigService configService;
	private PageNavigation pageNavigation;
	private UIPortal uiPortal;
	
//	private UITabDashboard selectedTabDashboard;
//	
	private List<UIComponent> children;//Reference to the children, that facilitates removing UITabDashboard
	
	final private static int MAX_SHOWED_TAB_NUMBER=6;
	
	public UITabPaneDashboard()throws Exception{
		configService=getApplicationComponent(UserPortalConfigService.class);
		uiPortal=Util.getUIPortal();
		pageNavigation=uiPortal.getSelectedNavigation();
		//loadExistingTabs();
		children=getChildren();
	}
	
	private void loadExistingTabs(){
		if(pageNavigation==null){
			return;
		}
		ArrayList<PageNode> pageNodes=pageNavigation.getNodes();
//		for(PageNode node : pageNodes){
//			createNewTabDashboard(node.getLabel());
//		}
	}
	
	private void initPageNavigation(){
		UserACL userACL=getApplicationComponent(UserACL.class);
	}
	
	//public void setSelectedIndex(int _selectedIndex){
	//	selectedIndex=_selectedIndex;
	//}
	
	//public int getSelectedIndex(){
	//	return selectedIndex;
	//}
	
	public int getCurrentNumberOfTabs(){
		return tabNbs;
	}
	
	public int getStartShowIndex(){
		return startShowIndex;
	}
	
	public int getEndShowIndex(){
		if(endShowIndex>0){
			return endShowIndex;
		}
		else{
			return Math.min(tabNbs, startShowIndex+MAX_SHOWED_TAB_NUMBER);
		}
	}
	
//	public UITabDashboard createNewTabDashboard(String tabLabel){
//		UITabDashboard newTab=null;
//		try{
//			newTab=addChild(UITabDashboard.class, null, null);
//		}catch(Exception ex){
//			logger.info("Could not create new UITabDashboard ",ex);
//			return null;
//		}
//		if(tabLabel!=null){
//			newTab.setTabLabel(tabLabel);
//		}
//		else{
//			newTab.setTabLabel("Tab_"+tabNbs);
//		}
//		newTab.createPageNode(configService,pageNavigation);
//		tabNbs++;
//		return newTab;
//	}
//	
//	public UITabDashboard removeTabDashboard(String childId){
//		UITabDashboard targetedDashboard=getTabDashboard(childId);
//		if(targetedDashboard==null){
//			logger.info("Could not find the tab dashboard specified by "+childId);
//			return null;
//		}
//		logger.info("Remove the pageNode from node navigation");
//		targetedDashboard.cleanData(configService, pageNavigation);
//		logger.info("Remove the tab dashboard from tab pane");
//		children.remove(targetedDashboard);
//		tabNbs--;
//		return targetedDashboard;
//	}
//	
//	private UITabDashboard getTabDashboard(String tabId){
//		Iterator<UIComponent> iterator=children.iterator();
//		UIComponent re;
//		while(iterator.hasNext()){
//			re=iterator.next();
//			if(re instanceof UITabDashboard && ((UITabDashboard)re).getTabLabel().equals(tabId)){
//				return (UITabDashboard)re;
//			}
//		}
//		return null;
//	}	
//	
	public PageNavigation getPageNavigation(){
		return pageNavigation;
	}
//	
//	public void setSelectedTabDashboard(UITabDashboard uiTabDashboard){
//		selectedTabDashboard=uiTabDashboard;
//		String uri=pageNavigation.getId()+"::"+selectedTabDashboard.getPageNode().getUri();
//		PageNodeEvent<UIPortal> pnevent=new PageNodeEvent<UIPortal>(uiPortal,PageNodeEvent.CHANGE_PAGE_NODE,uri);
//		try{
//			uiPortal.broadcast(pnevent, Phase.PROCESS);
//		}catch(Exception ex){
//			logger.info("Could not change page node",ex);
//		}
//	}
	
}

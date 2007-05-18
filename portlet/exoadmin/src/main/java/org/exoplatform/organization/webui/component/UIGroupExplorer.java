/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.organization.webui.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.component.UIBreadcumbs;
import org.exoplatform.webui.component.UIContainer;
import org.exoplatform.webui.component.UITree;
import org.exoplatform.webui.component.UIBreadcumbs.LocalPath;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
/**
 * Created by The eXo Platform SARL
 * Author : chungnv
 *          nguyenchung136@yahoo.com
 * Jun 23, 2006
 * 10:07:15 AM
 */
@ComponentConfig(events = @EventConfig(listeners = UIGroupExplorer.ChangeNodeActionListener.class) )
public class UIGroupExplorer extends UIContainer {
  
	private Group selectedGroup_ ;
	private Collection sibblingsGroup_ ;
	private Collection childrenGroup_ ;
  
	public UIGroupExplorer() throws Exception {
	  UITree tree = addChild(UITree.class, null, "TreeGroupExplorer");
    OrganizationService service = getApplicationComponent(OrganizationService.class) ;
    sibblingsGroup_ = service.getGroupHandler().findGroups(null);
    
    tree.setSibbling((List)sibblingsGroup_);
    tree.setIcon("Icon GroupAdminIcon");
    tree.setSelectedIcon("Icon PortalIcon");
    tree.setBeanIdField("id");
    tree.setBeanLabelField("groupName");
	}
	
	public void changeGroup(String groupId) throws Exception {	  
    OrganizationService service = getApplicationComponent(OrganizationService.class) ;
    
    UIGroupManagement uiGroupManagement = this.getParent() ;
    UIBreadcumbs uiBreadcumb = uiGroupManagement.getChild(UIBreadcumbs.class);
    uiBreadcumb.setPath(getPath(null, groupId)) ;   
    
    if(groupId != null){
      selectedGroup_ = service.getGroupHandler().findGroupById(groupId);
    } else {
      selectedGroup_ = null;
    }
    String parentGroupId = null ;
    if(selectedGroup_ != null) parentGroupId = selectedGroup_.getParentId();
	  Group parentGroup = null ;
	  if(parentGroupId != null)	parentGroup = service.getGroupHandler().findGroupById(parentGroupId);
	  
    childrenGroup_ = service.getGroupHandler().findGroups(selectedGroup_);    
    sibblingsGroup_ = service.getGroupHandler().findGroups(parentGroup);  
    
    UIGroupDetail uiGroupDetail = uiGroupManagement.getChild(UIGroupDetail.class);     
    UIGroupInfo uiGroupInfo = uiGroupDetail.getChild(UIGroupInfo.class) ;    
    uiGroupInfo.setGroup(selectedGroup_); 
    
    UITree tree = getChild(UITree.class);
    tree.setSibbling((List)sibblingsGroup_);
    tree.setChildren((List)childrenGroup_);
    tree.setSelected(selectedGroup_);
    tree.setParentSelected(parentGroup);
	}
	
	public List<LocalPath> getPath(List<LocalPath> list, String id) throws Exception {
    if(list == null) list = new ArrayList<LocalPath>(5);
    if(id == null) return list;
    OrganizationService service = getApplicationComponent(OrganizationService.class) ;
    Group group = service.getGroupHandler().findGroupById(id);
    if(group == null) return list;
    list.add(0, new LocalPath(group.getId(), group.getGroupName())); 
		getPath(list, group.getParentId());
		return list ;
	}
	
	public Group getCurrentGroup() { return selectedGroup_ ; }
  public void setCurrentGroup(Group g) { selectedGroup_ = g; }
  
	public Collection getChildrenGroup() { return childrenGroup_ ;}
  
	public Collection getSibblingGroups() { return sibblingsGroup_ ; }
  
	public void setChildGroup(Collection childrendGroup) { childrenGroup_ = childrendGroup ;	}
  
  @SuppressWarnings("unused")
  public void processRender(WebuiRequestContext context) throws Exception {
    renderChildren();
  }
	
	static  public class ChangeNodeActionListener extends EventListener<UITree> {
		public void execute(Event<UITree> event) throws Exception {      
      UIGroupExplorer uiGroupExplorer = event.getSource().getParent() ;      
			String groupId = event.getRequestContext().getRequestParameter(OBJECTID)  ;
      uiGroupExplorer.changeGroup(groupId) ;
      UIGroupManagement uiGroupManagement = uiGroupExplorer.getParent();
      UIGroupDetail uiGroupDetail = uiGroupManagement.getChild(UIGroupDetail.class);
      uiGroupDetail.setRenderedChild(UIGroupInfo.class);
		}
	}
  
}

/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.core;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.ComponentConfigs;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIContainerLifecycle;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * May 7, 2006
 */
@ComponentConfigs({
  @ComponentConfig(lifecycle = UIContainerLifecycle.class),
  @ComponentConfig(
      id = "TabContainer", 
      lifecycle = UIContainerLifecycle.class,
      events = @EventConfig(listeners = UIContainer.SelectTabActionListener.class)
  )
})
public class UIContainer extends UIComponent {
  
  private List<UIComponent> children ;
  
  public void visit() throws Exception {
    
  }

  public  boolean hasChildren() { return children != null ; }
  
  public List<UIComponent> getChildren() {
    if(children == null) children = new ArrayList<UIComponent>(3) ;
    return children ; 
  }
  
  public void addChild(UIComponent uicomponent) {
    if(children == null) children =  new ArrayList<UIComponent>(3) ;
    uicomponent.setParent(this) ;
    children.add(uicomponent) ;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends UIComponent> T removeChildById(String id) {
    if(children == null)  return null ;
    for(UIComponent child : children) {
      if(!id.equals(child.getId())) continue;         
      child.setParent(null) ;
      children.remove(child) ;
      return (T)child ;
    }
    return null ;
  }
  
  public <T extends UIComponent> T removeChild(Class<T> clazz) {
    if(children == null)  return null ;
    for(UIComponent child : children) {
      if(!clazz.isInstance(child)) continue;         
      child.setParent(null) ;
      children.remove(child) ;
      return clazz.cast(child) ;
    }  
    return null ;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends UIComponent> T getChildById(String id) {
    if(children == null)  return null ;
    for(UIComponent child : children) {
      if(id.equals(child.getId())) return  (T)child ;
    }
    return null ;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends UIComponent> T getChild(int idx){
    if(children == null)  return null ;
    return (T)children.get(idx);
  }
  
  public <T extends UIComponent> T getChild(Class<T> clazz){
    if(children == null)  return null ;
    for (UIComponent uichild : children){
      if (clazz.isInstance(uichild)) return clazz.cast(uichild);
    }      
    return null;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends UIComponent> T  replaceChild(String targetChildId, UIComponent newComponent) throws Exception {
    if(children == null)  throw new Exception("Cannot  find the child : " + targetChildId) ;
    for(int i = 0; i < this.children.size(); i++) {
      UIComponent child = this.children.get(i) ;
      if(targetChildId.equals(child.getId())) { 
        child.setParent(null) ;
        newComponent.setParent(this);
        children.set(i, newComponent) ;        
        return (T)child ;
      }
    }
    throw new Exception("Cannot  find the child : " + targetChildId) ;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends UIComponent> T  replaceChild(
           String targetChildId, Class<T> type, String configId, String id) throws Exception  {
    WebuiRequestContext  context =  WebuiRequestContext.getCurrentInstance() ;    
    WebuiApplication app  = (WebuiApplication)context.getApplication() ;
    UIComponent comp =  app.createUIComponent(type, configId, id, context) ;
    comp =  replaceChild(targetChildId, comp) ;
    return (T)comp ;
  }
  
  public <T extends UIComponent> T addChild(Class<T> type, String configId, String id) throws Exception  {
    WebuiRequestContext  context =  WebuiRequestContext.getCurrentInstance() ;
    WebuiApplication app  = (WebuiApplication)context.getApplication() ;
    T comp =  app.createUIComponent(type, configId, id, context) ;    
    addChild(comp) ;    
    return comp ;
  }
  
  @SuppressWarnings("unchecked")
  public <T extends UIComponent> T findComponentById(String id) {  
    if(getId().equals(id)) return (T)this ;
    if(children == null)  return null ;
    for (UIComponent uichild : children) {
      UIComponent found =  uichild.findComponentById(id) ;
      if(found != null)  return (T)found ;
    }
    return null ;
  } 
  
  /*@SuppressWarnings("unchecked")
  public <T extends UIComponent> T findRenderComponentById(String id) {
    if(!isRendered()) return null;
    if(getId().equals(id)) return (T)this ;
    if(children == null)  return null ;
    for (UIComponent uichild : children) {
      UIComponent found =  uichild.findComponentById(id) ;
      if(found != null)  return (T)found ;
    }
    return null ;
  } */
   
  public <T extends UIComponent> T findFirstComponentOfType(Class<T> type) {
    if (type.isInstance(this)) return type.cast(this);
    if(children == null)  return null ;
    for (UIComponent uichild : children) {
      T found = uichild.findFirstComponentOfType(type);
      if (found != null) return found;
    }
    return null;
  }
  
  public <T> void findComponentOfType(List<T> list, Class<T> type) {
    if (type.isInstance(this) && !list.contains(this)) list.add(type.cast(this));
    if(children == null)  return ;
    for (UIComponent uichild : children) {
      uichild.findComponentOfType(list, type);
    }
  }  
  
  public void setRenderedChild(String id) {
    List<UIComponent> list = getChildren() ;
    for(UIComponent child : list) {
      if(child.getId().equals(id))  child.setRendered(true);
      else  child.setRendered(false) ;
    }
  }
  
  public <T extends UIComponent> void setRenderedChild(Class<T> type) {
  	List<UIComponent> list = getChildren() ;
  	for(UIComponent child : list) {
  		if(type.isInstance(child))  child.setRendered(true);
  		else   child.setRendered(false) ;
  	}
  }
  
  public void setRenderedChildrenOfTypes(Class<?> [] types) {
    List<UIComponent> list = getChildren() ;
    for(UIComponent child : list) {
      child.setRendered(false) ;     
      for(Class<?> type : types){
        if(type.isInstance(child)) {
          child.setRendered(true) ;
          break;
        } 
      } 
    }
  }  
  
  public void renderChild(String id) throws Exception {     
    UIComponent uiChild = getChildById(id) ;
    if(uiChild.isRendered()) {
      uiChild.processRender((WebuiRequestContext)WebuiRequestContext.getCurrentInstance()) ;
    }
  }
  
  public <T extends UIComponent> void renderChild(Class<T> clazz) throws Exception {     
    UIComponent uiChild = getChild(clazz) ;
    if(uiChild.isRendered()) {
      uiChild.processRender((WebuiRequestContext)WebuiRequestContext.getCurrentInstance()) ;
    }
  }
  
  public void renderUIComponent(UIComponent uicomponent) throws Exception {
    uicomponent.processRender((WebuiRequestContext)WebuiRequestContext.getCurrentInstance()) ;
  }
  
  public void renderChild(int index) throws Exception {
    UIComponent uiChild = getChild(index) ;
    if(uiChild.isRendered()) {
      uiChild.processRender((WebuiRequestContext)WebuiRequestContext.getCurrentInstance()) ;
    }
  }
  
  public void renderChildren() throws Exception {
    WebuiRequestContext context = WebuiRequestContext.getCurrentInstance() ;
    renderChildren(context) ;
  }
  
  public void renderChildren(WebuiRequestContext context) throws Exception {
    List<UIComponent> list = getChildren() ;
    for(UIComponent child :  list) {
      if(child.isRendered()) child.processRender(context) ;
    }
  }
  
  static  public class SelectTabActionListener extends EventListener<UIContainer> {    
    public void execute(Event<UIContainer> event) throws Exception {
      System.out.println("\n\n ==  > select tab \n\n");
    }
  }
}
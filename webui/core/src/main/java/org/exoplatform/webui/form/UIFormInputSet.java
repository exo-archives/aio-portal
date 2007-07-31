/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.webui.form;

import java.io.Writer;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.bean.BeanDataMapping;
import org.exoplatform.webui.bean.ReflectionDataMapping;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.UIContainer;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jun 8, 2006
 */
public class UIFormInputSet extends  UIContainer {
  
  private BeanDataMapping  beanMapping = null;
  
  public UIFormInputSet() {}
  
	public UIFormInputSet(String name) {
    setId(name) ;
	}
	
	public UIFormInputSet addUIFormInput(UIFormInput input) {
		addChild((UIComponent)input) ;
		return this ;
	}
  
  public UIFormInputSet addUIFormInput(UIFormInputSet input) {
    addChild(input) ;
    return this ;
  }
	
	public String getName() {	return getId() ;	}
  
  @SuppressWarnings("unchecked")
  public <T extends UIFormInput> T getUIInput(String name) {
    return (T) findComponentById(name);
  }

  public UIFormStringInput getUIStringInput(String name) {
    return (UIFormStringInput) findComponentById(name) ;
  }
  
  public UIFormCheckBoxInput getUIFormCheckBoxInput(String name) {
    return (UIFormCheckBoxInput) findComponentById(name);
  }
  
  public UIFormSelectBox getUIFormSelectBox(String name) {
    return (UIFormSelectBox) findComponentById(name) ;
  }
  
  public UIFormInputInfo getUIFormInputInfo(String name) {
    return (UIFormInputInfo) findComponentById(name) ;
  }
  
  public UIFormTextAreaInput getUIFormTextAreaInput(String name) {
    return (UIFormTextAreaInput) findComponentById(name) ;
  }
  public void reset(){
    for(UIComponent uiChild : getChildren()){
      if(uiChild instanceof UIFormInput){
        ((UIFormInput)uiChild).reset();
      }
    }
  }
  
  public  void invokeGetBindingField(Object bean) throws Exception {
    if(beanMapping == null) beanMapping = new ReflectionDataMapping();
    beanMapping.mapField(this, bean);
  }
  
  public  void invokeSetBindingField(Object bean) throws Exception {
    if(beanMapping == null) beanMapping = new ReflectionDataMapping();
    beanMapping.mapBean(bean, this);
  }
  
	public void processDecode(WebuiRequestContext context) throws Exception {
		for(UIComponent child : getChildren()) 	{
      child.processDecode(context) ;
		}
	}
  
  public void processRender(WebuiRequestContext context) throws Exception {
    if(getComponentConfig() != null) {
      super.processRender(context) ;
      return ;
    }
    Writer w = context.getWriter() ;
    w.write("<div class=\"UIFormInputSet\">") ;
    w.write("<table class=\"UIFormGrid\">") ;
    ResourceBundle res = context.getApplicationResourceBundle() ;
    UIForm uiForm = getAncestorOfType(UIForm.class);
    for(UIComponent inputEntry :  getChildren()) {     
      String label ;
      try {
        label = uiForm.getLabel(res, inputEntry.getId());
        if(inputEntry instanceof UIFormInputBase) ((UIFormInputBase)inputEntry).setLabel(label);
      } catch(MissingResourceException ex){
        label = "&nbsp;" ;
        System.err.println("\n "+uiForm.getId()+".label." + inputEntry.getId()+" not found value");
      }
      w.write("<tr>") ;
      w.write("<td class=\"FieldLabel\">") ; w.write(label); w.write("</td>") ;
      w.write("<td class=\"FieldComponent\">") ; renderUIComponent(inputEntry) ; w.write("</td>") ;
      w.write("</tr>") ;
    }
    w.write("</table>") ;
    w.write("</div>") ;
  }
  
}
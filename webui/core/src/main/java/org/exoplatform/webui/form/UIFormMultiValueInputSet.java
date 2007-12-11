/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.webui.form;

import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.event.Event.Phase;
import org.exoplatform.webui.form.validator.Validator;

/**
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Sep 14, 2006
 * 
 * Represents a multi value selector
 */
@ComponentConfig(
  events = {
    @EventConfig(listeners = UIFormMultiValueInputSet.AddActionListener.class, phase = Phase.DECODE),
    @EventConfig(listeners = UIFormMultiValueInputSet.RemoveActionListener.class, phase = Phase.DECODE)
  }
)
public class UIFormMultiValueInputSet extends UIFormInputContainer<List> {
  /**
   * A list of validators
   */
  protected List<Validator>  validators ;
  /**
   * The type of items in the selector
   */
  private Class<? extends UIFormInput> clazz_;  
  private Constructor constructor_ = null;

  public UIFormMultiValueInputSet() throws Exception {
    super(null, null);
  }

  public UIFormMultiValueInputSet(String name, String bindingField) throws Exception {
    super(name, bindingField);    
    setComponentConfig(getClass(), null) ;  
  }
  
  public Class<List> getTypeValue(){return List.class; }

  public void setType(Class<? extends UIFormInput> clazz){
    this.clazz_ = clazz; 
    Constructor [] constructors = clazz_.getConstructors();
    if(constructors.length > 0) constructor_ = constructors[0];
  }  
  
  public Class<? extends UIFormInput> getUIFormInputBase() { return clazz_; }
  /**
   * @return the selected items in the selector
   */
  public List<?> getValue(){
    List<Object> values = new ArrayList<Object>();
    for (UIComponent child : getChildren()) {
      UIFormInputBase uiInput = (UIFormInputBase) child;
      if(uiInput.getValue() == null) continue;
      values.add(uiInput.getValue());
    }
    return values;
  }

  @SuppressWarnings("unchecked")
  public UIFormInput setValue(List<?> values) throws Exception {
    getChildren().clear();
    for(int i = 0; i < values.size(); i++){
      UIFormInputBase uiInput =  createUIFormInput(i);
      uiInput.setValue(values.get(i));
    }    
    return this;
  } 

  public void processDecode(WebuiRequestContext context) throws Exception {   
    super.processDecode(context);
    UIForm uiForm  = getAncestorOfType(UIForm.class);
    String action =  uiForm.getSubmitAction();
    Event<UIComponent> event = createEvent(action, Event.Phase.DECODE, context) ;    
    if(event == null)  return;
    event.broadcast() ;
  }

  public void processRender(WebuiRequestContext context) throws Exception {   
    if(getChildren() == null || getChildren().size() < 1) createUIFormInput(0);
    
    Writer writer = context.getWriter() ;    

    UIForm uiForm = getAncestorOfType(UIForm.class) ;
    int size = getChildren().size() ;
//    ResourceBundle res = context.getApplicationResourceBundle() ;

    for(int i = 0; i < size; i++) {
      UIFormInputBase uiInput = getChild(i) ;
      writer.append("<div class=\"MultiValueContainer\">") ;
      uiInput.processRender(context) ;

      if(i == size - 1) {
        if(size >= 2){
          writer.append("<a href=\"");
          writer.append(uiForm.event("Remove", getId()+String.valueOf(i))).append("\" title=\"Remove Item\">");
          writer.append("<img class=\"MultiFieldAction Remove16x16Icon\" src=\"/eXoResources/skin/sharedImages/Blank.gif\" ></a>");
        }
        writer.append("<a href=\"");
        writer.append(uiForm.event("Add", getId())).append("\" title=\"Add Item\">");
        writer.append("<img class=\"MultiFieldAction AddNewNodeIcon\" src=\"/eXoResources/skin/sharedImages/Blank.gif\" ></a>");
      }      
      writer.append("</div>") ;
    }    
  }

  public  UIFormInputBase createUIFormInput(int idx) throws Exception {
    Class [] classes = constructor_.getParameterTypes();    
    Object [] params = new Object[classes.length];
    params[0] = getId()+String.valueOf(idx);
    UIFormInputBase inputBase = (UIFormInputBase)constructor_.newInstance(params);
    addChild(inputBase);
    return inputBase;    
  }
  
  static  public class AddActionListener extends EventListener<UIFormMultiValueInputSet> {
    public void execute(Event<UIFormMultiValueInputSet> event) throws Exception {
      UIFormMultiValueInputSet uiSet = event.getSource();
      String id = event.getRequestContext().getRequestParameter(OBJECTID);  
      if(uiSet.getId().equals(id)){
        uiSet.createUIFormInput(uiSet.getChildren().size());
      }
    }
  }

  static  public class RemoveActionListener extends EventListener<UIFormMultiValueInputSet> {
    public void execute(Event<UIFormMultiValueInputSet> event) throws Exception {
      UIFormMultiValueInputSet uiSet = event.getSource();
      String id = event.getRequestContext().getRequestParameter(OBJECTID);  
      uiSet.removeChildById(id);
    }
  }

}

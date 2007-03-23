package org.exoplatform.webui.component;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.exoplatform.webui.application.RequestContext;
import org.exoplatform.webui.component.model.SelectItemOption;

public class UIFormRadioBoxInput extends UIFormInputBase<String>  {
  
  public static int VERTICAL_ALIGN = 1 ;
  public static int HORIZONTAL_ALIGN = 2 ;
  private List<SelectItemOption<String>> options_;
  
  private int align_ ;
  private int topRender_ = 0;
  
  public UIFormRadioBoxInput(String name, String value) {
    this(name, value, new ArrayList<SelectItemOption<String>>()); 
  }
  
  public UIFormRadioBoxInput(String name, String value, List<SelectItemOption<String>> options) {
    super(name, value, String.class) ;
    value_ = value ;
    this.options_ = options ;
    align_ = HORIZONTAL_ALIGN ;
  }
  
  final public List<SelectItemOption<String>> getOptions() { return options_ ; }
  final public UIFormRadioBoxInput setOptions(List<SelectItemOption<String>> options) { 
    this.options_ = options;
    return this ;
  }
  
  final public UIFormRadioBoxInput setAlign(int val) { 
    align_ = val ;
    return this ;
  }
  
  /**
   * Method set render one radio box in fois !
   * @return : object of UIFormRadioBoxInput
   */
  final public UIFormRadioBoxInput setRenderOneRadioBox() {
    topRender_ = 1 ;
    return this ;
  }
    
  @SuppressWarnings("unused")
  public void decode(Object input, RequestContext context) throws Exception {
    if (!enable_ ) return ;
    if (input != null) value_ = (String) input ;
  }
  
  public void processRender(RequestContext context) throws Exception {
    if(options_ == null) return ;
    Writer w =  context.getWriter() ;    
    if(value_ == null) {
      SelectItemOption<String> si = options_.get(0) ;
      value_ = si.getValue() ;
    }
   
    int index = 0; 
    for(int i = index; i < options_.size(); i++) {      
      SelectItemOption<String> si = options_.get(i) ;
      String checked = "" ;
      if (si.getValue().equals(value_)) checked = " checked" ;
//      if(align_ == VERTICAL_ALIGN) w.write("<div style='overflow:hidden; width: 100%'>");
      //if(align_ == VERTICAL_ALIGN) w.write("<div style='clear:both;'><span></span></div>") ;
      w.write("<input class='radio' type='radio'");
      if (readonly_) w.write(" readonly ");
      if (!enable_) w.write(" disabled ");
      w.write(checked); w.write(" name='"); w.write(getName()); w.write("'") ;
      w.write(" value='"); w.write(si.getValue());
      w.write("'/>");
      w.write(si.getLabel());
      if(align_ == VERTICAL_ALIGN) w.write("<br />");
//      if(align_ == VERTICAL_ALIGN) w.write("</div>");
      
      if(topRender_ == 1) {
        index= i + 1;
        if(index == options_.size()) index = 0 ;         
        break ;
      }      
    }
    
  }
  
}
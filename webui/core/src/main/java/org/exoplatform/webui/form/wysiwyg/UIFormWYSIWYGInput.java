/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.webui.form.wysiwyg;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.form.UIFormInputBase;

/*
 * Created by The eXo Platform SAS
 * @author : Hoa.Pham
 *          hoa.pham@exoplatform.com
 * Jun 23, 2008  
 */
public class UIFormWYSIWYGInput extends UIFormInputBase<String> {
  private final String BASIC_TOOLBAR = "Basic".intern();
  private final String DEFAULT_TOOLBAR = "Default".intern();

  private FCKEditorConfig fckConfig;  
  private boolean useBasicToolbar = true;  

  public UIFormWYSIWYGInput(String name, String bindingField, String value, boolean isBasic) {
    super(name, bindingField, String.class);
    useBasicToolbar = isBasic;    
    this.value_ = value;
  }

  public FCKEditorConfig getFCKConfig() { return fckConfig; }
  public void setFCKConfig(FCKEditorConfig config) {this.fckConfig = config; }

  public void decode(Object input, WebuiRequestContext context) throws Exception {
    value_ = (String) input;
    if(value_ != null && value_.length() == 0) value_ = null ;
  }

  public void processRender(WebuiRequestContext context) throws Exception {    
    HttpServletRequest request = context.getRequest();           
    FCKEditor editor = new FCKEditor(request,getName());
    if(fckConfig == null) {
      FCKEditorConfig editorConfig = new FCKEditorConfig();
      localize(editorConfig);
      editor.setConfig(editorConfig);
      if(useBasicToolbar) {
        editor.setToolbarSet(BASIC_TOOLBAR);
      }else {
        editor.setToolbarSet(DEFAULT_TOOLBAR);
      }            
    }else {
      localize(fckConfig);
      editor.setConfig(fckConfig);      
    }    
    if (value_ == null) 
      value_ = "" ;
    editor.setValue(value_);
    Writer w =  context.getWriter();            
    w.write(editor.createHtml());
    if (this.isMandatory()) w.write(" *");    
  }

  private void localize(final FCKEditorConfig config){
    WebuiRequestContext requestContext = WebuiRequestContext.getCurrentInstance();
    String currentLanguage = requestContext.getLocale().getLanguage();
    config.put("AutoDetectLanguage","false");
    config.put("DefaultLanguage",currentLanguage);    
  }

}

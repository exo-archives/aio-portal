/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.json;

import java.util.HashMap;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.PortalContainerInfo;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Dec 26, 2005
 */
public class JSONService {
  private HashMap<Class, ObjectToJSONConverterPlugin> plugins_ ;
  
  public JSONService(PortalContainerInfo pinfo, InitParams params) throws Exception {
    plugins_ = new HashMap<Class, ObjectToJSONConverterPlugin>() ;
  }

  
  public <T extends ObjectToJSONConverterPlugin>  void register(Class clazz, T plugin) {
    
  }
  
  public  void unregister(Class clazz) {
    
  }
  
  public <T>  void toJSONScript(T object, StringBuilder b, int indentLevel) {
    
  }
}
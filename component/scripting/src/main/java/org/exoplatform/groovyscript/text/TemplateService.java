/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.groovyscript.text;

import groovy.lang.Writable;
import groovy.text.Template;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.util.List;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.resolver.ResourceResolver;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.html.tidy.HTMLTidy;
/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Dec 26, 2005
 */
public class TemplateService  {
  
  private SimpleTemplateEngine engine_  ;
  private ExoCache templatesCache_ ;
  
  private boolean cacheTemplate_  =  true ;
  private HTMLTidy tidy_ ;

  @SuppressWarnings("unused")
  public TemplateService(InitParams params, 
                         CacheService cservice) throws Exception {
    engine_ = new SimpleTemplateEngine() ;
    templatesCache_ = cservice.getCacheInstance(TemplateService.class.getName()) ;
    templatesCache_.setLiveTime(10000) ;

//    tidy_ = new HTMLTidy();
  }
  
  public void merge(Template template, BindingContext context) throws  Exception {
    context.put("_ctx", context) ;
    context.setGroovyTemplateService(this) ;
    Writable writable = template.make(context) ;
    writable.writeTo(context.getWriter());
    
//    if(tidy_ == null) {
//      return;
//    }
//    
//    CharArrayWriter charsWriter = new CharArrayWriter() ;    
//    writable.writeTo(charsWriter) ;
//    char [] chars = charsWriter.toCharArray();
//    
//    List<String> messages = tidy_.check(chars);
//    if(messages.size() > 0) {
//      StringBuilder builder = new StringBuilder();
//      for(String msg : messages) {
//        builder.append(msg);
//      }
//      throw new Exception(builder.toString());
//    }
//    
////    context.getWriter().flush();
//    context.getWriter().write(charsWriter.toCharArray());
  }
  
  public void include(String name, BindingContext context) throws  Exception  {
    if(context == null)  throw new Exception("Binding cannot be null") ;
    context.put("_ctx", context) ;
    Template template = getTemplate(name, context.getResourceResolver()) ;
    Writable writable = template.make(context) ;
    writable.writeTo(context.getWriter()) ;
  }
  
  final public Template getTemplate(String name, ResourceResolver resolver) throws Exception {
    return getTemplate(name, resolver, cacheTemplate_) ;
  }
  
  final public Template getTemplate(String url, ResourceResolver resolver, boolean cacheable) throws Exception {
    Template template = null ;
    if(cacheable)  {
      String resourceId =  resolver.createResourceId(url) ;
      template = (Template)templatesCache_.get(resourceId) ;
    }
    if(template != null)  return template ;   
    InputStream is = resolver.getInputStream(url);
    byte[]  bytes = null;
    try{
      bytes = IOUtil.getStreamContentAsBytes(is)  ;
    }catch(Exception exp){
      throw new NullPointerException("Cann't load groovy template in "+url);
    }
    is.close();    
    
    String text =  new String(bytes) ;
    template = engine_.createTemplate(text) ;
    
    if(cacheable) {
      String resourceId =  resolver.createResourceId(url) ;
      templatesCache_.put(resourceId, template) ;
    }
    return template ;
  }
  
  final public void invalidateTemplate(String name, ResourceResolver resolver)throws Exception {
    String resourceId =  resolver.createResourceId(name) ;
    templatesCache_.remove(resourceId) ;
  }
  
}

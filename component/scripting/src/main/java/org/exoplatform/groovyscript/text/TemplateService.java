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
package org.exoplatform.groovyscript.text;

import groovy.lang.Writable;
import groovy.text.Template;

import java.io.InputStream;

import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.resolver.ResourceResolver;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
/**
 * Created by The eXo Platform SAS
 * Dec 26, 2005
 */
public class TemplateService  {
  
  private SimpleTemplateEngine engine_  ;
  private ExoCache templatesCache_ ;
  
  private boolean cacheTemplate_  =  true ;

  @SuppressWarnings("unused")
  public TemplateService(InitParams params, 
                         CacheService cservice) throws Exception {
    engine_ = new SimpleTemplateEngine() ;
    templatesCache_ = cservice.getCacheInstance(TemplateService.class.getName()) ;
    templatesCache_.setLiveTime(10000) ;
  }
  
  public void merge(Template template, BindingContext context) throws  Exception {
    context.put("_ctx", context) ;
    context.setGroovyTemplateService(this) ;
    Writable writable = template.make(context) ;
    writable.writeTo(context.getWriter());
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

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

package org.exoplatform.portal.skin;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.gatein.wci.WebAppListener;
import org.gatein.wci.WebAppEvent;
import org.gatein.wci.WebAppLifeCycleEvent;

/**
 * Created by The eXo Platform SAS
 * Jan 19, 2007
 */

public class SkinConfigDeployer implements WebAppListener
{

  /** . */
  private final SkinService skinService;

  public SkinConfigDeployer(SkinService skinService) {
    this.skinService = skinService;
  }

  public void onEvent(WebAppEvent event) {
    if (event instanceof WebAppLifeCycleEvent) {
      WebAppLifeCycleEvent waEvent = (WebAppLifeCycleEvent)event;
      if (waEvent.getType() == WebAppLifeCycleEvent.ADDED) {
        try {
          ServletContext scontext = event.getWebApp().getServletContext();
          InputStream is = scontext.getResourceAsStream("/WEB-INF/conf/script/groovy/SkinConfigScript.groovy") ;
          if(is == null)  return ;

          Binding binding = new Binding();
          binding.setVariable("SkinService", skinService) ;
          binding.setVariable("ServletContext", scontext) ;
          GroovyShell shell = new GroovyShell(binding);
          shell.evaluate(is);
          is.close() ;
        } catch (Exception ex) {
          ex.printStackTrace() ;
        }
      }
    }
	}
}
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
package org.exoplatform.portal.webui.application;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.exoplatform.services.log.Log;
import org.exoplatform.Constants;
import org.exoplatform.commons.utils.Text;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.portal.application.PortalRequestContext;
import org.exoplatform.resolver.ApplicationResourceResolver;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.core.lifecycle.Lifecycle;
import org.exoplatform.webui.core.lifecycle.WebuiBindingContext;
import org.exoplatform.webui.event.Event;
import org.jboss.portal.common.util.MultiValuedPropertyMap;
import org.jboss.portal.portlet.api.PortletInvoker;
import org.jboss.portal.portlet.api.invocation.RenderInvocation;
import org.jboss.portal.portlet.api.invocation.response.FragmentResponse;
import org.jboss.portal.portlet.api.invocation.response.PortletInvocationResponse;
import org.w3c.dom.Element;

/**
 * Created by The eXo Platform SAS May 8, 2006
 */
public class UIPortletLifecycle extends Lifecycle {

  protected static Log log = ExoLogger.getLogger("portal:UIPortletLifecycle");

  /**
   * This processAction method associated with the portlet UI component does the
   * following work:
   * 
   * 1) If the current request is one that target the portal than an event
   * targeting a Portal level ActionListener is sent. This case happen when the
   * incoming request contains the parameter
   * PortalRequestContext.UI_COMPONENT_ACTION (portal:action). When the event is
   * broadcasted the methods is over 2) In other cases, the request targets the
   * portlet either to a) change the portlet mode b) change the window state c)
   * make a processAction() or render() call to the portlet container (Portlet
   * API methods here) In those 3 cases, dedicated events are created and
   * broadcasted and the portlet is added in the list of components to update
   * within the AJAX call
   */
  public void processAction(UIComponent uicomponent, WebuiRequestContext context) throws Exception {
    String action = context.getRequestParameter(PortalRequestContext.UI_COMPONENT_ACTION);
    if (action != null) {
      Event<UIComponent> event = uicomponent.createEvent(action, Event.Phase.PROCESS, context);
      if (event != null)
        event.broadcast();
      return;
    }

    boolean addUpdateComponent = false;
    String portletMode = context.getRequestParameter("portal:portletMode");
    if (portletMode != null) {
      Event<UIComponent> event = uicomponent.createEvent("ChangePortletMode",
                                                         Event.Phase.PROCESS,
                                                         context);
      if (event != null)
        event.broadcast();
      addUpdateComponent = true;
    }

    String windowState = context.getRequestParameter("portal:windowState");
    if (windowState != null) {
      Event<UIComponent> event = uicomponent.createEvent("ChangeWindowState",
                                                         Event.Phase.PROCESS,
                                                         context);
      if (event != null)
        event.broadcast();
      addUpdateComponent = true;
    }

    /*
     * Check the type of the incoming request, can be either an ActionURL or a
     * RenderURL one
     * 
     * In case of a RenderURL, the parameter state map must be invalidated and
     * ths is done in the associated ActionListener
     */
    String portletActionType = context.getRequestParameter(Constants.TYPE_PARAMETER);
    if (portletActionType != null) {
      if (portletActionType.equals(Constants.PORTAL_PROCESS_ACTION)) {
        Event<UIComponent> event = uicomponent.createEvent("ProcessAction",
                                                           Event.Phase.PROCESS,
                                                           context);
        if (event != null)
          event.broadcast();
        addUpdateComponent = true;
      } else if (portletActionType.equals(Constants.PORTAL_SERVE_RESOURCE)) {
        Event<UIComponent> event = uicomponent.createEvent("ServeResource",
                                                           Event.Phase.PROCESS,
                                                           context);
        if (event != null)
          event.broadcast();
      } else {
        Event<UIComponent> event = uicomponent.createEvent("Render", Event.Phase.PROCESS, context);
        if (event != null)
          event.broadcast();
        addUpdateComponent = true;
      }
    }
    if (addUpdateComponent)
      context.addUIComponentToUpdateByAjax(uicomponent);
  }

  /**
   * This methods of the Lifecycle writes into the output writer the content of
   * the portlet
   * 
   * 1) Create a RenderInput object and fill it with all the Request information
   * 2) Call the portletContainer.render() method of the Portlet Container to
   * get the HTML generated fragment 3) Then if the current request is an AJAX
   * one, just write in the buffer the content returned by the portlet container
   * 4) If not AJAX, then merge the content with the UIPortlet.gtmpl
   */
  public void processRender(UIComponent uicomponent, WebuiRequestContext context) throws Exception {
    UIPortlet uiPortlet = (UIPortlet) uicomponent;
    PortalRequestContext prcontext = (PortalRequestContext) context;
    ExoContainer container = prcontext.getApplication().getApplicationServiceContainer();
    
    PortletInvoker portletInvoker = (PortletInvoker)container.getComponentInstanceOfType(PortletInvoker.class);
    
    //
    FragmentResponse fragmentResponse = null;
    
    try
    {
      RenderInvocation renderInvocation = uiPortlet.create(RenderInvocation.class, prcontext);

      //
      PortletInvocationResponse piResponse =  portletInvoker.invoke(renderInvocation);
      fragmentResponse = (FragmentResponse)piResponse;
    
    }
    catch (Exception e)
    {
    	e.printStackTrace();
    }
    
    Text markup = null;
    String portletTitle = null;
    if (fragmentResponse != null)
    {
    	markup = Text.create(fragmentResponse.getContent());
    	portletTitle = fragmentResponse.getTitle();
    	
    	if (fragmentResponse.getProperties() != null)
    	{
    		MultiValuedPropertyMap<Element> hMap = fragmentResponse.getProperties().getMarkupHeaders();
    		if (hMap != null)
    		{
    			Map<String, String> headers = new HashMap<String, String>();
    			Set<String> keys = hMap.keySet();
    		}
    	}
    	
    	//TODO: (mwringe )setup headers
    	Map<String, String> headers = new HashMap<String, String>();
//    	fragmentResponse.getProperties().getMarkupHeaders();	
    	prcontext.setHeaders(headers);
    	
    }
    if (portletTitle == null)
      portletTitle = "Portlet";

    if (context.useAjax() && !prcontext.getFullRender()) {
      if (markup != null) {
        markup.writeTo(prcontext.getWriter());
      }
    } else {
      WebuiApplication app = (WebuiApplication) prcontext.getApplication();
      ApplicationResourceResolver resolver = app.getResourceResolver();
      WebuiBindingContext bcontext = new WebuiBindingContext(resolver,
                                                             context.getWriter(),
                                                             uiPortlet,
                                                             prcontext);
      bcontext.put("uicomponent", uiPortlet);
      bcontext.put("portletContent", markup);
      bcontext.put("portletTitle", portletTitle);
      try {
        renderTemplate(uiPortlet.getTemplate(), bcontext);
      } catch (Throwable ex) {
      }
    }
    try {
      prcontext.getResponse().flushBuffer();
    } catch (Throwable ex) {
    }
  }
}

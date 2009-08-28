/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.jboss.portal.portlet.portal.jsp.taglib;

import org.jboss.portal.Mode;
import org.jboss.portal.WindowState;
import org.jboss.portal.portlet.portal.jsp.PortalRenderResponse;
import org.jboss.portal.portlet.portal.jsp.PagePortletControllerContext;
import org.jboss.portal.portlet.controller.impl.PortletURLRenderer;
import org.jboss.portal.portlet.api.RenderURL;
import org.jboss.portal.portlet.api.StateString;
import org.jboss.portal.portlet.api.URLFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.util.Map;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class PortletURLTag extends AbstractURLTag
{

   /** . */
   private String modeValue;

   /** . */
   private String windowStateValue;

   public String getMode()
   {
      return modeValue;
   }

   public void setMode(String mode)
   {
      this.modeValue = mode;
   }

   public String getWindowState()
   {
      return windowStateValue;
   }

   public void setWindowState(String windowState)
   {
      this.windowStateValue = windowState;
   }

   public int doStartTag(PortalRenderResponse renderResponse) throws JspException
   {
      return EVAL_BODY_BUFFERED;
   }

   public int doEndTag(PortalRenderResponse renderResponse) throws JspException
   {
      PagePortletControllerContext context = renderResponse.getPortletControllerContext();
      PortletTag portletTag = (PortletTag)SimpleTagSupport.findAncestorWithClass(this, PortletTag.class);

      Mode mode = null;
      if (modeValue != null)
      {
         mode = Mode.create(modeValue.trim());
      }

      //
      WindowState windowState = null;
      if (windowStateValue != null)
      {
         windowState = WindowState.create(windowStateValue.trim());
      }

      PortletURLRenderer renderer = new PortletURLRenderer(
         renderResponse.getPageNavigationalState(),
         context.getClientRequest(),
         context.getClientResponse(),
         context.getPageNavigationalStateSerialization());

      final WindowState ws = windowState;
      final Mode md = mode;

      RenderURL url = new RenderURL()
      {


         public StateString getNavigationalState()
         {
            return null;
         }

         public Map<String, String[]> getPublicNavigationalStateChanges()
         {
            return params;
         }

         public Mode getMode()
         {
            return md;
         }

         public WindowState getWindowState()
         {
            return ws;
         }
      };

      try
      {
         JspWriter out = pageContext.getOut();
         String renderedURL = renderer.renderURL(portletTag.result.getWindowDef().getWindowId(), url, new URLFormat(null, null, true, null));
         out.write(renderedURL);
      }
      catch (IOException e)
      {
         throw new JspException(e);
      }

      //
      return EVAL_PAGE;
   }

}

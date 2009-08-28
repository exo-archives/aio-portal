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

import org.jboss.portal.portlet.api.spi.PortletInvocationContext;
import org.jboss.portal.portlet.api.ContainerURL;
import org.jboss.portal.portlet.api.URLFormat;
import org.jboss.portal.portlet.api.RenderURL;
import org.jboss.portal.portlet.api.ResourceURL;
import org.jboss.portal.portlet.api.ActionURL;
import org.jboss.portal.portlet.api.StateString;
import org.jboss.portal.common.util.MarkupInfo;
import org.jboss.portal.common.net.media.MediaType;
import org.exoplatform.portal.application.PortalRequestContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Iterator;
import java.io.Writer;
import java.io.IOException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class ExoPortletInvocationContext implements PortletInvocationContext {

  private MarkupInfo markupInfo;
  private String baseURL;
  private HttpServletResponse response;
  private HttpServletRequest request;

  public ExoPortletInvocationContext( PortalRequestContext portalRequestContext, UIPortlet portlet) {
    String baseURL = new StringBuilder(portalRequestContext.getRequestURI()).append("?"
	        + PortalRequestContext.UI_COMPONENT_ID).append("=").append(portlet.getId()).toString();

    //
    this.markupInfo =  new MarkupInfo(MediaType.create("text/html"), "UTF-8");
    this.baseURL = baseURL;
    this.request = portalRequestContext.getRequest();
    this.response = portalRequestContext.getResponse();
  }

  public String encodeResourceURL(String url) throws IllegalArgumentException {
    return response.encodeURL(url);
  }

  public MarkupInfo getMarkupInfo() {
    return this.markupInfo;
  }

  public String renderURL(ContainerURL containerURL, URLFormat format) {
    String url = baseURL;

    String type;
    if (containerURL instanceof RenderURL) {
      type = "render";
    }
    else if (containerURL instanceof ResourceURL) {
      type = "resource";
    }
    else if (containerURL instanceof ActionURL) {
      type = "action";
    }
    else {
      throw new Error("Unrecognized containerURL type");
    }

    url += "&portal:type=" + type;

    url += "&portal:isSecure=" + request.isSecure();

    if (containerURL instanceof ActionURL) {
      ActionURL actionURL = (ActionURL)containerURL;
      Map<String, String[]> map = StateString.decodeOpaqueValue(actionURL.getInteractionState().getStringValue());
      Iterator<String> keys = map.keySet().iterator();
      while (keys.hasNext()) {
        String key = keys.next();
        String[] values = map.get(key);
        for (String value : values) {
          url += "&" + key + "=" + value;
        }
      }
    }

    return url;
  }

  public void renderURL(Writer writer, ContainerURL containerURL, URLFormat format) throws IOException {
    String url = renderURL(containerURL, format);
    writer.write(url);
  }
}

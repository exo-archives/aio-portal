package org.exoplatform.portal.webui.javascript;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.portal.application.ResourceRequestFilter;
import org.exoplatform.web.application.javascript.JavascriptConfigService;

public class JavascriptServlet extends HttpServlet {

  public void destroy() {
  }

  public ServletConfig getServletConfig() {
    return null;
  }

  public String getServletInfo() {
    return null;
  }

  public void init(ServletConfig arg0) throws ServletException {
  }

  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    JavascriptConfigService service = (JavascriptConfigService) ExoContainerContext
        .getCurrentContainer().getComponentInstanceOfType(
            JavascriptConfigService.class);
    long lastModified = service.getLastModified();
    long ifModifedSince = request.getDateHeader(ResourceRequestFilter.IF_MODIFIED_SINCE);
    
    response.setContentType("application/x-javascript");
    if (!PropertyManager.isDevelopping()) {
      if (ifModifedSince >= lastModified) {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return;
      }
    }
    
    ServletOutputStream stream = response.getOutputStream();
    byte[] mergedJS = service.getMergedJavascript();
    response.setDateHeader(ResourceRequestFilter.LAST_MODIFIED, lastModified);
    stream.write(mergedJS);        
  }

}

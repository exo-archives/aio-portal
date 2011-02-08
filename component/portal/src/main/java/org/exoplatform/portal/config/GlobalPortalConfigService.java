/**
 * 
 */
package org.exoplatform.portal.config;

import org.exoplatform.container.xml.InitParams;
import org.exoplatform.container.xml.ValueParam;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SAS
 * @author Kien Nguyen
 *		   nguyenanhkien2a@gmail.com
 *
 *  Dec 29, 2010
 */
public class GlobalPortalConfigService implements Startable {
  
  public static String TIME_24h_FORMAT = "time.24h.format"; //true or false
  
  private boolean time24hFormat_ = false;

  public void start() {

  }

  public void stop() {

  }
  
  public GlobalPortalConfigService(InitParams params) throws Exception {
    ValueParam timeFormatParam = params.getValueParam(TIME_24h_FORMAT);
    time24hFormat_ = timeFormatParam.getValue().equalsIgnoreCase("true") ? true : false;
  }

  public void setTime24hFormat(boolean time24hFormat) {
    this.time24hFormat_ = time24hFormat;
  }

  public boolean getTime24hFormat() {
    return time24hFormat_;
  }
}

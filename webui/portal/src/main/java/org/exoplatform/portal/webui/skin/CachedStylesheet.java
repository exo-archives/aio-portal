package org.exoplatform.portal.webui.skin;

import java.util.Date;

public class CachedStylesheet {
  private String text;

  private long lastModified;

  public CachedStylesheet(String text) {
    this.text = text;
//  Remove miliseconds because string of date retrieve from Http header doesn't have miliseconds 
    lastModified = (new Date().getTime() / 1000) * 1000;
  }

  public String getText() {
    return text;
  }

  public long getLastModified() {
    return lastModified;
  }
}

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
package org.exoplatform.services.parser.rss.test;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.rss.parser.DefaultRSSChannel;
import org.exoplatform.services.rss.parser.DefaultRSSItem;
import org.exoplatform.services.rss.parser.RSSDocument;
import org.exoplatform.services.rss.parser.RSSParser;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL
 * Author : Nguyen Thi Hoa
 *          hoa.nguyen@exoplatform.com
 * Jul 20, 2006  
 */

public class TestRSSParser extends BasicTestCase {
  
  private RSSParser parser_;
  
  public TestRSSParser(String name){
    super(name);
  }
  
  public void setUp() throws Exception{
    PortalContainer manager  = PortalContainer.getInstance();      
    parser_ = (RSSParser) manager.getComponentInstanceOfType(RSSParser.class) ;
  }
  
  public void tearDown() throws Exception{
    
  }
  
  public void testAtom30() throws Exception{
    RSSDocument<DefaultRSSChannel, DefaultRSSItem> document = createRSSDocument("atom03.xml");   
    
    List<DefaultRSSItem> items = document.getItems();   
    assertEquals(2, items.size());   
    assertEquals(items.get(0).getTitle(),"Wanted: Used Acme Bio Truck");
    assertEquals(items.get(0).getLink(),"http://provider-website.com/item1-info-page.html");
    assertEquals(items.get(0).getTime(),"2003-12-13T08:29:29-04:00");    
  }  
  
  public void testURLRss() throws Exception {
    URL url = new URL("http://itredux.com/blog/feed/atom/");
    RSSDocument<DefaultRSSChannel, DefaultRSSItem> document =  parser_.createDocument(url.toURI(), "utf-8");
    List<DefaultRSSItem> items = document.getItems();   
    for(DefaultRSSItem item  : items){
      System.out.println(item.getTitle());
    }
  }

  public RSSDocument<DefaultRSSChannel, DefaultRSSItem> createRSSDocument(String path) throws Exception {      
    File uri = new File("src"+File.separatorChar+"resources"+File.separatorChar+path); 
    return parser_.createDocument(uri, "utf-8");    
  }
  
}
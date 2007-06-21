/***************************************************************************
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.parser.html.test;

import java.io.File;
import java.net.URL;

import org.exoplatform.services.html.HTMLDocument;
import org.exoplatform.services.html.HTMLNode;
import org.exoplatform.services.html.Name;
import org.exoplatform.services.html.parser.HTMLParser;
import org.exoplatform.services.html.path.NodePath;
import org.exoplatform.services.html.path.NodePathParser;
import org.exoplatform.services.html.path.NodePathUtil;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Nov 29, 2006  
 */
public class TestFileDetect extends BasicTestCase {
   private File file_;
   
   public void setUp() throws Exception {
     this.file_ = new File("src"+File.separatorChar+"resources"+File.separatorChar+"normal.html");
     assertNotNull(this.file_);
     System.out.println("FILE PATH: " + this.file_.getCanonicalPath());
   }
   
   public void testCharsetWithFile() throws Exception { 
     HTMLDocument document = HTMLParser.createDocument(this.file_,null);
     assertNotNull(document);
     assertEquals("ASCII",HTMLParser.getCharset());
     assertNotSame("UTF-8",HTMLParser.getCharset());
     System.out.println("CHARSET: " + HTMLParser.getCharset());
     
     System.out.println("DOCUMENT-TEXTVALUE: " + document.getTextValue());
     System.out.println("DOCUMENT-ROOT: " + document.getRoot().getName().toString());
     System.out.println("CLASS: " + document.getClass().getName()+ "\n");
     
     System.out.println("DOCUMENT-DOCTYPE-TEXTVALUE: " + document.getDoctype().getTextValue());
     System.out.println("DOCUMENT-DOCTYPE-VALUE: " + new String(document.getDoctype().getValue()));
     System.out.println("DOCUMENT-DOCTYPE-NAME: " + document.getDoctype().getName().toString());     
   }
   
   public void testCharsetWithURL() throws Exception {
      URL url_ = new URL("http://www.24h.com.vn");
      assertNotNull(url_);
      HTMLDocument document = HTMLParser.createDocument(url_.openConnection().getInputStream(),null);
      document = HTMLParser.createDocument(url_.openStream(),null);
      assertNotNull(document);
      assertEquals("windows-1252",HTMLParser.getCharset());
      System.out.println("\n\nCHARSET: " + HTMLParser.getCharset());
      
      assertNull(document.getDoctype());
      /*assertNotNull(document.getDoctype());
      System.out.println("DOCUMENT-DOCTYPE-NAME: "+document.getDoctype().getName().toString());      
      System.out.println("DOCUMENT-DOCTYPE-VALUE: " + document.getDoctype().getValue().toString());
      System.out.println("DOCUMENT-DOCTYPE-TEXTVALUE: " + document.getDoctype().getTextValue());
      */
      
      //assertNull(document.getRoot());
      assertNotNull(document.getRoot());
   }
   
   public void testCharsetWithTEXT()throws Exception {
     String text = "<html>" +
                     "<head>" +
                     "</head>" +
                     "<body>" +
                       "<h1>This is a HTML file for testing!</h1>"+
                     "</body>"+
                   "</html>";
     HTMLDocument document = HTMLParser.createDocument(text);
     assertNotNull(document);
     NodePath path = NodePathParser.toPath("html.body.h1");
     HTMLNode node = NodePathUtil.lookFor(document.getRoot(),path);
     assertNotNull(node);
     assertEquals(node.getName(),Name.H1);
     assertEquals(node.getName().toString(),"H1");
     System.out.println("NAME: " + node.getName());
     System.out.println("VALUE: " + new String(node.getValue()));
     System.out.println("TEXTVALUE: " + node.getTextValue());
     //assertNull(node.getChildren());
     assertNotNull(node.getChildren());
     assertEquals(node.getChildren().size(),1);
     assertEquals(!node.getChildren().isEmpty(),true);
     assertEquals(node.getChildren().get(0).getName(),Name.CONTENT);
     assertEquals(node.getChildren().get(0).getName().toString(),"CONTENT");
     
     HTMLNode child= node.getChildren().get(0);
     assertNotNull(child);
     assertNull(child.getChildren());
     assertEquals(child.getTextValue(),"This is a HTML file for testing!");
     //assertEquals(child.getValue(),"content");
     System.out.println("CONTENT-VALUE: " + new String(child.getValue()));
     assertEquals(child.getTextValue(),new String(child.getValue()));
   }
}

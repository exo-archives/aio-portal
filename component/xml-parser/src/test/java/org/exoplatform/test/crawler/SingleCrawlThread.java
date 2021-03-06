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
package org.exoplatform.test.crawler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.exoplatform.services.html.HTMLDocument;
import org.exoplatform.services.html.parser.HTMLParser;
import org.exoplatform.services.html.path.NodePath;
import org.exoplatform.services.html.path.NodePathUtil;

/**
 * Created by The eXo Platform SARL
 * Author : Lai Van Khoi
 *          laivankhoi46pm1@yahoo.com
 * Dec 1, 2006  
 */
public class SingleCrawlThread extends Thread{

  private String url_;
  private String charset_ = "utf-8";
  private NodePath childPath_; //Note. Can replace it with: NodePath[] childPath.

  private int index_ = 0;
  private ByteArrayOutputStream data;

  public SingleCrawlThread(){
    new Thread(this).start(); //Note.
  }
  //---------------------------------------
  public void run(){
    while(true){
      try{
        if(this.url_ !=null){
          System.out.println("Start download " + this.url_);
          //Get data from downloading through a specified URL.
          data = this.loadInputStream(new URL(this.url_).openStream());
          this.url_=null;
        }
        Thread.sleep(1000);
      }
      catch(Exception exp){exp.printStackTrace();}
    }
  }
  //--------------------------------------
  private ByteArrayOutputStream loadInputStream(InputStream input)throws Exception{
    ByteArrayOutputStream output=new ByteArrayOutputStream();

    BufferedInputStream buffer = new BufferedInputStream (input);   
    byte[] bytes = new byte[buffer.available()];
    int available = -1;
    while ((available=buffer.read(bytes))>-1){
      output.write(bytes,0,available);
    }
    return output;
  } 
  //-----------------------------------
  public void startDownload(String url){
    this.url_=url;
  }
  //----------------------------------
  public void startDownload(String url, int idx, NodePath childPath, String charset){
    this.url_=url;
    this.index_=idx;
    this.childPath_ = childPath;
    this.charset_=charset;
  }
  //---------------------------------
  public boolean isComplete(){
    return (this.url_==null);
  }
  //---------------------------------
  public void saveData()throws Exception {
    String fileName = String.valueOf(this.index_) + ".htm";
    this.saveData(fileName);   
  }
  //---------------------------------
  public void saveData(String fileName)throws Exception{
    if(this.data==null||this.data.size()<1)return;

    File file = new File(fileName);   
    System.out.println("FILE PATH: " + file.getAbsolutePath());
    //FileOutputStream is meant for writing streams of raw bytes of data such as image data.
    //For writing streams of characters, consider using FileWriter.
    FileOutputStream output = new FileOutputStream(file);

    //The initial whole HTMLDocument
    HTMLDocument document = HTMLParser.createDocument(this.data.toByteArray(),this.charset_);

    //The new HTMLDocument after spliting (separating) only childPath<NodePath>-->only a Node.
    document = NodePathUtil.create(document.getRoot(),new NodePath[]{this.childPath_});


    output.write(document.getTextValue().getBytes("utf-8"));
    output.flush();

    output.close();
    this.data=null;
  }
  public ByteArrayOutputStream getData(){
    return this.data;
  }
}

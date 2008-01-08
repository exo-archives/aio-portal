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
package org.exoplatform.web.command.handler;

import java.io.Writer;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.upload.UploadResource;
import org.exoplatform.upload.UploadService;
import org.exoplatform.web.WebAppController;
import org.exoplatform.web.command.Command;

/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Dec 9, 2006  
 */
public class UploadHandler extends Command {
  
  static enum  UploadServiceAction {
    PROGRESS, UPLOAD, DELETE, ABORT
  }
  
  private String action ;
  private String [] uploadId;
  
  public void setAction(String action) { this.action = action; }
  
  public void setUploadId(String[] uploadId) { this.uploadId = uploadId; }

  @SuppressWarnings("unused")
  public void execute(WebAppController controller,  HttpServletRequest req, HttpServletResponse res) throws Exception { 
    res.setHeader("Cache-Control", "no-cache");
    
    ExoContainer container =  PortalContainer.getInstance();
    UploadService service = (UploadService)container.getComponentInstanceOfType(UploadService.class) ;
    if(action == null ||  action.length() < 1) return;
    
    UploadServiceAction  uploadActionService = UploadServiceAction.valueOf(action.toUpperCase());
    if(uploadActionService == UploadServiceAction.PROGRESS){
      Writer  writer = res.getWriter();
      if(uploadId == null) return;        
      StringBuilder value = new StringBuilder();
      value.append("{\n  upload : {");
      for(int i=0; i<uploadId.length; i++){
        UploadResource upResource = service.getUploadResource(uploadId[i]);
        if(upResource == null) continue;
        double percent =  100;
        if(upResource.getStatus() == UploadResource.UPLOADING_STATUS){
          percent = (upResource.getUploadedSize()*100)/upResource.getEstimatedSize();
        }
        value.append("\n    \"").append(uploadId[i]).append("\": {");
        value.append("\n      \"percent\":").append('\"').append((int)percent).append("\",");
        value.append("\n      \"fileName\":").append('\"').append(encodeName(upResource.getFileName())).append("\"");
        value.append("\n    }");
        if(i < uploadId.length - 1) value.append(',');
      }       
      value.append("\n  }\n}");
      writer.append(value);
    }else if(uploadActionService == UploadServiceAction.UPLOAD){
      service.createUploadResource(req) ;       
    }else if(uploadActionService == UploadServiceAction.DELETE){
      service.removeUpload(uploadId[0]);
    }else if(uploadActionService == UploadServiceAction.ABORT){
      UploadResource upResource = service.getUploadResource(uploadId[0]);
      if(upResource != null) upResource.setStatus(UploadResource.UPLOADED_STATUS) ;
    }    
  }
  
  public String encodeName(String name) throws Exception  {
    String[] arr = name.split(" ");
    String str = "";
    for(int i = 0;i < arr.length;i++) {
      str += " " + URLEncoder.encode(arr[i], "UTF-8");
    }
    return str;
  }
  
}
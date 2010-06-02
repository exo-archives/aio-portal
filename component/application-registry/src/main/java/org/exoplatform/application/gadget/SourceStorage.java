/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
package org.exoplatform.application.gadget;

/**
 * Created by The eXo Platform SAS
 * Author : Pham Thanh Tung
 *          thanhtungty@gmail.com
 * Aug 6, 2008  
 */
public interface SourceStorage {

  /**
   * Return Source object which is retrieved from db
   * @param sourcePath
   */
  public Source getSource(String sourcePath) throws Exception ;
  
  /**
   * create or update a Source node in db
   * @param dirPath - if not null, path to node will be created in db
   * @param source - Source object that will be saved
   */
  public void saveSource(String dirPath, Source source) throws Exception ;
  
  /**
   * delete source node from db
   * @param sourcePath - path to node
   */
  public void removeSource(String sourcePath) throws Exception ;
  
  /**
   * Return uri to source
   * @param sourcePath
   */
  public String getSourceURI(String sourcePath) ;

}
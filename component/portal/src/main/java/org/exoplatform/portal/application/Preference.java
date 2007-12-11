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
package org.exoplatform.portal.application;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 27, 2003
 * Time: 9:21:41 PM
 */
public class Preference {
  
  private String name;
  private ArrayList<String> values = new ArrayList<String>(3);
  private boolean readOnly = false;  
  
  public Preference () {
  }
    
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  
  public List getValues() { return values; }
  public void setValues(ArrayList<String> values) { this.values = values; }
  public void addValue(String value) { values.add(value) ;}

  public boolean isReadOnly() { return readOnly; }
  public void    setReadOnly(boolean b) { readOnly = b ; }
  
}
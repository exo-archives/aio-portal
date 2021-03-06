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
package org.exoplatform.services.token;

import org.exoplatform.services.chars.CharsUtil;
import org.exoplatform.services.chars.SpecChar;
import org.exoplatform.services.common.util.Queue;

/**
 *  Author : Nhu Dinh Thuan
 *          Email:nhudinhthuan@yahoo.com
 * Aug 5, 2006
 */
public class TokenParser {  

  public static abstract class Factory<T> extends Queue<T>  {
    
    public abstract int create(char [] data, int start, int end, int type);
    
  }

  private static char [] closeComment = {SpecChar.HYPHEN, SpecChar.HYPHEN, SpecChar.CLOSE_TAG};

  public synchronized <T,E extends Factory<T>> void createBeans(Factory<T> factory, char [] data) throws Exception {
    if( data == null || data.length < 2) return ;    
    int start = 1;
    int end = 0;  
    boolean open = false;    
    while(end < data.length){      
      if(data[end] == SpecChar.OPEN_TAG && !open){          
        start = factory.create(data, start, end, TypeToken.CONTENT)+1;
        open = true;
      }else if(data[end] == SpecChar.OPEN_TAG && open){
        if(Character.isWhitespace(data[start])){
          start = factory.create(data, start-1, end, TypeToken.CONTENT)+1;
        }else{
          start = factory.create(data, start, end, TypeToken.TAG)+1;
        }
        open = true;
      }else if(data[end] == SpecChar.CLOSE_TAG && open) {    
        start = factory.create(data, start, end, TypeToken.TAG)+1;
        if(end < (start-1)) end = start-1;
        open = false;
      } else if(data[end] == SpecChar.PUNCTUATION_MASK) {
        if(isComment(data, end)){
          start = end;          
          end  = findEndComment(data, end); 
          start = factory.create(data, start-1, end, TypeToken.COMMENT); 
          if(start < data.length  && data[start] == SpecChar.OPEN_TAG) start++; 
          open = false;
          continue;
        }
      }
      end++;      
    }
    if(start<end) factory.create(data, start, end, TypeToken.CONTENT);   
  } 

  public boolean isComment(char[] value, int start){
    if(start < 1) return false;
    if(value[start-1] != SpecChar.OPEN_TAG) return false;
    return value[start+1] == SpecChar.HYPHEN && value[start+2] == SpecChar.HYPHEN;
  }

  public int findEndComment(char[] value, int start){
    int index = -1;
    index = CharsUtil.indexOf(value, closeComment, start);
    if(index > -1) return index+3;
    index = CharsUtil.indexOf(value, SpecChar.n, start);
    if(index > -1) return index+1;
    return value.length;
  }  


}

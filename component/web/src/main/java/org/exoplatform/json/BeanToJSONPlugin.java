/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.json;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan.nguyen@exoplatform.com
 * Mar 20, 2007  
 */
abstract public class BeanToJSONPlugin<T> {
  
  protected JSONService service_;
  protected DateFormat dateFormat_;
  
  abstract public void toJSONScript(T object, StringBuilder builder, int indentLevel) throws Exception;
  
  protected void appendIndentation(StringBuilder builder,  int indentLevel) {
    for(int i = 0; i < indentLevel * JSONService.NUMBER_SPACE; i++) builder.append(' ');
  }

  public void setService(JSONService service) { service_ = service; }
  
  
  protected boolean isCharacterType(Class type) {
    return ( type == char.class          ||
            type == String.class         ||
            type == Character.class      ||
            type == StringBuilder.class  ||
            type == StringBuffer.class
          );
  }
  
  protected boolean isDateType(Class type) {
    return ( type == Date.class || type == Calendar.class );
  }

  protected boolean isPrimitiveType(Class type) {
    return ( type == int.class    ||
            type == long.class    ||
            type == byte.class    ||
            type == double.class  ||
            type == float.class   ||
            type == boolean.class ||
            type == short.class   ||
    
            type == Integer.class || 
            type == Long.class    || 
            type == Boolean.class ||
            type == Double.class  || 
            type == Float.class   || 
            type == Short.class 
         );
  }

  protected String encode(CharSequence seq) {
    StringBuilder builder = new StringBuilder();
    int i = 0;
    int start  = 0;
    while(i < seq.length()) {
      if(seq.charAt(i) == '\'') {
        builder.append(seq.subSequence(start, i));
        builder.append('\\').append('\\').append('\'');
        start = i+1;
      } else if (seq.charAt(i) == '\"') {
        builder.append(seq.subSequence(start, i));
        builder.append('\\').append('\"');
        start = i+1;
      }
      i++;
    }
    if(start > 0 && start < seq.length()) {
      builder.append(seq.subSequence(start, seq.length())); 
    }
    if(builder.length() < 1) return seq.toString(); 
    return builder.toString();
  }

  protected void toDateValue(StringBuilder builder, Object value) {
    String dateValue = null;
    Date date = null;
    if(value instanceof Calendar) {
      date = ((Calendar)value).getTime();
    } else if(value instanceof Date) {
      date = (Date)value;
    } 
    
    if(dateFormat_ != null)  dateValue = dateFormat_.format(date); else dateValue = date.toString();
    builder.append('\'').append(encode(dateValue)).append('\'').append(',').append('\n');
  }
  
  public void setDateFormat(DateFormat dateFormat) { dateFormat_ = dateFormat; }
  
}

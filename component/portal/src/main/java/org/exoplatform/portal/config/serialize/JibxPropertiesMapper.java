/***************************************************************************
 * Copyright 2001-2007 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.config.serialize;

import java.util.Iterator;
import java.util.Map;

import org.exoplatform.portal.config.model.Properties;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;
/**
 * Created by The eXo Platform SARL
 * Author : Nhu Dinh Thuan
 *          nhudinhthuan@exoplatform.com
 * Jun 2, 2007  
 */
public class JibxPropertiesMapper implements IMarshaller, IUnmarshaller, IAliasable {

  private static final String SIZE_ATTRIBUTE_NAME = "size";
  private static final String ENTRY_ELEMENT_NAME = "entry";
  private static final String KEY_ATTRIBUTE_NAME = "key";
  private static final int DEFAULT_SIZE = 10;

  private String marshalURI;
  private int marshallIndex;
  private String marshallName;

  public JibxPropertiesMapper() {
    marshalURI = null;
    marshallIndex = 0;
    marshallName = "properties";
  }

  public JibxPropertiesMapper(String uri, int index, String name) {
    marshalURI = uri;
    marshallIndex = index;
    marshallName = name;
  }

  @SuppressWarnings("unused")
  public boolean isExtension(int index) { return false; }

  public void marshal(Object obj, IMarshallingContext ictx) throws JiBXException {
    if (!(obj instanceof Properties)) throw new JiBXException("Invalid object type for marshaller");
    if (!(ictx instanceof MarshallingContext)) throw new JiBXException("Invalid object type for marshaller");
    
    MarshallingContext ctx = (MarshallingContext)ictx;
    Properties map = (Properties)obj;
    MarshallingContext mContext = ctx.startTagAttributes(marshallIndex, marshallName);
    mContext.attribute(marshallIndex, SIZE_ATTRIBUTE_NAME, map.size()).closeStartContent();

    Iterator iter = map.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();
      if(key == null || value == null) continue;
      ctx.startTagAttributes(marshallIndex, ENTRY_ELEMENT_NAME);
      ctx.attribute(marshallIndex, KEY_ATTRIBUTE_NAME, key);      
      ctx.closeStartContent();
      ctx.content(value);
      ctx.endTag(marshallIndex, ENTRY_ELEMENT_NAME);
    }

    ctx.endTag(marshallIndex, marshallName);
  }

  public boolean isPresent(IUnmarshallingContext ctx) throws JiBXException {
    return ctx.isAt(marshalURI, marshallName);
  }

  @SuppressWarnings("unchecked")
  public Object unmarshal(Object obj, IUnmarshallingContext ictx) throws JiBXException {
    UnmarshallingContext ctx = (UnmarshallingContext)ictx;
    if (!ctx.isAt(marshalURI, marshallName)) ctx.throwStartTagNameError(marshalURI, marshallName);

    int size = ctx.attributeInt(marshalURI, SIZE_ATTRIBUTE_NAME, DEFAULT_SIZE);
    Properties map = (Properties)obj;
    if (map == null) map = new Properties(size);

    ctx.parsePastStartTag(marshalURI, marshallName);
    while (ctx.isAt(marshalURI, ENTRY_ELEMENT_NAME)) {
      Object key = ctx.attributeText(marshalURI, KEY_ATTRIBUTE_NAME, null);
      ctx.next();
      Object value = ctx.getText();
      map.put(key.toString(), value.toString());
      ctx.parsePastEndTag(marshalURI, ENTRY_ELEMENT_NAME);
    }
    ctx.parsePastEndTag(marshalURI, marshallName);
    return map;
  }
}
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
package org.exoplatform.portal.pc;

import org.gatein.pc.state.StateConverter;
import org.gatein.pc.state.StateConversionException;
import org.gatein.pc.state.SimplePropertyMap;
import org.gatein.pc.state.producer.PortletState;
import org.gatein.pc.api.PortletStateType;
import org.gatein.pc.api.state.PropertyMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoStateConverter implements StateConverter {

  public <S extends Serializable> S marshall(PortletStateType<S> stateType, PortletState state)
        throws StateConversionException, IllegalArgumentException
  {
     if (stateType.getJavaType().equals(HashMap.class))
     {
        Object map = marshall(state);
        return (S)map;
     }
     else
     {
        throw new UnsupportedOperationException();
     }
  }

  public HashMap marshall(PortletState state)
  {
     if (state == null)
     {
        throw new IllegalArgumentException("No null state");
     }

     HashMap map = new HashMap();
     Iterator<String> iKeys = state.getProperties().keySet().iterator();
     while (iKeys.hasNext())
     {
        String key = iKeys.next();
        List<String> propList = state.getProperties().getProperty(key);

        map.put(key, propList.toArray());
     }

     map.put("portletID", state.getPortletId());

     return map;
  }

  public <S extends Serializable> PortletState unmarshall(PortletStateType<S> stateType, S marshalledState)
        throws StateConversionException, IllegalArgumentException
  {
     if (stateType.getJavaType().equals(HashMap.class))
     {
        HashMap map = (HashMap)marshalledState;
        return unmarshall(map);
     }
     else
     {
        throw new UnsupportedOperationException();
     }
  }

  public PortletState unmarshall(Map marshalledState)
  {
     if (marshalledState == null)
     {
        throw new IllegalArgumentException("No null map");
     }

     PropertyMap properties = new SimplePropertyMap(marshalledState.size());


     Iterator<String> iKeys = marshalledState.keySet().iterator();
     while (iKeys.hasNext())
     {
        String key = iKeys.next();
        if (key != "portletID")
        {
           Object mapValue = marshalledState.get(key);

           if (mapValue instanceof Object[])
           {
              Object[] values = (Object[])mapValue;

              List valueList = new ArrayList<String>();
              for (Object value: values)
              {
                 if (value instanceof String)
                    valueList.add((String)value);
              }

              properties.put(key, valueList);
           }
        }
     }

     String portletID = (String) marshalledState.get("portletID");

     return new PortletState(portletID, properties);
  }

}

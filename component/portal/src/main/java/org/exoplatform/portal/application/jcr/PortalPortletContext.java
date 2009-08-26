package org.exoplatform.portal.application.jcr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.exoplatform.portal.application.PortletPreferences;
import org.exoplatform.portal.application.Preference;
import org.exoplatform.services.portletcontainer.pci.ExoWindowID;
import org.gatein.pc.api.PortletContext;
import org.gatein.pc.api.PortletStateType;
import org.gatein.pc.api.StatefulPortletContext;

public class PortalPortletContext<P extends Map & Serializable> extends StatefulPortletContext<P>
{
	public static final PortletStateType<HashMap> HASHSTATETYPE = new PortletStateType<HashMap>()
	{

		@Override
		public boolean equals(HashMap state1, HashMap state2) {
			return state1.equals(state2);
		}

		@Override
		public Class<HashMap> getJavaType() {
			return HashMap.class;
		}

		@Override
		public int hashCode(HashMap state) {
			return state.hashCode();
		}

		@Override
		public String toString(HashMap state) {
			return state.toString();
		}
		
	};
	
	public PortalPortletContext(String id , PortletStateType<P> stateType, P preferences)
	{
		super(id, stateType, preferences);
	}
	
	public static PortletContext createPortalPortletContext (PortletContext context, PortletPreferences preferences)
	{
		String id = context.getId();
		HashMap map = new HashMap();
		map.put("portletID", id);
		if (preferences != null)
	    {
	    	ArrayList<Preference> preferenceList = preferences.getPreferences();

	    	for (Preference pref : preferenceList)
	    	{
	    		String key = pref.getName();
	    		List values = pref.getValues();
	    		map.put(key, values.toArray());
	    	}
	    	return new PortalPortletContext<HashMap>("_dumb", PortalPortletContext.HASHSTATETYPE, map);
	    }
		else
		{
			return new PortalPortletContext<HashMap>(context.getId(), PortalPortletContext.HASHSTATETYPE, map);
		}
		
	}
	
}

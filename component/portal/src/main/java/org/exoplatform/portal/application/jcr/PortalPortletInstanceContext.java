package org.exoplatform.portal.application.jcr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.portal.application.PortletPreferences;
//import org.exoplatform.portal.application.Preference;
import org.exoplatform.portal.config.DataStorage;
import org.exoplatform.portal.pc.ExoPortletStateType;
import org.exoplatform.services.portletcontainer.pci.WindowID;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;
import org.gatein.pc.api.PortletContext;
import org.gatein.pc.api.PortletStateType;
import org.gatein.pc.api.StateEvent;
import org.gatein.pc.api.StatefulPortletContext;
import org.gatein.pc.api.spi.InstanceContext;
import org.gatein.pc.api.state.AccessMode;

public class PortalPortletInstanceContext implements InstanceContext
{

	   /** . */
	   private final String id;

	   /** . */
	   private final AccessMode accessMode;

	   /** . */
	   private PortletContext clonedContext;

	   /** . */
	   private PortletContext modifiedContext;
	   
	   private WindowID windowId;

	   public PortalPortletInstanceContext(String id, WindowID windowID)
	   {
		   this(id, windowID, AccessMode.READ_WRITE);
	   }

	   public PortalPortletInstanceContext(String id, WindowID windowID, AccessMode accessMode)
	   {
		  this.windowId = windowID;
	      if (id == null)
	      {
	         throw new IllegalArgumentException();
	      }
	      if (accessMode == null)
	      {
	         throw new IllegalArgumentException();
	      }

	      //
	      this.id = id;
	      this.accessMode = accessMode;
	   }

	   public String getId()
	   {
	      return id;
	   }

	   public AccessMode getAccessMode()
	   {
	      return accessMode;
	   }

	   public void onStateEvent(StateEvent event)
	   {
	      switch (event.getType())
	      {
	         case PORTLET_CLONED_EVENT:
	            clonedContext = event.getPortletContext();
	            break;
	         case PORTLET_MODIFIED_EVENT:
	            modifiedContext = event.getPortletContext();
	            
/*
	            ExoContainer container = ExoContainerContext.getCurrentContainer();
	            DataStorage dataStorage = (DataStorage)container.getComponentInstance(DataStorage.class);
	            
	            ExoPortletPreferences exoPortletPreferences = new ExoPortletPreferences();
	            
	            PortletContext portletContext = event.getPortletContext();
	            if (portletContext instanceof StatefulPortletContext)
	            {
	            	StatefulPortletContext statefulPortletContext = (StatefulPortletContext) portletContext;
	            	if (statefulPortletContext.getType().equals(PortalPortletContext.HASHSTATETYPE))
	            	{
	            		HashMap map = (HashMap)statefulPortletContext.getState();
	            		Iterator<String> iterator = map.keySet().iterator();
	            		while (iterator.hasNext())
	            		{
	            			String key = iterator.next();
	            			if (!key.equals("portletID"))
	            			{
	            				Object[] values = (Object[])map.get(key);
	            				ArrayList<String> preferences = new ArrayList<String>(values.length);
	            				for (Object value : values)
	            				{
	            					preferences.add((String)value);
	            				}
	            				Preference preference = new Preference();
	            				preference.setName(key);
	            				preference.setValues(preferences);
	            				exoPortletPreferences.addPreference(preference);
	            			}
	            		}
	            		
	            		PortletPreferences portletPreferences = new PortletPreferences(exoPortletPreferences);
	            		try
	            		{
	            			portletPreferences.setWindowId(this.windowId.getPersistenceId());
	            			
	            			String owner = windowId.getOwner();
	            		    String [] components = owner.split("#");
	            		    if(components.length < 2) throw new Exception("WindowId is invalid "+windowId);
	            			
	            			portletPreferences.setOwnerType(components[0]);
	            			portletPreferences.setOwnerId(components[1]);
	            			dataStorage.save(portletPreferences);
	            		}
	            		catch (Exception e)
	            		{
	            			e.printStackTrace();
	            		}
	            	}
	            }
	            break;
*/

             throw new UnsupportedOperationException("TODO");
	      }
	   }

	   public PortletContext getClonedContext()
	   {
	      return clonedContext;
	   }

	   public PortletContext getModifiedContext()
	   {
	      return modifiedContext;
	   }

	   public ExoPortletStateType getStateType() {
		  return ExoPortletStateType.getInstance();
	   }
	}

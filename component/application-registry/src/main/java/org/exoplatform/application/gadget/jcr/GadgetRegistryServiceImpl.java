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
package org.exoplatform.application.gadget.jcr;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;

import org.exoplatform.application.gadget.Gadget;
import org.exoplatform.application.gadget.GadgetRegistryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.registry.RegistryEntry;
import org.exoplatform.services.jcr.ext.registry.RegistryService;

/**
 * Created by The eXo Platform SAS
 * Author : Pham Thanh Tung
 *          thanhtungty@gmail.com
 * Jun 18, 2008  
 */
public class GadgetRegistryServiceImpl implements GadgetRegistryService {
  
  private static final String PATH = RegistryService.EXO_SERVICES + "/Gadgets" ;
  
  private RegistryService regService_ ;
  private DataMapper mapper_ = new DataMapper() ;
  
  public GadgetRegistryServiceImpl(RegistryService service) throws Exception {
    regService_ = service ;
  }

  public Gadget getGadget(String name) throws Exception {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry entry ;
    try {
      entry = regService_.getEntry(sessionProvider, PATH + "/" + name) ;
    } catch (PathNotFoundException pnfe) {
      sessionProvider.close() ;
      return null ;
    }
    Gadget gadget = mapper_.toApplciation(entry.getDocument()) ;
    sessionProvider.close() ;    
    return gadget ;
  }
  
  public List<Gadget> getAllGadgets() throws Exception {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    Node regNode = regService_.getRegistry(sessionProvider).getNode() ;
    if(!regNode.hasNode(PATH)) {
      sessionProvider.close() ;
      return new ArrayList<Gadget>() ;
    }
    NodeIterator itr = regNode.getNode(PATH).getNodes() ;
    List<Gadget> gadgets = new ArrayList<Gadget>() ;
    while(itr.hasNext()) {
      String entryPath = itr.nextNode().getPath().substring(regNode.getPath().length() + 1) ;
      RegistryEntry entry = regService_.getEntry(sessionProvider, entryPath) ;
      Gadget gadget = mapper_.toApplciation(entry.getDocument()) ;
      gadgets.add(gadget) ;
    }
    sessionProvider.close() ;
    return gadgets ;
  }
  
  public void addGadget(Gadget gadget) throws Exception {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    RegistryEntry entry ;
    try {
      entry = regService_.getEntry(sessionProvider, PATH + "/" + gadget.getName()) ;
      mapper_.map(entry.getDocument(), gadget) ;
      regService_.recreateEntry(sessionProvider, PATH, entry) ;
    } catch (PathNotFoundException pnfe) {
      entry = new RegistryEntry(gadget.getName()) ;
      mapper_.map(entry.getDocument(), gadget) ;
      regService_.createEntry(sessionProvider, PATH, entry) ;
    } finally {
      sessionProvider.close() ;      
    }
  }

  public void removeGadget(String id) throws Exception {
    SessionProvider sessionProvider = SessionProvider.createSystemProvider() ;
    regService_.removeEntry(sessionProvider, PATH + "/" + id) ;
    sessionProvider.close() ;
  }
  
}
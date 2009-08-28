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

import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.RootContainer;
import org.jboss.portal.portlet.api.PortletInvoker;
import org.jboss.portal.portlet.container.ContainerPortletDispatcher;
import org.jboss.portal.portlet.container.ContainerPortletInvoker;
import org.jboss.portal.portlet.federation.FederatingPortletInvoker;
import org.jboss.portal.portlet.federation.impl.FederatingPortletInvokerService;
import org.jboss.portal.portlet.PortletInvokerInterceptor;
import org.jboss.portal.portlet.impl.state.producer.PortletStatePersistenceManagerService;
import org.jboss.portal.portlet.impl.state.StateManagementPolicyService;
import org.jboss.portal.portlet.mc.PortletApplicationDeployer;
import org.jboss.portal.portlet.state.producer.ProducerPortletInvoker;
import org.jboss.portal.portlet.state.producer.PortletStatePersistenceManager;
import org.jboss.portal.portlet.state.StateConverter;
import org.jboss.portal.portlet.aspects.portlet.ConsumerCacheInterceptor;
import org.jboss.portal.portlet.aspects.portlet.PortletCustomizationInterceptor;
import org.jboss.portal.portlet.aspects.portlet.EventPayloadInterceptor;
import org.jboss.portal.portlet.aspects.portlet.RequestAttributeConversationInterceptor;
import org.jboss.portal.portlet.aspects.portlet.CCPPInterceptor;
import org.jboss.portal.portlet.aspects.portlet.ProducerCacheInterceptor;
import org.jboss.portal.portlet.aspects.portlet.ContextDispatcherInterceptor;
import org.jboss.portal.portlet.aspects.portlet.SecureTransportInterceptor;
import org.jboss.portal.portlet.aspects.portlet.ValveInterceptor;
import org.picocontainer.Startable;
import org.gatein.wci.ServletContainer;
import org.gatein.wci.impl.DefaultServletContainerFactory;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class ExoKernelIntegration implements Startable {

  protected PortletApplicationDeployer portletApplicationRegistry;

  public void start()
  {
     ExoContainer container = RootContainer.getInstance();

     // The producer persistence manager
     PortletStatePersistenceManagerService producerPersistenceManager = new PortletStatePersistenceManagerService();
     // register the persistence manager so that it can be used by WSRP
     container.registerComponentInstance(PortletStatePersistenceManager.class, producerPersistenceManager);

     // The producer state management policy
     StateManagementPolicyService producerStateManagementPolicy = new StateManagementPolicyService();
     producerStateManagementPolicy.setPersistLocally(false);

     // The producer state converter
     StateConverter producerStateConverter = new ExoStateConverter();//StateConverterV0();

     // The portlet container invoker
     ContainerPortletInvoker containerPortletInvoker = new ContainerPortletInvoker();
     // continued below


     // The portlet application deployer
     portletApplicationRegistry = new PortletApplicationDeployer();
     portletApplicationRegistry.setContainerPortletInvoker(containerPortletInvoker);

     //Container Stack
     ContainerPortletDispatcher portletContainerDispatcher = new ContainerPortletDispatcher();
     EventPayloadInterceptor eventPayloadInterceptor = new EventPayloadInterceptor();
     eventPayloadInterceptor.setNext(portletContainerDispatcher);
     RequestAttributeConversationInterceptor requestAttributeConversationInterceptor = new RequestAttributeConversationInterceptor();
     requestAttributeConversationInterceptor.setNext(eventPayloadInterceptor);
     CCPPInterceptor ccppInterceptor = new CCPPInterceptor();
     ccppInterceptor.setNext(requestAttributeConversationInterceptor);
     ProducerCacheInterceptor producerCacheInterceptor = new ProducerCacheInterceptor();
     producerCacheInterceptor.setNext(ccppInterceptor);
     ContextDispatcherInterceptor contextDispatcherInterceptor = new ContextDispatcherInterceptor();
     contextDispatcherInterceptor.setNext(producerCacheInterceptor);
     SecureTransportInterceptor secureTransportInterceptor = new SecureTransportInterceptor();
     secureTransportInterceptor.setNext(contextDispatcherInterceptor);
     ValveInterceptor valveInterceptor = new ValveInterceptor();
     valveInterceptor.setPortletApplicationRegistry(portletApplicationRegistry);
     valveInterceptor.setNext(secureTransportInterceptor);

     // inject ServletContainer in objects that need it
     ServletContainer servletContainer = (ServletContainer)container.getComponentInstance(ServletContainer.class);
     portletApplicationRegistry.setServletContainerFactory(DefaultServletContainerFactory.getInstance());
     contextDispatcherInterceptor.setServletContainerFactory(DefaultServletContainerFactory.getInstance());

     // The portlet container invoker continued
     containerPortletInvoker.setNext(valveInterceptor);

     // The producer portlet invoker
     ProducerPortletInvoker producerPortletInvoker = new ProducerPortletInvoker();
     producerPortletInvoker.setNext(containerPortletInvoker);
     producerPortletInvoker.setPersistenceManager(producerPersistenceManager);
     producerPortletInvoker.setStateManagementPolicy(producerStateManagementPolicy);
     producerPortletInvoker.setStateConverter(producerStateConverter);

     // register producer portlet invoker so that WSRP can use it
     container.registerComponentInstance(ProducerPortletInvoker.class, producerPortletInvoker);

     // The consumer portlet invoker
     PortletCustomizationInterceptor portletCustomizationInterceptor = new PortletCustomizationInterceptor();
     portletCustomizationInterceptor.setNext(producerPortletInvoker);
     ConsumerCacheInterceptor consumerCacheInterceptor = new ConsumerCacheInterceptor();
     consumerCacheInterceptor.setNext(portletCustomizationInterceptor);
     PortletInvokerInterceptor consumerPortletInvoker = new PortletInvokerInterceptor();
     consumerPortletInvoker.setNext(consumerCacheInterceptor);

     //container.registerComponentInstance(PortletInvoker.class, consumerPortletInvoker);

     // Federating portlet invoker
     FederatingPortletInvoker federatingPortletInvoker = new FederatingPortletInvokerService();

     // register local portlet invoker with federating portlet invoker
     federatingPortletInvoker.registerInvoker(FederatingPortletInvoker.LOCAL_PORTLET_INVOKER_ID, consumerPortletInvoker);
     /* register with container */
     container.registerComponentInstance(PortletInvoker.class, federatingPortletInvoker);

     portletApplicationRegistry.start();
  }


  public void stop()
  {
     if (portletApplicationRegistry != null)
     {
        portletApplicationRegistry.stop();
     }
  }
}

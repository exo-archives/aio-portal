eXo.require("eXo.projects.Module") ;
eXo.require("eXo.projects.Product") ;

function getProduct(version) {
  var product = new Product();
  
  product.name = "eXoPortal" ;
  product.portalwar = "portal.war" ;
  product.codeRepo = "portal" ;//module in modules/portal/module.js
  product.serverPluginVersion = "3.0.0-Beta01"

  var kernel = Module.GetModule("kernel/tags/2.2.Alpha3") ;
  var core = Module.GetModule("core/tags/2.3.Alpha4") ;
  var ws = Module.GetModule("ws/tags/2.1.Alpha4", {kernel : kernel, core : core});
  var eXoJcr = Module.GetModule("jcr/tags/1.12.Alpha4", {kernel : kernel, core : core, ws : ws}) ;
  
//  var kernel = Module.GetModule("kernel/tags/2.1.2") ;
//  var core = Module.GetModule("core/tags/2.2.2") ;
//  var ws = Module.GetModule("ws/tags/2.0.2", {kernel : kernel, core : core});
//  var eXoJcr = Module.GetModule("jcr/tags/1.11.2", {kernel : kernel, core : core, ws : ws}) ;
  var portal = Module.GetModule("portal", {kernel : kernel, ws:ws, core : core, eXoJcr : eXoJcr});
  
  product.addDependencies(portal.web.rest) ;
  product.addDependencies(portal.portlet.exoadmin) ;
  product.addDependencies(portal.portlet.web) ;
  product.addDependencies(portal.portlet.dashboard) ;
  product.addDependencies(portal.eXoGadgetServer) ;
  product.addDependencies(portal.eXoGadgets) ;
  product.addDependencies(portal.webui.portal);
  
  product.addDependencies(portal.web.eXoResources);
  product.addDependencies(portal.web.eXoMacSkin);
  product.addDependencies(portal.web.eXoVistaSkin);

  product.addDependencies(portal.web.portal) ;

  product.addServerPatch("tomcat", portal.server.tomcat.patch) ;
  product.addServerPatch("jboss",  portal.server.jboss.patch) ;
  product.addServerPatch("jbossear",  portal.server.jbossear.patch) ;
  product.addServerPatch("jonas",  portal.server.jonas.patch) ;
  product.addServerPatch("ear",  portal.server.websphere.patch) ;

  /* cleanup duplicated lib */
  product.removeDependency(new Project("commons-httpclient", "commons-httpclient", "jar", "3.0"));
  product.removeDependency(new Project("commons-collections", "commons-collections", "jar", "3.1"));

  product.module = portal ;
  product.dependencyModule = [ kernel, core, ws, eXoJcr];

  return product ;
}

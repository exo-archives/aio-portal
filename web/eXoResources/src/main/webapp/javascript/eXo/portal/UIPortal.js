/**
 * This class contains information that describes a document object
 * @param node
 * @return
 */
function UIComponent(node) {
  this.node = node ;
  if(node) this.type = node.className ;
  var children =  eXo.core.DOMUtil.getChildrenByTagName(node, "div") ;
  if(children.length > 0) {
	  this.metaData =  children[0] ;
	  this.control = children[1] ; 
	  this.layout = children[2] ; 
	  this.view = children[3] ;
  } 
  this.component = "";
  
  var div = eXo.core.DOMUtil.getChildrenByTagName(this.metaData, "div");
  if(div.length > 0) {
  	this.id = div[0].firstChild.nodeValue ;
  	this.title = div[1].firstChild.nodeValue ;
  }
	//minh.js.exo
 //bug PORTAL-1161.
	//this.description = div[2].firstChild.nodeValue ;
};
//UIComponent.prototype.getDescription = function() { return this.description ; };

UIComponent.prototype.getId = function() { return this.id ; };
UIComponent.prototype.getTitle = function() { return this.title ; };
UIComponent.prototype.getElement = function() { return this.node ; };
UIComponent.prototype.getUIComponentType = function() { return this.type ; };

UIComponent.prototype.getUIComponentBlock = function() { return this.node ; };
UIComponent.prototype.getControlBlock = function() { return this.control ; };
UIComponent.prototype.getLayoutBlock = function() { return this.layout ; };
UIComponent.prototype.getViewBlock = function() { return this.view ; };

/*******************************************************************************/

function UIPortal() {
  this.portalUIComponentDragDrop = false;
};
/**
 * Find a UIComponent (as UIPortlet, UIContainer, UIPageBody, UIPortal) 
 * that is parent of element parameter
 * @param element
 */
UIPortal.prototype.findUIComponentOf = function(element) {
  var parent = element.parentNode ;
  while(parent != null) {
    var className = parent.className ;
    if(className == 'UIPortlet' || className == 'UIContainer' ||  
       className == 'UIPageBody' ||  className == 'UIPortal')  {
      return parent ;
    }
    parent = parent.parentNode ;
  }
  return null ;
};
/**
 * Display Mask layer in front of page's surface
 */
UIPortal.prototype.showMaskLayer = function() {
	
	var uiPortalApplication = document.getElementById("UIPortalApplication") ;
	var object = document.createElement("div") ;
	object.className = "PreviewMode" ;
	object.style.display = "none" ;
	uiPortalApplication.appendChild(object) ;

	this.maskLayer = eXo.core.UIMaskLayer.createMask("UIPortalApplication", object, 30, "TOP-RIGHT") ;
	this.maskLayer.title = this.previewTitle ;
	this.maskLayer.style.cursor = "pointer" ;
	this.maskLayer.onclick = function() {
		ajaxGet(eXo.env.server.createPortalURL("UIWorkingWorkspace", "Preview", true, null));
	}
	this.maskLayer.style.zIndex = parseInt(object.style.zIndex) + 1 ;
	eXo.core.Browser.addOnScrollCallback("3743892", eXo.core.UIMaskLayer.setPosition) ;
} ;
/**
 * Remove mask layer from page's surface
 */
UIPortal.prototype.hideMaskLayer = function() {
	if(this.maskLayer) {
		var uiPortalApplication = document.getElementById("UIPortalApplication") ;
		eXo.core.UIMaskLayer.removeMask(this.maskLayer) ;
		this.maskLayer = null ;
		var maskObject = eXo.core.DOMUtil.findFirstDescendantByClass(uiPortalApplication, "div", "PreviewMode") ;
		uiPortalApplication.removeChild(maskObject) ;
	}
} ;
/**
 * Change skin of Portal
 * @param url
 */
UIPortal.prototype.changeSkin = function(url) {
 var skin = '';
 if(eXo.webui.UIItemSelector.SelectedItem != undefined) {
   skin = eXo.webui.UIItemSelector.SelectedItem.option;
 }
 if(skin == undefined) skin = '';
  //ajaxAsyncGetRequest(url + '&skin='+skin, false);
  window.location = url + '&skin='+skin;
} ;
/**
 * Change language of Portal
 * @param url
 */
UIPortal.prototype.changeLanguage = function(url) {
	var language = '';
	if(eXo.webui.UIItemSelector.SelectedItem != undefined) {
  	language = eXo.webui.UIItemSelector.SelectedItem.option;
	}
	if(language == undefined) language = '';  
  //ajaxAsyncGetRequest(url + '&language='+language, false);
  window.location = url + '&language='+language;
} ;
/**
 * Change current portal
 */
UIPortal.prototype.changePortal = function(accessPath, portal) {
  window.location = eXo.env.server.context + "/" + accessPath + "/" + portal+"/";
} ;

eXo.portal.UIPortalComponent = UIComponent.prototype.constructor ;
eXo.portal.UIPortal = new UIPortal() ;
eXo.portal.UIComponent = UIPortal.prototype.constructor ;

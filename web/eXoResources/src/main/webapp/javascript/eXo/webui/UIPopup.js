eXo.require('eXo.core.DragDrop') ;

/* TODO: need to manage zIndex for all popup */
//var zIndex = 2 ;

var popupArr = new Array() ;

function UIPopup() {
	this.zIndex = 3 ;
} ;

UIPopup.prototype.init = function(popup, containerId) {
	if(typeof(popup) == "string") popup = document.getElementById(popup) ;
	if(containerId) popup.containerId = containerId ;
	popup.onmousedown = this.changezIndex ;
} ;

UIPopup.prototype.changezIndex = function() {
	this.style.zIndex = ++eXo.webui.UIPopup.zIndex;
} ;

UIPopup.prototype.create = function() {	
	var popup = document.createElement("div") ;
	with(popup.style) {
		position = "relative" ;
		display = "none" ;
	}
	return popup ;
} ;

UIPopup.prototype.setSize = function(popup, w, h) {
	popup.style.width = w + "px" ;
	popup.style.height = h + "px" ;
} ;

UIPopup.prototype.show = function(popup) {
	if(typeof(popup) == "string") {	
		popup = document.getElementById(popup) ;
	}
	popup.style.zIndex = ++eXo.webui.UIPopup.zIndex ;
	popup.style.display = "block" ;
} ;

UIPopup.prototype.hide = function(popup) {
	if(typeof(popup) == "string") {
		popup = document.getElementById(popup) ;
	}
	
	popup.style.display = "none" ;
} ;

UIPopup.prototype.setPosition = function(popup, x, y) {
	if(popup.containerId) {
		var container = document.getElementById(popup.containerId) ;
		container.style.position = "relative" ;
	}	
	popup.style.position = "absolute" ;
	popup.style.top = y + "px" ;
	popup.style.left = x + "px" ;
} ;

UIPopup.prototype.setAlign = function(popup, pos) {
	if ( typeof(popup) == 'string') popup = document.getElementById(popup) ;
	var intTop = 0 ;
	var intLeft = 0 ;
	switch (pos) {
		case 1:							// Top Left
		  intTop = 0 ;
		  intLeft = 0 ;
			break ;
		case 2:							// Top Right
  		intTop = 0 ;  		
		  intLeft = (eXo.core.Browser.getBrowserWidth() - popup.offsetWidth) ;
			break ;
		case 3:							// Bottom Left
		  intTop = (eXo.core.Browser.getBrowserHeight() - popup.offsetHeight) ;
		  intLeft = 0 ;				
			break ;
		case 4:							// Bottom Right
  		intTop = (eXo.core.Browser.getBrowserHeight() - popup.offsetHeight) ;
		  intLeft = (eXo.core.Browser.getBrowserWidth() - popup.offsetWidth) ;			
			break ;
		default:
		  intTop = (eXo.core.Browser.getBrowserHeight() - popup.offsetHeight) / 2 ;
		  intLeft = (eXo.core.Browser.getBrowserWidth() - popup.offsetWidth) / 2 ;	
			break ;
	}
	
	this.setPosition(popup, intLeft, intTop) ;
} ;

UIPopup.prototype.initDND = function(evt) {
  var DragDrop = eXo.core.DragDrop ;

	DragDrop.initCallback = null ;

  DragDrop.dragCallback = null ;

  DragDrop.dropCallback = null ;
  
  var clickBlock = this ;
  var dragBlock = eXo.core.DOMUtil.findAncestorByClass(this, "UIDragObject") ;
  DragDrop.init(null, clickBlock, dragBlock, evt) ;
} ;


UIPopup.prototype.closeAll = function() {
	var len = popupArr.length ;
	for(var i = 0 ; i < len ; i++) {
		popupArr[i].style.display = "none" ;
	}
	popupArr.clear() ;
} ;

eXo.webui.UIPopup = new UIPopup() ;
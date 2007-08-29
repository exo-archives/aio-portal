function MouseObject() {
  this.init(null) ;
} ;

MouseObject.prototype.init = function(mouseEvent) {
  this.mousexInPage = null ;
  this.mouseyInPage = null ;

  this.lastMousexInPage = null ;
  this.lastMouseyInPage = null ;

  this.mousexInClient = null ;
  this.mouseyInClient = null ;

  this.lastMousexInClient = null ;
  this.lastMouseyInClient = null ;

  this.deltax = null ;
  this.deltay = null ;
  if(mouseEvent != null) this.update(mouseEvent) ;
} ;

MouseObject.prototype.update = function(mouseEvent) {
  var  x = eXo.core.Browser.findMouseXInPage(mouseEvent) ;
  var  y = eXo.core.Browser.findMouseYInPage(mouseEvent) ;

  this.lastMousexInPage =  this.mousexInPage != null ? this.mousexInPage : x ;
  this.lastMouseyInPage =  this.mouseyInPage != null ? this.mouseyInPage : y ;

  this.mousexInPage = x ;
  this.mouseyInPage = y ;

  x  =  eXo.core.Browser.findMouseXInClient(mouseEvent) ;
  y  =  eXo.core.Browser.findMouseYInClient(mouseEvent) ;

  this.lastMousexInClient =  this.mousexInClient != null ? this.mousexInClient : x ;
  this.lastMouseyInClient =  this.mouseyInClient != null ? this.mouseyInClient : y ;

  this.mousexInClient = x ;
  this.mouseyInClient = y ;

  this.deltax = this.mousexInClient - this.lastMousexInClient ;
  this.deltay = this.mouseyInClient - this.lastMouseyInClient ;
} ;

/************************************************************************************/

/**
* This function aims is to configure the javascript environment according to the browser in use
*
* Common configuration are made first, then we detect the current browser and according to the one 
* in use, we call delegated methods such as this.initIE() which will add some new configuration or
* overide the existing ones
*
* In all cases the method createHttpRequest is acting as the usual XMLHttpRequest in use in all AJAX
* calls
*/
function Browser() {
  this.onLoadCallback = new eXo.core.HashMap() ;
  this.onResizeCallback = new eXo.core.HashMap() ;
  this.onScrollCallback = new eXo.core.HashMap() ;
  
  window.onresize =  this.onResize ;
  window.onscroll =  this.onScroll ;
  
  this.initCommon() ;
  var detect = navigator.userAgent.toLowerCase() ;
  if(detect.indexOf("opera") + 1)  this.initOpera() ;
  else if(detect.indexOf("ie") + 1) this.initIE() ;
  else if(detect.indexOf("safari") + 1) this.initSafari() ;
  else this.initMozilla() ;
} ;

Browser.prototype.initCommon = function() {
  this.getBrowserHeight = function() { return document.documentElement.clientHeight ; }
  this.getBrowserWidth = function() { return document.documentElement.clientWidth ; }
  this.createHttpRequest = function() { return new XMLHttpRequest() ; }
} ;

Browser.prototype.initIE = function() {
  this.browserType = "ie" ;
  this.createHttpRequest = function() {
  	 return new ActiveXObject("Msxml2.XMLHTTP") ; 
  }
  this.eventListener = function(object, event, operation) {
    event = "on" + event ;
    object.attachEvent(event, operation) ;
  }
  this.setOpacity = function(component, value) {component.style.filter = "alpha(opacity=" + value + ")" ;}
  this.getEventSource = function(e) { return window.event.srcElement ; }
} ;

Browser.prototype.initMozilla = function() {
  this.browserType = "mozilla" ;
  this.eventListener = function(object, event, operation) { object.addEventListener(event, operation, false) ; }
  this.setOpacity = function(component, value) { component.style.opacity = value/100 ; }
  this.getEventSource = function(e) { return e.target ; }
} ;

Browser.prototype.initSafari = function() {
  this.browserType = "safari" ;
  this.getBrowserHeight = function() { return self.innerHeight ; } ;
  this.getBrowserWidth = function() { return self.innerWidth ; } ;
  this.eventListener = function(object, event, operation) { object.addEventListener(event, operation, false) ; }
  this.setOpacity = function(component, value) { component.style.opacity = value/100 ; }
  this.getEventSource = function(e) {
  	var targ = e.target ;
  	if (targ.nodeType == 3) targ = targ.parentNode ;
  	return targ ;
  }
} ;

Browser.prototype.initOpera = function() {
  this.browserType = "opera" ;
  this.getBrowserHeight = function() {
    return document.body.clientHeight ;
  }
  this.getBrowserWidth = function() {
    return document.body.clientWidth ;
  }
} ;

Browser.prototype.isIE6 = function() {
  var agent = navigator.userAgent ;
  return (agent.indexOf("MSIE 6") >=0);
} ;

Browser.prototype.isIE7 = function() {
  var agent = navigator.userAgent ;
  return (agent.indexOf("MSIE 7") >=0);
} ;

Browser.prototype.findMouseXInClient = function(e) {
  if (!e) e = window.event ;
  return e.clientX ;
} ;

Browser.prototype.findMouseYInClient = function(e) {
  if (!e) e = window.event ;
  return e.clientY ;
} ;
/**
 * Adds a function to the list of functions to call on load
 */
Browser.prototype.addOnLoadCallback = function(id, method) {
  this.onLoadCallback.put(id, method) ;
} ;
/**
 * Calls the functions in the onLoadCallback array, if they exist
 * and clean the array
 */
Browser.prototype.onLoad = function() {
	try {
  	var callback = eXo.core.Browser.onLoadCallback ;
	  for(var name in callback.properties) {
	    var method = callback.get(name) ;
	    if (typeof(method) == "function") method() ;
	  }
	} catch(e) {}
  this.onLoadCallback = new eXo.core.HashMap();
} ;
/**
 * Adds a function to the list of functions to call when the window is resized
 */
Browser.prototype.addOnResizeCallback = function(id, method) {
  this.onResizeCallback.put(id, method) ;
} ;
/**
 * Calls the functions in the onResizeCallback array, if they exist
 */
Browser.prototype.onResize = function(event) {
  var callback = eXo.core.Browser.onResizeCallback ;
  for(var name in callback.properties) {
    var method = callback.get(name) ;
    if (typeof(method) == "function") method(event) ;
  }
} ;
/**
 * Adds a function to the list of functions to call when the user scrolls
 */
Browser.prototype.addOnScrollCallback = function(id, method) {
  this.onScrollCallback.put(id, method) ;
} ;
/**
 * Calls the functions in the onScrollCallback array, if they exist
 */
Browser.prototype.onScroll = function(event) {
  var callback = eXo.core.Browser.onScrollCallback ;
	for(var name in callback.properties) {
    var method = callback.get(name) ;
    try {
    	if (typeof(method) == "function") method(event) ;
    }catch(err){}
	}
} ;
/************************************TO BROWSER PAGE CLASS************************************************/
Browser.prototype.getBrowserType = function() {  
  return this.browserType ;
} ;
/**
 * Returns the horizontal position of an object relative to the window
 */
Browser.prototype.findPosX = function(obj) {
  var curleft = 0;
  var str = "" ;
  while (obj) {
    curleft += obj.offsetLeft ;
    obj = obj.offsetParent ;
  }
  return curleft ;
} ;
/**
 * Returns the vertical position of an object relative to the window
 */
Browser.prototype.findPosY = function(obj) {
  var curtop = 0 ;
  while (obj) {
    curtop += obj.offsetTop ;
    obj = obj.offsetParent ;
  }
  return curtop ;
} ;
/**
 * Returns the horizontal position of an object relative to its container
 */
Browser.prototype.findPosXInContainer = function(obj, container) {
  var objX = eXo.core.Browser.findPosX(obj) ;
  var containerX = eXo.core.Browser.findPosX(container) ;  
  return (objX - containerX) ;
} ;
/**
 * Returns the vertical position of an object relative to its container
 */
Browser.prototype.findPosYInContainer = function(obj, container) {
  var objY = eXo.core.Browser.findPosY(obj) ;
  var containerY = eXo.core.Browser.findPosY(container) ;
  return (objY - containerY) ;
} ;

/**
 * find the x position of the mouse in the page
 */
Browser.prototype.findMouseXInPage = function(e) {
  var posx = -1 ;
  if (!e) e = window.event ;
  if (e.pageX || e.pageY) {
    posx = e.pageX ;
  } else if (e.clientX || e.clientY) {
    posx = e.clientX + document.body.scrollLeft ;
  }
  return posx ;
} ;
/**
 * find the y position of the mouse in the page
 */
Browser.prototype.findMouseYInPage = function(e) {
  var posy = -1 ;
  if (!e) e = window.event ;
  if (e.pageY) {
    posy = e.pageY ;
  } else if (e.clientX || e.clientY) {
    //IE 6
    if (document.documentElement && document.documentElement.scrollTop) {
      posy = e.clientY + document.documentElement.scrollTop ;
    } else {
      posy = e.clientY + document.body.scrollTop ;
    }
  }
  return  posy ;
} ;
/**
 * find the x position of the mouse relative to object
 */
Browser.prototype.findMouseRelativeX = function(object, e) {
  var posx = -1 ;
  var posXObject = eXo.core.Browser.findPosX(object) ;
  
  /*
   * posXObject is added more 3px on IE6
   * posXObject is double on IE7
   * */
  
  if((eXo.core.Browser.getBrowserType() == "ie") && (!eXo.core.Browser.isIE6())) {
  	posXObject = posXObject / 2 ;
  }
  
  if (!e) e = window.event ;
  if (e.pageX || e.pageY) {
    posx = e.pageX - posXObject ;
  } else if (e.clientX || e.clientY) {
    posx = e.clientX + document.body.scrollLeft - posXObject ;
  }
  return posx ;
} ;
/**
 * find the y position of the mouse relative to object
 */
Browser.prototype.findMouseRelativeY = function(object, e) {
  var posy = -1 ;
  var posYObject = eXo.core.Browser.findPosY(object) ;
  if (!e) e = window.event ;
  if (e.pageY) {
    posy = e.pageY - posYObject ;
  } else if (e.clientX || e.clientY) {
    //IE 6
    if (document.documentElement && document.documentElement.scrollTop) {
      posy = e.clientY + document.documentElement.scrollTop - posYObject ;
    } else {
      posy = e.clientY + document.body.scrollTop - posYObject ;
    }
  }
  return  posy ;
} ;

/* 
 * Set Position for a Component in a container
 */
Browser.prototype.setPositionInContainer = function(container, component, posX, posY) {
	var offsetX = component.offsetLeft ;
	var offsetY = component.offsetTop ;

	var posXInContainer = eXo.core.Browser.findPosXInContainer(component, container) ;
	var posYInContainer = eXo.core.Browser.findPosYInContainer(component, container) ;

	var deltaX = posX - (posXInContainer - offsetX) ;
	var deltaY = posY - (posYInContainer - offsetY) ;

	component.style.left = deltaX + "px" ;
	component.style.top = deltaY + "px" ;
} ;

/************************************************************************************/
eXo.core.Browser = new Browser() ;
eXo.core.Mouse = new MouseObject() ;
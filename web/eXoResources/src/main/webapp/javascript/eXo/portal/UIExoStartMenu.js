/**
 * Manages the Start Menu in the workspace area
 * Inherits from UIPopupMenu (superClass attribute)
 */
function UIExoStartMenu() {
  this.buttonClicked = false ;
  this.clipTop = 1 ;
	this.clipBottom = 1 ;
	this.stepScroll = 5 ;
	
  this.itemStyleClass = "MenuItem" ;
  this.itemOverStyleClass = "MenuItemOver" ;
  this.containerStyleClass = "MenuItemContainer" ;
} ;

/**
 * Init function called when the page loads
 * After the configuration of the parameters, call the buildMenu function that
 * adds the javascript events to the buttons in the menu
 */
UIExoStartMenu.prototype.init = function(popupMenu, container, x, y) {
  var uiStart = eXo.portal.UIExoStartMenu ;
  
  this.superClass = eXo.webui.UIPopupMenu ;
  this.superClass.init(popupMenu, container.id, x, y) ;
  
  this.exoStartButton = eXo.core.DOMUtil.findFirstDescendantByClass(container, "div", "ExoStartButton") ;
  this.exoStartButton.onmouseover = function() {
  	if(!eXo.portal.UIExoStartMenu.buttonClicked) this.className = "ExoStartButton ButtonOver" ;
  };
  this.exoStartButton.onmouseout = function() {
  	if(!eXo.portal.UIExoStartMenu.buttonClicked) this.className = "ExoStartButton ButtonNormal" ;
	};
  
  this.buildMenu(popupMenu) ;
} ;
/**
 * Configure some parameters on load, and inits the Start Menu
 */
UIExoStartMenu.prototype.onLoad = function() {
  var uiStartContainer = document.getElementById("StartMenuContainer") ;
  var uiExoStart = document.getElementById("UIExoStart") ;
  eXo.portal.UIExoStartMenu.init(uiStartContainer, uiExoStart, 0, 0) ;
  eXo.webui.UIPopupMenu.hide(uiStartContainer) ;
  eXo.core.ExoDateTime.getTime() ;
};
/**
 * Browse the different buttons in the menu, and adds
 *  . the onMouseOver listener
 *  . the onMouseOut listener
 *  . an onClick event for buttons that contain a link
 *  . an id
 */
UIExoStartMenu.prototype.buildMenu = function(popupMenu) {
	if(typeof(popupMenu) == "string") popupMenu = document.getElementById(popupMenu) ;
	
  var blockMenuItems = eXo.core.DOMUtil.findDescendantsByClass(popupMenu, "div", this.containerStyleClass) ;
  for (var i = 0; i < blockMenuItems.length; i++) {
    if (!blockMenuItems[i].id) blockMenuItems[i].id = Math.random().toString() ;
		blockMenuItems[i].resized = false ;
  }
	
  var menuItems = eXo.core.DOMUtil.findDescendantsByClass(popupMenu, "div", this.itemStyleClass) ;
  for(var i = 0; i < menuItems.length; i++) {
		var menuItemContainer = eXo.core.DOMUtil.findFirstChildByClass(menuItems[i], "div", "MenuItemContainer") ;
		if (menuItemContainer) menuItems[i].menuItemContainer = menuItemContainer ;
		
		menuItems[i].onmouseover = this.onMenuItemOver ; 
		menuItems[i].onmouseout = this.onMenuItemOut ;

    var labelItem = eXo.core.DOMUtil.findFirstDescendantByClass(menuItems[i], "div", "LabelItem") ;
    var link = eXo.core.DOMUtil.findDescendantsByTagName(labelItem, "a")[0] ;
    this.superClass.createLink(menuItems[i], link) ;
  }
};

/**
 * Called when the user points at a button
 * If this button has a submenu, adds it to the currentVisibleContainers array of UIPopupMenu
 * See UIPopupMenu for more details about how the elements are shown
 */
UIExoStartMenu.prototype.onMenuItemOver = function(event) {
	this.className = eXo.portal.UIExoStartMenu.itemOverStyleClass ;
	this.style.position = "relative" ;
	if (this.menuItemContainer) {

		var menuItemContainer = this.menuItemContainer ;
		menuItemContainer.style.display = "block" ;
		// fix width for menuContainer, only IE.
		if (!menuItemContainer.resized) eXo.portal.UIExoStartMenu.setContainerSize(menuItemContainer);
		
	 	var blockMenu = eXo.core.DOMUtil.findFirstDescendantByClass(menuItemContainer, "div", "BlockMenu") ;
		var parentMenu = blockMenu.parentNode;
		var topElement = eXo.core.DOMUtil.findFirstChildByClass(parentMenu, "div", "TopNavigator") ;
	 	var bottomElement = eXo.core.DOMUtil.findFirstChildByClass(parentMenu, "div", "BottomNavigator") ;

		var menuContainer = eXo.core.DOMUtil.findFirstDescendantByClass(blockMenu, "div", "MenuContainer") ;
		if (!menuContainer.id) menuContainer.id = "eXo" + new Date().getTime() + Math.random().toString().substring(2) ;
		
		
		var x = this.offsetWidth + this.offsetLeft ;
	  var rootX = eXo.core.Browser.findPosX(this) ;
		if (x + menuItemContainer.offsetWidth + rootX > eXo.core.Browser.getBrowserWidth()) {
	    	x -= (menuItemContainer.offsetWidth + this.offsetWidth) ;
	  }
	  if (eXo.core.Browser.getBrowserType() == "ie") x -= 10;
	 	menuItemContainer.style.left = x + "px" ;
		
		var browserHeight = eXo.core.Browser.getBrowserHeight() ;
		if (menuContainer.offsetHeight + 64 > browserHeight) {
				var curentHeight = browserHeight - 64;
				blockMenu.style.height = curentHeight + "px" ;
				topElement.style.display = "block" ;
				bottomElement.style.display = "block" ;

				if(!menuContainer.curentHeight || (menuContainer.curentHeight != curentHeight)) {
					eXo.portal.UIExoStartMenu.initSlide(menuContainer, curentHeight) ;
				}
				topElement.onmousedown = function() {
					eXo.portal.UIExoStartMenu.scrollDown(menuContainer.id, curentHeight) ;
				};
				topElement.onmouseoup = function() {
					if (menuContainer.repeat) {
						clearTimeout(menuContainer.repeat) ;
						menuContainer.repeat = null ;
					}
				};
				topElement.onclick = function(event) {
					clearTimeout(menuContainer.repeat) ;
					menuContainer.repeat = null ;
					event = event || window.event ;
					event.cancelBubble = true ;
				};
				
				bottomElement.onmousedown = function() {
					eXo.portal.UIExoStartMenu.scrollUp(menuContainer.id, curentHeight) ;
				};
				bottomElement.onmouseoup = function() {
					if (menuContainer.repeat) {
						clearTimeout(menuContainer.repeat) ;
						menuContainer.repeat = null ;
					}
				};			
				bottomElement.onclick = function(event) {
					clearTimeout(menuContainer.repeat) ;
					menuContainer.repeat = null ;
					event = event || window.event ;
					event.cancelBubble = true ;
				};
	  } else {
			blockMenu.style.height = menuContainer.offsetHeight + "px" ;
			menuContainer.style.clip = "rect(0px 1280px auto auto)" ;
			menuContainer.curentHeight = null;
			menuContainer.style.position = "static";
			topElement.style.display = "none" ;
			bottomElement.style.display = "none" ;
	  }
		var Y = eXo.portal.UIExoStartMenu.getDimension(this) ;
		if (Y != undefined)	menuItemContainer.style.top = Y + "px" ;

    eXo.portal.UIExoStartMenu.superClass.pushVisibleContainer(this.menuItemContainer.id) ;
	}
};

/**
 * Called when the user leaves a button
 * If this button has a submenu, adds it to the elementsToHide array of UIPopupMenu, 
 * ad removes it from the currentVisibleContainers array.
 * See UIPopupMenu for more details about how the elements are hidden
 */
UIExoStartMenu.prototype.onMenuItemOut = function(event) {
	this.className = eXo.portal.UIExoStartMenu.itemStyleClass ;
	if (this.menuItemContainer) {
    eXo.portal.UIExoStartMenu.superClass.pushHiddenContainer(this.menuItemContainer.id) ;
    eXo.portal.UIExoStartMenu.superClass.popVisibleContainer() ;
    eXo.portal.UIExoStartMenu.superClass.setCloseTimeout() ;
	}
};

/**
 * Shows the start menu
 */
UIExoStartMenu.prototype.showStartMenu = function(event) {
  event = event || window.event ;
  event.cancelBubble = true ;

  var uiStartContainer = document.getElementById("StartMenuContainer") ;
  eXo.portal.UIExoStartMenu.exoStartButton.className = "ExoStartButton ButtonClicked" ;
  if(uiStartContainer.style.display == "block") {
    eXo.portal.UIExoStartMenu.hideUIStartMenu() ;
  } else {
    eXo.portal.UIExoStartMenu.buttonClicked = true ;
    var menuY = eXo.core.Browser.findPosY(eXo.portal.UIExoStartMenu.exoStartButton) ;
    this.superClass.show(uiStartContainer) ;		
		var y = menuY - uiStartContainer.offsetHeight ;
		
    if(window.pageYOffset) y -= window.pageYOffset ;
    else if (document.documentElement.scrollTop) y -= document.documentElement.scrollTop ;
    else if (document.body.scrollTop) y -= document.body.scrollTop ;
		this.superClass.setPosition(uiStartContainer, 0, y) ;
		
    uiStartContainer.style.width = "238px" ;
    uiStartContainer.style.height = uiStartContainer.offsetHeight + "px" ;
  }
  /*Hide eXoStartMenu whenever click on the UIApplication*/
  var uiPortalApplication = document.getElementById("UIPortalApplication") ;
  uiPortalApplication.onclick = eXo.portal.UIExoStartMenu.hideUIStartMenu ;
};
/**
 * Hides the start menu when the user clicks anywhere on the page
 */
UIExoStartMenu.prototype.hideUIStartMenu = function() {
  var uiStartContainer = document.getElementById("StartMenuContainer") ;
  eXo.webui.UIPopupMenu.hide(uiStartContainer) ;
  eXo.portal.UIExoStartMenu.buttonClicked = false ;
  eXo.portal.UIExoStartMenu.exoStartButton.className = "ExoStartButton ButtonNormal" ;
  eXo.portal.UIExoStartMenu.clearStartMenu() ;
};

UIExoStartMenu.prototype.clearStartMenu = function() {
  eXo.webui.UIPopupMenu.currentVisibleContainers.clear() ;
  eXo.webui.UIPopupMenu.setCloseTimeout() ;
};

UIExoStartMenu.prototype.getDimension = function(menuItem) {
	if(document.documentElement.scrollTop)  var topPage = document.documentElement.scrollTop ;
	else if(document.body) var topPage = document.body.scrollTop ;
	var PosY = eXo.core.Browser.findPosY(menuItem) - topPage ;
	var browserHeight = eXo.core.Browser.getBrowserHeight() ;
	var menuItemContainer = menuItem.menuItemContainer ;
	var offsetHeight = menuItemContainer.offsetHeight ;
	var deltaDown = browserHeight - PosY ;
	if(offsetHeight < deltaDown )	var  y = 0;
	else if(offsetHeight < PosY ) var  y = (- offsetHeight + menuItem.offsetHeight) ;
	else if(offsetHeight < browserHeight) var y = (- offsetHeight + deltaDown - 6) ;
	return y;
};

UIExoStartMenu.prototype.initSlide = function(menuContainer, clipBottom) {
	menuContainer.curentHeight = clipBottom ;
	menuContainer.style.position = "absolute" ;
	menuContainer.style.top = 0 + "px" ;
	menuContainer.style.clip = 'rect(0px, 1280px,' + clipBottom + 'px, 0px)' ;
};

UIExoStartMenu.prototype.scrollUp = function(id, height) {
	var scrollObject = document.getElementById(id) ;
	var menuHeight = scrollObject.offsetHeight - height - this.stepScroll ;
	if (eXo.portal.UIExoStartMenu.clipBottom < menuHeight) {
		eXo.portal.UIExoStartMenu.clipTop += this.stepScroll ;
		eXo.portal.UIExoStartMenu.clipBottom += this.stepScroll ;
		var clipTop = eXo.portal.UIExoStartMenu.clipTop;
		var	clipBottom = eXo.portal.UIExoStartMenu.clipBottom + height ;

		scrollObject.style.clip = 'rect(' + clipTop + 'px, 1280px,' + clipBottom + 'px, 0px)' ;		
		scrollObject.style.top = -clipTop + "px" ;
		if (scrollObject.repeat) {
			clearTimeout(scrollObject.repeat) ;
			scrollObject.repeat = null ;
		}
		scrollObject.repeat = setTimeout("eXo.portal.UIExoStartMenu.scrollUp('" + id + "', " + height + ")", 1)	;
	}	
};

UIExoStartMenu.prototype.scrollDown = function(id, height) {
	var scrollObject = document.getElementById(id) ;
	if (eXo.portal.UIExoStartMenu.clipTop > this.stepScroll) {
		eXo.portal.UIExoStartMenu.clipTop -= this.stepScroll ;
		eXo.portal.UIExoStartMenu.clipBottom -= this.stepScroll ;
		var clipTop = eXo.portal.UIExoStartMenu.clipTop ;
		var	clipBottom = eXo.portal.UIExoStartMenu.clipBottom + height ;

		scrollObject.style.clip = 'rect(' + clipTop + 'px, 1280px,' + clipBottom + 'px, 0px)' ;		
		scrollObject.style.top = -clipTop + "px" ;
		if (scrollObject.repeat) {
			clearTimeout(scrollObject.repeat) ;
			scrollObject.repeat = null ;
		}
		scrollObject.repeat = setTimeout("eXo.portal.UIExoStartMenu.scrollDown('" + id + "', " + height + ")", 1)	;
	}
};
	
/**
 * Called only once for each submenu (thanks to the boolean resized)
 * Sets the width of the decorator parts to the width of the content part.
 */
UIExoStartMenu.prototype.setContainerSize = function(menuItemContainer) {

  var menuCenter = eXo.core.DOMUtil.findFirstDescendantByClass(menuItemContainer, "div", "StartMenuML") ;
  var menuTop = eXo.core.DOMUtil.findFirstDescendantByClass(menuItemContainer, "div", "StartMenuTL") ;
  var decorator = eXo.core.DOMUtil.findFirstDescendantByClass(menuTop, "div", "StartMenuTR") ;
  var menuBottom = menuTop.nextSibling ;
  while (menuBottom.className != "StartMenuBL") menuBottom = menuBottom.nextSibling ;
  var w = menuCenter.offsetWidth - decorator.offsetLeft ;
  menuTop.style.width = w + "px" ;
  menuBottom.style.width = w + "px" ;
  menuCenter.style.width = w + "px" ;
  menuItemContainer.resized = true ;
  
  if (eXo.core.Browser.isIE7()) {
	  var pageOwnerContainer = eXo.core.DOMUtil.findDescendantsByClass(menuItemContainer, "div", "PageOwnerContainer") ;
	  for (var i = 0; i < pageOwnerContainer.length; i ++) {
	  	pageOwnerContainer[i].style.width = pageOwnerContainer[i].offsetWidth + "px";
	  }
  }
};

eXo.portal.UIExoStartMenu = new UIExoStartMenu() ;
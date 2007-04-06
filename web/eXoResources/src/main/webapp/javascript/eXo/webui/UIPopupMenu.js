eXo.require('eXo.webui.UIPopup');

function UIPopupMenu() {
	// Used when timeout is finished and submenus must be hidden
	this.elementsToHide = new Array();
	this.currentVisibleContainers = new Array();
} ;

UIPopupMenu.prototype.init = function(popupMenu, container, x, y) {
	this.superClass = eXo.webui.UIPopup;
	this.superClass.init(popupMenu, container.id) ;
	this.superClass.setPosition(popupMenu, x, y) ;
		
	var menuItems = eXo.core.DOMUtil.findDescendantsByClass(popupMenu, "div", "MenuItemL") ;
	var menuContainer = eXo.core.DOMUtil.findFirstDescendantByClass(popupMenu, "div", "MenuItemContainer") ;
	// Store the menu containers width to avoid some bugs
	var menuItemWidths = new Array();
	for(var i = 0; i<menuItems.length; i++) {
		menuItems[i].onmouseover = eXo.webui.UIPopupMenu.onMenuItemOver ;
		menuItems[i].onmouseout = eXo.webui.UIPopupMenu.onMenuItemOut ;
		menuItems[i].onclick = eXo.webui.UIPopupMenu.onClick;
		// Set width of each menu item depending on its container width
		var cont = eXo.core.DOMUtil.findAncestorByClass(menuItems[i], "MenuItemContainer") ;
		if (!cont.id) cont.id = "cont-"+i;
		if (!menuItemWidths[cont.id])
			menuItemWidths[cont.id] = cont.offsetWidth;
		menuItems[i].style.width = menuItemWidths[cont.id] + "px";
		// Set the right arrow icon if necessary
		var childContainer = eXo.core.DOMUtil.findFirstDescendantByClass(menuItems[i], "div", "MenuItemContainer") ;
		if (childContainer) {
			var itemRight = eXo.core.DOMUtil.findFirstDescendantByClass(menuItems[i], "div", "MenuItemR") ;
			itemRight.className += " HasSubMenu";
		}
	}
} ;

UIPopupMenu.prototype.onClick = function(e) {
	var targ = eXo.core.DOMUtil.getEventSource(e);
	window.status = targ.innerHTML;
} ;


UIPopupMenu.prototype.onMenuItemOver = function(e) {
	var menuItem = this ;
	menuItem.className = "MenuItemL OnMouseOver" ;
	
	var menuItemContainer = eXo.core.DOMUtil.findFirstDescendantByClass(menuItem, "div", "MenuItemContainer") ;
	// If the pointed menu item has a submenu
	if(menuItemContainer) {
		menuItem.style.position = "relative" ;
		// Shows the submenu
		eXo.webui.UIPopupMenu.showMenuItemContainer(menuItem, menuItemContainer) ;
		/* Adds the submenu id to the list of visible submenus
		 * I use an array for "sub submenus" (not only one submenu to show)
		 */
		eXo.webui.UIPopupMenu.currentVisibleContainers.push(menuItemContainer.id);
	} else {
		//eXo.webui.UIPopupMenu.currentVisibleContainers.clear();
	}
} ;

UIPopupMenu.prototype.onMenuItemOut = function(e) {
	var menuItem = this ;
	menuItem.className = "MenuItemL" ;
	// If the menu we just left has (had) a submenu
	var container = eXo.core.DOMUtil.findFirstDescendantByClass(menuItem, "div", "MenuItemContainer") ;
	if(container) {
		eXo.webui.UIPopupMenu.menuItemcontainer = container ;
		/* Adds the submenu to the list of submenus to hide
		 * This list will be parsed when timeout is finished
		 */
		eXo.webui.UIPopupMenu.elementsToHide.push(container.id);
		/* Removes the last submenu that was visible from the visible submenus list
		 * The last submenu that was visible is the one we just left
		 */
		eXo.webui.UIPopupMenu.currentVisibleContainers.pop();
		// Sets the timeout and the callback function
		setTimeout("eXo.webui.UIPopupMenu.doOnMenuItemOut()", 300) ;
	}
} ;

/* The callback function called when timeout is finished
 * Hides the submenus that are no longer pointed
 */
UIPopupMenu.prototype.doOnMenuItemOut = function() {
	while (eXo.webui.UIPopupMenu.elementsToHide.length > 0) {
		var container = document.getElementById(eXo.webui.UIPopupMenu.elementsToHide.shift());
		/* It can happen that a submenu appears in both the "to-hide" list and the "keep-visible" list
		 * This happens because when the mouse moves from the border of an item to the content of this item,
		 * a mouseOut Event is fired and the item submenu is added to the "to-hide" list while it remains in the 
		 * "keep-visible" list.
		 * Here, we check that the item submenu doesn't appear in the "keep-visible" list before we hide it
		 */
		if (!eXo.webui.UIPopupMenu.currentVisibleContainers.contains(container.id))
			container.style.visibility = "hidden" ;
	}
} ;

UIPopupMenu.prototype.showMenuItemContainer = function(menuItem, menuItemContainer) {
	this.superClass.setPosition(menuItemContainer, menuItem.offsetWidth, 0) ;
	menuItemContainer.style.visibility = "visible" ;
} ;

UIPopupMenu.prototype.hide = function(object) {
	if(typeof(object) == "string") object = document.getElementById(object);
	object.style.display = "none" ;
	
} ;

UIPopupMenu.prototype.show = function(object) {
	if(typeof(object) == "string") object = document.getElementById(object);
	object.style.display = "block" ;
	
} ;

eXo.webui.UIPopupMenu = new UIPopupMenu() ;
eXo.require('eXo.webui.UIVerticalScroller') ;
function UIWorkspace(id) {
  this.id = id ;
  this.showControlWorkspace = false ;
  this.isFirstTime = true ;
};

if(eXo.portal.Workspace == undefined) {
  eXo.portal.Workspace = new UIWorkspace("UIWorkspace") ;
};
if(eXo.portal.UIControlWorkspace == undefined) {
  eXo.portal.UIControlWorkspace = new UIWorkspace("UIControlWorkspace") ;
};

eXo.portal.UIControlWorkspace.onResize = function(width, height) {
	this.width = width ;
	this.height = height ;
	var uiWorkspace = document.getElementById(this.id) ;
	var uiWorkspaceContainer = document.getElementById("UIWorkspaceContainer") ;
	this.uiWorkspaceControl = document.getElementById("UIWorkspaceControl") ;
	var uiWorkspacePanel = document.getElementById("UIWorkspacePanel") ;
	
	uiWorkspace.style.width = width + "px" ;
	uiWorkspace.style.height = height + "px" ;
		
	/*	In case uiWorkspaceContainer is setted display to none, uiWorkspaceControl.offsetHeight equal 0 
	 * 	23 is the height of User Workspace Title.
	 * */
	
	if(eXo.portal.UIControlWorkspace.showControlWorkspace == true) {
		uiWorkspaceContainer.style.display = "block" ;
		uiWorkspaceContainer.style.width = eXo.portal.UIControlWorkspace.defaultWidth + "px" ;
	}
	var startMenuContainer = document.getElementById("StartMenuContainer");
	startMenuContainer.style.display = "none";
	uiWorkspacePanel.style.height = (height - this.uiWorkspaceControl.offsetHeight - 23) + "px" ;
	
	/*Fix Bug on IE*/
	eXo.portal.UIControlWorkspace.slidebar.style.height = height + "px" ;
	uiWorkspace.style.top = document.documentElement.scrollTop + "px" ;
} ;

eXo.portal.UIControlWorkspace.onResizeDefault = function() {
	var cws = eXo.portal.UIControlWorkspace ;
	cws.defaultWidth = 250 ;
	cws.slidebarDefaultWidth = 6;
	cws.slidebar = document.getElementById("ControlWorkspaceSlidebar") ;
	
	if(this.isFirstTime) {
		cws.originalSlidebarWidth = cws.slidebar.offsetWidth ;
	}
	
	if(cws.showControlWorkspace) {
		cws.slidebar.style.display = "none" ;
		cws.onResize(cws.defaultWidth, eXo.core.Browser.getBrowserHeight()) ;
	} else {
		cws.slidebar.style.display = "block" ;
		cws.onResize(cws.slidebar.offsetWidth, eXo.core.Browser.getBrowserHeight()) ;
	}
};

eXo.portal.UIControlWorkspace.setVisible = function(visible) {
	var cws = eXo.portal.UIControlWorkspace ;
	if(cws.isFirstTime == true && visible && cws.showControlWorkspace == false) {
		cws.isFirstTime = false ;
		cws.showWorkspace();
	}
}
   	
eXo.portal.UIControlWorkspace.showWorkspace = function() {	
	var cws = eXo.portal.UIControlWorkspace ;
	var uiWorkspace = document.getElementById(this.id) ;
	var uiWorkspaceContainer = document.getElementById("UIWorkspaceContainer") ;
	var uiWorkspacePanel = document.getElementById("UIWorkspacePanel") ;
	var slidebar = document.getElementById("ControlWorkspaceSlidebar") ;

	if(cws.showControlWorkspace == false) {
		// shows the workspace
		cws.showControlWorkspace = true ;
		slidebar.style.display = "none" ;
		eXo.portal.UIControlWorkspace.width = cws.defaultWidth;
		uiWorkspace.style.width = cws.defaultWidth + "px" ;
		eXo.portal.UIWorkingWorkspace.onResize(null, null) ;
		uiWorkspaceContainer.style.display = "block" ;
		uiWorkspaceContainer.style.width = cws.defaultWidth + "px" ;
		uiWorkspacePanel.style.height = (eXo.portal.UIControlWorkspace.height - 
																		 eXo.portal.UIControlWorkspace.uiWorkspaceControl.offsetHeight - 23) + "px" ;
		/*23 is height of User Workspace Title*/
		eXo.portal.UIPortalControl.fixHeight();
	} else {
		// hides the workspace
		cws.showControlWorkspace = false ;
		uiWorkspaceContainer.style.display = "none" ;
		slidebar.style.display = "block" ;
		eXo.portal.UIControlWorkspace.width = eXo.portal.UIControlWorkspace.slidebar.offsetWidth ;
		uiWorkspace.style.width = slidebar.offsetWidth + "px";
		eXo.portal.UIWorkingWorkspace.onResize(null, null) ;
	}
	
	/* Reorganize opened windows */
	eXo.portal.UIWorkingWorkspace.reorganizeWindows(this.showControlWorkspace);
	/*Resize Dockbar*/
	var uiPageDesktop = document.getElementById("UIPageDesktop") ;
	if(uiPageDesktop != null) eXo.desktop.UIDockbar.resizeDockBar() ;
	/* Resizes the scrollable containers */
	eXo.portal.UIPortalControl.initAllManagers();
	
	if(document.getElementById("UIWidgets")) {
		eXo.widget.UIWidget.resizeContainer();
		eXo.webui.UIVerticalScroller.init();
	}
	/* BEGIN - Check positon of widgets in order to avoid hide widgets when we expand/collapse workspace*/
	if(uiPageDesktop = document.getElementById("UIPageDesktop")) {
		var DOMUtil = eXo.core.DOMUtil ;
		var uiWidget = DOMUtil.findChildrenByClass(uiPageDesktop, "div", "UIWidget") ;
		var uiControlWorkspace = document.getElementById("UIControlWorkspace") ;
		var size = uiWidget.length ;
		var limitX = 50 ;
			
		for(var i = 0 ; i < size ; i ++) {
			var dragObject = uiWidget[i] ;
			if (cws.showControlWorkspace == true) {
				dragObject.style.left = (dragObject.offsetLeft - uiControlWorkspace.offsetWidth) + "px";				
			}
			else {				
				dragObject.style.left = (dragObject.offsetLeft + uiControlWorkspace.offsetWidth + dragObject.offsetWidth) + "px";				
			}
			var offsetHeight = uiPageDesktop.offsetHeight - dragObject.offsetHeight  - limitX;
	  	var offsetTop = dragObject.offsetTop ;
	  	var offsetWidth = uiPageDesktop.offsetWidth - dragObject.offsetWidth - limitX ;
	  	var offsetLeft = dragObject.offsetLeft ;
	  	
	  	if (dragObject.offsetLeft < 0) dragObject.style.left = "0px" ;
	  	if (dragObject.offsetTop < 0) dragObject.style.top = "0px" ;
	  	if (offsetTop > offsetHeight) dragObject.style.top = (offsetHeight + limitX) + "px" ;
	  	if (offsetLeft > offsetWidth) dragObject.style.left = (offsetWidth + limitX) + "px" ;				
		}		
	}
	/* -- END -- */
	var params = [ {name: "objectId", value : cws.showControlWorkspace} ] ;
	ajaxAsyncGetRequest(eXo.env.server.createPortalURL(this.id, "SetVisible", true, params), false) ;
};

/*#############################-Working Workspace-##############################*/
if(eXo.portal.UIWorkingWorkspace == undefined) {
  eXo.portal.UIWorkingWorkspace = new UIWorkspace("UIWorkingWorkspace") ;
}

eXo.portal.UIWorkingWorkspace.onResize = function(width, height) {
	var uiWorkspace = document.getElementById(this.id) ;
	var uiControlWorkspace = document.getElementById("UIControlWorkspace") ;
  var controlWorkspaceWidth = eXo.portal.UIControlWorkspace.width ;
    
//  if(eXo.core.Browser.isIE6()) {
//  	var slidebar = document.getElementById("ControlWorkspaceSlidebar") ;
//  	if(slidebar) {
//  		uiWorkspace.style.width = (eXo.core.Browser.getBrowserWidth() - controlWorkspaceWidth - slidebar.offsetWidth) + "px";
//  	}
//  }

	if(eXo.core.Browser.isIE6()) {
		var tabs = eXo.core.DOMUtil.findFirstDescendantByClass(uiWorkspace, "div", "UIHorizontalTabs") ;
		tabs.style.left = 0;
	}

  if(uiControlWorkspace) {
  	uiWorkspace.style.marginLeft = controlWorkspaceWidth + "px" ;
  } else {
  	uiWorkspace.style.marginLeft = "0px" ;
  }
  //var uiApplication = document.getElementById("UIPortalApplication");
};

eXo.portal.UIWorkingWorkspace.onResizeDefault = function(event) {
  eXo.portal.UIWorkingWorkspace.onResize(null, null) ;
};

eXo.portal.UIWorkingWorkspace.resizeWorkspacePanel = function(h) {
  var workspacePanel = document.getElementById("UIWorkspacePanel");
  workspacePanel.style.height = h + "px";
};

eXo.portal.UIWorkingWorkspace.reorganizeWindows = function(showControlWorkspace) {
	var uiDesk = document.getElementById("UIPageDesktop");
	if (uiDesk != null) {
		var uiCtrl = document.getElementById("UIControlWorkspace");
		var uiWindows = eXo.core.DOMUtil.findDescendantsByClass(uiDesk, "div", "UIWindow");
		for (var k = 0; k < uiWindows.length; k++) {
			if (uiWindows[k].style.display != "none") {
				// We reorganize the opened windows (display != none) only
				var uiWindow = uiWindows[k];
				if (showControlWorkspace) {
					// When the ControlWorkspace is shown
					uiWindow.oldW = uiWindow.offsetWidth;
					if ((uiWindow.offsetLeft + uiWindow.offsetWidth) > uiDesk.offsetWidth) {
						/*
						 * If the window is too large to fit in the screen after the control panel is shown
						 * we remove the control panel width to the window width
						 */ 
						uiWindow.style.width = 
						(uiWindow.offsetWidth - eXo.portal.UIControlWorkspace.defaultWidth + eXo.portal.UIControlWorkspace.slidebarDefaultWidth) + "px";
						
						if ((eXo.desktop.UIWindow.originalWidth + uiWindow.offsetLeft) > uiDesk.offsetWidth && uiWindow.maximized) {
							/* if the maximized window original size is too large
							 * we remove the control panel width to the original width
							 * when the window is demaximized but the control panel is still there, the window
							 * will not come out of the screen
							 */
							eXo.desktop.UIWindow.originalWidth -= eXo.portal.UIControlWorkspace.defaultWidth;
						}
					}
				} else {
					// When the ControlWorkspace is hidden
					if (uiWindow.maximized) {
						// If the window is maximized, we set the size to its maximum : the desktop size
						uiWindow.style.width = uiDesk.offsetWidth+"px";
					} else {
						uiWindow.style.width = uiWindow.oldW+"px";
					}
				}
			}
		}
	} else {return;}
};	
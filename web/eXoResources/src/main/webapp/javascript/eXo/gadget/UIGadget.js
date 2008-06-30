eXo.gadget.UIGadget = {
	createGadget : function(url,id, metadata, userPref) {
		//eXo = eXo || {};
	  window.gadgets = window.gadgets || {};
	  eXo.gadgets = window.gadgets;
		//window.gadgets = eXo.gadget.Gadgets;
	
		if (!eXo.gadgets || !eXo.gadgets.rpc) {
	    	eXo.core.Using.register('rpc','/eXoGadgetServer/gadgets/js/rpc.js?c=1&debug=1&p=1');
	    	eXo.core.Using.register('eXo.gadgets.Gadgets','/eXoGadgets/javascript/eXo/gadgets/Gadgets.js');
	    	eXo.core.Using.register('eXo.gadgets.CookieBasedUserPrefStore','/eXoGadgets/javascript/eXo/gadgets/CookieBasedUserPrefStore.js');
	
	    	eXo.core.Using("rpc");
			  eXo.core.Using("eXo.gadgets.Gadgets");
	  		eXo.core.Using("eXo.gadgets.CookieBasedUserPrefStore");
		}
		var gadget;
		if (metadata != null) {
	  	gadget = gadgets.container.createGadget({specUrl: url,height: metadata.gadgets[0].height});
    	gadget.metadata = metadata.gadgets[0];
		} else {
			gadget = gadgets.container.createGadget({specUrl: url});
		}	
    gadgets.container.addGadget(gadget);
    if(userPref != null) gadget.setUserPrefs(userPref) ;
	  var gadgetBlock = document.getElementById(id);
		gadgetBlock.innerHTML = "<div id='gadget_" + gadget.id + "' class='UIGadgetContent'> </div>";
		gadgets.container.renderGadgets();
		var uiGadget = eXo.core.DOMUtil.findAncestorByClass(gadgetBlock, "UIGadget");
		//TODO: dang.tung - isn't portlet
		if(uiGadget != null) {
			var isDesktop = false;
			if(uiGadget.parentNode.className=="UIPageDesktop") {
				uiGadget.style.position = "absolute" ;
				isDesktop = true;
			}
			else uiGadget.style.width="auto" ;	
			eXo.gadget.UIGadget.init(uiGadget, isDesktop);	
		}
		
	},

	init : function(uiGadget, inDesktop) {
		var uiWindow = eXo.core.DOMUtil.findAncestorByClass(uiGadget, "UIWindow");
		if(uiWindow == null){
			uiGadget.onmouseover = eXo.gadget.UIGadget.showGadgetControl ;
			uiGadget.onmouseout = eXo.gadget.UIGadget.hideGadgetControl ;
		} else{
			var gadgetControl = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetControl");
			gadgetControl.style.display = "block" ;
			eXo.core.DOMUtil.findFirstDescendantByClass(gadgetControl, "div", "GadgetTitle").style.display = "block" ;
		}
		
		if(inDesktop) {
			var dragHandleArea = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetDragHandleArea");
			
			if(uiGadget.style.zIndex<0) uiGadget.style.zIndex = 0;
			eXo.core.DragDrop2.init(dragHandleArea, uiGadget);
			
			var uiPageDesktop =document.getElementById("UIPageDesktop");
			var offsetHeight = uiPageDesktop.offsetHeight - uiGadget.offsetHeight ;
			var offsetWidth = uiPageDesktop.offsetWidth - uiGadget.offsetWidth ;
			var dragPosX = uiGadget.offsetLeft;
			var dragPosY = uiGadget.offsetTop;
			
			if (dragPosX < 0) uiGadget.style.left = "0px" ;
	  	if (dragPosY < 0) uiGadget.style.top = "0px" ;
	  	if (dragPosY > offsetHeight) uiGadget.style.top = offsetHeight  + "px" ;
	  	if (dragPosX > offsetWidth) uiGadget.style.left = offsetWidth + "px" ;
		
			// drag start callback
			uiGadget.onDragStart = function(x, y, lastMouseX, lastMouseY, e){
				var uiPageDesktop =document.getElementById("UIPageDesktop");
				if(uiPageDesktop==null) return;
				var uiGadgets = eXo.core.DOMUtil.findChildrenByClass(uiPageDesktop, "div", "UIGadget");
				for(var i=0; i<uiGadgets.length; i++){
					var uiMask = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "UIMask");
					if(uiMask!=null){
						var gadgetContent = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "gadgets-gadget-content");
						uiMask.style.marginTop = - gadgetContent.offsetHeight + "px";
						uiMask.style.height = gadgetContent.offsetHeight + "px";
						uiMask.style.width = gadgetContent.offsetWidth + "px";
						uiMask.style.backgroundColor = "white";
						eXo.core.Browser.setOpacity(uiMask, 3);
						uiMask.style.display = "block";
					}
				}
			}
			
			//drag callback
			uiGadget.onDrag = function(nx, ny, ex, ey, e){
				if(nx<0) uiGadget.style.left = "0px";
				if(ny<0) uiGadget.style.top = "0px";
			}
			
			//drop callback		
			uiGadget.onDragEnd = function(x, y, clientX, clientY){
				var uiPageDesktop =document.getElementById("UIPageDesktop");
				var uiGadgets = eXo.core.DOMUtil.findChildrenByClass(uiPageDesktop, "div", "UIGadget");
				for(var i=0; i<uiGadgets.length; i++){
					var uiMask = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "UIMask");
					if(uiMask){
						uiMask.style.display = "none";
					}
				}
				
				var offsetHeight = uiPageDesktop.offsetHeight - uiGadget.offsetHeight ;
				var offsetWidth = uiPageDesktop.offsetWidth - uiGadget.offsetWidth ;
				var dragPosX = uiGadget.offsetLeft;
				var dragPosY = uiGadget.offsetTop;
				
				if (dragPosX < 0) uiGadget.style.left = "0px" ;
		  	if (dragPosY < 0) uiGadget.style.top = "0px" ;
		  	if (dragPosY > offsetHeight) uiGadget.style.top = offsetHeight  + "px" ;
		  	if (dragPosX > offsetWidth) uiGadget.style.left = offsetWidth + "px" ;
		  	
		  	eXo.gadget.UIGadget.saveWindowProperties(uiGadget);
			}
		}
		
	},

	showGadgetControl : function(e) {
		if (!e) e = window.event ;
	  e.cancelBubble = true ;
		var DOMUtil = eXo.core.DOMUtil;
		var uiGadget = this ;
		var gadgetControl = DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetControl");
		gadgetControl.style.visibility = "visible" ;

		var uiPageDesktop = DOMUtil.findAncestorByClass(uiGadget, "UIPageDesktop");
		if(uiPageDesktop) {
			var dragHandleArea = DOMUtil.findFirstDescendantByClass(gadgetControl, "div", "GadgetTitle");
		}
	},


	hideGadgetControl : function(e) {
		if (!e) e = window.event ;
	  e.cancelBubble = true ;
		var uiGadget = this ;
		var gadgetControl = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetControl");
		gadgetControl.style.visibility = "hidden" ;
		uiGadget.style.border = "none";
	},

	editGadget : function(id) {
		var DOMUtil = eXo.core.DOMUtil ;
		var uiapp = document.getElementById(id) ;
		var id = eXo.core.DOMUtil.findFirstDescendantByClass(uiapp,"iframe","gadgets-gadget") ;
		var tempId = id.id.split('_')[2] ;
		gadgets.container.getGadget(tempId).handleOpenUserPrefsDialog();
	},
	
	minimizeGadget: function(selectedElement){
		alert("minimize");
	},

	deleteGadget : function(selectedElement) {
		var DOMUtil = eXo.core.DOMUtil ;
		var uiGadgetContainer = DOMUtil.findAncestorByClass(selectedElement, "UIWidgetContainer") ;
		var uiPage = DOMUtil.findAncestorByClass(selectedElement, "UIPage") ;
		var uiGadget = DOMUtil.findAncestorByClass(selectedElement, "UIGadget") ;
		var containerBlockId ;
		var isInControlWorkspace = false ;
		
		gadgetId = uiGadget.id;
		var portletFragment = DOMUtil.findAncestorById(uiGadget, "PORTLET-FRAGMENT");
		
		if(portletFragment != null){
			var compId = portletFragment.parentNode.id;
			var uicomp = DOMUtil.getChildrenByTagName(portletFragment, "div")[0].className;
			if (confirm("Are you sure you want to delete this gadget ?")) {
				var href = eXo.env.server.portalBaseURL + "?portal:componentId=" + compId;
				href += "&portal:type=action&uicomponent=" + uicomp;
				href += "&op=DeleteGadget";
				href += "&objectId=" + gadgetId + "&ajaxRequest=true";
				ajaxGet(href);
			}
			
		} else {
			if(uiPage) {
				var uiPageIdNode = DOMUtil.findFirstDescendantByClass(uiPage, "div", "id");
				containerBlockId = uiPageIdNode.innerHTML;
			}
			else {
					containerBlockId = uiGadgetContainer.id ;
					isInControlWorkspace = true ;
			}
			if (confirm("Are you sure you want to delete this gadget ?")) {
				var params = [
			  	{name: "objectId", value : gadgetId}
			  ] ;
				var result = ajaxAsyncGetRequest(eXo.env.server.createPortalURL(containerBlockId, "DeleteGadget", true, params), false) ;
				if(result == "OK") {
					DOMUtil.removeElement(uiGadget) ;
					if(isInControlWorkspace) eXo.webui.UIVerticalScroller.refreshScroll(0) ;
				}
			}
		}
	},

	saveWindowProperties : function(object) {
		var DOMUtil = eXo.core.DOMUtil ;
		var uiPage = DOMUtil.findAncestorByClass(object, "UIPage") ;
  	var uiPageIdNode = DOMUtil.findFirstDescendantByClass(uiPage, "div", "id");
		containerBlockId = uiPageIdNode.innerHTML;
		
		var gadgetApp = DOMUtil.findFirstDescendantByClass(object, "div", "GadgetApplication");
		var gadgetId = object.id;
		
  	var params = [
	  	{name: "objectId", value : gadgetId},
	  	{name: "posX", value : object.offsetLeft},
	  	{name: "posY", value : object.offsetTop},
	  	{name: "zIndex", value : object.style.zIndex}
	  ] ;
		
  	ajaxAsyncGetRequest(eXo.env.server.createPortalURL(containerBlockId, "SaveGadgetProperties", true, params), false) ;

	} ,

	resizeContainer : function() {
		var gadgets  = document.getElementById("UIGadgets") ;
		if(gadgets == null) return ;

		var DOMUtil = eXo.core.DOMUtil ;
		var workspacePanel = document.getElementById("UIWorkspacePanel") ;
		if(workspacePanel.style.display == "none") return;
		var uiGadgetContainer = DOMUtil.findFirstDescendantByClass(gadgets, "div", "UIGadgetContainer");
		if(uiGadgetContainer == null) return ;
		var gadgetNavigator = DOMUtil.findFirstChildByClass(uiGadgetContainer, "div", "GadgetNavigator") ;
		var gadgetContainerScrollArea = DOMUtil.findFirstChildByClass(uiGadgetContainer, "div", "GadgetContainerScrollArea") ;
		var itemSelectorContainer = DOMUtil.findFirstChildByClass(gadgets, "div", "ItemSelectorContainer") ;

		var availableHeight = workspacePanel.offsetHeight - (itemSelectorContainer.offsetHeight + gadgetNavigator.offsetHeight + 40) ;
		if(eXo.core.Browser.isIE6() || workspacePanel.offsetHeight < 1) {
			//var html = document.getElementsByTagName("html")[0];
			var uiControlWorkspace = document.getElementById("UIControlWorkspace") ;
			var fixHeight = uiControlWorkspace.offsetHeight - 153;
	    fixHeight = (fixHeight < 0) ? 0 : fixHeight ;
			/* 153 is total value (UserWorkspaceTitleHeight + UIExoStartHeight + GadgetNavigatorHeight + 40)
			 * 40 is distance between UIGadgets and UIExoStart
			 * */
			if(gadgetContainerScrollArea.offsetHeight == fixHeight) return;
			gadgetContainerScrollArea.style.height = fixHeight + "px" ;
		} else {
			if(availableHeight < 0) return ;
			gadgetContainerScrollArea.style.height = availableHeight + "px" ;
		}
	  gadgetContainerScrollArea.style.overflow = "hidden" ;
	}
}
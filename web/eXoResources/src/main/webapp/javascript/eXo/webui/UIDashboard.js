eXo.webui.UIDashboard = {
	
	currCol : null,
	
	targetObj : null,
	
	init : function (dragItem, dragObj) {
		
		eXo.core.DragDrop2.init(dragItem, dragObj)	;

		dragObj.onDragStart = function(x, y, lastMouseX, lastMouseY, e) {
			var DOMUtil = eXo.core.DOMUtil;
			var uiDashboard = eXo.webui.UIDashboard ;
			var portletFragment = DOMUtil.findAncestorByClass(dragObj, "PORTLET-FRAGMENT");
			if(!portletFragment) return;
			
			var uiWorkingWS = document.getElementById("UIWorkingWorkspace");
			var gadgetContainer = DOMUtil.findFirstDescendantByClass(portletFragment, "div", "GadgetContainer");
			var portletApp = DOMUtil.findAncestorByClass(gadgetContainer, "UIApplication");

			var ggwidth = dragObj.offsetWidth;
			var ggheight = dragObj.offsetHeight;
			
			//find position to put drag object in
			var mx = eXo.webui.UIDashboardUtil.findMouseRelativeX(uiWorkingWS, e);
			var ox = eXo.webui.UIDashboardUtil.findMouseRelativeX(dragObj, e);
			var x = mx-ox;
				
			var my = eXo.webui.UIDashboardUtil.findMouseRelativeY(uiWorkingWS, e);
			var oy = eXo.webui.UIDashboardUtil.findMouseRelativeY(dragObj, e);
			var y = my-oy;

			var temp = dragObj;
			while(temp.parentNode && DOMUtil.hasDescendant(portletFragment, temp)) {
				if(temp.scrollLeft>0) 
					x -= temp.scrollLeft;
				if(temp.scrollTop>0)
					y -= temp.scrollTop;
				temp = temp.parentNode;
			}
			
			var slideBar = document.getElementById("ControlWorkspaceSlidebar");
			if(slideBar!=null && slideBar.style.display!="none" && eXo.core.Browser.getBrowserType()=="ie")
				x -= slideBar.offsetWidth;
			
			var uiTarget = null;
			if(!DOMUtil.hasClass(dragObj, "SelectItem")) {
				uiTarget = uiDashboard.createTarget(ggwidth, 0);
				dragObj.parentNode.insertBefore(uiTarget, dragObj.nextSibling);
				uiDashboard.currCol = eXo.webui.UIDashboardUtil.findColIndexInDashboard(dragObj);
			}else{
				var dragCopyObj = dragObj.cloneNode(true);
				DOMUtil.addClass(dragCopyObj, "CopyObj");
				dragObj.parentNode.insertBefore(dragCopyObj, dragObj);
				uiDashboard.targetObj = null;
			}
			dragObj.style.width = ggwidth +"px";

			//increase speed of mouse when over iframe by create div layer above it
			var uiGadgets = DOMUtil.findDescendantsByClass(gadgetContainer, "div", "UIGadget");
			
			for(var i=0; i<uiGadgets.length; i++) {
				var uiMask = DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "UIMask");
				if(uiMask!=null) {
					var gadgetContent = DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "gadgets-gadget-content");
					uiMask.style.marginTop = - gadgetContent.offsetHeight + "px";
					uiMask.style.height = gadgetContent.offsetHeight + "px";
					uiMask.style.width = gadgetContent.offsetWidth + "px";
					uiMask.style.display = "block";
					uiMask.style.backgroundColor = "white";
					eXo.core.Browser.setOpacity(uiMask, 3);
				}
			}
			
			if(!DOMUtil.hasClass(dragObj, "Dragging"))
				DOMUtil.addClass(dragObj, "Dragging");
				
			//set position of drag object
			dragObj.style.position = "absolute";
			eXo.webui.UIDashboardUtil.setPositionInContainer(uiWorkingWS, dragObj, x, y);
			if(uiTarget!=null) {
				uiTarget.style.height = ggheight +"px";
				uiDashboard.targetObj = uiTarget;
			}
		}
		
		
		
		dragObj.onDrag = function(nx, ny, ex, ey, e) {	
			var DOMUtil = eXo.core.DOMUtil;		
			var uiTarget = eXo.webui.UIDashboard.targetObj;
			var portletFragment = DOMUtil.findAncestorByClass(dragObj, "PORTLET-FRAGMENT");

			if(!portletFragment) return;
			
			var dashboardCont = DOMUtil.findFirstDescendantByClass(portletFragment, "div", "GadgetContainer");
			var cols = null;
			
			eXo.webui.UIDashboard.scrollOnDrag(dragObj);
			if(eXo.webui.UIDashboardUtil.isIn(ex, ey, dashboardCont)) {
				if(!uiTarget) {
					uiTarget = eXo.webui.UIDashboard.createTargetOfAnObject(dragObj);
					eXo.webui.UIDashboard.targetObj = uiTarget;
				}
				
				var uiCol = eXo.webui.UIDashboard.currCol ;
				
				if(!uiCol) {
					if(!cols) cols = DOMUtil.findDescendantsByClass(dashboardCont, "div", "UIColumn");
					for(var i=0; i<cols.length; i++) {
						var uiColLeft = eXo.webui.UIDashboardUtil.findPosX(cols[i]) - dashboardCont.scrollLeft;
						if(uiColLeft<ex  &&  ex<uiColLeft+cols[i].offsetWidth) {
							uiCol = cols[i];
							eXo.webui.UIDashboard.currCol = uiCol;
							break;
						}
					}
					
				}
				
				if(!uiCol) return;

				var uiColLeft = eXo.webui.UIDashboardUtil.findPosX(uiCol) - dashboardCont.scrollLeft;
				if(uiColLeft<ex  &&  ex<uiColLeft+uiCol.offsetWidth ) {
					var gadgets = DOMUtil.findDescendantsByClass(uiCol, "div", "UIGadget");
					//remove drag object from dropable target
					for(var i=0; i<gadgets.length; i++) {
						if(dragObj.id==gadgets[i].id) {
							gadgets.splice(i,1);
							break;
						}
					}

					if(gadgets.length == 0) {
						uiCol.appendChild(uiTarget);
						return;
					}

					//find position and add uiTarget into column				
					for(var i=0; i<gadgets.length; i++) {
						var oy = eXo.webui.UIDashboardUtil.findPosY(gadgets[i]) + (gadgets[i].offsetHeight/3) - dashboardCont.scrollTop;
						
						if(ey<=oy) {
							uiCol.insertBefore(uiTarget, gadgets[i]);
							break;
						}
						if(i==gadgets.length-1 && ey>oy) uiCol.appendChild(uiTarget);
					}
					
				}	else {

					//find column which draggin in					
					if(cols == null) cols = DOMUtil.findDescendantsByClass(dashboardCont, "div", "UIColumn");
					for(var i=0; i<cols.length; i++) {
						var uiColLeft = eXo.webui.UIDashboardUtil.findPosX(cols[i]) - dashboardCont.scrollLeft;
						if(uiColLeft<ex  &&  ex<uiColLeft+cols[i].offsetWidth) {
							eXo.webui.UIDashboard.currCol = cols[i];
							break;
						}
					}
				}
			} else {
				//prevent dragging gadget object out of DashboardContainer
				if(uiTarget!=null && DOMUtil.hasClass(dragObj, "SelectItem")) {
					uiTarget.parentNode.removeChild(uiTarget);					
					eXo.webui.UIDashboard.targetObj = uiTarget = null;
				}
			}
		}


	
		dragObj.onDragEnd = function(x, y, clientX, clientY) {
			var uiDashboard = eXo.webui.UIDashboard;
			var uiDashboardUtil = eXo.webui.UIDashboardUtil;
			var portletFragment = eXo.core.DOMUtil.findAncestorByClass(dragObj, "PORTLET-FRAGMENT");
			
			if(!portletFragment) return;
			
			var masks = eXo.core.DOMUtil.findDescendantsByClass(portletFragment, "div", "UIMask");
			for(var i=0; i<masks.length; i++) {
				eXo.core.Browser.setOpacity(masks[i], 100);
				masks[i].style.display = "none";
			}
			
			var uiTarget = uiDashboard.targetObj;
			if(uiTarget && !uiTarget.parentNode) { 
				uiTarget = null; 
			}
			dragObj.style.position = "static";
			if(eXo.core.DOMUtil.hasClass(dragObj, "Dragging")) {
				eXo.core.DOMUtil.replaceClass(dragObj," Dragging","");
			}

			var dragCopyObj = eXo.core.DOMUtil.findFirstDescendantByClass(portletFragment, "div", "CopyObj");
			if(dragCopyObj) {
				dragCopyObj.parentNode.replaceChild(dragObj, dragCopyObj);
				dragObj.style.width = "auto";
			}
			
			if(uiTarget) {	
				//if drag object is not gadget module, create an module
				var col = uiDashboardUtil.findColIndexInDashboard(uiTarget);
				var row = uiDashboardUtil.findRowIndexInDashboard(uiTarget);
				var compId = portletFragment.parentNode.id;
				
				if(eXo.core.DOMUtil.hasClass(dragObj, "SelectItem")) {
					var url = uiDashboardUtil.createRequest(compId, 'AddNewGadget', col, row, dragObj.id);
					ajaxGet(url);
				} else {
					//in case: drop to old position
					if(uiDashboardUtil.findColIndexInDashboard(dragObj) == col 
								&& uiDashboardUtil.findRowIndexInDashboard(dragObj) == (row-1)) {
						uiTarget.parentNode.removeChild(uiTarget);
					} else {					
						uiTarget.parentNode.replaceChild(dragObj, uiTarget);
						row = uiDashboardUtil.findRowIndexInDashboard(dragObj);
						gadgetId = dragObj.id;
						var url = uiDashboardUtil.createRequest(compId, 'MoveGadget', col, row, gadgetId);
						ajaxAsyncGetRequest(url);
					}
				}
			}

			uiTarget = eXo.core.DOMUtil.findFirstDescendantByClass(portletFragment, "div", "UITarget");
			while (uiTarget) {
				eXo.core.DOMUtil.removeElement(uiTarget);
				uiTarget = eXo.core.DOMUtil.findFirstDescendantByClass(portletFragment, "div", "UITarget");
			}
			uiDashboard.targetObj = uiDashboard.currCol = uiDashboard.compId = null;
		}	
	},
	
	onLoad : function(windowId) { 
		var portletWindow = document.getElementById(windowId);
		if(!portletWindow) return;
		
		var DOMUtil = eXo.core.DOMUtil;
		var uiDashboard = DOMUtil.findFirstDescendantByClass(portletWindow, "div", "UIDashboard");
		var portletFragment = DOMUtil.findAncestorByClass(uiDashboard, "PORTLET-FRAGMENT") ;
		var uiContainer = DOMUtil.findFirstDescendantByClass(uiDashboard, "div", "UIDashboardContainer");
		var uiSelect = DOMUtil.findFirstDescendantByClass(uiDashboard, "div", "UIDashboardSelectContainer") ;
		if(!uiContainer) return;
		
		var gadgetContainer = DOMUtil.findFirstChildByClass(uiContainer, "div", "GadgetContainer");
		uiDashboard.style.overflow = "hidden";
		portletFragment.style.overflow = "hidden" ;
		
		var uiSelect = DOMUtil.findFirstDescendantByClass(uiDashboard, "div", "UIDashboardSelectContainer");
		if(uiSelect.style.display != "none") {
			uiContainer.style.marginLeft = "211px" ;
		} else {
			uiContainer.style.marginLeft = "0px" ;	
		}
		
		var gadgetContainer = DOMUtil.findFirstChildByClass(uiContainer, "div", "GadgetContainer");
		var colsContainer = DOMUtil.findFirstChildByClass(gadgetContainer, "div", "UIColumns");
		var columns = DOMUtil.findChildrenByClass(colsContainer, "div", "UIColumn");
		var colsSize = 0;
		for(var i=0; i<columns.length; i++) {
			if(columns[i].style.display != "none") colsSize++;
		}
		colsContainer.style.width = colsSize*320 + 20 + "px";

		eXo.webui.UIDashboard.initDragDrop(windowId) ;
		eXo.webui.UIDashboard.initHeight(windowId) ;
	},
	
	initDragDrop : function(windowId) {
		var DOMUtil = eXo.core.DOMUtil ;
		var portletWindow = document.getElementById(windowId) ;
		var gadgetControls = DOMUtil.findDescendantsByClass(portletWindow, "div", "GadgetControl");
		for(var j=0; j<gadgetControls.length; j++) {
			var uiGadget = DOMUtil.findAncestorByClass(gadgetControls[j],"UIGadget");
			eXo.webui.UIDashboard.init(gadgetControls[j], uiGadget);
			var iframe = DOMUtil.findFirstDescendantByClass(uiGadget, "iframe", "gadgets-gadget") ;
			if (iframe) {
				iframe.onResizeCallback = eXo.webui.UIDashboard.initHeight ;
				iframe.style.width = "auto" ;
			}
			uiGadget.minimizeCallback = eXo.webui.UIDashboard.initHeight ;
		}
	},
	
	initHeight : function(windowId) {
		var DOMUtil = eXo.core.DOMUtil;
		var portletWindow = document.getElementById(windowId);
		var uiDashboard = DOMUtil.findFirstDescendantByClass(portletWindow, "div", "UIDashboard") ;
		var portletFragment = DOMUtil.findAncestorByClass(uiDashboard, "PORTLET-FRAGMENT") ;
		
		var uiSelect = DOMUtil.findFirstDescendantByClass(uiDashboard, "div", "UIDashboardSelectContainer");
		var uiContainer = DOMUtil.findFirstDescendantByClass(uiDashboard, "div", "UIDashboardContainer");
		
		if(document.getElementById("UIPageDesktop")) {
			var itemCont = DOMUtil.findFirstChildByClass(uiSelect, "div", "DashboardItemContainer");
			var middleItemCont = DOMUtil.findFirstDescendantByClass(uiSelect, "div", "MiddleItemContainer");
			var topItemCont = DOMUtil.findNextElementByTagName(middleItemCont, "div");
			var bottomItemCont = DOMUtil.findPreviousElementByTagName(middleItemCont, "div");
			
			
			var minusHeight = 0 ;
			var minusHeightEle = DOMUtil.findPreviousElementByTagName(middleItemCont.parentNode, "div") ; 
			while(minusHeightEle) {
				minusHeight += minusHeightEle.offsetHeight ;
				minusHeightEle = DOMUtil.findPreviousElementByTagName(minusHeightEle, "div") ;
			}
			minusHeightEle = DOMUtil.findPreviousElementByTagName(itemCont, "div") ;
			while(minusHeightEle) {
				minusHeight += minusHeightEle.offsetHeight ;
				minusHeightEle = DOMUtil.findPreviousElementByTagName(minusHeightEle, "div") ;
			}
			minusHeightEle = null;
			var windowHeight = portletWindow.offsetHeight ; 
			var uiWindow = DOMUtil.findAncestorByClass(portletWindow, "UIWindow") ;
			if(uiWindow && uiWindow.style.display == "none") {
				windowHeight = parseInt(DOMUtil.getStyle(portletFragment, "height")) ;
			}
			middleItemCont.style.height = windowHeight - minusHeight
						- parseInt(DOMUtil.getStyle(itemCont,"paddingTop"))
						- parseInt(DOMUtil.getStyle(itemCont,"paddingBottom"))
						- 5 + "px";
			//dbContainer.style.height = windowHeight + "px";
			if(middleItemCont.scrollHeight > middleItemCont.offsetHeight) {
				topItemCont.style.display = "block";
				bottomItemCont.style.display = "block";
				middleItemCont.style.height = middleItemCont.offsetHeight - topItemCont.offsetHeight - bottomItemCont.offsetHeight + "px";
			} else {
				topItemCont.style.display = "none";
				bottomItemCont.style.display = "none";
			}
		}
		
		

	},
	
	createTarget : function(width, height) {
		var uiTarget = document.createElement("div");
		uiTarget.id = "UITarget";
		uiTarget.className = "UITarget";
		uiTarget.style.width = width + "px";
		uiTarget.style.height = height + "px";
		return uiTarget;
	},
	
	createTargetOfAnObject : function(obj) {
		var ggwidth = obj.offsetWidth;
		var ggheight = obj.offsetHeight;
		var uiTarget = document.createElement("div");
		uiTarget.id = "UITarget";
		uiTarget.className = "UITarget";
		uiTarget.style.height = ggheight + "px";
		return uiTarget;
	},
	
	showHideSelectContainer : function(comp) {
		var DOMUtil = eXo.core.DOMUtil;
		var uiDashboardPortlet = DOMUtil.findAncestorByClass(comp, "UIDashboard");
		var portletFragment = DOMUtil.findAncestorByClass(uiDashboardPortlet, "PORTLET-FRAGMENT");
		var uiSelectContainer = DOMUtil.findFirstDescendantByClass(uiDashboardPortlet, "div", "UIDashboardSelectContainer");
		var uiContainer = DOMUtil.findFirstDescendantByClass(uiDashboardPortlet, "div", "UIDashboardContainer");
		
		var portletId = portletFragment.parentNode.id;
		
		var addButton = DOMUtil.findFirstDescendantByClass(uiContainer, "div", "ContainerControlBarL");
		
		var url = eXo.env.server.portalBaseURL + '?portal:componentId=' + portletId +
						'&portal:type=action&portal:isSecure=false&uicomponent=' + uiDashboardPortlet.id +
						'&op=SetShowSelectContainer&ajaxRequest=true' ;
		if(uiSelectContainer.style.display != "none") {
			uiSelectContainer.style.display = "none";
			url += '&isShow=false';
			addButton.style.visibility = "visible";
			uiContainer.style.marginLeft = "0px" ;
		} else {
			uiSelectContainer.style.display = "block";
			url += '&isShow=true';
			addButton.style.visibility = "hidden";
			uiContainer.style.marginLeft = "211px" ;
		}
		ajaxAsyncGetRequest(url, false);
	}, 
	
	onTabClick : function(clickElement, normalStyle, selectedType) {
		var DOMUtil = eXo.core.DOMUtil;
		var category = DOMUtil.findAncestorByClass(clickElement, "GadgetCategory");
		var categoryContent = DOMUtil.findFirstChildByClass(category, "div", "ItemsContainer");
		var categoriesContainer = DOMUtil.findAncestorByClass(category, "GadgetItemsContainer");
		var categories = DOMUtil.findChildrenByClass(categoriesContainer, "div", "GadgetCategory");
		var gadgetTab = DOMUtil.findFirstChildByClass(category, "div", "GadgetTab");
		
		if(DOMUtil.hasClass(gadgetTab, normalStyle)) {
			for(var i=0; i<categories.length; i++) {
				DOMUtil.findFirstChildByClass(categories[i], "div", "GadgetTab").className = "GadgetTab " + normalStyle;
				DOMUtil.findFirstChildByClass(categories[i], "div", "ItemsContainer").style.display = "none";
			}
			DOMUtil.findFirstChildByClass(category, "div", "GadgetTab").className = "GadgetTab " + selectedType;
			categoryContent.style.display = "block";
		} else {
			DOMUtil.findFirstChildByClass(category, "div", "GadgetTab").className = "GadgetTab " + normalStyle;
			categoryContent.style.display = "none";
		}
	},
	
	enableContainer : function(elemt) {
		var DOMUtil = eXo.core.DOMUtil;
		if(DOMUtil.hasClass(elemt, "DisableContainer")) {
			DOMUtil.replaceClass(elemt, " DisableContainer", "");
		}
		var arrow = DOMUtil.findFirstChildByClass(elemt, "div", "Arrow");
		if(DOMUtil.hasClass(arrow, "DisableArrowIcon")) DOMUtil.replaceClass(arrow," DisableArrowIcon", "");
	},
	
	disableContainer : function(elemt) {
		var DOMUtil = eXo.core.DOMUtil;
		if(!DOMUtil.hasClass(elemt, "DisableContainer")) {
			DOMUtil.addClass(elemt, "DisableContainer");
		}
		var arrow = DOMUtil.findFirstChildByClass(elemt, "div", "Arrow");
		if(!DOMUtil.hasClass(arrow, "DisableArrowIcon")) DOMUtil.addClass(arrow," DisableArrowIcon");
	},
	
	scrollOnDrag : function(dragObj) {
		var DOMUtil = eXo.core.DOMUtil;
		var dashboardUtil = eXo.webui.UIDashboardUtil;
		var uiDashboard = DOMUtil.findAncestorByClass(dragObj, "UIDashboard");
		var gadgetContainer = DOMUtil.findFirstDescendantByClass(uiDashboard, "div", "GadgetContainer");
		var colCont = DOMUtil.findFirstChildByClass(gadgetContainer, "div", "UIColumns");
		
		if(!DOMUtil.findFirstDescendantByClass(colCont, "div", "UITarget")) return;
		
		var visibleWidth = gadgetContainer.offsetWidth;
		var visibleHeight = gadgetContainer.offsetHeight;
		var trueWidth = colCont.offsetWidth;
		var trueHeight = colCont.offsetHeight;
		
		var objLeft = dashboardUtil.findPosXInContainer(dragObj, gadgetContainer);
		var objRight = objLeft + dragObj.offsetWidth;
		var objTop = dashboardUtil.findPosYInContainer(dragObj, gadgetContainer);
		var objBottom = objTop + dragObj.offsetHeight;
		
		//controls horizontal scroll
		var deltaX = gadgetContainer.scrollLeft;
		if((trueWidth - (visibleWidth + deltaX) > 0) && objRight > visibleWidth) {
			gadgetContainer.scrollLeft += 5;
		} else {
			if(objLeft < 0 && deltaX > 0) gadgetContainer.scrollLeft -= 5;
		}
		
		//controls vertical scroll
		var buttonHeight = DOMUtil.findFirstChildByClass(gadgetContainer, "div", "ContainerControlBarL").offsetHeight;
		var deltaY = gadgetContainer.scrollTop;
		if((trueHeight - (visibleHeight -10 - buttonHeight + deltaY) > 0) && objBottom > visibleHeight) {
			gadgetContainer.scrollTop += 5;
		}	else {
			if(objTop < 0 && deltaY > 0) gadgetContainer.scrollTop -= 5;
		}
	}	
	
}

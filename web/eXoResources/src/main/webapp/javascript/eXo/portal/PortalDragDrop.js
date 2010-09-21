/**
 * This class manages the drag and drop of components on the page.
 * It uses a DradDrop object to manage the events, sets some callback functions
 * and some parameters to initialize the DragDrop object.
 */
function PortalDragDrop() { 
	this.count = 0 ;
} ;

/**
 * This function inits the PortalDragDrop object
 * It initializes a DragDrop object that will manage the drag events
 */

PortalDragDrop.prototype.init = function(e) {
	if (!e) e = window.event;
	
	if(e && e.preventDefault) e.preventDefault();
	
	if(((e.which) && (e.which == 2 || e.which == 3)) || ((e.button) && (e.button == 2)))	return;
	
	var DOMUtil = eXo.core.DOMUtil ;
	var Browser = eXo.core.Browser ;
  var DragDrop = eXo.core.DragDrop ;
	/**
	 * This function is called after the DragDrop object is initialized
	 */
  DragDrop.initCallback = function (dndEvent) {
  	/* TODO : TrongTT has removed this snippet
  	if (!eXo.core.Browser.isFF() && document.getElementById("UIWorkingWorkspace")) {
			document.getElementById("UIWorkingWorkspace").style.position = "relative";
		}
		*/
  	var PortalDragDrop = eXo.portal.PortalDragDrop ;
    this.origDragObjectStyle = new eXo.core.HashMap() ;
    var dragObject = dndEvent.dragObject ;
    var properties = ["top", eXo.core.I18n.isLT() ? "left" : "right", "zIndex", "opacity", "filter", "position", "width"] ;
    this.origDragObjectStyle.copyProperties(properties, dragObject.style) ;
   	
    PortalDragDrop.originalDragObjectTop = Browser.findPosY(dragObject) ;
    var originalDragObjectLeft = Browser.findPosX(dragObject) ;
    var originalMousePositionY = Browser.findMouseYInPage(e) ;
    var originalMousePositionX = Browser.findMouseXInPage(e) ;
    PortalDragDrop.deltaYDragObjectAndMouse = originalMousePositionY - PortalDragDrop.originalDragObjectTop ;
    PortalDragDrop.deltaXDragObjectAndMouse = originalMousePositionX - originalDragObjectLeft ;
    
    PortalDragDrop.parentDragObject = dragObject.parentNode ;
    //backup parent's sibling (the next td element), used to restore dragobject position when drag column and press ESC
    PortalDragDrop.backupParentSibling = DOMUtil.findNextElementByTagName(dragObject.parentNode, "td");
    /*PortalDragDrop.backupDragObjectWidth = dragObject.offsetWidth ;
        
    PortalDragDrop.backupTopPosition = PortalDragDrop.originalDragObjectTop ;
    PortalDragDrop.backupLeftPosition = originalDragObjectLeft ;
    PortalDragDrop.backupOffsetWidth = dragObject.offsetWidth ;
    PortalDragDrop.backupOffsetHeight = dragObject.offsetHeight ;*/
    
    /*Case: dragObject out of UIPortal*/
    if(DOMUtil.findFirstChildByClass(dragObject, "div", "CONTROL-BLOCK") == null) {
      var cloneObject = dragObject.cloneNode(true) ;
      dragObject.parentNode.insertBefore(cloneObject, dragObject) ;
      
      dndEvent.dragObject = cloneObject ;
      
      cloneObject.style.position = "absolute" ;
      if(eXo.core.I18n.isLT()) cloneObject.style.left = originalDragObjectLeft + "px" ;
      else cloneObject.style.right = (Browser.getBrowserWidth() - originalDragObjectLeft - dragObject.offsetWidth) + "px" ;
            
      cloneObject.style.top = (PortalDragDrop.originalDragObjectTop - document.documentElement.scrollTop) + "px" ;
      cloneObject.style.opacity = 0.5 ;
      cloneObject.style.filter = "alpha(opacity=50)" ;
      cloneObject.style.width = dragObject.offsetWidth + "px" ;
    }
    
    //fix bug ie in RTL 
    if(eXo.core.I18n.isRT() && Browser.isIE6() && DOMUtil.findFirstChildByClass(dragObject, "div", "CONTROL-BLOCK")) {
    	dragObject.style.right = parseInt(dragObject.style.right) 
    			+ document.getElementById("UIControlWorkspace").offsetWidth + "px" ;
    }
    
  };
  
  /**
   * DragDrop will catch mouse move event, update dndEvent.foundTargetObject and call this method
   * This method will update dragObject and preview block position
   */
  DragDrop.dragCallback = function(dndEvent) {
    var dragObject = dndEvent.dragObject ;
    /* Control Scroll */
    eXo.portal.PortalDragDrop.scrollOnDrag(dragObject, dndEvent) ;
    // window.status = "foundTargetObject: " + dndEvent.foundTargetObject + "    lastFoundTargetObject: " + dndEvent.lastFoundTargetObject;
    if(dndEvent.foundTargetObject) {
      /*Check and asign UIPage to uiComponentLayout when DND on UIPage*/
      var uiComponentLayout ;
      if(dndEvent.foundTargetObject.className == "UIPage") {
        uiComponentLayout = DOMUtil.findFirstDescendantByClass(dndEvent.foundTargetObject, "div", "VIEW-PAGE") ;
      } else if(dndEvent.foundTargetObject.className == "UIPortal") {
        uiComponentLayout = DOMUtil.findFirstDescendantByClass(dndEvent.foundTargetObject, "div", "LAYOUT-PORTAL") ;
      } else {
        var foundUIComponent = new eXo.portal.UIPortalComponent(dndEvent.foundTargetObject) ;
        uiComponentLayout = foundUIComponent.getLayoutBlock() ;
      }
      
      /*Set Height is auto for the empty container layout*/
      
      if(uiComponentLayout.className == "LAYOUT-CONTAINER") uiComponentLayout.style.height = "auto" ;
      
      try {
	      if(eXo.portal.PortalDragDrop.backupLastFoundTarget) {
	      	var lastFoundUIComponent = new eXo.portal.UIPortalComponent(eXo.portal.PortalDragDrop.backupLastFoundTarget);
	      	
	      	var lastFoundComponentLayout = lastFoundUIComponent.getLayoutBlock();
		      if((lastFoundComponentLayout.className == "LAYOUT-CONTAINER") && (lastFoundComponentLayout.offsetHeight < 30)) {
		      	if (eXo.core.DOMUtil.findFirstDescendantByClass(lastFoundComponentLayout, "div", "UIContainer") == null) {
		      		lastFoundComponentLayout.style.height = "60px" ;
		      	}
		      }
	      }
      } catch(err) {
      	//window.status = err.toString() ;
      }
     
      /*################################################################################*/
      
      dndEvent.foundTargetObject.uiComponentLayoutType = uiComponentLayout ;
      
      var componentIdElement = DOMUtil.getChildrenByTagName(uiComponentLayout, "div")[0] ;
      var layoutTypeElement = DOMUtil.getChildrenByTagName(componentIdElement, "div")[0] ;
      eXo.portal.PortalDragDrop.layoutTypeElementNode = layoutTypeElement ;
      
      if(layoutTypeElement != null) {
        /* ===============================CASE ROW LAYOUT================================ */
        var rowContainer = DOMUtil.findFirstDescendantByClass(uiComponentLayout, "div", "UIRowContainer") ;
	      var childRowContainer = DOMUtil.getChildrenByTagName(rowContainer, "div") ;
        
        var listComponent = new Array() ;
        for(var i = 0; i < childRowContainer.length; i++) {
          if((childRowContainer[i].className != "DragAndDropPreview") && (childRowContainer[i] != dragObject)) {
            listComponent.push(childRowContainer[i]) ;
          }
        }
        
        dndEvent.foundTargetObject.listComponentInTarget = listComponent ;
        /*Set properties for drag object */
        eXo.portal.PortalDragDrop.setDragObjectProperties(dragObject, dndEvent.backupMouseEvent) ;
        
        var insertPosition = eXo.portal.PortalDragDrop.findInsertPosition(listComponent, dragObject, "row") ;
        				        
//        if(dndEvent.foundTargetObject == dndEvent.lastFoundTargetObject &&
//           dndEvent.lastFoundTargetObject.foundIndex == insertPosition) {
//            return ;
//        }
        dndEvent.foundTargetObject.foundIndex = insertPosition ;
        
        /*Undo preview */
        if(dndEvent.lastFoundTargetObject) {
          eXo.portal.PortalDragDrop.backupLastFoundTarget = dndEvent.lastFoundTargetObject ;          
          //eXo.portal.PortalDragDrop.undoPreview(dndEvent) ;
        }
        eXo.portal.PortalDragDrop.removeNullPreview();
        
        /* Insert preview block */
        if(insertPosition >= 0) {
          rowContainer.insertBefore(eXo.portal.PortalDragDrop.createPreview(), listComponent[insertPosition]) ;
        } else {
          rowContainer.appendChild(eXo.portal.PortalDragDrop.createPreview()) ;
        }

      } else {
        /* ===============================CASE COLUMN LAYOUT================================ */
    	/*Set properties for drag object */
        eXo.portal.PortalDragDrop.setDragObjectProperties(dragObject, dndEvent.backupMouseEvent) ;

        var trContainer = eXo.portal.PortalDragDrop.parentDragObject.parentNode;          
        var tdElementList = DOMUtil.getChildrenByTagName(trContainer, "td") ;
        var listComponent = new Array();
        
        for (var i = 0; i < tdElementList.length; i++) {
      	  if (tdElementList[i] != dragObject.parentNode) {
      		  listComponent.push(tdElementList[i]);
      	  }
        }
        
        dndEvent.foundTargetObject.listComponentInTarget = listComponent ;
        /*Find Insert Position */
        var insertPosition = eXo.portal.PortalDragDrop.findInsertPosition(listComponent, dragObject, "column") ;
        
        if(dndEvent.lastFoundTargetObject && dndEvent.foundTargetObject == dndEvent.lastFoundTargetObject &&
          dndEvent.lastFoundTargetObject.foundIndex == insertPosition) {
          return ;
        }
        
        dndEvent.foundTargetObject.foundIndex = insertPosition ;
        /*Undo preview */
        //eXo.portal.PortalDragDrop.removeNullPreview();
        //We don't need to remove everytime
        if (DOMUtil.findChildrenByClass(dragObject.parentNode, "div", "DragAndDropPreview").length == 0) {
          dragObject.parentNode.appendChild(eXo.portal.PortalDragDrop.createPreview());
        }  
        
        if(insertPosition >= 0) {
          trContainer.insertBefore(dragObject.parentNode, listComponent[insertPosition]) ;
        } else {
          trContainer.appendChild(dragObject.parentNode) ;
        }
      }
			//when dragObject out of page
      //Can't drag small column with ie6
//			if ((Browser.findPosY(dragObject) < 2)  || (Browser.findPosX(dragObject) + 64 > eXo.core.Browser.getBrowserWidth())) {
//				DragDrop.dropCallback(dndEvent);
//				document.onmousemove = null;
//			} 
    } 
  } ;

  /**
   * DragDrop catch mouse up event, update dndEvent.foundTargetObject and call this function
   * this foundTargetObject exists, this will call doDropCallback, if not, this will restore original dragObject position
  */
  DragDrop.dropCallback = function(dndEvent) {
  	this.origDragObjectStyle.setProperties(dndEvent.dragObject.style, false) ;

    if(dndEvent.foundTargetObject != null) {
      eXo.portal.PortalDragDrop.doDropCallback(dndEvent) ;
    } else {
      if(DOMUtil.findFirstChildByClass(dndEvent.dragObject, "div", "CONTROL-BLOCK") == null) {
					dndEvent.dragObject.parentNode.removeChild(dndEvent.dragObject) ;
			}
//      dndEvent.foundTargetObject = eXo.portal.PortalDragDrop.backupLastFoundTarget ;
//      eXo.portal.PortalDragDrop.doDropCallback(dndEvent) ;
			// fix bug WEBOS-196
			var srcElement = dndEvent.dragObject ; 
			srcElement.style.width = "auto" ;
  		
			eXo.portal.PortalDragDrop.removeNullPreview();
			
		     if (DOMUtil.hasClass(srcElement, "UIColumnContainer")) {
				  var tdNode = srcElement.parentNode ;
				  var lastSibling = eXo.portal.PortalDragDrop.backupParentSibling;
				  if (lastSibling == null) {
					  tdNode.parentNode.appendChild(tdNode);
				  } else {
					  tdNode.parentNode.insertBefore(tdNode, lastSibling);
				  }
			  }
    }
  };
  
  var clickObject = this ;
  var controlBlock = DOMUtil.findAncestorByClass(clickObject, "CONTROL-BLOCK") ;

  var dragBlock;
  if(controlBlock != null) {
    dragBlock = eXo.portal.UIPortal.findUIComponentOf(controlBlock) ;    
  } else {
  	dragBlock = DOMUtil.findAncestorByClass(clickObject, "DragObjectPortlet") ;
	if(!dragBlock) {
  		dragBlock = clickObject;
	}
  }
  DragDrop.init(eXo.portal.PortalDragDrop.findDropableTargets(dragBlock), clickObject, dragBlock, e) ;
};

/**
 * this will remove preview block and update new postion for dragObject
 */
PortalDragDrop.prototype.doDropCallback = function(dndEvent) {
	if(!dndEvent.lastFoundTargetObject) {
		dndEvent.lastFoundTargetObject = eXo.portal.PortalDragDrop.backupLastFoundTarget ;
	}

	//eXo.portal.PortalDragDrop.undoPreview(dndEvent) ;
	eXo.portal.PortalDragDrop.removeNullPreview();
	
	var srcElement = dndEvent.dragObject ;
      
  var targetElement = dndEvent.foundTargetObject ;
  
  var newComponent = false;
  if(eXo.core.DOMUtil.hasDescendantClass(srcElement, "DragControlArea") && (targetElement.foundIndex != null)) {
//  	alert("My Test: " + eXo.portal.PortalDragDrop.layoutTypeElementNode);
    if(eXo.portal.PortalDragDrop.layoutTypeElementNode != null) {
      eXo.portal.PortalDragDrop.divRowContainerAddChild(srcElement, targetElement, targetElement.foundIndex) ;
    }
  }
  
  if(eXo.core.DOMUtil.findFirstChildByClass(dndEvent.dragObject, "div", "CONTROL-BLOCK") == null) {
    dndEvent.dragObject.parentNode.removeChild(dndEvent.dragObject) ;
    newComponent = true;
  }

  var params = [
    {name: "srcID", value: (srcElement.id.replace(/^UIPortlet-/, "")).replace(/^UIContainer-/,"")},
    {name: "targetID", value: targetElement.id.replace(/^.*-/, "")},
    {name: "insertPosition", value: targetElement.foundIndex},
    {name: "newComponent", value: newComponent}
  ] ;
  
  try {
    dndEvent.lastFoundTargetObject.foundIndex = undefined;
  } catch(err) {
  	
  }
	// Modified by Philippe : added callback function
  ajaxGet(eXo.env.server.createPortalURL("UIPortal", "MoveChild", true, params)) ;
};

/**
 * Return all components that are droppable in the DOM
 */
PortalDragDrop.prototype.findDropableTargets = function(dragBlock) {
  var dropableTargets = new Array() ;
  
  if (dragBlock && eXo.core.DOMUtil.hasClass(dragBlock, "UIColumnContainer")) {
  	  var uiTableContainer = eXo.core.DOMUtil.findAncestorByClass(dragBlock, "UITableColumnContainer");
  	  dropableTargets.push(uiTableContainer);
  	  return dropableTargets ;
  } else {  
  	  var uiWorkingWorkspace = document.getElementById("UIWorkingWorkspace") ;
  	  var uiPortal = eXo.core.DOMUtil.findFirstDescendantByClass(uiWorkingWorkspace, "div", "UIPortal") ;
  	  var uiContainers = eXo.core.DOMUtil.findDescendantsByClass(uiWorkingWorkspace, "div", "UIContainer") ;
  	  if(uiPortal) {
  	    dropableTargets.push(uiPortal) ;
  	  } else {
  	  	var uiPage = eXo.core.DOMUtil.findFirstDescendantByClass(uiWorkingWorkspace, "div", "UIPage") ;
  	    if(uiPage) dropableTargets.push(uiPage) ;
  	  }
  	  for(var i = 0; i < uiContainers.length; i++) {
  		if (eXo.core.DOMUtil.hasClass(uiContainers[i], "UITableColumnContainer")) continue;  
  	    dropableTargets.push(uiContainers[i]) ;
  	  }
  	   return dropableTargets ;
   }
};

/**
 * This will scroll page if we drag out of the page
 */
PortalDragDrop.prototype.scrollOnDrag = function(dragObject, dndEvent) {
  var dragObjectTop = eXo.core.Browser.findPosY(dragObject) ;
  var browserHeight = eXo.core.Browser.getBrowserHeight() ;
  var mouseY = eXo.core.Browser.findMouseYInClient(dndEvent.backupMouseEvent) ;
  var deltaTopMouse = eXo.core.Browser.findMouseYInPage(dndEvent.backupMouseEvent) - mouseY ;
  var deltaTop = mouseY - (Math.round(browserHeight * 5/6)) ;
  var deltaBottom = mouseY - (Math.round(browserHeight/6)) ;
  if(deltaTop > 0) {
    document.documentElement.scrollTop += deltaTop - 5 ;
  }
  
  if(deltaBottom < 0 && document.documentElement.scrollTop > 0) {
    document.documentElement.scrollTop += deltaBottom ;
  }
};

PortalDragDrop.prototype.findInsertPosition = function(components, dragObject, layout) {
  var dragObjectX = eXo.core.Browser.findPosX(dragObject) ;
  
  if(layout == "row") {
    for(var i = 0; i < components.length; i++) {
      var componentTop = eXo.core.Browser.findPosY(components[i]) ;
      var dragObjectTop = eXo.core.Browser.findPosY(dragObject) ;
      var componentMiddle = componentTop + Math.round(components[i].offsetHeight / 2) ;
            
      if(dragObjectTop > componentMiddle) continue ;
      else return i;
    }
    return -1 ;
    
  } else {
    for(var i = 0; i < components.length; i++) {
      var componentInTD = eXo.core.DOMUtil.getChildrenByTagName(components[i] ,"div")[0] ;    	
      var componentX = eXo.core.Browser.findPosX(components[i]) ;
      
      if(dragObjectX > componentX) continue ;
      else return i ;
    }
    return -1 ;
  }  
};

PortalDragDrop.prototype.setDragObjectProperties = function(dragObject, e) {
  var uiControlWorkspace = document.getElementById("UIControlWorkspace") ;
  var uiPage = eXo.core.DOMUtil.findAncestorByClass(dragObject, "UIPage");
  var csWidth = uiControlWorkspace.offsetWidth ;
  
  /* IE's Bug: It always double when set position, margin-left for 
   * UIWorkingWorkspace is problem.
   * If WorkingWorkspace is setted a width, that bug disappear
   * but the layout on IE has breakdown!!!
   * */
  if(eXo.core.Browser.isIE7() || (eXo.core.Browser.isIE6() && (uiPage == null))) csWidth = csWidth * 2 ;
  dragObject.style.position = "absolute" ;
  if(eXo.core.DOMUtil.findFirstChildByClass(dragObject, "div", "CONTROL-BLOCK") != null) {
    dragObject.style.top = (eXo.core.Browser.findMouseYInPage(e) - 
                            eXo.portal.PortalDragDrop.deltaYDragObjectAndMouse) + "px" ;
    if(eXo.core.I18n.isLT()) dragObject.style.left = (eXo.core.Browser.findMouseXInPage(e) - csWidth -
	                              eXo.portal.PortalDragDrop.deltaXDragObjectAndMouse) + "px" ;
    dragObject.style.width = "300px" ;
  }   
};

PortalDragDrop.prototype.createPreview = function() {

  var previewBlock = document.createElement("div") ;
  var components = eXo.core.DragDrop.dndEvent.foundTargetObject.listComponentInTarget ;
  
  previewBlock.className = "DragAndDropPreview" ;
  previewBlock.id = "DragAndDropPreview" ;
   
  return previewBlock ;
};

PortalDragDrop.prototype.removeNullPreview = function() {
	var dropObject = document.getElementById("DragAndDropPreview") ;
	if (dropObject) {
		dropObject.parentNode.removeChild(dropObject);
	}
};
/*
PortalDragDrop.prototype.undoPreview = function(dndEvent) {
	var DOMUtil = eXo.core.DOMUtil ;
  var uiComponentLayout ;
  try{
	  if(dndEvent.lastFoundTargetObject.className == "UIPage") {
	    uiComponentLayout = DOMUtil.findFirstDescendantByClass(dndEvent.lastFoundTargetObject, "div", "VIEW-PAGE") ;
	  } else if(dndEvent.lastFoundTargetObject.className == "UIPortal") {
	    uiComponentLayout = DOMUtil.findFirstDescendantByClass(dndEvent.lastFoundTargetObject, "div", "LAYOUT-PORTAL") ;
	  } else {
	    var foundUIComponent = new eXo.portal.UIPortalComponent(dndEvent.lastFoundTargetObject) ;
	    uiComponentLayout = foundUIComponent.getLayoutBlock() ;
	  }
  }catch(e) {}  
  var componentIdElement = DOMUtil.getChildrenByTagName(uiComponentLayout ,"div")[0] ;
  var layoutTypeElement = DOMUtil.getChildrenByTagName(componentIdElement ,"div")[0] ;
  
  var dropHere = document.getElementById("DragAndDropPreview") ;

  var dragObject = dndEvent.dragObject ;
	
  if(dropHere != null) {
    if(layoutTypeElement != null) {
      dropHere.parentNode.removeChild(dropHere) ;
    } else {
      var tableLayoutElement = DOMUtil.getChildrenByTagName(componentIdElement ,"table")[0] ;
      var trContainer = DOMUtil.findFirstDescendantByClass(tableLayoutElement, "tr", "TRContainer") ;
      
      trContainer.removeChild(dropHere.parentNode) ;
      childTRContainer = DOMUtil.getChildrenByTagName(trContainer, "td") ;
      
      for(var i = 0; i < childTRContainer.length; i++) {
        if(childTRContainer[i] == eXo.portal.PortalDragDrop.parentDragObject) {
          childTRContainer[i].style.width = "0px" ;
        }
      }
      
    }
  }
  dragObject.style.border = "none" ;
};
*/
PortalDragDrop.prototype.divRowContainerAddChild = function(insertBlock, targetElement, insertPosition) {
  var listComponent = eXo.core.DragDrop.dndEvent.foundTargetObject.listComponentInTarget ;
  var uiRowContainer = eXo.core.DOMUtil.findFirstDescendantByClass(targetElement, "div", "UIRowContainer") ;
  insertBlock.style.width = "auto" ;
  
  if(insertPosition >= 0) {
    uiRowContainer.insertBefore(insertBlock, listComponent[insertPosition]) ;
  } else {
    uiRowContainer.appendChild(insertBlock) ;
  }
  
  if(eXo.portal.PortalDragDrop.parentDragObject.parentNode.className == "TRContainer") {
    eXo.portal.PortalDragDrop.parentDragObject.parentNode.removeChild(eXo.portal.PortalDragDrop.parentDragObject) ;
  }
};

PortalDragDrop.prototype.fixCss =  function() {
	return;
	/*
	* minh.js.exo
	* don't need this method;
	*/
	var DOMUtil = eXo.core.DOMUtil ;
 	uiPortal = document.getElementById("UIPortal-UIPortal") ;
 	if(uiPortal) {
	  parentByClass = DOMUtil.findAncestorByClass(uiPortal, "Vista") ;
	  if(parentByClass) {
	  	layoutPortal = DOMUtil.findFirstDescendantByClass(uiPortal, "div", "LAYOUT-PORTAL") ;
	  	viewPortal = DOMUtil.findFirstDescendantByClass(uiPortal, "div", "VIEW-PORTAL") ;
	  	uiRowContainer = DOMUtil.findFirstDescendantByClass(uiPortal, "div", "UIRowContainer") ;
	  	if(uiRowContainer != null) { 
	  		if(layoutPortal.style.display == "block" || viewPortal.style.display == "none") {
	  		  uiPortal.style.paddingTop = "8px";
			  	uiPortal.style.paddingRight = "0px";
			  	uiPortal.style.paddingBottom = "0px";
			  	uiPortal.style.paddingLeft = "0px";
	  		} else {
	  			uiPortal.style.padding = "0px" ;
	  		}
	  	} 
	  }
 	}
} ;

eXo.portal.PortalDragDrop = new PortalDragDrop() ;

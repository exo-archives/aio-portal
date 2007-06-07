eXo.require('eXo.core.TemplateEngine');

function UIWidgetContainerManagement() {
	
}

UIWidgetContainerManagement.prototype.show = function() {
	var DOMUtil = eXo.core.DOMUtil ;
	
	var context = new Object();
	context.uiMaskWorkspace = {
		width: "700px"
	}
	
	context.uiMaskWorkspace.content = eXo.core.TemplateEngine.merge("eXo/widget/UIWidgetContainerManagement.jstmpl", context) ;
	
	var innerHTML = eXo.core.TemplateEngine.merge("eXo/portal/UIMaskWorkspace.jstmpl", context) ;
 	var uiMaskWorkspaceNode = DOMUtil.createElementNode(innerHTML, "div");
		
	eXo.core.UIMaskLayer.createMask("UIPortalApplication", uiMaskWorkspaceNode, 30) ;
	
	eXo.widget.UIWidgetContainerManagement.loadWidgetContainer(true);
};

UIWidgetContainerManagement.prototype.destroy = function() {
	var uiWidgetContainerManagement = document.getElementById("UIWidgetContainerManagement");
	var uiMaskWorkspace = eXo.core.DOMUtil.findAncestorByClass(uiWidgetContainerManagement, "UIMaskWorkspace");
	var uiMaskLayer = uiMaskWorkspace.previousSibling ;
	
	var parentNode = uiMaskWorkspace.parentNode ;
	parentNode.removeChild(uiMaskWorkspace);
	parentNode.removeChild(uiMaskLayer);
};

UIWidgetContainerManagement.prototype.loadWidgetContainer = function(refresh) {
	var DOMUtil = eXo.core.DOMUtil ;
	
	var uiWidgetContainerManagement = document.getElementById("UIWidgetContainerManagement");
	
	var url = eXo.env.server.context + "/command?";
	url += "type=org.exoplatform.portal.application.handler.GetWidgetContainerHandler";
	
	if(refresh == null || refresh == undefined) refresh = false;
		
	var containers = eXo.core.CacheJSonService.getData(url, refresh);
	
	if(containers == null || containers == undefined) return;
	
	var portalWidgetContainer = DOMUtil.findFirstDescendantByClass(uiWidgetContainerManagement, "div", "PortalWidgetContainer");
	var containerList = DOMUtil.findFirstDescendantByClass(uiWidgetContainerManagement, "div", "ContainerList");
	
	var itemList = '' ;
	
	for(container in containers.widgetContainer) {
		var containerName = containers.widgetContainer[container] ;
		itemList += '<div class="NormalItem"  onclick="eXo.widget.UIWidgetContainerManagement.selectContainer(this);">'+containerName+'</div>' ;
	}
	
	containerList.innerHTML = itemList ;
	
	/*Set Selected For The First Item*/
	var containerListElement = DOMUtil.getChildrenByTagName(containerList, "div");
	containerListElement[0].className = "SelectedItem";
};

UIWidgetContainerManagement.prototype.selectContainer = function(selectedElement) {	
	var DOMUtil = eXo.core.DOMUtil ;
	var containerList = DOMUtil.findAncestorByClass(selectedElement, "ContainerList");
	var containers = DOMUtil.getChildrenByTagName(containerList, "div");
	
	selectedElement.className = "SelectedItem" ;
	for(var i = 0; i < containers.length; i++) {
		if(containers[i] != selectedElement) containers[i].className = "NormalItem" ;
	}
};

UIWidgetContainerManagement.prototype.showAddPopup = function() {
	var DOMUtil = eXo.core.DOMUtil ;
	var context = new Object();
	context = {
		popup : {
			title : "Add New Widget Container",
			popupId : "UIAddWidgetContainerForm",
			width : "400px",
			height : "180px",
			closeAction : "eXo.widget.UIWidgetContainerManagement.closeAddPopup('UIAddWidgetContainerForm');"
		}
	}
	context.popup.content = eXo.core.TemplateEngine.merge("eXo/widget/UIAddWidgetContainerForm.jstmpl");
	
	var uiWidgetContainerManagement = document.getElementById("UIWidgetContainerManagement");
	
	var uiPopupWindowTemplate = eXo.core.TemplateEngine.merge('eXo/webui/UIPopupWindow.jstmpl', context);
 	var uiPopupWindowNode = DOMUtil.createElementNode(uiPopupWindowTemplate, "div");
 	uiPopupWindowNode.style.width = context.popup.width;
 	uiWidgetContainerManagement.appendChild(uiPopupWindowNode);
 	eXo.webui.UIPopupWindow.init(context.popup.popupId, false);
 	
	eXo.webui.UIPopupWindow.show(context.popup.popupId);
	
//	var params = [
//  	{name: "objectId", value : "Test"}
//  ] ;
//	ajaxGet(eXo.env.server.createPortalURL("UIWidgets", "AddWidgetContainer", true, params)) ;
};

UIWidgetContainerManagement.prototype.closeAddPopup = function(popupId) {
	var popup = document.getElementById(popupId);
	var parentPopup = popup.parentNode ;
	parentPopup.removeChild(popup);
};

eXo.widget.UIWidgetContainerManagement = new UIWidgetContainerManagement();
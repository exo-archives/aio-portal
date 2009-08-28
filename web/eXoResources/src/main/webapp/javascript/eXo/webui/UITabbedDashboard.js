eXo.webui.UITabbedDashboard = {
	
	init : function(){},
	
	renameTabLabel : function(inputElement, keyEvent) {
		var keynum = keyEvent.keyCode;
		if(keynum == 13){
			var DOMUtil = eXo.core.DOMUtil;
			var newTabLabel = inputElement.value;
			var parent = DOMUtil.findAncestorByClass(inputElement, "MiddleTab");
			var newTabContent = "<span ondblclick='eXo.webui.UITabbedDashboard.showEditLabelInput(this)'>" +
		                	newTabLabel + "</span>";
			parent.innerHTML = newTabContent;
			
			//Send request to server to change node name
			var portletFrag = DOMUtil.findAncestorByClass(parent, "PORTLET-FRAGMENT");
			var compId = portletFrag.parentNode.id;
			var nodeIndex = inputElement.id;
			var href = eXo.env.server.portalBaseURL + "?portal:componentId=" + compId;
			href += "&portal:type=action";
			href += "&portal:isSecure=false";
			href += "&uicomponent=UITabPaneDashboard";
			href += "&op=RenameTabLabel";
			href += "&objectId=" + nodeIndex;
			href += "&newTabLabel=" + newTabLabel;
			window.location = href;
		}	
	},
	
	showEditLabelInput : function(selectedElement, nodeIndex){
		var parent = eXo.core.DOMUtil.findAncestorByClass(selectedElement, "MiddleTab");
		var inputElement = "<input type='text' ";
		inputElement += " id='" + nodeIndex + "'";
		inputElement += " onkeypress='eXo.webui.UITabbedDashboard.renameTabLabel(this, event)' />";
		parent.innerHTML = inputElement;
	}
}
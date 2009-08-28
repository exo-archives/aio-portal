eXo.webui.UITabbedDashboard = {
	
	init : function(){},
	
	renameTabLabel : function(inputElement, keyEvent, backupContent) {
		var keynum = keyEvent.keyCode;
		
		//if user presses on ENTER key, then rename the tab
		if(keynum == 13){
			var newTabLabel = inputElement.value;
			if(newTabLabel.length < 1){
				return;
			}
			var DOMUtil = eXo.core.DOMUtil;
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
			return;
		}
		//If user presses on ESCAPE key, then reset ancient label
		else if(keynum == 27){
			if(backupContent.length < 1){
				return;
			}
			//window.location = eXo.env.server.portalBaseURL;
			}	
	},
	
	showEditLabelInput : function(selectedElement, nodeIndex, currentContent){
		var inputElement = "<input type='text' ";
		inputElement += " id='" + nodeIndex + "'";
		inputElement += " value='" + currentContent +"'";
		inputElement += " style='border:medium none;'"
		inputElement += " onkeypress='" + "eXo.webui.UITabbedDashboard.renameTabLabel(this, event,";
		inputElement += "\"" + currentContent + "\")' />";
		selectedElement.parentNode.innerHTML = inputElement;
		
	}
}
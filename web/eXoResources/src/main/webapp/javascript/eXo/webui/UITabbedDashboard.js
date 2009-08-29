eXo.webui.UITabbedDashboard = {
	
	init : function(){},
	
	renameTabLabel : function(e){
		if(!e){
			e = windwow.event;
		}
		var keyNum = e.keyCode;
		
		//If user presses on ENTER button, then rename the btton
		if(keyNum == 13){
			var inputElement = eXo.core.Browser.getEventSource(e);
			var newTabLabel = inputElement.value;
			if(newTabLabel.length < 1){
				return;
			}
			var DOMUtil = eXo.core.DOMUtil;
			var portletFrag = DOMUtil.findAncestorByClass(inputElement, "PORTLET-FRAGMENT");
			var compId = portletFrag.parentNode.id;
			var nodeIndex = inputElement.id;
			
			//Change the tab label
			var spanElement = document.createElement("span");
			spanElement.innerHTML = newTabLabel;
			inputElement.parentNode.replaceChild(spanElement, inputElement);
			
			//Send request to server to change node name
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
		//If user presses on the ESCAPE key, then reset the span element on the tab
		else if(keyNum == 27){
			var inputElement = eXo.core.Browser.getEventSource(e);
			var spanElement = document.createElement("span");
			spanElement.ondblclick = "#"; //TODO: Set the callback on this property
			spanElement.innerHTML = inputElement.name;
			inputElement.parentNode.replaceChild(spanElement, inputElement);	
		}
	},

	showEditLabelInput : function(selectedElement, nodeIndex, currentContent){

		var inputElement = document.createElement("input");
		inputElement.type = "text";
		inputElement.id = nodeIndex;
		inputElement.name = currentContent; // To store old value
		inputElement.value = currentContent;
		inputElement.style.border = "medium none";
		inputElement.onkeypress = eXo.webui.UITabbedDashboard.renameTabLabel;
		selectedElement.parentNode.replaceChild(inputElement, selectedElement);
	}
}
eXo.webui.UITabbedDashboard = {
	
	init : function(){},
	
	renameTabLabel : function(e){
		if(!e){
			e = windwow.event;
		}
		var keyNum = e.keyCode;
		
		//If user presses on ENTER button, then rename the tab label
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
		inputElement.focus();
		inputElement.select();
		inputElement.value = currentContent;
		inputElement.style.border = "1px solid #b7b7b7";
		inputElement.onkeypress = eXo.webui.UITabbedDashboard.renameTabLabel;
		selectedElement.parentNode.replaceChild(inputElement, selectedElement);
	},
	
	createDashboardPage : function(e){
		if(!e){
			e = window.event;
		}	
		var keyNum = e.keyCode;
		
		//If user presses on ENTER button
		if(keyNum == 13){
			var inputElement = eXo.core.Browser.getEventSource(e);
			var newTabLabel = inputElement.value;
			
			//Send request to server to change node name
			var href = eXo.env.server.portalBaseURL + "?portal:componentId=" + inputElement.id;
			href += "&portal:type=action";
			href += "&portal:isSecure=false";
			href += "&uicomponent=UITabPaneDashboard";
			href += "&op=AddDashboard";
			href += "&objectId=" + newTabLabel;
			window.location = href;
		}
		//If user presses on ESCAPE button
		else if(keyNum == 27){
			var DOMUtil = eXo.core.DOMUtil;
			var inputElement = eXo.core.Browser.getEventSource(e);
			var editingTabElement = DOMUtil.findAncestorByClass(inputElement, "UITab GrayTabStyle");
			
			//Remove the editing tab
			editingTabElement.parentNode.removeChild(editingTabElement);
		}
	},
	
	createTabDashboard : function(addTabElement){
		var DOMUtil = eXo.core.DOMUtil;
		var tabContainer = addTabElement.parentNode;
		var tabElements = DOMUtil.findChildrenByClass(tabContainer, "div", "UITab GrayTabStyle");
		var portletFrag = DOMUtil.findAncestorByClass(tabContainer, "PORTLET-FRAGMENT");
		var selectedTabElement = DOMUtil.findFirstDescendantByClass(tabContainer, "div", "SelectedTab");
		var buttonElement = DOMUtil.findFirstDescendantByClass(tabContainer, "div", "AddDashboard").parentNode;
		
		var newTabElement = selectedTabElement.parentNode.cloneNode(true);
		//selectedTabElement.className = "NormalTab";
		tabContainer.insertBefore(newTabElement, buttonElement);	
		
		var inputElement = document.createElement("input");
		inputElement.type = "text";
		inputElement.focus();
		inputElement.select();
		inputElement.value = "Tab_" + tabElements.length;
		inputElement.style.border = "1px solid #b7b7b7";
		inputElement.onkeypress = eXo.webui.UITabbedDashboard.createDashboardPage;
		inputElement.id = portletFrag.parentNode.id; //Store the id of the portlet here
		
		var spanElement = DOMUtil.findDescendantsByTagName(newTabElement, "span")[0];
		spanElement.parentNode.replaceChild(inputElement, spanElement);
				
	}
}
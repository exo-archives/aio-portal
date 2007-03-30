function UIHorizontalTabs() {
  this.backupNavigationTabStyle ;
  this.backupItemStyle ;
};

UIHorizontalTabs.prototype.init = function() {

} ;

UIHorizontalTabs.prototype.changeTabNavigationStyle = function(clickedEle, over) {
	var uiMouseOverTab = eXo.core.DOMUtil.findAncestorByClass(clickedEle, "UITab") ;
	var tabStyle = eXo.core.DOMUtil.getChildrenByTagName(uiMouseOverTab, "div")[0] ;
	
	if(over) {
		this.backupNavigationTabStyle = tabStyle.className ;
		tabStyle.className = "HighlightNavigationTab" ;
	} else {
		tabStyle.className = this.backupNavigationTabStyle ;
	}
}

UIHorizontalTabs.prototype.itemOver = function(selectedElement, over) {
	if(over) {
		this.backupItemStyle = selectedElement.className ;
		if(selectedElement.className == "MenuItem") {
			selectedElement.className = "MenuItemSelected" ;
		} else {
			selectedElement.className = "MenuItemSelected MenuItemExpand" ;
		}
	} else {
		selectedElement.className = this.backupItemStyle ;
	}
}

UIHorizontalTabs.prototype.displayTabContent = function(clickedEle) {
	var DOMUtil = eXo.core.DOMUtil;
  var uiSelectTab = DOMUtil.findAncestorByClass(clickedEle, "UITab") ;

  var uiHorizontalTabs = DOMUtil.findAncestorByClass(clickedEle, "UIHorizontalTabs") ;
  var uiTab = eXo.core.DOMUtil.findDescendantsByClass(uiHorizontalTabs, "div", "UITab") ;
  var parentdHorizontalTab = uiHorizontalTabs.parentNode ;

  var contentTabContainer = DOMUtil.findFirstDescendantByClass(parentdHorizontalTab, "div", "UITabContentContainer") ;
  var uiTabContent = DOMUtil.getChildrenByTagName(contentTabContainer, "div") ;
  
  var index = 0 ;
  for(var i = 0; i < uiTab.length; i++) {
    var styleTabDiv = DOMUtil.getChildrenByTagName(uiTab[i], "div")[0] ;
    if(uiSelectTab == uiTab[i]) {
      styleTabDiv.className = "SelectedTabStyle" ;
    	index = i ;
    } else {
      styleTabDiv.className = "NormalTabStyle" ;
    }                                                                  
    uiTabContent[i].style.display = "none" ;
  }
  uiTabContent[index].style.display = "block" ;
  eXo.webui.WebUI.fixHeight(uiTabContent[index],'UIWorkspacePanel') ;
  // Used for UIFormTabPane
  if(DOMUtil.findFirstDescendantByClass(document, "div", "UIFormTabPane")) eXo.webui.UIForm.onFixSize();

};

UIHorizontalTabs.prototype.changeTabForUIFormTabpane = function(clickedElemt, formId, hiddenValue) {
	this.displayTabContent(clickedElemt) ;
	eXo.webui.UIForm.setHiddenValue(formId, 'currentSelectedTab', hiddenValue) ;
}

eXo.webui.UIHorizontalTabs = new UIHorizontalTabs();

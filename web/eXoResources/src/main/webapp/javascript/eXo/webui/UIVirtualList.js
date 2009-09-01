function UIVirtualList() {}

UIVirtualList.prototype.init = function(componentId) {
  var uiVirtualList = document.getElementById(componentId);
  if (uiVirtualList == null) return;  
  uiVirtualList.style.height = "300px";    
  var children = eXo.core.DOMUtil.getChildrenByTagName(uiVirtualList,"div");
  var childrenHeight = 0;
  for (var i=0; i<children.length;i++) {
  	childrenHeight += children[i].offsetHeight;  	
  }
  
  if (!uiVirtualList.isFinished && childrenHeight <= uiVirtualList.offsetHeight) {
		uiVirtualList.onscroll();
  } else {  	
  	uiVirtualList.isInitiated = true;
  	uiVirtualList.scrollTop = 0;   	
  }
}

UIVirtualList.prototype.getFeedBox = function(componentId) {
	var DOMUtil = eXo.core.DOMUtil;
	var uiVirtualList = document.getElementById(componentId);
	var feedBox = DOMUtil.findFirstDescendantByClass(uiVirtualList, "div","FeedBox");
	if (feedBox == null) {
		feedBox = DOMUtil.findFirstDescendantByClass(uiVirtualList, "tbody","FeedBox");
	}	
	return feedBox;
}

UIVirtualList.prototype.scrollMove = function(uiVirtualList, url) {
	if (uiVirtualList.isFinished || uiVirtualList.isLocked) return;
	var DOMUtil = eXo.core.DOMUtil;	
var componentHeight = uiVirtualList.offsetHeight;
	var scrollPosition = uiVirtualList.scrollTop;
	var scrollerHeight = uiVirtualList.scrollHeight;	
	var scrollable_gap = scrollerHeight - (scrollPosition + componentHeight);	
	// if scrollbar reaches bottom	
	if (scrollable_gap <= 1) {		
		var feedBox = this.getFeedBox(uiVirtualList.id);
		var appendHTML = feedBox.innerHTML;
		uiVirtualList.storeHTML = appendHTML;
		uiVirtualList.isLocked = true;
		ajaxGet(url);
	}
}

UIVirtualList.prototype.updateList = function(componentId) {
  var DOMUtil = eXo.core.DOMUtil;
var uiVirtualList = document.getElementById(componentId);
  if (uiVirtualList == null) return;
 
  var feedBox = this.getFeedBox(uiVirtualList.id);
  var loadedContent = uiVirtualList.storeHTML;
  
  if (eXo.core.Browser.browserType != "ie") {
  	feedBox.innerHTML = loadedContent + feedBox.innerHTML; 
  } else {  	
  	var index = uiVirtualList.innerHTML.indexOf(feedBox.className);
  	index = uiVirtualList.innerHTML.indexOf(">", index) + 1;
  	var firstSec = uiVirtualList.innerHTML.substring(0, index);
  	var secondSec = uiVirtualList.innerHTML.substring(index);  	
  	uiVirtualList.innerHTML = firstSec + loadedContent + secondSec;  	
  }
	uiVirtualList.isLocked = false;
  if (!uiVirtualList.isFinished && !uiVirtualList.isInitiated) {
  	this.init(componentId);
  }
}

UIVirtualList.prototype.loadFinished = function(componentId) {  
  var uiVirtualList = document.getElementById(componentId);
  if (uiVirtualList == null) return;
  uiVirtualList.isFinished = true;
}

eXo.webui.UIVirtualList = new UIVirtualList();
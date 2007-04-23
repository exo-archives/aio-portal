eXo.require('eXo.core.TemplateEngine');
eXo.require('eXo.webui.UIHorizontalTabs');
eXo.require('eXo.core.CacheJSonService');

function UIAddApplication() {
  
};

UIAddApplication.prototype.init = function(containerId) {
	var DOMUtil = eXo.core.DOMUtil ;
	var container = document.getElementById(containerId);
	var context = new Object();
	
	context.uiMaskWorkspace = {
		width: "700px"
	}
	
	if(document.getElementById("UIMaskWorkspaceJSTemplate") == null) {
		var uiAddAppContainer = document.createElement('div') ;
		uiAddAppContainer.id = "UIAddApplicationContainer" ;
		uiAddAppContainer.style.display = "none" ;
		
		context.uiMaskWorkspace.content = eXo.core.TemplateEngine.merge('eXo/desktop/UIAddApplication.jstmpl', context) ;
		uiAddAppContainer.innerHTML = eXo.core.TemplateEngine.merge("eXo/portal/UIMaskWorkspace.jstmpl", context) ;
		
		container.appendChild(uiAddAppContainer) ;
	}
	var uiAddApplicationContainer = document.getElementById("UIAddApplicationContainer");
	eXo.desktop.UIAddApplication.showAddApplication(uiAddApplicationContainer);
	this.loadPortlets(false);
};

/**Created: by Duy Tu**/
function getUrl(src) {
	var img = document.createElement('img');	
        img.src = src;
	return(img.src);
};

UIAddApplication.prototype.loadPortlets = function(refresh) {
	var uiAddApplicationContainer = document.getElementById("UIAddApplicationContainer");
	var url = eXo.env.server.context + "/service?serviceName=portletRegistry";
	if(refresh == null || refresh == undefined) var refresh = false;
  var category = eXo.core.CacheJSonService.getData(url, refresh);
  window.status = "Onload1.3";
  if(category == null || category == undefined) return;
  var itemList = eXo.core.DOMUtil.findFirstDescendantByClass(uiAddApplicationContainer, "div", "ItemList") ;
  var itemDetailList = eXo.core.DOMUtil.findFirstDescendantByClass(uiAddApplicationContainer, "div", "ItemDetailList") ;
  var items  = '';
  var itemDetails = '';
  var checkSrc = ''; 
  var selected  = false;
  
  /**Repaired: by Vu Duy Tu **/
  for(id in category.portletRegistry) {  	
		var cate = category.portletRegistry[id];
		if(!selected){
      items += '<div class="SelectedItem Item" onclick="eXo.webui.UIItemSelector.onClick(this)"';      
		} else {
			items += '<div class="Item" onclick="eXo.webui.UIItemSelector.onClick(this)"';
		}
	  items += 'onmouseover="eXo.webui.UIItemSelector.onOver(this, true)" onmouseout="eXo.webui.UIItemSelector.onOver(this, false)">' +
	           '  <div class="LeftItem">' +
	           '    <div class="RightItem"> ' + 
						 '		  	<div class="ItemTitle" id="'+id+'">' + 
						          cate["name"]+ 
						 '      </div>' +
						 '    </div>' + 
	           '  </div>' + 
	           '</div> ' ;
	  if(!selected) {
 	    itemDetails += '<div class="ItemDetail" style="display: block">';
	  } else {
	  	itemDetails += '<div class="ItemDetail" style="display: none">';
	  }
	   itemDetails += '<div class="ItemDetailTitle">' +
	          	    	'	 <div class="TitleIcon ViewListIcon"><span></span></div>' +
						  	    '	 <div class="Title">Select Portlets</div>' +
						  	    '	 <div style="clear: left;"><span></span></div>' +
	          	      '</div>';
	          	      
	  itemDetails += '  <div class="ApplicationListContainer">';
	  var portlets = cate["portlets"];
	    window.status = "Onload5";
	    
	  var count=0;var i = 2;
	  for(id in portlets) {
	  	portlet = portlets[id];
	  	var cssFloat = "float:left";
	  	count = i%2;
      if(count == 1)cssFloat = "float:right";
      ++i; if(i==100)i=2;
      var srcBG = "/eXoResources/skin/portal/webui/component/view/UIPageDesktop/DefaultSkin/icons/80x80/" + portlet["title"]+".png";
      var srcNormalBG = "/eXoResources/skin/portal/webui/component/view/UIPageDesktop/DefaultSkin/icons/80x80/DefaultPortlet.png";
			srcBG = getUrl(srcBG);
			srcNormalBG = getUrl(srcNormalBG);
	    itemDetails += '<div class="Application" style="'+cssFloat+';">' +
			               '		<div class="ApplicationDescription">' +
			               '			<div class="PortletIcon" title="'+portlet["title"]+'"' +
			               '        onclick="eXo.desktop.UIAddApplication.addPortlet(\''+id+'\',\'true\')">' +
			               '        <span>' +
			               '          <img class="PlIcon" src="'+srcBG+'" onError="src=\''+srcNormalBG+'\'">' +
			               '        </span>' +
			               '      </div>' +
			               '		  <div style="float: right;">' +
				             ' 			  <div class="SelectButton" onclick="eXo.desktop.UIAddApplication.addPortlet(\''+id+'\',\'false\')" ><span></span></div>' +
				             ' 			  <div class="AddButton" onclick="eXo.desktop.UIAddApplication.addPortlet(\''+id+'\',\'true\')"' +
				             '          title="Add this application to the desktop page">' +
						         ' 			    <span></span>' +
						      	 '		    </div>' +
					      		 '	    </div>' +
					      		 '	    <div style="clear: both"><span></span></div>' +
					      		 '	  </div>' +
			               '		<div class="TitleBarApplication">' +
			               '			<div class="ApplicationLabel">'+portlet["title"]+'</div>' +
			               '		</div>' +
		              	 '</div>';
	  }
    itemDetails += '  </div>' +
									 '</div>';  
		if(!selected) selected = true;							 
  }
  itemList.innerHTML = items;  
  itemDetailList.innerHTML = itemDetails;

};

UIAddApplication.prototype.isError = function(object) {
	var a = "b";
	object.onerror = function() {
	  a = "c";
	}
	if(a == "c") return false;
	return true ;
};

UIAddApplication.prototype.addPortlet = function(id, save) {
	var params = [
		{name: "portletId", value : id},
		{name: "save", value : save}
	] ;
	ajaxGet(eXo.env.server.createPortalURL("UIPortal", "AddPortletToDesktop", true, params)) ;
};

UIAddApplication.prototype.showAddApplication = function(object) {
	eXo.core.UIMaskLayer.createMask("UIPortalApplication", object, 30) ;
	eXo.desktop.UIDockbar.reset() ;
};

UIAddApplication.prototype.removeAddApplication = function() {
	var uiAddApplicationContainer = document.getElementById("UIAddApplicationContainer") ;
	var maskLayer = uiAddApplicationContainer.previousSibling ;
	eXo.core.UIMaskLayer.removeMask(maskLayer) ;
	
	uiAddApplicationContainer.parentNode.removeChild(uiAddApplicationContainer) ;
};

UIAddApplication.prototype.importJavascript = function(object) {
	eXo.require(object); 
};

eXo.desktop.UIAddApplication = new UIAddApplication() ;
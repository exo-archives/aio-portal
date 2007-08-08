/***********************************************************************************************
 * Portal  Ajax  Response Data Structure
 * {PortalResponse}
 *      |
 *      |--->{PortletResponse}
 *      |
 *      |--->{PortletResponse}
 *      |          |-->{portletId}
 *      |          |-->{portletTitle}
 *      |          |-->{portletMode}
 *      |          |-->{portletState}
 *      |          |
 *      |          |-->{Data}
 *      |          |      |
 *      |          |      |--->{BlockToUpdate}
 *      |          |      |         |-->{blockId}
 *      |          |      |         |-->{data}
 *      |          |      |
 *      |          |      |--->{BlockToUpdate}
 *      |          |--->{Script}
 *      |
 *      |--->{Data}
 *      |      |
 *      |      |--->{BlockToUpdate}
 *      |      |         |-->{blockId}
 *      |      |         |-->{data}
 *      |      |
 *      |      |--->{BlockToUpdate}
 *      |--->{Script}
 *
 * Modified from AjaxRequest.js by Matt Kruse <matt@ajaxtoolbox.com>
 * WWW: http://www.AjaxToolbox.com/
 **************************************************************************************************/
eXo.require('eXo.desktop.UIDesktop') ;
eXo.require('eXo.core.UIMaskLayer') ;
eXo.require('eXo.core.Skin') ;

function PortletResponse(responseDiv) {
  var  DOMUtil = eXo.core.DOMUtil ;
  var div = eXo.core.DOMUtil.getChildrenByTagName(responseDiv, "div") ;
  this.portletId =  div[0].innerHTML ;
  this.portletTitle =  div[1].innerHTML ;
  this.portletMode =  div[2].innerHTML ;
  this.portletState =  div[3].innerHTML ;
  this.portletData =  div[4].innerHTML ;
  if(div[5])this.script = div[5].innerHTML ;
  this.blocksToUpdate = null ;
  var blocks = DOMUtil.findChildrenByClass(div[4], "div", "BlockToUpdate") ;
  if(blocks.length > 0 ) {
    this.blocksToUpdate = new Array() ;
    for(var i = 0 ; i < blocks.length; i++) {
      var obj = new Object() ; 
      var div = eXo.core.DOMUtil.getChildrenByTagName(blocks[i], "div") ;
      obj.blockId = div[0].innerHTML ;
      obj.data = div[1] ;
      this.blocksToUpdate[i] = obj ;
    }
  }
  //alert("portlet Id: " +  this.portletId) ;
  //alert("portlet Title: " +  this.portletTitle) ;
  //alert("portlet Mode: " +  this.portletMode) ;
  //alert("portlet State: " +  this.portletState) ;
  //alert("portlet Data: " +  this.portletData) ;
};
/*****************************************************************************************/
function PortalResponse(responseDiv) {
  var  DOMUtil = eXo.core.DOMUtil ;
  this.portletResponses = new Array() ;
  var div = DOMUtil.getChildrenByTagName(responseDiv, "div") ;
  for(var i = 0 ; i < div.length; i++) {
    if(div[i].className == "PortletResponse") {
      this.portletResponses[this.portletResponses.length] =  new PortletResponse(div[i]) ;
    } else if(div[i].className == "PortalResponseData") {
      this.data = div[i] ;
      var blocks = DOMUtil.findChildrenByClass(div[i], "div", "BlockToUpdate") ;
      this.blocksToUpdate = new Array() ;
      for(var j = 0 ; j < blocks.length; j++) {
        var obj = new Object() ; 
        var dataBlocks = DOMUtil.getChildrenByTagName(blocks[j], "div") ;
        obj.blockId = dataBlocks[0].innerHTML ;
        obj.data = dataBlocks[1] ;
        this.blocksToUpdate[j] = obj ;
      }
    } else if(div[i].className == "PortalResponseScript") {
      this.script = div[i].innerHTML;
    }
  }
};
/*****************************************************************************************/
function AjaxRequest(method, url, queryString) {	
	var instance = new Object();
	
	instance.timeout = 60000;
	instance.aborted = false;
	
	if(method != null) instance.method = method; else	instance.method = "GET";
	if(url != null) instance.url = url; else instance.url = window.location.href;
	if(queryString != null) instance.queryString = queryString; else instance.queryString = null;

	instance.request = null;
	
	instance.responseReceived = false;

	instance.status = null;
	instance.statusText = null;
	
	instance.responseText = null;
	instance.responseXML = null;
	
	instance.onTimeout = null;
	instance.onLoading = null;
	instance.onLoaded = null;
	instance.onInteractive = null;
	instance.onComplete = null;
	instance.onSuccess = null;
	// Added by Philippe
	instance.callBack = null;

	instance.onError = null;
	
	instance.onLoadingInternalHandled = false;
	instance.onLoadedInternalHandled = false;
	instance.onInteractiveInternalHandled = false;
	instance.onCompleteInternalHandled = false;
	
	instance.request = eXo.core.Browser.createHttpRequest() ;
	instance.request.onreadystatechange = function() {
		if (instance == null || instance.request == null) { return; }
		if (instance.request.readyState == 1) { instance.onLoadingInternal(instance); }
		if (instance.request.readyState == 2) { instance.onLoadedInternal(instance); }
		if (instance.request.readyState == 3) { instance.onInteractiveInternal(instance); }
		if (instance.request.readyState == 4) { instance.onCompleteInternal(instance); }
  };
	

	instance.onLoadingInternal = function() {
		if (instance.onLoadingInternalHandled) return; 

		if (typeof(instance.onLoading) == "function") instance.onLoading(instance);
		instance.onLoadingInternalHandled = true;
	};
	
	instance.onLoadedInternal = function() {
		if (instance.onLoadedInternalHandled) return;
		if (typeof(instance.onLoaded) == "function") instance.onLoaded(instance);
		instance.onLoadedInternalHandled = true;
	};
	
	instance.onInteractiveInternal = function() {
		if (instance.onInteractiveInternalHandled) return;
		if (typeof(instance.onInteractive) == "function") instance.onInteractive(instance);
		instance.onInteractiveInternalHandled = true;
	};
	
	instance.onCompleteInternal = function() {
		if (instance.onCompleteInternalHandled || instance.aborted) return; 
		
		try{
			instance.responseReceived = true;
			instance.status = instance.request.status;
			instance.statusText = instance.request.statusText;
			instance.responseText = instance.request.responseText;
			instance.responseXML = instance.request.responseXML;
		}catch(err){
			return;
		}
		
		if(typeof(instance.onComplete) == "function") instance.onComplete(instance);
		
		if (instance.request.status == 200 && typeof(instance.onSuccess) == "function") {
			instance.onSuccess(instance);
			instance.onCompleteInternalHandled = true;
			// Added by Philippe
			if (typeof(instance.callBack) == "function") instance.callBack(instance);
		} else if (typeof(instance.onError) == "function") {
			instance.onError(instance);
			instance.onCompleteInternalHandled = false;
		}
		
		// Remove IE doesn't leak memory
		delete instance.request['onreadystatechange'];
		instance.request = null;

	};
		
	instance.onTimeoutInternal = function() {
		if (instance == null || instance.request == null || instance.onCompleteInternalHandled) return;
		instance.aborted = true;
		instance.request.abort();
		
		if (typeof(instance.onTimeout) == "function") instance.onTimeout(instance);
		
		delete instance.request['onreadystatechange'];
		instance.request = null;
	};
	
	instance.process = function() {
		if (instance.request == null) return;
		instance.request.open(instance.method, instance.url, true);		
		if (instance.method == "POST") {
			instance.request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		} else {
			instance.request.setRequestHeader("Content-Type", "text/plain;charset=UTF-8");
		}
		
		if (instance.timeout > 0) setTimeout(instance.onTimeoutInternal, instance.timeout);
		
		instance.request.send(instance.queryString);
	};
	
	return instance;
};

/*****************************************************************************************/

function HttpResponseHandler(){
	var instance = new Object();
	
	instance.executeScript = function(script) {
		if(script == null || script == "") return ;
	  try {
	    eval(script) ;       
	    return;
	  } catch(err) {                  
	  }
	  var elements = script.split(';') ;
	  if(elements != null && elements.length > 0) {
		  for(var i = 0; i < elements.length; i++) {
		    try {
		      eval(elements[i]) ;
		    } catch(err) {
		      alert(err +" : "+elements[i] + "  -- " + i) ;      
		    }
		  }
	  } 
	} ;
	
	instance.updateBlocks = function(blocksToUpdate, parentId) {
	  if(blocksToUpdate == null) return ;
	  var parentBlock = null;
	  if(parentId != null && parentId != "") parentBlock =  document.getElementById(parentId) ;
	  
	  for(var i = 0; i < blocksToUpdate.length; i++) {
	    var blockToUpdate =  blocksToUpdate[i] ;	
	    var target = null;   	
	    if(parentBlock != null) {
	    	target = eXo.core.DOMUtil.findDescendantById(parentBlock, blockToUpdate.blockId) ;
	    } else {
	    	target = document.getElementById(blockToUpdate.blockId) ;
	    }
	    if(target == null) alert("target  BlockToUpdate.blockId " + blockToUpdate.blockId);
//	    console.group("blockToUpdate.data");
//	    	console.dir(blockToUpdate.data);
//	    	console.groupEnd();
	    var newData =  eXo.core.DOMUtil.findDescendantById(blockToUpdate.data, blockToUpdate.blockId) ;
	   	//var newData =  blockToUpdate.data.getElementById(blockToUpdate.blockId) ;
	    if(newData == null) alert("block to update Id" + blockToUpdate.blockId);
	    target.innerHTML = newData.innerHTML ;   
	  }  
	} ;
	
	instance.ajaxTimeout = function(request){
		eXo.core.UIMaskLayer.removeMask(eXo.portal.AjaxRequest.maskLayer) ;
	  eXo.portal.AjaxRequest.maskLayer = null ;
	  eXo.portal.CurrentRequest = null;
	  window.location.href = window.location.href;  
	}
	
	instance.ajaxResponse = function(request){
		var temp =  document.createElement("div") ;
	  temp.innerHTML =  this.request.responseText ;
	  var responseDiv = eXo.core.DOMUtil.findFirstDescendantByClass(temp, "div", "PortalResponse") ;
	  var response = new PortalResponse(responseDiv)  ;
	  //Handle the portlet responses
	  var portletResponses =  response.portletResponses ;
	  if(portletResponses != null) {
	    for(var i = 0; i < portletResponses.length; i++) {
	      var portletResponse = portletResponses[i] ;
	      instance.updateBlocks(portletResponse.blocksToUpdate, "UIPortlet-"+portletResponse.portletId) ;    
	      instance.executeScript(portletResponse.script) ;    
	    }
	  } 
	  if(response.blocksToUpdate == undefined) {
	  	if(confirm("Session timeout. Refresh your browser?")) instance.ajaxTimeout(request);
	  }
	  //Handle the portal responses
	  instance.updateBlocks(response.blocksToUpdate) ;    
	  instance.executeScript(response.script) ;    
	  eXo.core.UIMaskLayer.removeMask(eXo.portal.AjaxRequest.maskLayer) ;
	  eXo.portal.AjaxRequest.maskLayer = null ;	
	  eXo.portal.CurrentRequest = null;
	} ;
	
	instance.ajaxLoading = function(request){
		var mask = document.getElementById("AjaxLoadingMask") ; //eXo code
		if(eXo.portal.AjaxRequest.maskLayer == null) {
			eXo.portal.AjaxRequest.maskLayer = eXo.core.UIMaskLayer.createMask("UIPortalApplication", mask, 30) ;
			eXo.core.Browser.addOnScrollCallback("5439383", eXo.core.UIMaskLayer.setPosition) ;
		}
	}
	
	return instance;
}

/*****************************************************************************************/
// Modified by Philippe : added callback function
function ajaxGet(url, callback) {
	if (!callback) callback = null;
  doRequest("Get", url, null, callback) ;
}

function ajaxPost(formElement, callback) {
	if (!callback) callback = null;
  var queryString = eXo.webui.UIForm.serializeForm(formElement) ;
  var url = formElement.action + "&ajaxRequest=true" ;
  doRequest("POST", url, queryString, callback) ;
}

function doRequest(method, url, queryString, callback) {
  request = new AjaxRequest(method, url, queryString);
	handler = new HttpResponseHandler();
	request.onSuccess = handler.ajaxResponse;
	request.onLoading = handler.ajaxLoading;
  request.onTimeout = handler.ajaxTimeout;
  request.callBack = callback;
 	eXo.portal.CurrentRequest = request;
  request.process();
}	;

function ajaxAbort() {	
  eXo.core.UIMaskLayer.removeMask(eXo.portal.AjaxRequest.maskLayer) ;
  eXo.portal.AjaxRequest.maskLayer = null ;	  

  eXo.portal.CurrentRequest.request.abort();  
  eXo.portal.CurrentRequest.aborted = true;
  eXo.portal.CurrentRequest = null;
}

function ajaxAsyncGetRequest(url, async) {
	if(async == undefined) async = true;
	var request =  eXo.core.Browser.createHttpRequest();
  request.open('GET', url, async);
  request.setRequestHeader("Cache-Control", "max-age=86400");
  request.send(null);  
	if(!async) return request.responseText;
}

function ajaxRedirect(url) {
	url =	url.replace(/&amp;/g, "&") ;
	window.location.href = url;
}

eXo.portal.AjaxRequest = AjaxRequest.prototype.constructor ;

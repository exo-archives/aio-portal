ImplodeExplode = function() {
} ;


var discoverEXO = function(object) {
	/*
	 * minh.js.exo
	 */
	removeInfoDiv();
	var infoDiv =	document.createElement("div") ;
			infoDiv.setAttribute("id" , "Show.EX0.0bject") ;
			infoDiv.style.background = "#848484" ;
			infoDiv.style.border = "4px double green" ;
			infoDiv.style.bottom = "0px" ;
			infoDiv.style.color = "white" ;
			infoDiv.style.height = "300px" ;
			infoDiv.style.margin = "auto" ;
			infoDiv.style.overflow = "auto" ;
			infoDiv.style.padding = "10px" ;
			infoDiv.style.position = "absolute" ;
			infoDiv.style.width = "400px" ;
			infoDiv.style.zIndex = "999" ;
	var closeButton = document.createElement("div") ;
			closeButton.setAttribute("id" , "Remove.Information.0bject") ;
			closeButton.style.textAlign = "right" ;
			closeButton.innerHTML = "<span style='color: red; font-weight: bold; cursor: pointer;'>[ X ]</span>" ;
	var csHTML = new String() ;
	for (var exo in object) {
		csHTML +=  exo + " : " + object[exo] + "<br />" ;
	}

	document.body.appendChild(infoDiv) ;
	infoDiv.appendChild(closeButton) ;
	infoDiv.innerHTML += csHTML ;
	
	closeButton = document.getElementById("Remove.Information.0bject") ;
	closeButton.onclick = removeInfoDiv ;
	
	function removeInfoDiv() {
	 if (document.getElementById("Show.EX0.0bject")) {
		var infoDiv = document.getElementById("Show.EX0.0bject") ;
		document.body.removeChild(infoDiv) ;
		}
	}
} ;




/*TODO: it has a confusion posX and posY */
/*
 * minh.js.exo
 */
ImplodeExplode.prototype.doInit = function(uiWindow, clickedElement, containerId, numberOfFrame) {
	
	var container = document.getElementById(containerId) ;
	
	this.object = uiWindow ;
	this.object.loop = numberOfFrame ;
	this.object.iconX = eXo.core.Browser.findPosYInContainer(clickedElement, container) ;
	this.object.iconY = eXo.core.Browser.findPosXInContainer(clickedElement, container) ;
	this.object.iconW = clickedElement.offsetWidth ;
	this.object.iconH = clickedElement.offsetHeight ;

	if(this.object.animation == null) {		
		this.object.animation = document.createElement("div") ;		
		container.appendChild(this.object.animation) ;	
		this.object.animation.style.display = "block" ;
		this.object.animation.style.background = "#ffffff" ;
		this.object.animation.style.position = "absolute" ;	
		eXo.core.Browser.setOpacity(this.object.animation, 50) ;
		this.object.animation.style.zIndex = this.object.maxIndex ;
	}
} ;

ImplodeExplode.prototype.doCenterInit = function(uiWindow, clickedElement, containerId, numberOfFrame) {
	var container = document.getElementById(containerId) ;

	this.object = uiWindow ;
	this.object.loop = numberOfFrame ;
	if(this.object.style.display == "none") {
		this.object.iconX = this.object.originalX + this.object.originalH/2 ;
		this.object.iconY = this.object.originalY + this.object.originalW/2 ;
	} else {
		this.object.iconX = eXo.core.Browser.findPosYInContainer(this.object, container) + this.object.offsetHeight/2 ;
		this.object.iconY = eXo.core.Browser.findPosXInContainer(this.object, container) + this.object.offsetWidth/2 ;
	}
	this.object.iconW = 1 ;
	this.object.iconH = 1 ;
	if(this.object.animation == null) {
		this.object.animation = document.createElement("div") ;
		container.appendChild(this.object.animation) ;
		this.object.animation.style.display = "block" ;
		this.object.animation.style.background = "white" ;
		this.object.animation.style.position = "absolute" ;
		eXo.core.Browser.setOpacity(this.object.animation, 40) ;
		this.object.animation.style.zIndex = this.object.maxIndex ;
	}
	uiWindow = this.object;
} ;

/*
 * minh.js.exo
 * fix bug speed click in dockbar.
 * reference -> variable this.busy in method ...doExplode ...doImplode
 */
ImplodeExplode.prototype.explode = function(uiWindow, clickedElement, containerId, numberOfFrame, type) {
//alert("Object: " + uiWindow.className + "\nIcon: " + clickedElement.className + "\n Container: " + containerId);
	if (!this.busy) {
		if(type) {
			eXo.animation.ImplodeExplode.doCenterInit(uiWindow, clickedElement, containerId, numberOfFrame) ;
		} else {
			eXo.animation.ImplodeExplode.doInit(uiWindow, clickedElement, containerId, numberOfFrame) ;
		}
		this.object.step = numberOfFrame - 1 ;
		this.object.isShowed = true ;
		eXo.animation.ImplodeExplode.doExplode(containerId) ;
	}
};

ImplodeExplode.prototype.implode = function(uiWindow, clickedElement, containerId, numberOfFrame, type) {
	if (!this.busy) {	
		if(type) {
			eXo.animation.ImplodeExplode.doCenterInit(uiWindow, clickedElement, containerId, numberOfFrame) ;
		} else {
			eXo.animation.ImplodeExplode.doInit(uiWindow, clickedElement, containerId, numberOfFrame) ;
		}
		this.object.originalX = this.object.offsetTop ;
		this.object.originalY = this.object.offsetLeft ;
		this.object.originalW = this.object.offsetWidth ;
		this.object.originalH = this.object.offsetHeight ;
		this.object.step = 1 ;
		if(this.object.style.display == "block") {
			this.object.style.display = "none" ;
		}
		this.object.isShowed = false ;
	
		eXo.animation.ImplodeExplode.doImplode(containerId) ;
	} 
} ;

ImplodeExplode.prototype.doImplode = function(containerId) {
	this.busy = true ;
	var container = document.getElementById(containerId) ;
	var win = this.object ;
	var X0 = win.originalX + (win.step*(win.iconX - win.originalX))/win.loop ;
	var Y0 = win.originalY + ((X0 - win.originalX)*(win.iconY - win.originalY))/(win.iconX - win.originalX) ;
	var W0 = ((win.originalW - win.iconW)*(win.loop - win.step))/win.loop + win.iconW ;
	var H0 = ((win.originalH - win.iconH)*(win.loop - win.step))/win.loop + win.iconH ;

	win.animation.style.top = X0 + "px" ;
	win.animation.style.left = Y0 + "px" ;
	win.animation.style.width = W0 + "px" ;
	win.animation.style.height = H0 + "px" ;

	win.step++;
	if(W0 > win.iconW) {
		setTimeout("eXo.animation.ImplodeExplode.doImplode('" + containerId + "');", 0) ;
	}	else {
		container.removeChild(win.animation) ;
		win.animation = null ;
		this.busy = false ;
	}

} ;


ImplodeExplode.prototype.doExplode = function(containerId ) {
			this.busy = true ;
			var container = document.getElementById(containerId) ;
			var win = this.object;
		
			var X0 = win.originalX + (win.step*(win.iconX - win.originalX))/win.loop ;
			var Y0 = win.originalY + ((X0 - win.originalX)*(win.iconY - win.originalY))/(win.iconX - win.originalX) ;
			var W0 = ((win.originalW - win.iconW)*(win.loop - win.step))/win.loop + win.iconW ;
			var H0 = ((win.originalH - win.iconH)*(win.loop - win.step))/win.loop + win.iconH ;
			
			win.animation.style.top = X0 + "px" ;
			
			win.animation.style.left = Y0 + "px" ;
			win.animation.style.width = W0 + "px" ;
			win.animation.style.height = H0 + "px" ;
			
			win.step--;
			
			if(W0 < win.originalW) {
				setTimeout("eXo.animation.ImplodeExplode.doExplode('" + containerId + "');", 0) ;
			} else {
				win.style.top = X0 + "px" ;
				win.style.left = Y0 + "px" ;
				win.style.width = W0 + "px" ;
				win.style.height = H0 + "px" ;
				win.style.display = "block" ;
				container.removeChild(win.animation) ;
				win.animation = null ;
				this.busy = false ;
			}
			
};





//ImplodeExplode.prototype.doExplode = function(containerId) {
//		var container = document.getElementById(containerId) ;
//		var win = this.object;
//
//		var X0 = win.originalX + (win.step*(win.iconX - win.originalX))/win.loop ;
//		var Y0 = win.originalY + ((X0 - win.originalX)*(win.iconY - win.originalY))/(win.iconX - win.originalX) ;
//		var W0 = ((win.originalW - win.iconW)*(win.loop - win.step))/win.loop + win.iconW ;
//		var H0 = ((win.originalH - win.iconH)*(win.loop - win.step))/win.loop + win.iconH ;
//		
//		win.animation.style.top = X0 + "px" ;
//		
//		win.animation.style.left = Y0 + "px" ;
//		win.animation.style.width = W0 + "px" ;
//		win.animation.style.height = H0 + "px" ;
//		
//		win.step--;
//		
//		if(W0 < win.originalW) {
//			setTimeout("eXo.animation.ImplodeExplode.doExplode('" + containerId + "');", 0) ;
//		} else {
//			win.style.top = X0 + "px" ;
//			win.style.left = Y0 + "px" ;
//			win.style.width = W0 + "px" ;
//			win.style.height = H0 + "px" ;
//			win.style.display = "block" ;
//			container.removeChild(win.animation) ;
//			win.animation = null ;
//	}	
//};

eXo.animation.ImplodeExplode = new ImplodeExplode() ;
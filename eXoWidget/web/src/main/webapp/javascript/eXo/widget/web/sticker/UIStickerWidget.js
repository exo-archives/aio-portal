eXo.require('eXo.widget.UIExoWidget');

function UIStickerWidget() {
	var attrs = new Array("content");
	this.init("UIStickerWidget", "sticker", attrs);
}
UIStickerWidget.inherits(eXo.widget.UIExoWidget);

UIStickerWidget.prototype.sendContent = function(object) {	
	var DOMUtil = eXo.core.DOMUtil ;
	var uiWidgetContainer = DOMUtil.findAncestorByClass(object, "WidgetApplication") ;
	var uiWidget = DOMUtil.findAncestorByClass(object, "UIWidget");
	containerBlockId = uiWidgetContainer.id;
	
	var parent = uiWidgetContainer.parentNode ;
	
  url = eXo.env.server.context + "/command?" ;
  url += "type=org.exoplatform.web.command.handler.StickerWidgetHandler&action=saveContent&objectId="+uiWidget.id+"&content="+encodeURIComponent(object.value) ;
  ajaxAsyncGetRequest(url, false);
} ;

if(eXo.widget.web == null) eXo.widget.web = {} ;
if(eXo.widget.web.sticker == null) eXo.widget.web.sticker = {};
eXo.widget.web.sticker.UIStickerWidget = new UIStickerWidget()  ;

eXo.require('eXo.webui.UIItemSelector');

function UIForm() {
};

UIForm.prototype.onFixSize = function() {
	var arrowIcon = eXo.core.DOMUtil.findFirstDescendantByClass(document, "div", "Button");
	if(arrowIcon != null) {
		arrowIcon.className = "IconHolder ArrowS1Down16x16Icon" ;
	}
	
  var uiFormTabPane = eXo.core.DOMUtil.findFirstDescendantByClass(document, "div", "UIFormTabPane");
  var tabPaneContent = eXo.core.DOMUtil.findFirstDescendantByClass(uiFormTabPane, "div", "TabPaneContent");
  var uiQuickHelp = eXo.core.DOMUtil.findFirstChildByClass(tabPaneContent, "div", "UIQuickHelp");
  
  if(uiQuickHelp == null) return ;
  
  var workingAreaWithHelp = eXo.core.DOMUtil.findFirstChildByClass(tabPaneContent, "div", "WorkingAreaWithHelp");
  var scrollArea = eXo.core.DOMUtil.findFirstDescendantByClass(uiQuickHelp, "div", "ScrollArea");

  scrollArea.style.height = (workingAreaWithHelp.offsetHeight - 78 ) + "px";

  scrollArea.style.overflow = "auto";
};

/*ie bug  you cannot have more than one button tag*/
UIForm.prototype.submitForm = function(formId, action, useAjax) {
  var form = document.getElementById(formId) ;
  form.elements['formOp'].value = action ;
  if(useAjax) ajaxPost(form) ;
  else  form.submit();
} ;

/*ie bug  you cannot have more than one button tag*/
UIForm.prototype.submitEvent = function(formId, action, params) {
  var form = document.getElementById(formId) ;
  form.elements['formOp'].value = action ; 
  form.action =  form.action + "&" +  params ; 
  //form.submit();
  ajaxPost(form) ;
} ;

UIForm.prototype.selectBoxOnChange = function(formId, elemt) {
	var selectBox = eXo.core.DOMUtil.findAncestorByClass(elemt, "UISelectBoxOnChange");
	var contentContainer = eXo.core.DOMUtil.findFirstDescendantByClass(selectBox, "div", "SelectBoxContentContainer") ;
	var tabs = eXo.core.DOMUtil.findChildrenByClass(contentContainer, "div", "SelectBoxContent");
	for(var i=0; i < tabs.length; i++) {
		tabs[i].style.display = "none";
	}
	tabs[elemt.selectedIndex].style.display = "block";
} ;

UIForm.prototype.setHiddenValue = function(formId, typeId, hiddenValue) {
  var form = document.getElementById(formId) ;
  if(form == null){
	  maskWorkspace =	document.getElementById("UIMaskWorkspace");
	  form = eXo.core.DOMUtil.findDescendantById(maskWorkspace, formId);
  }
  form.elements[typeId].value = hiddenValue;  
} ;

UIForm.prototype.serializeForm = function (formElement) {
	var queryString = "";
  var element ;
  var elements = formElement.elements;
  
  this.addField = function(name, value) { 
	  if (queryString.length > 0) queryString += "&";
	  queryString += name + "=" + encodeURIComponent(value);
  };
  
  for(var i = 0; i < elements.length; i++) {
    element = elements[i];
    if(element.disabled) continue;
    switch(element.type) {
      case "text":
      case "hidden":
      case "password":
      case "textarea" :  
        this.addField(element.name, element.value);  
        break; 
          
      case "checkbox":
      case "radio":
        if(element.checked) this.addField(element.name, element.value);  
        break;  
  
      case "select-one":
        if(element.selectedIndex > -1){
        	this.addField(element.name, element.options[element.selectedIndex].value);  
        }
        break;
    } // switch
   } // for   
   return queryString;
};

eXo.webui.UIForm = new UIForm();

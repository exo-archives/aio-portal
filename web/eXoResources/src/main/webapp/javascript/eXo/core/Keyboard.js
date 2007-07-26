/**
 * @author uoc.nb
 */
function Keyboard() {
  this.listeners = [] ;
  this.controlKeyCodes = [8, 9, 13, 35, 36, 37, 38, 39, 40, 46] ;
  document.onkeydown = function(e) {
    return eXo.core.Keyboard.onKeyDown(e) ;
  }
  document.onkeypress = function(e) {    
    return eXo.core.Keyboard.onKeyPress(e) ;
  } ;
}

Keyboard.prototype.register = function(listener) {
  this.listeners[this.listeners.length] = listener ;
}

Keyboard.prototype.clearListeners = function() {
  this.listeners = [] ;
}

Keyboard.prototype.getKeynum = function(event) {
  var keynum = false ;
  if(window.event) { /* IE */
    keynum = window.event.keyCode;
    event = window.event ;
  } else if(event.which) { /* Netscape/Firefox/Opera */
    keynum = event.which ;
  }
  if(keynum == 0) {
    keynum = event.keyCode ;
  }
  return keynum ;
}

Keyboard.prototype.onKeyDown = function(event) {
  var keynum = this.getKeynum(event) ; 
  var keychar = '' ;
  
  var eventHandler = false ;
 
  if(keynum == 13) {
    eventHandler = 'onEnter' ;
  } else if(keynum == 9) {
    eventHandler = 'onTab' ;
  } else if(keynum == 8 || (eXo.core.OS.isMac && keynum == 51)) {
    eventHandler = 'onBackspace' ;
  } else if(keynum == 46) {
    eventHandler = 'onDelete' ;
  } else if(keynum == 37){
    eventHandler = 'onLeftArrow' ;
  } else if(keynum == 39){
    eventHandler = 'onRightArrow' ;
  } else if(keynum == 38){
    eventHandler = 'onUpArrow' ;
  } else if(keynum == 40){
    eventHandler = 'onDownArrow' ;
  } else if(keynum == 36){
    eventHandler = 'onHome' ;
  } else if(keynum == 35){
    eventHandler = 'onEnd' ;
  }
  
  return this.listenerCallback(eventHandler, event, keynum, keychar) ;
}

Keyboard.prototype.onKeyPress = function(event) {
  var keynum = this.getKeynum(event) ; 
  var keychar = String.fromCharCode(keynum) ;
  var eventHandler = false ;

  if (this.controlKeyCodes.contains(keynum)) {
    return true ;
  }
 
  if((keynum >= 65 && keynum <= 90) || (keynum >= 97 && keynum <= 122)) {
    eventHandler = 'onAlphabet' ;
  } else if(keynum >= 48 && keynum <= 57) {
    eventHandler = 'onDigit' ;
  } else if((keynum >= 32 && keynum <= 47) || (keynum >= 58 && keynum <= 64) || 
            (keynum >= 91 && keynum <= 96) || (keynum >= 123 && keynum <= 65532)) {
    eventHandler = 'onPunctuation' ;
  }
  
  return this.listenerCallback(eventHandler, event, keynum, keychar) ;
}

/**
 * 
 * @param {String} eventHandler
 * @param {Object} event
 * @param {Number} keynum
 * @param {Char} keychar
 */
Keyboard.prototype.listenerCallback = function(eventHandler, event, keynum, keychar) {
  var retVal = true ; 
  if(!eventHandler || eventHandler == '') {
    return retVal ;
  }
  
  // Fix special character
  if (keychar == '"') {
    keychar = '\\"' ;
  } else if (keychar == '\\') {
    keychar = '\\\\' ;
  }
  for(var i=0; i<this.listeners.length; i++) {
    retVal &= eval('this.listeners[' + i + '].' + eventHandler + '(' + keynum + ', "' + keychar + '") ;') ;
  } 
  
  if(!retVal) {
    eXo.core.Keyboard.cancelEvent(event) ;
    return false ;
  }
  
  // Release event if nobody want to capture
  return true ;
} ;

/**
 * 
 * @param {Event} event
 */
Keyboard.prototype.cancelEvent = function(event) {
  if(eXo.core.Browser.browserType == 'ie') { // Cancel bubble for ie
    window.event.cancelBubble = true ;
    window.event.returnValue = true ;
    return ;
  } else { // Cancel event for Firefox, Opera, Safari
    event.stopPropagation() ;
    event.preventDefault() ;
  }
} ;

eXo.core.Keyboard = new Keyboard() ;
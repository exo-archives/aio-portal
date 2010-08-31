/**
 * @author Nguyen Ba Uoc
 */
// 4test
if (eXo.require) eXo.require('eXo.core.html.HTMLEntities');

function HTMLUtil() {
  this.entities = eXo.core.html.HTMLEntities ;
}

/**
 * Encode string
 * @param {String} str string to encode 
 * @return {String} encoded string
 * 
 */
HTMLUtil.prototype.entitiesEncode = function(str) {
  if (!str || str == '') {
    return str ;
  }
  for(var n in this.entities) {
    var entityChar = String.fromCharCode(this.entities[n]) ;
    if(entityChar == '&') {
      entityChar = '\\' + entityChar ;
    }
    while(str.indexOf(entityChar) != -1) {
      str = str.replace(entityChar, '&' + n + ';') ;
    }
  }
  return str ;
}

/**
 * Decode string
 * @param {String} str to decode
 * @return {String} decoded string
 * 
 */
HTMLUtil.prototype.entitiesDecode = function(str) {
  if (!str || str == '') {
    return str ;
  }
  for(var n in this.entities) {
    var entityChar = String.fromCharCode(this.entities[n]) ;
    var htmlEntity = '&' + n + ';' ;
    while(str.indexOf(htmlEntity) != -1) {
      str = str.replace(htmlEntity, entityChar) ;
    }
  }
  return str ;
}

eXo.core.HTMLUtil = new HTMLUtil() ;
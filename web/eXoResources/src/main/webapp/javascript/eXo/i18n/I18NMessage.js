/**
 * Copyright (C) 2009 eXo Platform SAS.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

function I18NMessage() {}

I18NMessage.prototype.getMessage = function(str, params) {
	var msg;
	try {
		msg = eval("this." + str);
	} catch(e) {}
	if (msg == null || msg == "undefined") msg = str;
	
	if (params != null && params.constructor.toString().indexOf("Array") > 0) {
		for (var i=0; i < params.length; i++) {
			msg = msg.replace("{" + i + "}" ,params[i]);
		}
	}	
	return msg;
}

eXo.i18n.I18NMessage = new I18NMessage();
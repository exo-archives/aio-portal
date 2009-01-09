eXo.gadget.UIGadget = {
    createGadget : function(url, id, metadata, userPref, view, isdev, debug, nocache) {
        //eXo = eXo || {};
        window.gadgets = window.gadgets || {};
        eXo.gadgets = window.gadgets;
        //window.gadgets = eXo.gadget.Gadgets;
        if (!eXo.gadgets || !eXo.gadgets.rpc) {
            eXo.core.Using.register('rpc', '/eXoGadgetServer/gadgets/js/rpc.js?c=1');
            eXo.core.Using.register('eXo.gadgets.Gadgets', '/eXoGadgets/javascript/eXo/gadgets/Gadgets.js');
            eXo.core.Using.register('eXo.gadgets.ExoBasedUserPrefStore', '/eXoGadgets/javascript/eXo/gadgets/ExoBasedUserPrefStore.js');

            eXo.core.Using("rpc");
            eXo.core.Using("eXo.gadgets.Gadgets");
            eXo.core.Using("eXo.gadgets.ExoBasedUserPrefStore");

        }
        //TODO: dang.tung - set language for gadget
        //-----------------------------------------
        var language = eXo.core.I18n.getLanguage();
        gadgets.container.setLanguage(language);
        //-----------------------------------------
        var gadget;
        if (metadata != null) {
            gadget = gadgets.container.createGadget({specUrl: url,height: metadata.gadgets[0].height, secureToken: metadata.gadgets[0].secureToken, view: view});
            gadget.metadata = metadata.gadgets[0];
        } else {
            gadget = gadgets.container.createGadget({specUrl: url});
        }
        gadget.parentId = id;
        gadget.debug = debug;
        gadget.nocache = nocache;
        gadget.isdev = isdev;
        
        gadgets.container.addGadget(gadget);
        // i use the internal var "gadget.userPrefs_" to not call the save on the server side
        if (userPref != null) gadget.userPrefs_ = userPref;
        var gadgetBlock = document.getElementById(id);
        gadgetBlock.innerHTML = "<div id='gadget_" + gadget.id + "' class='UIGadgetContent'> </div>";
        gadgets.container.renderGadgets();
        var uiGadget = eXo.core.DOMUtil.findAncestorByClass(gadgetBlock, "UIGadget");
        //TODO: dang.tung - isn't portlet
        if (uiGadget != null) {
            var isDesktop = false;
            if (uiGadget.parentNode.className == "UIPageDesktop") {
                uiGadget.style.position = "absolute";
                isDesktop = true;
            }
            else uiGadget.style.width = "auto";
            eXo.gadget.UIGadget.init(uiGadget, isDesktop, gadget.metadata);
        }

    },

    init : function(uiGadget, inDesktop, metadata) {
        var portletFragment = eXo.core.DOMUtil.findAncestorByClass(uiGadget, "PORTLET-FRAGMENT");
        if (portletFragment == null) {
            uiGadget.onmouseover = eXo.gadget.UIGadget.showGadgetControl;
            uiGadget.onmouseout = eXo.gadget.UIGadget.hideGadgetControl;
        } else {
            var gadgetControl = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetControl");
            gadgetControl.style.display = "block";
            var gadgetTitle = eXo.core.DOMUtil.findFirstDescendantByClass(gadgetControl, "div", "GadgetTitle") ;
            gadgetTitle.style.display = "block";
            if (metadata && metadata.title != null && metadata.title.length > 0) gadgetTitle.innerHTML = metadata.title;
        }

        if (inDesktop) {
            var dragHandleArea = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetDragHandleArea");

            if (uiGadget.style.zIndex < 0) uiGadget.style.zIndex = 0;
            eXo.core.DragDrop2.init(dragHandleArea, uiGadget);

            var uiPageDesktop = document.getElementById("UIPageDesktop");
            var offsetHeight = uiPageDesktop.offsetHeight - uiGadget.offsetHeight ;
            var offsetWidth = uiPageDesktop.offsetWidth - uiGadget.offsetWidth ;
            var dragPosX = uiGadget.offsetLeft;
            var dragPosY = uiGadget.offsetTop;

            if (dragPosX < 0) uiGadget.style.left = "0px";
            if (dragPosY < 0) uiGadget.style.top = "0px";
            if (dragPosY > offsetHeight) uiGadget.style.top = offsetHeight + "px";
            if (dragPosX > offsetWidth) uiGadget.style.left = offsetWidth + "px";

            // drag start callback
            uiGadget.onDragStart = function(x, y, lastMouseX, lastMouseY, e) {
                var uiPageDesktop = document.getElementById("UIPageDesktop");
                if (uiPageDesktop == null) return;
                var uiGadgets = eXo.core.DOMUtil.findChildrenByClass(uiPageDesktop, "div", "UIGadget");
                for (var i = 0; i < uiGadgets.length; i++) {
                    var uiMask = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "UIMask");
                    if (uiMask != null) {
                        var gadgetContent = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "gadgets-gadget-content");
                        uiMask.style.marginTop = - gadgetContent.offsetHeight + "px";
                        uiMask.style.height = gadgetContent.offsetHeight + "px";
                        uiMask.style.width = gadgetContent.offsetWidth + "px";
                        uiMask.style.backgroundColor = "white";
                        eXo.core.Browser.setOpacity(uiMask, 3);
                        uiMask.style.display = "block";
                    }
                }
            }

            //drag callback
            uiGadget.onDrag = function(nx, ny, ex, ey, e) {
                if (nx < 0) uiGadget.style.left = "0px";
                if (ny < 0) uiGadget.style.top = "0px";
            }

            //drop callback
            uiGadget.onDragEnd = function(x, y, clientX, clientY) {
                var uiPageDesktop = document.getElementById("UIPageDesktop");
                var uiGadgets = eXo.core.DOMUtil.findChildrenByClass(uiPageDesktop, "div", "UIGadget");
                for (var i = 0; i < uiGadgets.length; i++) {
                    var uiMask = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadgets[i], "div", "UIMask");
                    if (uiMask) {
                        uiMask.style.display = "none";
                    }
                }

                var offsetHeight = uiPageDesktop.offsetHeight - uiGadget.offsetHeight ;
                var offsetWidth = uiPageDesktop.offsetWidth - uiGadget.offsetWidth ;
                var dragPosX = uiGadget.offsetLeft;
                var dragPosY = uiGadget.offsetTop;

                if (dragPosX < 0) uiGadget.style.left = "0px";
                if (dragPosY < 0) uiGadget.style.top = "0px";
                if (dragPosY > offsetHeight) uiGadget.style.top = offsetHeight + "px";
                if (dragPosX > offsetWidth) uiGadget.style.left = offsetWidth + "px";

                eXo.gadget.UIGadget.saveWindowProperties(uiGadget);
            }
        }

    },

    showGadgetControl : function(e) {
        if (!e) e = window.event;
        e.cancelBubble = true;
        var DOMUtil = eXo.core.DOMUtil;
        var uiGadget = this ;
        var gadgetControl = DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetControl");
        gadgetControl.style.visibility = "visible";

        var uiPageDesktop = DOMUtil.findAncestorByClass(uiGadget, "UIPageDesktop");
        if (uiPageDesktop) {
            var dragHandleArea = DOMUtil.findFirstDescendantByClass(gadgetControl, "div", "GadgetTitle");
        }
    },


    hideGadgetControl : function(e) {
        if (!e) e = window.event;
        e.cancelBubble = true;
        var uiGadget = this ;
        var gadgetControl = eXo.core.DOMUtil.findFirstDescendantByClass(uiGadget, "div", "GadgetControl");
        gadgetControl.style.visibility = "hidden";
        uiGadget.style.border = "none";
    },

    editGadget : function(id) {
        var DOMUtil = eXo.core.DOMUtil ;
        var uiapp = document.getElementById(id) ;
        var id = eXo.core.DOMUtil.findFirstDescendantByClass(uiapp, "iframe", "gadgets-gadget") ;
        var tempId = id.id.split('_')[2] ;
        gadgets.container.getGadget(tempId).handleOpenUserPrefsDialog();
    },

    minimizeGadget: function(selectedElement) {
        var DOMUtil = eXo.core.DOMUtil ;
        var uiGadget = DOMUtil.findAncestorByClass(selectedElement, "UIGadget") ;
        var portletFrag = DOMUtil.findAncestorByClass(uiGadget, "PORTLET-FRAGMENT") ;
        if (!portletFrag) return;

        var gadgetApp = DOMUtil.findFirstChildByClass(uiGadget, "div", "GadgetApplication") ;
        var minimized = false;
        if (gadgetApp.style.display != "none") {
            minimized = true;
            gadgetApp.style.display = "none";
            DOMUtil.replaceClass(selectedElement, "MinimizeGadget", "RestoreGadget");
        } else {
            minimized = false;
            gadgetApp.style.display = "block";
            DOMUtil.replaceClass(selectedElement, "RestoreGadget", "MinimizeGadget");
        }

        var compId = portletFrag.parentNode.id;
        var uicomp = DOMUtil.getChildrenByTagName(portletFrag, "div")[0].className ;
        var href = eXo.env.server.portalBaseURL + "?portal:componentId=" + compId ;
        href += "&amp;portal:type=action&amp;uicomponent=" + uicomp;
        href += "&amp;op=MinimizeGadget";
        href += "&amp;minimized=" + minimized;
        href += "&amp;objectId=" + uiGadget.id + "&amp;ajaxRequest=true";
        ajaxAsyncGetRequest(href);
        if (uiGadget.minimizeCallback) uiGadget.minimizeCallback(portletFrag.parentNode.id);
    },

    deleteGadget : function(selectedElement) {
        var DOMUtil = eXo.core.DOMUtil ;
        var uiGadgetContainer = DOMUtil.findAncestorByClass(selectedElement, "UIWidgetContainer") ;
        var uiPage = DOMUtil.findAncestorByClass(selectedElement, "UIPage") ;
        var uiGadget = DOMUtil.findAncestorByClass(selectedElement, "UIGadget") ;
        var containerBlockId ;
        var isInControlWorkspace = false ;

        gadgetId = uiGadget.id;
        var portletFragment = DOMUtil.findAncestorByClass(uiGadget, "PORTLET-FRAGMENT");

        if (portletFragment != null) {
            var compId = portletFragment.parentNode.id;
            var uicomp = "";
            if (DOMUtil.findChildrenByClass(portletFragment, "div", "UIDashboard")) {
                uicomp = "UIDashboard";
            }
            else
                uicomp = DOMUtil.getChildrenByTagName(portletFragment, "div")[0].className;
            if (confirm(this.confirmDeleteGadget)) {
                var href = eXo.env.server.portalBaseURL + "?portal:componentId=" + compId;
                href += "&portal:type=action&uicomponent=" + uicomp;
                href += "&op=DeleteGadget";
                href += "&objectId=" + gadgetId + "&ajaxRequest=true";
                ajaxGet(href);
            }

        } else {
            if (uiPage) {
                var uiPageIdNode = DOMUtil.findFirstDescendantByClass(uiPage, "div", "id");
                containerBlockId = uiPageIdNode.innerHTML;
            }
            else {
                containerBlockId = uiGadgetContainer.id;
                isInControlWorkspace = true;
            }
            if (confirm("Are you sure you want to delete this gadget ?")) {
                var params = [
                    {name: "objectId", value : gadgetId}
                ] ;
                var result = ajaxAsyncGetRequest(eXo.env.server.createPortalURL(containerBlockId, "DeleteGadget", true, params), false) ;
                if (result == "OK") {
                    DOMUtil.removeElement(uiGadget);
                    if (isInControlWorkspace) eXo.webui.UIVerticalScroller.refreshScroll(0);
                }
            }
        }
    },

    saveWindowProperties : function(object) {
        var DOMUtil = eXo.core.DOMUtil ;
        var uiPage = DOMUtil.findAncestorByClass(object, "UIPage") ;
        var uiPageIdNode = DOMUtil.findFirstDescendantByClass(uiPage, "div", "id");
        containerBlockId = uiPageIdNode.innerHTML;

        var gadgetApp = DOMUtil.findFirstDescendantByClass(object, "div", "GadgetApplication");
        var gadgetId = object.id;

        var params = [
            {name: "objectId", value : gadgetId},
            {name: "posX", value : object.offsetLeft},
            {name: "posY", value : object.offsetTop},
            {name: "zIndex", value : object.style.zIndex}
        ] ;

        ajaxAsyncGetRequest(eXo.env.server.createPortalURL(containerBlockId, "SaveGadgetProperties", true, params), false);

    } ,

    resizeContainer : function() {
        var widgets = document.getElementById("UIWidgets") ;
        if (widgets == null) return;

        var DOMUtil = eXo.core.DOMUtil ;
        var workspacePanel = document.getElementById("UIWorkspacePanel") ;
        if (workspacePanel.style.display == "none") return;
        var uiWidgetContainer = DOMUtil.findFirstDescendantByClass(widgets, "div", "UIWidgetContainer");
        if (uiWidgetContainer == null) return;
        var widgetNavigator = DOMUtil.findFirstChildByClass(uiWidgetContainer, "div", "WidgetNavigator") ;
        var widgetContainerScrollArea = DOMUtil.findFirstChildByClass(uiWidgetContainer, "div", "WidgetContainerScrollArea") ;
        var itemSelectorContainer = DOMUtil.findFirstChildByClass(widgets, "div", "ItemSelectorContainer") ;

        var availableHeight = workspacePanel.offsetHeight - (itemSelectorContainer.offsetHeight + widgetNavigator.offsetHeight + 40) ;
        if (eXo.core.Browser.isIE6() || workspacePanel.offsetHeight < 1) {
            //var html = document.getElementsByTagName("html")[0];
            var uiControlWorkspace = document.getElementById("UIControlWorkspace") ;
            var fixHeight = uiControlWorkspace.offsetHeight - 153;
            fixHeight = (fixHeight < 0) ? 0 : fixHeight;
            /* 153 is total value (UserWorkspaceTitleHeight + UIExoStartHeight + WidgetNavigatorHeight + 40)
             * 40 is distance between UIWidgets and UIExoStart
             * */
            if (widgetContainerScrollArea.offsetHeight == fixHeight) return;
            widgetContainerScrollArea.style.height = fixHeight + "px";
        } else {
            if (availableHeight < 0) return;
            widgetContainerScrollArea.style.height = availableHeight + "px";
        }
        widgetContainerScrollArea.style.overflow = "hidden";
    }
}
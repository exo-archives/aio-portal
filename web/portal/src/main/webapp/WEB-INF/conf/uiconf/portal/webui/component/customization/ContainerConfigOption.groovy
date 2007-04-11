import org.exoplatform.webui.component.model.SelectItemCategory ;
import org.exoplatform.webui.component.model.SelectItemOption ;
  
  List templates = new ArrayList() ;
  
  SelectItemCategory row = new SelectItemCategory("row") ; 
    row.addSelectItemOption(new SelectItemOption("oneRow",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>",
        "OneRowContainerLayout"));
    
     row.addSelectItemOption(new SelectItemOption("twoRows",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "</container>",
        "TwoRowContainerLayout")) ;
     row.addSelectItemOption(new SelectItemOption("threeRows",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +          
        "</container>",
        "ThreeRowContainerLayout"));
  templates.add(row);
     
  SelectItemCategory column = new SelectItemCategory("column") ;
    column.addSelectItemOption(new SelectItemOption("twoColumns","" +
        "<container template=\"system:/groovy/portal/webui/component/view/UITableColumnContainer.gtmpl\">" +
        "</container>", 
        "OneRowContainerLayout")) ;
    column.addSelectItemOption(new SelectItemOption("twoColumns",
        "<container template=\"system:/groovy/portal/webui/component/view/UITableColumnContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "</container>",
        "TwoColumnContainerLayout")) ;
    column.addSelectItemOption(new SelectItemOption("threeColumns",
        "<container template=\"system:/groovy/portal/webui/component/view/UITableColumnContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "</container>",
        "ThreeColumnContainerLayout")) ;
  templates.add(column);  
  
  SelectItemCategory tabs = new SelectItemCategory("tabs") ;
    tabs.addSelectItemOption(new SelectItemOption("twoTabs",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
          "<container template=\"system:/groovy/portal/webui/component/view/UITabContainer.gtmpl\">" +
          "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
          "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
          "</container>" +
        "</container>",
        "TwoTabContainerLayout")) ;
    tabs.addSelectItemOption(new SelectItemOption("threeTabs",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
          "<container template=\"system:/groovy/portal/webui/component/view/UITabContainer.gtmpl\">" +
          "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
          "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
          "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
          "</container>" +
        "</container>",
        "ThreeTabContainerLayout")) ;
  templates.add(tabs);  
  
  SelectItemCategory mixed = new SelectItemCategory("mixed") ;
    mixed.addSelectItemOption(new SelectItemOption("twoColumnsOneRow",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UITableColumnContainer.gtmpl\">" +
        "    <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "    <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  </container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "</container>",
        "TwoColumnOneRowContainerLayout")) ;
    mixed.addSelectItemOption(new SelectItemOption("oneRowTwoColumns",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UITableColumnContainer.gtmpl\">" +
        "    <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "    <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  </container>" +
        "</container>",
        "OneRowTwoColumnContainerLayout")) ;
    mixed.addSelectItemOption(new SelectItemOption("oneRow2Column1Row",
        "<container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\">" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UITableColumnContainer.gtmpl\">" +
        "    <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "    <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "  </container>" +
        "  <container template=\"system:/groovy/portal/webui/component/view/UIContainer.gtmpl\"></container>" +
        "</container>",
        "OneRow2Column1RowContainerLayout")) ;
  templates.add(mixed);

return templates;
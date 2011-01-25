Summary

    * Status: Problem with Scroll bar in a frame with IE7
    * CCP Issue: CCP-715, Product Jira Issue: PORTAL-3885.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * The Scroll bar of a frame containing an eXo portal page doesn't work in IE7
    * create an HTML file containing a frame/iframe that contains an eXo portal page, for example:

      <html>
      <head>
      </head>
       <frameset rows="200,*,25" frameborder="yes" border="0" framespacing="0"> 
           <frame name="page" src="http://localhost:8080/portal" marginwidth="0" marginheight="0" frameborder="0" />
          </frameset>
      </html>

      => In Firefox everything works well but in IE7 the scroll bar does not work either with the mouse wheel or with the arrow keys, only moving the scroll bar by clicking on it works.

Fix description

How is the problem fixed?

    * This is a CSS issue. In web/eXoResources/src/main/webapp/skin/DefaultSkin/portal/webui/component/UIPortalApplicationSkin.css, replace

      html { margin: 0px ; padding: 0px; *overflow-x: hidden; }

      by

      html { margin: 0px ; padding: 0px; }

Patch file: PORTAL-3885.patch

Tests to perform

Reproduction test
* Cf. above

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* No

Configuration changes

Configuration changes:
* No

Will previous configuration continue to work?
* Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?
* Function or ClassName change

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Support review : patch validated

QA Feedbacks
*


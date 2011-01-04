Summary

    * Status: File explorer: right click not working (IE8)
    * CCP Issue: CCP-573, Product Jira Issue: PORTAL-3870, ECM-5516
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
File Explorer: right click not working (IE8)

Fix description

How is the problem fixed?

    * Actually the context menu is shown because when the popup menu is shown, IE8 dispatches the onmouseout event, and this event will enable the default context menu of browser.
      Fix this bug by adding 1 more pixel to style.top and style.left of the popup menu. The mouse pointer will therefore not be over popup menu, so IE8 will not dispatch the onmouseout to popup's parent.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PORTAL-3870.patch

Tests to perform

Reproduction test
To reproduce (use IE8)

   1. Go to File Explorer
   2. Choose a node and right click on it
      It will appear but masked by the right click menu of IE8.

Tests performed at DevLevel
* Cf. above

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

*Can this bug fix have an impact any side effects on current client projects?

    * Function or ClassName change: no

Is there a performance risk/cost?

    * No

Validation (PM/Support/QA)

PM Comment

    * Validated on behalf of PM

Support Comment

    * Support review: patch validated

QA Feedbacks
*

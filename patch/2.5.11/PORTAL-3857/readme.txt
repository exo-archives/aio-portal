Summary

    * Status: Broken link in the navigation after moving nodes
    * CCP Issue: CCP-584, Product Jira Issue: PORTAL-3857
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
When moving a navigation node from its page, the link becomes broken and refers to the home page.
Fix description

How is the problem fixed?

    * We shouldn't set selected node when setting navigation every time, selected node is only set if it is null.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch files: PORTAL-3857.patch

Tests to perform

Reproduction test
Steps to reproduce this issue in WCM/AIO

   1. Connect as root on the acme site
   2. Go to a page rather than the home page, e.g: "Newsletter"
   3. Click on "Edit navigation"
   4. Right-click on the node "Newsletter", then click Move Down/Up
   5. Save and finish
      Observation: URL is still that of Newsletter but the content is that of Overview, and in the navigation bar, Overview item is focused.
   6. Click Newsletter in the navigation bar: still the content of acme home page.
   7. Refresh (F5): no effect.
   8. Click another item in the navigation bar. Then return to Newsletter: ok now.

Tests performed at DevLevel
In Portal standalone:

   1. Go to a page different from home (Web Explorer for e.g)
   2. Go to edit navigation
   3. Click Save and Finish or Abort
      Observation: URL is still that of Web Explorer but Portal is redirected to Home page.
   4. Click Web Explorer link but cannot view Web Explorer content.
   5. Refresh (F5): no effect.
   6. Click any another item in the navigation bar. Then return to Web Explorer: ok now.

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
* Function or Class Name change: no

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validate on behalf of PM

Support Comment
* Support review:Patch validated

QA Feedbacks
*


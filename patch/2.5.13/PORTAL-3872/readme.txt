Summary

    * Status: W3C Validation: ID must begin with a letter
    * CCP Issue: CCP-668, Product Jira Issue: PORTAL-3872.
    * Fixes also: WCM-3002
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
* ID must begin with a letter, not a digit. 
  Step to reproduce in Portal standalone:
   1. Add new page
   2. Add a portlet to page (eg. Account Portlet)
   3. Validate this new page => ERROR: 
      <div class="MiddleDecoratorLeft">
        <div class="MiddleDecoratorRight">
          <div class="MiddleDecoratorCenter">
            <div id="25768119" style="width: 100%">
              <div class="PORTLET-FRAGMENT UIResizableBlock UIApplication" style="width: 100%; height: null;">
			          <div id="UIAccountPortlet-26425811" class="UIAccountPortlet">

Fix description

How is the problem fixed?

    * Add a prefix with letter before ID number.

Patch file: PORTAL-3872.patch

Tests to perform

Reproduction test

    * cf above

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

    * No

Is there a performance risk/cost?
    * The patch only affects new pages or pages which are edited after patch applied because the patch only changes how to generate ID to make W3C compliant, it doesn't either change how to display ID or change ID values that was stored in database before.

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Patch validated

QA Feedbacks
*


Summary

    * Status: Duplicate id "UICLVPortlet"
    * CCP Issue: CCP-505, Product Jira Issues: PORTAL-3865, WCM-2904.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
* Duplicate ID of UICLVPortlet in a page.

Fix description

How is the problem fixed?

    * Instead of using only portlet name, use both portlet name and hashcode to build portlet id

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PORTAL-3865.patch

Tests to perform

Reproduction test
* Step to reproduce:

   1. Add new page
   2. Add 2 Content List View portlets to page. Save
   3. Validate page by using W3C validator => ERROR

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
* Function or ClassName change: no

Is there a performance risk/cost?
* No
Validation (PM/Support/QA)

PM Comment
* validated on behalf of pm

Support Comment
* Support review : patch validated

QA Feedbacks
*


Summary

    * Status: UICLVPortlet: document type does not allow element "div"
    * CCP Issue: CCP-505, Product Jira Issue: PORTAL-3868.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
In HTML source of a page containing UICLVPortlet, W3C validator detects 1 error:

    * Document type does not allow element "div" here; missing one of "object", "ins", "del", "map", "button" start-tag.

Fix description

How is the problem fixed?

    * Avoid Document type invalidation, change <a> to <div> tag

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PORTAL-3868.patch

Tests to perform

Reproduction test

    * Steps to reproduce:

   1. Login as root
   2. Add new page AAA
   3. Add a Content List View to AAA -> Save
   4. Validate AAA by using W3C validator => ERROR.

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


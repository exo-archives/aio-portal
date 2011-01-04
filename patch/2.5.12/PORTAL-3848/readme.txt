Summary

    * Status: Should change message when delete navigation of portal
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3848.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * When deleting the navigation of portal, there is an ERROR pop-up message. This should be a WARNING.

Fix description

How is the problem fixed?

    * When deleting navigation (DeleteNavigationActionListener), change the ApplicationMessage type from ERROR to WARNING.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PORTAL-3848

Tests to perform

Reproduction test

    * cf above

Tests performed at DevLevel
* cf. above

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

    * No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Support review: patch validated

QA Feedbacks
*

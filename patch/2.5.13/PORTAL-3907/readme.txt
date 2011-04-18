Summary

    * Status: W3C validation: CSS errors
    * CCP Issue: CCP-785, Product Jira Issue: PORTAL-3907.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * CSS errors according to W3C validation.

Fix description

How is the problem fixed?

    * Change CSS classes to meet CSS3 level validation.
    * It remains however 5 errors concerning IE browsers that cannot be fixed (-moz-user-select, filter:alpha(opacity=30), word-wrap:word-break, zoom:1,and text-overflow: ellipsis).

Patch file: PORTAL-3907.patch

Tests to perform

Reproduction test
* Steps to reproduce:
   1. Go to http://localhost:8080/portal/private/classic.
   2. Validate CSS. We got many errors.

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
* Validated on behalf of PM

Support Comment
* Patch validated

QA Feedbacks
*

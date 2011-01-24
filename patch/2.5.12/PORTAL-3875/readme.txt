Summary

    * Status: Problem when searching page or user not existing in list by using ENTER key
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3875.
    * Complexity: minor

The Proposal
Problem description

What is the problem to fix?
* Problem when searching page or user not existing in list by using ENTER key

Fix description

How is the problem fixed?
* Change catching ENTER key

Patch file: PORTAL-3875.patch

Tests to perform

Reproduction test

   1. Go to Page Management (or User and group management)
   2. Input a name which does not exist
   3. Press Enter key
      => Always show 2 messages "No result found."

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
* Support review: patch validated

QA Feedbacks
*


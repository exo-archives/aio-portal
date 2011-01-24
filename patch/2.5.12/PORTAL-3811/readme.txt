Summary

    * Status: Always redirect to home page when URL contains non existing page
    * CCP Issue: CCP-433, Product Jira Issue: PORTAL-3811.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * In address bar put a name of page that does not exist in portal.
      => We are always redirect to the home page.

Fix description

How is the problem fixed?

    * Add new configuration "node.notfound" into portal-configuration.xml. User can enable this function by setting true to node.notfound configuration.

Patch file: PORTAL-3811.patch

Tests to perform

Reproduction test
* cf. above

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

    * None

Is there a performance risk/cost?
* No
Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM.

Support Comment
* Validated

QA Feedbacks
*

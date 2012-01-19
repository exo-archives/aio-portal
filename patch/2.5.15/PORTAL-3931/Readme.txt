Summary

    * Status: Display problems with RSS Reader Portlet
    * CCP Issue: CCP-1095, Product Jira Issue: PORTAL-3931.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
Steps to reproduce :

    * Create a page with RSS Reader Portlet
    * Add a category
    * Enter as URL http://rss.feedsportal.com/c/629/f/502199/index.rss and choose RSS as type
    * After saving the category, if the flow is long, you will view a pagination. Click the second page, display problems appear.

Fix description

How is the problem fixed?

    * In "RefsDecoder.java", in "decodeChars" function, it lacks an instruction assigning index to temp when end < 0.

Tests to perform

Reproduction test
* cf. above

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

    * Function or ClassName change: None

Is there a performance risk/cost?
* No
Validation (PM/Support/QA)

PM Comment

    * N/A

Support Comment

    * SL3VN review: Patch validated

QA Feedbacks
*


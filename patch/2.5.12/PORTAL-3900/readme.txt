Summary

    * Status: Page template names in French exceed border
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3900.
    * Complexity: trivial

The Proposal
Problem description

What is the problem to fix?
* At step 2 of Creating/Editing Page Wizard, page template names in French exceed the border.

Fix description

How is the problem fixed?
* Modify text in webui_fr.properties file to make it fit the box width.

Patch file: PORTAL-3900.patch

Tests to perform

Reproduction test: in Portal standalone
1. Login as administrators.
2. Change language to French
3. Create/Edit Page Wizard
4. Go to step 2
5. Select Mix Page Configs

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
* Patch Validated

Support Comment
* Patch validated

QA Feedbacks
*


Summary

    * Status: Lost status of running applications after logout/login in the first time
    * CCP Issue: CCP-479, Product Jira Issue: PORTAL-3922.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Lost status of running applications after logout/login in the first time

Fix description

How is the problem fixed?

    * Update status of running applications into database, instead of saving only in memory

Patch file: PORTAL-3922.patch

Tests to perform

Reproduction test
*Step to reproduce:
1. Login
2. Add new application to page, let this application run
3. Logout
4. Login again
5. The application is changed to status "Quit" (not running) --> error

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
*No

Configuration changes

Configuration changes:
*No

Will previous configuration continue to work?
*Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
*Rendering is lightweight slower during actions "show/hide" application because we need to spend time for saving status into database

Validation (PM/Support/QA)

PM Comment
* Patch validated on behalf of PM

Support Comment
* Patch validated

QA Feedbacks
*


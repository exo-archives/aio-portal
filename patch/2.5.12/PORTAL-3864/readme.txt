Summary

    * Status: Problem with the procedure "Forgot your User Name/Password?"
    * CCP Issue: CCP-648, Product Jira Issue: PORTAL-3864.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * When the number of users exceeds 20 users who are not in the top of the first 20 of list, they cannot use the procedure of "Forgot your User Name/Password?" and they have the message "Your email does not exist in eXo".

Problem analysis
The action listener responsible for sending mails to the user who has forgot her/his password does an erroneous check before sending the mail. It retrieves only the first page of user list and searches for the user having the desired mail within (the first page). That means if the rank of the current user (user list is alphabetical order) is greater than the size of the page (by default 20), this user will not be loaded in the first page (but in the nth page) and thus, the search operation will fail and we get the message "This email doesn't exist in eXo".
Fix description

How is the problem fixed?
The proposed patch ensures that when we look for the user having the desired mail, we iterate over all pages of users.

Patch information:
Patch files: PORTAL-3864.patch

Tests to perform

Reproduction test
*Cf. above

Tests performed at DevLevel
*Cf. above

Tests performed at QA/Support Level
*


Documentation changes

Documentation changes:
*None


Configuration changes

Configuration changes:
*None

Will previous configuration continue to work?
*yes


Risks and impacts

Can this bug fix have any side effects on current client projects?

    * NO

Is there a performance risk/cost?

    * No

Validation (PM/Support/QA)

PM Comment
* Validated by Dev team on behalf of PM

Support Comment
* Support review: patch validated

QA Feedbacks
*


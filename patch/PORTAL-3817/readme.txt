Summary

    * Status: Find user: Enter key press not catched
    * CCP Issue: CCP-464, Product Jira Issue : PORTAL-3817
    * Complexity: N/A
    * Impacted Client(s): Bull-Services and probably all.
    * Client expectations (date/content): N/A

The Proposal
Problem description

What is the problem to fix ?

    *  In search form, when user type text in text field and then press 'Enter' key, the page is reloaded and no search action is executed

Fix description

How the problem is fixed ?

    *  We implement a function to catch 'Enter' press event and execute a script command. In search form, set onkeypress for text field to handle 'Enter' key press

Patch informations:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files:
PORTAL-3817.patch


Tests to perform

Which test should have detect the issue ?
*

Is a test missing in the TestCase file ?
*No

Added UnitTest ?
*No

Recommended Performance test?
*No


Documentation changes

Where is the documentation for this feature ?
*None

Changes Needed:
*
Configuration changes

Is this bug changing the product configuration ?
*No

Describe configuration changes:
*

Previous configuration will continue to work?
*Yes


Risks and impacts

Is there a risk applying this bug fix ?
*No

Is this bug fix can have an impact on current client projects ?
*No

Is there a performance risk/cost?
*No


Validation By PM & Support

PM Comment
*

Support Comment
*
QA Feedbacks

Performed Tests
*


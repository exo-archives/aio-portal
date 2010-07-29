Summary

    * Status: Customise WebUI Mandatory Text Fields
    * CCP Issue: CCP-452, Product Jira Issue: PORTAL-3814
    * Complexity: APRISA RSA and probably all.
    
The Proposal
Problem description

What is the problem to fix?

    *  If user want to customize CSS style for * character, there is no way to control it

Fix description

How is the problem fixed?

    *  Add <span> tag to hold * character, and if user want to customize css style of * character, the simple way,  accessing span tag around the * character

Patch information:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files:
PORTAL-3814.patch


Tests to perform

Which test should have detect the issue?
*No

Is a test missing in the TestCase file?
*No

Added UnitTest?
*No

Recommended Performance test?
*No


Documentation changes

Where is the documentation for this feature?
*No

Changes Needed:
*No


Configuration changes

Is this bug changing the product configuration?
*No

Describe configuration changes:
*No

Previous configuration will continue to work?
*Yes


Risks and impacts

Is there a risk applying this bug fix?
*No

Can  this bug fix have an impact on current client projects?
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


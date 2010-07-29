Summary

    * Status: Plus ('+') symbol in an URL causes an ERROR 500
    * CCP Issue: CCPID, Product Jira Issue: PORTAL-3684
    * Complexity: N/A
    
The Proposal
Problem description

What is the problem to fix?

    *  Exception when user types a "+" symbol in URL

Fix description

How is the problem fixed?

    * Encode URL in full

Patch information:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files:
PORTAL-3684.patch

    * Properties

Tests to perform

Which test should have detect the issue?
*

Is a test missing in the TestCase file?
*NO

Added UnitTest?
*NO

Recommended Performance test?
*NO


Documentation changes

Where is the documentation for this feature?
*NO

Changes Needed:
*NO


Configuration changes

Is this bug changing the product configuration?
*NO

Describe configuration changes:
*NO

Will previous configuration continue to work?
*YES


Risks and impacts

Is there a risk applying this bug fix?
*NO

Can this bug fix have an impact on current client projects?
*NO

Is there a performance risk/cost?
*NO


Validation By PM & Support

PM Comment
*

Support Comment
*Patch validated

When we  type this Url http://localhost:8080/portal/private/classic/organization/newStaff/xxx+xxxx :=>Page displayed: OK and the exception never appear.
 
QA Feedbacks

Performed Tests
*


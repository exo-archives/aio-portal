Sumary

    * Status: Do not delete Portlet Preference in System Files when deleting portlets
    * CCP Issue: CCP-379, Product Jira Issue : PORTAL-3790
    * Complexity: HIGH
    * Impacted Client(s): APRIA RSA and probably all.
    * Client expectations (date/content): N/A

The Proposal
Problem description

What is the problem to fix ?

    *  

Fix description

How the problem is fixed ?

    * The PortletPreference is persistent after deleting the Portlet. 

Patch informations:

Patches files:
* File PORTAL3790-20100531.patch

Tests to perform

Which test should have detect the issue ?
*

Is a test missing in the TestCase file ?
*

Added UnitTest ?
*

Recommended Performance test?
*
Documentation changes

Where is the documentation for this feature ?
*

Changes Needed:
*
Configuration changes

Is this bug changing the product configuration ?
*

Describe configuration changes:
*

Previous configuration will continue to work?
*
Risks and impacts

Is there a risk applying this bug fix ?
*

Is this bug fix can have an impact on current client projects ?
*

Is there a performance risk/cost?
*
Validation By PM & Support

PM Comment
*

Support Comment
*
QA Feedbacks

Performed Tests
*


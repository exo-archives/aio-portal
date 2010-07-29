Summary

    * Status: ClientAbortException after importing new site
    * CCP Issue: CCP-389, Product Jira Issue : PORTAL-3806
    * Complexity:
    
The Proposal
Problem description

What is the problem to fix?

    * When importing a new site (content 20 Mo, content's version history 25Mo, navigation 15Ko) a ClientAbortException (ClientAbortException: java.net.SocketException: Software caused connection abort: socket write error) appears in the stack trace after index's creation (the site is imported with success): 

Fix description

How is the problem fixed?

    *  

Patch information:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files:
* File PORTAL-3806.patch

Tests to perform

Which test should have detect the issue?
* To test this kind of bug, you can do a request which would have a large response returned then abort the request right after as fast as possible (do not wait its response ).

Is a test missing in the TestCase file?
*

Added UnitTest?
*

Recommended Performance test?
*
Documentation changes

Where is the documentation for this feature?
*

Changes Needed:
*
Configuration changes

Is this bug changing the product configuration?
*

Describe configuration changes:
*

Previous configuration will continue to work?
*
Risks and impacts

Is there a risk applying this bug fix?
*

Can this bug fix have an impact on current client projects?
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


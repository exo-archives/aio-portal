Summary

    * Status: Managing tables in FCKEditor
    * CCP Issue: CCP-415, Product Jira Issue: PORTAL-3831
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * With IE6: Table properties popup window is showed with incorrect height.
    * With all browser : Table properties popup window reset height, background-color, border-color properties of table.

Fix description

How is the problem fixed?

    * Fix fck_table.html:
      - In IE6, DOM is loaded slow. 
        If we call DOM's methods too early, the popup window is showed with incorrect height. 
        We need to wait before calling any DOM's methods.
      - fck_table.html lacks JavaScript call to DOM's methods to get table properties

Patch information:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files: PORTAL-3831.patch; PORTAL-3831-sampleFCKPortlet.patch

Tests to perform

Tests performed at DevLevel?

    * Test with PORTAL standalone:
      
   1. Apply both PORTAL-3831.patch and PORTAL-3831-sampleFCKPortlet.patch.
   2. Build Portal.
   3. Copy exo.portal.sample.portlets-2.5.11-SNAPSHOT.war from the local repository to webapps/ folder.
   4. Start the server
   5. Login to portal
   6. Go to Administration/Application Registry
   7. Import "Sample FCK Portlet"
   8. Edit an existing page or create a new one.
   9. Add Sample FCK Portlet to this page.
  10. Add a table and edit its properties.

Tests performed at QA/Support Level?
*

Documentation changes

Documentation Changes:
* NO

Configuration changes

Configuration changes:
* No

Previous configuration will continue to work?
* Yes

Risks and impacts

Can this bug fix have an impact on current client projects?

    * No

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Patch validated. After testing the problem is gone with PORTAL standalone.

QA Feedbacks
*


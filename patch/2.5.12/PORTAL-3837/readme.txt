Summary

    * Status: Portlet Preference is still there after aborting to create a new page
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3837
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
Portlet Preference is still there after aborting to create a new page

Problem analysis
    * When creating or editing page, we can modify the portlet preferences. These changes will be saved directly to JCR. 
      And if we click abort, the created portlet preferences are not deleted.

Fix description

How is the problem fixed?
    * Create DeletePortletPreferences task. Each time a portlet is newly created, the Delete preferences task will be registered.
      If we click Abort, portal will execute registered tasks.
      If we click Finish button, the tasks will not be called.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PORTAL-3837.patch

Tests to perform

Reproduction test

   1. Login by admin
   2. Go to Groups/Application Registry. Auto Import portlet if this action has not been done yet.
   3. Go to Site Editor/Add page wizard
   4. Input Page name
   5. Add a portlet window associated with a preference (e.g: IFRAME portlet)
   6. Edit the added portlet
      In Edit form: Click Save to save the URL
   7. Click Close
   8. Click Abort to escape adding new page
   9. Go to Groups/Sites Explorer/System Files
  10. Select /exo:registry/exo:applications/MainPortalData/classic/portletPreferences => Still see Portlet's Portlet Preference

Tests performed at DevLevel
* Cf. above

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

    * Function or ClassName change:

Is there a performance risk/cost?
*

Validation (PM/Support/QA)

PM Comment
* Validated patch on behalf of PM

Support Comment
* Support review: patch validated

QA Feedbacks
*


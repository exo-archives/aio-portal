Summary

    * Status: Can't show page which selected after click Discard in Edit Site
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3842.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Select a page (e.g. Application Registry)
    * Choose Edit Site
    * Click Discard icon
    * Continue to select page above
      -> Don't show this page -> Still keep on homepage but URL show: http://localhost:8080/portal/private/classic/administration/applicationregistry
      (See attach file in PORTAL-3842)

Fix description

How is the problem fixed?

    * When clicking Abort or Rollback in Edit Site, we restore the UIPortal from the database. But we didn't set the selectedNode property of UIPortal.
      When portal comes back to normal mode, it automatically chooses the first node. but the URL of the browser is not changed. After that step, if we select the node that has the same URL with the current browser URL, UIPortalApplication will not throw the ChangePageNodeEvent --> can't view the page of that node.
    * So we need to set again the last selected node before portal comes back to normal mode.

Patch information:
Patch files: PORTAL-3842.patch

Tests to perform

Reproduction test
* Cf. above

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

    * No

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* patch validated onbehalf of PM

Support Comment
* Support review: patch validated

QA Feedbacks
*


Summary

    * Status: Don't support translating portlet's title by default for newly adding portlets
    * CCP Issue: CCP-479, Product Jira Issue: PORTAL-3920.
    * Needed for: WEBOS-336.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * In PORTAL-3893, we support an option: "show edited title" in portlet's edit mode, if the option is not checked, we will get portlet title from resource bundle for translation purpose.
      But WebOS doesn't have portlet's edit mode to change this option, so we can't translate title in WebOS.
      So, for newly added portlets, we should set this option 'true' by default to support translation title for both WebOS and Classic portals.

Fix description

How is the problem fixed?

    * For newly adding portlets, we set the option show-edited-title with 'true' value by default to support portlet title translation for both WebOS and Classic portals
    * For default portlets, everyone will have owner resource bundle file that contains key javax.portlet.title for translation

Patch file: PORTAL-3920.patch

Tests to perform

Reproduction test
* In WebOS, add Portal portlets (Account Portlet, Application Registry, Organization Portlet) to dockbar
* Change language to French
* These portlet titles aren't translated in French

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

    * Function or ClassName change: none

Is there a performance risk/cost?
* No
Validation (PM/Support/QA)

PM Comment
* Patch validated on behalf of PM

Support Comment
* Patch validated

QA Feedbacks
*


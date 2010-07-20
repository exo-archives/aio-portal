Summary

    * Status: Internationalization of navigation is applied only when user logout
    * CCP Issue: CCP-428, Product Jira Issue : PORTAL-3808
    * Complexity: HIGH
    * Impacted Client(s): AMG, spff, and probably all.
    * Client expectations (date/content): N/A

The Proposal
Problem description

What is the problem to fix ?

    * Any change of navigation internationalization is taken into effect once the user logouts and re-logins, or changes the language setting. The localized navigation is cached to improve performance. It is only refreshed when the session is renewed or the language is changed. 

Fix description

How the problem is fixed ?
   On persisting the L10n resource changes (via I18n portlet), there are two cases:

  1. Language of edited L10n resource is portal 's current language

      In this case, the navigation of portal would be relocalized. Hence, the labels on navigation nodes are updated

  2. Language of edited L10n resource is not portal 's current language

      In this case, we need not localize the navigation of portal. As to view the newly updated L10n resource, user needs to make a change of language. Which
    relocalize all navigation implicitely

Patch informations:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files:
PORTAL-3808.patch

    * Properties

Tests to perform

Which test should have detect the issue ?
*

Is a test missing in the TestCase file ?
*

Added UnitTest ?
* No

Recommended Performance test?
* Not need

Documentation changes

Where is the documentation for this feature ?
* none

Changes Needed:
* No

Configuration changes

Is this bug changing the product configuration ?
* No

Describe configuration changes:
* No

Previous configuration will continue to work?
* Yes


Risks and impacts

Is there a risk applying this bug fix ?
* No

Is this bug fix can have an impact on current client projects ?
* No

Is there a performance risk/cost?
* No
Validation By PM & Support

PM Comment
*

Support Comment
*
QA Feedbacks

Performed Tests
*


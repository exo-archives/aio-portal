Summary

    * Status: Create new OrganistationService for JAAS Module
    * CCP Issue: CCP-193, Product Jira Issue: PORTAL-3854.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

Using JAAS login module we notice that the portal navigation gets the logged user membership from the Organization service instead from IdentityRegistry.

Steps to reproduce :

   1. Use the attached customer login module, teste is a shared password for all users.
   2. Login as root/teste
      => We have access to the SiteAdmin and SiteExplorer portlets (the root already exists in the Organization Service).
   3. Login as test/teste => We haven't access to the SiteAdmin and SiteExplorer portlets => In the navigation menu, siteAdmin and siteExplorer menus does not exist.

Fix description

How is the problem fixed?

Change in getUserPortalConfig(String portalName, String accessUser) method of org.exoplatform.portal.config.UserPortalConfigService class.

This method previously retrieves groups to which belongs the current user just from the organization service and generates appropriate navigation for him. That is why, if the user does not exist in the organization service, the generated navigation is not correct. So, the fix for this problem is to retrieve groups of the authenticated user from the identity registry service instead of from the organization service to allow JAAS based user and group management. User navigation is therefore generated properly even if (s)he is a JAAS user.

Patch information:
Patch files: PORTAL-3854.patch

Tests to perform

Reproduction test

* Cf. above

Tests performed at DevLevel
* Cf. above

Tests performed at QA/Support Level
*


Documentation changes

Documentation changes:
* None


Configuration changes

Configuration changes:
* None

Will previous configuration continue to work?
* Yes


Risks and impacts

Can this bug fix have any side effects on current client projects?

    * No

Is there a performance risk/cost?
* No


Validation (PM/Support/QA)

PM Comment
*

Support Comment
* Support review : validated

QA Feedbacks
*


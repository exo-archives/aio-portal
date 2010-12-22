Summary

    * Status: Users authenticated using JAAS cannot add portlets to page
    * CCP Issue: CCP-193, Product Jira Issue: PORTAL-3862.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
When you try to create a new page using "Create page wizard" by any user who does not exist in organization service and who has been authenticated using JAAS custom module, you cannot add portlets to this page.

    * Prerequisites:
      To reproduce this issue you have to fix PORTAL-3854 first. Otherwise you cannot see the Control workspace arrow icon.

    * Steps to reproduce:

   1. Get DMS 2.5.7 (for example) deployed under Jboss server (we can reproduce the same bug in tomcat but steps 2. 3 will be little bit different).
   2. Place the attached files jboss-service.xml and login-config.xml under $JBOSS_HOME/server/default/deploy/exoplatform.sar/META-INF to enable JAAS
   3. Place exo-teste-lm.jar (Custom login modules using JAAS) from teste-exo-lm-bug.zip under $JBOSS_HOME/server/default/deploy/exoplatform.sar
   4. Start the server
   5. Type the URL of classic home page (eg: http://localhost:8080/portal/private/classic/) in your browser.
   6. Login using teste/teste
   7. Click Control workspace arrow icon on the left of the screen
   8. Go to eXo start | Administration | Basic | Create page wizard.
   9. Give a name to the page and click next.
      Click next another time. In the third screen of the wizard, you will notice that the portlet categories list box is empty despite "teste" has the necessary memberships in login-config.xml: Not ok.

Otherwise, if you login as root/teste (teste is a shared password for all users thanks to the custom login module), you can drag and drop portlets to the page because the user root exists in the organization service.
Fix description

How is the problem fixed?

This problem is due to the fact that Application Registry service bypasses IdentityRegistry and gets the authenticated user groups and memberships from Organization Service (instead of from IdentityRegistry). Thus, users who do not exist in Organization service and who are authenticated using JAAS cannot drag and drop portlets even if they have the necessary membership in JAAS Configuration.

So, we propose to change getApplicationCategories(String accessUser, String... appTypes) method in org.exoplatform.application.registry.jcr.ApplicationRegistryServiceImpl class.
The modification is to retrieve memberships of the authenticated user using IdentityRegistry Service instead of Organization Service to allow JAAS based authorization.

Patch information:
Patch files: PORTAL-3862.patch

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

    * Validated by PM

Support Comment

    * Support review: patch validated

QA Feedbacks
*
Labels parameters


Summary

    * Status: Add key javax.portlet.title to help translate title of default portlets
    * CCP Issue: CCP-479, Product Jira Issue: PORTAL-3909.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Some default portlets don't translate title, we should translate them by adding key javax.portlet.title into resource bundle

Fix description

How is the problem fixed?

    *  Add key javax.portlet.title into resource bundle file of default portlets as AccountPortlet, ApplicationRegistry, OrganizationPortlet, GadgetPortlet to help translate portlet title

Patch files: PORTAL-3909.patch

Tests to perform

Reproduction test
*Steps to reproduce:

1. Login
2. Create page wizard
3. Add Account Portlet, Application Registry, Organization Portlet (in exoadmin), Gadget Wrapper Porlet (in Dashboard) in the page
(If these portlets aren't displayed, go to Administration/Application Registry and click on Auto Import)
4. Change language to French
Titles of these porlets are still in English. They should be translated in French

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
*NO
Configuration changes

Configuration changes:
*NO

Will previous configuration continue to work?
*YES
Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
*NO
Validation (PM/Support/QA)

PM Comment
* PL review: Patch validated

Support Comment
* Support review: Patch validated

QA Feedbacks
*


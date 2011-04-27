Summary

    * Status: Translation of portlets' Tooltip
    * CCP Issue: CCP-479, Product Jira Issue: PORTAL-3893
    * Fixes also: WEBOS-336
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * The language of the portlet doesn't change => the portlet's title is hard coded in the portlet.xml.
    * The same problem exists for any portlet chosen from the page navigations: the language doesn't change => the portlet's title is hard coded in the page.xml

Fix description

How is the problem fixed?

    * Change priority for getting portlet's title in resource bundle file
    * Add some title key javax.portlet.title into portlet's resource bundle file
    * Add a new option "Show Edited title" in Portlet edit mode to help user choose types of title will be displayed: edited title or value from resource bundle

Patch files: PORTAL-3893.patch

Tests to perform

Reproduction test

   1. Add an application
   2. Change language
   3. Sign out
   4. Login

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
*YES, need to update how to use translated title of portlet because we have some change of User guide
Configuration changes

Configuration changes:
*NO

Will previous configuration continue to work?
*YES
Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change: None

Is there a performance risk/cost?
*NO
Validation (PM/Support/QA)

PM Comment
* PL review: Patch validated

Support Comment
* Support review: Patch validated

QA Feedbacks
*


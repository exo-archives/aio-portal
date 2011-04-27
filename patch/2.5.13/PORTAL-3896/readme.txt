Summary

    * Status: Broken Design on page wizard creation when tabs titles are too long
    * CCP Issue: CCP-656, Product Jira Issue: PORTAL-3896.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Broken Design on page wizard creation when tab titles are too long.

Fix description

How is the problem fixed?

    *  Fix static width for every tab

Patch files: PORTAL-3896.patch

Tests to perform

Reproduction test
* Steps to reproduce:
On AIO 1.6.x

   1. Go to "Site Editor" => "Add Page wizard"
   2. Add a new Page.
   3. At step 2. Select Tabs Page Configs and "Three tabs layout"
   4. At step 3. "Show container" to edit title of labels
      Edit the title of one/all tabs (like: title_of_tab_tooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo_long)
      Result: the design is broken

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
*No
Configuration changes

Configuration changes:
*No

Will previous configuration continue to work?
*Yes
Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
*No
Validation (PM/Support/QA)

PM Comment
* Validated

Support Comment
* Validated

QA Feedbacks
*


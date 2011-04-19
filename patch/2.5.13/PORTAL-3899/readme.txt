Summary

    * Status: Hard-coded title in UIFormMultiValueInputSet
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3899.
    * Fixes also: PORTAL-3910, KS-3007
    * Complexity: low

The Proposal
Problem description

What is the problem to fix?
* Two tool tips ("Add Item" and "Remove Item") are hard-coded in UIFormMultiValueInputSet.

Fix description

How is the problem fixed?
* In UIFormMultiValueInputSet.java, replace the hard-code titles by using values from resource bundle

Patch file: PORTAL-3899.patch

Tests to perform

Reproduction test
   1. Case 1 (KS-3007):
         1. Go to Forum
         2. Select a Forum (e.g Live demo)
         3. Select a Topic (e.g Demo data policy)
         4. In More actions, select Add a poll
            Tool-tips for Remove Item and Add Item are not translated in French
   2. Case 2 (PORTAL-3910):
      Upload a file in Files Explorer, put the mouse over the "plus" sign next to "ajouter une taxonomie" (language is French), "add item" appears not translated into
   3. Case 3:
      In File Explorer, click View Node Properties, then Add new property tab
      Tool-tip: "Add Item" is not translated in French
   4. Case 4:
      Upload a file in File Explorer, then Add metadata
      Tool-tip: "Add Item" is not translated in French
   5. Case 5:
      Go to: Site administration → Advanced Configuration → Create an Action → Type → Add
      Tool-tips: "Remove Item" and "Add Item" are not translated in French

Tests performed at DevLevel
* cf. above

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
* Can this bug fix have any side effects on current client projects?
    * Function or ClassName change: no

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Patch validated on behalf of PM

Support Comment
* Patch validated

QA Feedbacks
*

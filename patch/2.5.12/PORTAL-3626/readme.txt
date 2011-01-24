Summary

    * Status: Problem when move up/ move down node
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3626.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
    * Login as administrator
    * Go to Edit Page and navigation
    * Create new node "Test" by right clicking on navigation
    * Right click on "Test" node. Choose icon "Add new node" to create node "Test 1"
    * Right click on "Test 1" node--> Choose icon "Add new node" to create node "Test 2"
    * Save
    * Back again Tree when choose Edit Page and Navigation function
    * Cut node "Test 1" and Paste this node in the root navigation ("Test 1" hasn't got parent)
    * Click right "Test 1" and create "Test 3" node. Similarly, create "Test 4".
    * So: Test 2, Test 3, Test 4 are descendants of Test 1
    * Test 3, Test 4 can Move up and down but Test 2 can't move up/down.

The same issue occurs when copy and paste node.

Fix description

How is the problem fixed?

    * Change condition for checking node

Patch file: PORTAL-3626.patch

Tests to perform

Reproduction test
* cf. above

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

Can this bug fix have any side effects on current client projects?
* None

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM.

Support Comment
* Validated

QA Feedbacks
*

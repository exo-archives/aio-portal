Summary

    * Status: Impossible to paste node on navigation several times in the same location
    * CCP Issue: CCP-568, Product Jira Issue: PORTAL-3825.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * In the Edit portal navigation we need to add several times the same node in the same location. Currently we can't and a message appears tell us that there's an existing node with the same name.

Steps to reproduce:

    * Under ACME site, go to Edit Navigation,
    * Copy the News node.
    * Paste it under Newsletters -> News node exists now under Newsletters.
    * Try to copy the same node again under the same location (Newsletters) => A popup error appears tell us that this node name already exists.

Fix description

How is the problem fixed?

    * Support to Cut, Copy or Clone and then Paste a node, if that node name's existed. we add the postfix "_" + intNum. We don't allow to Cut and paste a node to itself. Portal will do nothing if we Cut and paste a node to the same parent node
    * Add a method to detects name conflicts and change the name : add the postfix "_1" (increase the number if need)

Patch information:
Patch files: PORTAL-3825.patch

Tests to perform

Reproduction test
* Follow the "how to reproduce steps"

Tests performed at DevLevel

   1. Copy one node several times.
   2. Delete some of these nodes
   3. Use the clone node functionality to clone again that node

Tests performed at QA/Support Level
* Same: tested also by the customer

Documentation changes

Documentation changes:
* Must add new entry in the documentation to describe this new feature DOC

Configuration changes

Configuration changes:
* nothing

Will previous configuration continue to work?
* nothing

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
*
Validation (PM/Support/QA)

PM Comment
*

Support Comment
*Support review:patch validated

QA Feedbacks
*


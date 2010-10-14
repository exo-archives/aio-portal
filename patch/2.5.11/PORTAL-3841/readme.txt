Summary

    * Status: Wrong default membership description in a special case
    * CCP Issue: N/A; Product Jira Issue: PORTAL-3841.
    * Complexity: Low

The Proposal
Problem description

What is the problem to fix?
When editing a membership, if its previous description is empty, the default description will be that of the previously selected membership.

Fix description

How is the problem fixed?

    * Set the MembershipType's description to "" (empty string) if it is null. When it's null, portal will not update its description TextArea.

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch files: PORTAL-3841.patch

Tests to perform

Reproduction test

    * Go to Community Management
    * Choose Membership Management
    * Click Add new membership (test) without description
    * Select to edit an existing membership (e.g: validator)
    * Select to edit the new membership (test)
      -> Still show the description of validator membership: NOK.

Tests performed at DevLevel
* Cf. above

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

    * Function or ClassName change: no.

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Support review: patch validated

QA Feedbacks
*


Summary

    * Status: Value of attribute "id" of containers fails in W3C validation test
    * CCP Issue: CCP-505, Product Jira Issue: PORTAL-3826. Needs for WCM-2864.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
    * To pass the W3C validation test, id and name attributes must begin with a letter, not a digit.
      The id auto-generated in UIContainer div is actually a digit:

      <div class="UIContainer" id="UIContainer-6226623" >
        <div class="VIEW-CONTAINER">
          <div id="6226623">
      	<div class="UIRowContainer">
      	    <div id="6226623">

      These id's should begin with a letter, for example, VIEW-CONTAINER-6226623 and UIRowContainer-6226623, respectively.

Fix description

How is the problem fixed?

    * Add uid_ to the prefix of UI object identifier (such as id of UIContainer, UIGadget, etc)

Patch file: PORTAL-3826.patch

Tests to perform

Reproduction test
* See html or run W3C validation tests

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

    * Function or ClassName change: no

Is there a performance risk/cost?
*NO

Validation (PM/Support/QA)

PM Comment

    * Validated on behalf of PM

Support Comment

    * Support Patch review : validated

QA Feedbacks
*


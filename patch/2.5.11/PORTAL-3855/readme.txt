Summary

    * Status: A drag & drop bug with JavaScript in IE
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3855.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * How to reproduce the problem:
         1. In IE browser, click on a container/portlet from User Workspace to drag it out of the browser window and drop to the right area.
         2. Move the mouse into the browser window to continue dragging and click to finish.
            Observation: the dragged object is not released from the mouse.

Fix description

How is the problem fixed?

    * Fix in PortalDragDrop.js. Currently we check if Dragdrop object has already initialized in PortalDragDrop.init(). This check causes however the problem with object that is dragged and dropped from the outside.
    * Modify the checking condition: no need to check if the current dragObject is also the object that has triggered the drag event.

Patch files: PORTAL-3855.patch

Tests to perform

Reproduction test
* Cf. above

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
* Support review: validated

QA Feedbacks
*


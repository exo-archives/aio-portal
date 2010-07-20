Summary

    * Status: Cannot load upload form and FCK in edit mode of portlet
    * CCP Issue: none, Product Jira Issue: PORTAL-3717
    * Complexity: N/A
    * Impacted Client(s): N/A
    * Client expectations (date/content): N/A

The Proposal
Problem description

What is the problem to fix ?

    *  eXo.webui.UIUpload.initUploadEntry(..) do not work because HTML elements have not rendered yet when execute eXo.webui.UIUpload.initUploadEntry(..)

Fix description

How the problem is fixed ?

    * Execute eXo.webui.UIUpload.initUploadEntry(..) after all HTML elements are rendered
    * Replace "rcontext.getJavascriptManager().addJavascript" by "rcontext.getJavascriptManager().addCustomizedOnLoadScript"

Patch informations:

    * Final files to use should be attached to this page (Jira is for the dicussion)

Patches files:
PORTAL-3717.patch

Tests to perform

Which test should have detect the issue ?
*

Is a test missing in the TestCase file ?
*

Added UnitTest ?
*No

Recommended Performance test?
*No


Documentation changes

Where is the documentation for this feature ?
*None

Changes Needed:
*Not need


Configuration changes

Is this bug changing the product configuration ?
*No

Describe configuration changes:
*No

Previous configuration will continue to work?
*Yes


Risks and impacts

Is there a risk applying this bug fix ?
*No

Is this bug fix can have an impact on current client projects ?
*No

Is there a performance risk/cost?
*No


Validation By PM & Support

PM Comment
*

Support Comment
*Validated by Support Team


QA Feedbacks

Performed Tests
*


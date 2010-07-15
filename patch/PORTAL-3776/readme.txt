Summary

    * Status: Problem with popup save-success after saving portlet CLV
    * CCP Issue: CCP-372, Product Jira Issue: PORTAL-3776
    * Complexity: HIGH
    * Impacted Client(s): Bull-Services
    * Client expectations (date/content): N/A

The Proposal
Problem description

What is the problem to fix ?

    * All contents of Porlets, page, containers are rendered, and use JavaScript to hide/display them in each mode ( edit page, edit portal, view normal ...)
    * Unfortunately, it causes some problems :

            Make D&D performance down a lot
            May conflict in updating UI components by AJAX while we are editting a portlet which has portlet EDIT mode
Fix description

How the problem is fixed ?

    *  Do not render content of portlets while editting layout
    * Use an action in server side to show the preview of layout instead of using javascript

Patch informations:

    * Final files to use should be attached to this page (Jira is for the dicussion)



Patches files:
* PORTAL-3776.patch



Tests to perform
Which test should have detect the issue ?
*

Is a test missing in the TestCase file ?
*

Added UnitTest ?
* No

Recommended Performance test?
* No


Documentation changes
Where is the documentation for this feature ?
* None

Changes Needed:
* Not need



Configuration changes
Is this bug changing the product configuration ?
* No

Describe configuration changes:
* Nothing

Previous configuration will continue to work?
* Yes


Risks and impacts
Is there a risk applying this bug fix ?
* No

Is this bug fix can have an impact on current client projects ?
* No

Is there a performance risk/cost?
* No


Validation By PM & Support
PM Comment
*

Support Comment
* Validated by Support Team


QA Feedbacks
Performed Tests
*


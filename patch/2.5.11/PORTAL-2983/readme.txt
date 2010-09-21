Summary

    * Status: Resizing containers in portal pages doesn't work correctly
    * CCP Issue: N/A, Product Jira Issue: PORTAL-2983
    * Complexity: LOW

The Proposal
Problem description

What is the problem to fix?

    * It doesn't work when setting width for each column in a table container (when editing or creating a new page)

Fix description

How is the problem fixed?

    * Set column container's template as UIColumnContainer instead of UIContainer
    * Each column width will be the width of the container which it contains
    * Change container's template when it drags and drops out of Table container

Patch information:
Patches files:
PORTAL-2983.patch

Tests to perform

Which test should have detected the issue?

   1. Create a new page
   2. Select Row based container
   3. First container enter 30%, and second one 70%
      Bug: instead of having 2 columns of 30/70, the portlet is taking 30% of the first container, the second one 70% of the container (and each container takes 50% of the page).

Is a test missing in the TestCase file?
* NO

Added UnitTest?
* NO

Recommended Performance test?
* NO


Documentation changes

Where is the documentation for this feature?
* NONE

Changes Needed:
* NONE


Configuration changes

Is this bug changing the product configuration?
* YES

Describe configuration changes:

    * Set default template as UIColumnContainer instead of UIContainer (ContainerConfigOption.groovy)
    * Change page layout's template

Will previous configuration continue to work?
*


Risks and impacts

Is there a risk applying this bug fix?
* To check in WCM-2903 if there's a regression in WCM/AIO due to changes in container templates.

Can this bug fix have any side effects on current client projects?
* NO

Is there a performance risk/cost?
* NO


Validation By PM & Support

PM Comment

    * Validated by TL on behalf of PM.

Support Comment

    * Support Patch review: validated

QA Feedbacks

Performed Tests
*


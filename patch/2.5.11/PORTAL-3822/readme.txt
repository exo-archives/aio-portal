Summary

    * Status: Metadata: date field is reset after clicking on "+" to add a new value in another field
    * CCP Issue: CCP-523, Product Jira Issue: PORTAL-3822
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * UIFormDateTimeInput date field is reset after clicking on "+" to add a new value in another field.

Fix description

How is the problem fixed?

    * In decode, if input is null, reset value for date of UIFormDateTimeInput

Patch information:
Patches files:
PORTAL-3822.patch

Tests to perform

Which test should have detect the issue?
*

Is a test missing in the TestCase file?
*

Added UnitTest?

    * No

Recommended Performance test?

    * No

Documentation changes

Where is the documentation for this feature?

    * None

Changes Needed:

    * No

Configuration changes

Is this bug changing the product configuration?

    * No

Describe configuration changes:

    * No

Previous configuration will continue to work?

    * Yes

Risks and impacts

Is there a risk applying this bug fix?

    * No

Can this bug fix have an impact on current client projects?

    * No

Is there a performance risk/cost?

    * No

Validation By PM & Support

PM Comment

    * Validated by Trong

Support Comment

    * Validated patch by Support Team

QA Feedbacks

Performed Tests
*


Summary

    * Status: Add service to support reading global configurable parameters for portal
    * CCP Issue: Product Jira Issue: PORTAL-3895.
    * Complexity: N/A

The Proposals
Problem description

What is the problem to fix?

    * PORTAL-3811 and PORTAL-3815 need to have configurable parameters, so we should create a new service (called  GlobalPortalConfigService) to read these parameters. First phase, we only support from initParams of service.

Fix description

How is the problem fixed?

    * Add new configuration into portal-configuration.xml to support config time format, node not found.

Patch file: PORTAL-3895.patch

Tests to perform

Tests performed at DevLevel
* After applying the patch, build successful the project.
* Apply also the patches of PORTAL-3811 and PORTAL-3815, these issues cannot be reproduced. 

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
*No

Configuration changes

Configuration changes:
* Add new configuration into portal-configuration.xml

Will previous configuration continue to work?
* Yes, the new configuration is only necessary for PORTAL-3811 and PORTAL-3815.

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * None

Is there a performance risk/cost?
*No

Validation (PM/Support/QA)

PM Comment
* Validated

Support Comment
* Validated

QA Feedbacks
*


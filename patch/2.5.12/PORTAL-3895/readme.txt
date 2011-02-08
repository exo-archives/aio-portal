Summary

    * Status: Add service to support reading global configurable parameters for portal
    * CCP Issue: Product Jira Issue: PORTAL-3895.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * In PORTAL-3815, PORTAL-3871. All need to have configurable parameters, so we should create new service (call: GlobalPortalConfigService) to help read these parameters. First phase, we only support from initParams of service

Fix description

How is the problem fixed?

    * Add new configuration into portal-configuration.xml to support config time format, node not found and user name case-insensitive

Patch information:
Patch files: PORTAL-3895.patch


Tests to perform

Reproduction test
*

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*


Documentation changes

Documentation changes:
*No


Configuration changes

Configuration changes:
*Add new configuration into portal-configuration.xml

<component>
<key>org.exoplatform.portal.config.GlobalPortalConfigService</key>

<type>org.exoplatform.portal.config.GlobalPortalConfigService</type>

<init-params>

<value-param>

<name>time.24h.format</name>

<description>Default is 12h format. If value is true, 24h format will be apply for time of portal </description>

<value>false</value>

</value-param>

</init-params>

</component>

Will previous configuration continue to work?
*


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


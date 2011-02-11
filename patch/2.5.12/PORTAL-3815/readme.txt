Summary

    * Status: Time's format of eXoStart
    * CCP Issue: CCP-456, Product Jira Issue: PORTAL-3815
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * In User workspace, next to eXo start button, there is a time display. Customer wants to display time with 24h format.

Fix description

How is the problem fixed?

    * Add configuration of 24h time format in portal-configuration.xml

Patch information:
Patch files: PORTAL-3815.patch


Tests to perform

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:
* Add information of this new parameter in Admin guide.


Configuration changes

Configuration changes:
* A new value is added into portal-configuration.xml file.

Will previous configuration continue to work?
* This issue depends on PORTAL-3895 (add GlobalConfigService class)
* Any module/product using UIExoStart.gtmpl (eg. DMS) must add GlobalPortalConfigService configuration in portal-configuration.xml (as ECM-5576).


Risks and impacts

Can this bug fix have any side effects on current client projects?
* See Configuration changes.

Is there a performance risk/cost?
* No


Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM.

Support Comment
* Validated

QA Feedbacks
*


Summary

    * Status: Impossible to login
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3812
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
Environment: FF 3.6.3 Fr, Windows XP SP3
When trying to login to AIO 1.6.5 with browser in French, this exception is thrown:

[ERROR] resources - Error: locale.portal.webui_fr <java.lang.NullPointerException>java.lang.NullPointerException
at org.exoplatform.services.resources.jcr.DataMapper.getDataValue(DataMapper.java:97)
at org.exoplatform.services.resources.jcr.DataMapper.toResourceBundleData(DataMapper.java:68)
at org.exoplatform.services.resources.jcr.ResourceBundleServiceImpl.getResourceBundleData(ResourceBundleServiceImpl.java: 92)
...

Fix description

How is the problem fixed?

    * Using UTF-8/BOM encoding.

Patch information:
Patches files:
webui_fr.properties

Tests to perform

Reproduction test
Login to AIO 1.6.5 with browser in French.

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*


Documentation changes

Documentation changes:
*


Configuration changes

Configuration changes:
*

Will previous configuration continue to work?
*


Risks and impacts

Can this bug fix have an impact on current client projects?

    * Function or ClassName change?

Is there a performance risk/cost?
*


Validation (PM/Support/QA)

PM Comment
*

Support Comment
* Support patch review 20100729: tested and validated

QA Feedbacks
*


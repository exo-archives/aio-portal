Summary

    * Status: Labels are not translated in French
    * CCP Issue: CCP-470, Product Jira Issue: PORTAL-3829.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Some labels are not translated into French.

Fix description

How is the problem fixed?

    * Translate labels to French in the following resources files:
          o ApplicationRegistryPortlet_fr.properties
          o expression_fr.properties
          o BrowserPortlet_fr.properties
          o GroovyPortlet_fr.properties
          o NavigationPortlet_fr.properties
          o ConsolePortlet_fr.properties
          o I18nPortlet_fr.properties
          o OrganizationPortlet_fr.properties
          o webui_fr.properties
          o MessageResource_fr.js

    * Remove hard code in the following templates
          o UIFormInputIconSelector.gtmpl
          o UIPortletInfo.gtmpl
          o UIWizardPageSelectLayoutForm.gtmpl
          o UIGadget.js

    * Get the display name of the current locale instead of that of the default locale
          o UIUserProfileInputSet.java

Patch information:

    * Final files to use should be attached to this page (Jira is for the discussion)

Patch file: PORTAL-3829.patch

Tests to perform

Reproduction test

    * Review all labels in AIO 1.6.6

Tests performed at DevLevel

    * Cf. above

Tests performed at QA/Support Level
*

Documentation changes

Documentation changes:

    * NO

Configuration changes

Configuration changes:

    * NO

Will previous configuration continue to work?

    * YES

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * NO

Is there a performance risk/cost?

    * NO

Validation (PM/Support/QA)

PM Comment
* Patch validated on behalf of PM

Support Comment
* Support review: patch validated

QA Feedbacks
*


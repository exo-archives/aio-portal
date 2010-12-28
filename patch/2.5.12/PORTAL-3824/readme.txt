Summary

    * Status: Modify the max value of StringLengthValidator in UIPortletForm
    * CCP Issue: CCP-504, Product Jira Issue: PORTAL-3824
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

Currently in UIPortletForm the tile cannot exceed 60 characters.
in UIPortletForm.java we have:

UIFormInputSet uiSettingSet = new UIFormInputSet("PortletSetting");
  	uiSettingSet.
      addUIFormInput(new UIFormStringInput("id", "id", null).
                     addValidator(MandatoryValidator.class).setEditable(false)).
      addUIFormInput(new UIFormStringInput("windowId", "windowId", null).setEditable(false)).
    	addUIFormInput(new UIFormStringInput("title", "title", null).
                     addValidator(StringLengthValidator.class, 3, 60))

The max value is 60, we want to increase this value to 100
Fix description

How is the problem fixed?

    * Remove min-length and max-length limitation of portlet title. In case of empty title, the portlet display name is used.

Patch information:
Patch files: PORTAL-3824.patch

Tests to perform

Tests performed at DevLevel?
*No

Tests performed at QA/Support Level?
*No

Documentation changes

Documentation Changes:
*No

Configuration changes

Configuration changes:
*No

Will previous configuration continue to work?
*Yes

Risks and impacts

Can this bug fix have an impact on current client projects?

    * Function or ClassName change? No

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
*Validate the patch on behalf of PM

Support Comment
* Validated by Support

QA Feedbacks
*


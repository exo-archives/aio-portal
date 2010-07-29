Summary

    * Status: java.lang.NullPointerException in UserACL for ANONYMOUS users
    * CCP Issue: CCP-461, Product Jira Issue : PORTAL-3816
    * Complexity: LOW

The Proposal
Problem description

What is the problem to fix?

    * java.lang.NullPointerException in function org.exoplatform.portal.config.UserACL.hasPermission(Page, String) 

      if(PortalConfig.USER_TYPE.equals(page.getOwnerType())){
            if(remoteUser.equals(page.getOwnerId())){
              page.setModifiable(true);
              return true;
            }
            return false;
          }

      exception happen if remoteUser=null (in line " if(remoteUser.equals(page.getOwnerId()))")

Fix description

How the problem is fixed?

    * We replace " if(remoteUser.equals(page.getOwnerId())) " by " if(page.getOwnerId().equals(remoteUser)) "

Patch information:

    * Final files to use should be attached to this page (Jira is for the dicussion)
 	
File PORTAL3816-20100615.patch 	

    * Properties

Tests to perform

Which test should have detect the issue?
* 

Is a test missing in the TestCase file?
* 

Added UnitTest?
*No

Recommended Performance test?
*No
Documentation changes

Where is the documentation for this feature?
*None

Changes Needed:
*Nothing
Configuration changes

Is this bug changing the product configuration?
*No

Describe configuration changes:
*No

Previous configuration will continue to work?
*Yes
Risks and impacts

Is there a risk applying this bug fix?
*No

Can this bug fix have an impact on current client projects?
*No

Is there a performance risk/cost?
*No
Validation By PM & Support

PM Comment
*

Support Comment
*
QA Feedbacks

Performed Tests
*


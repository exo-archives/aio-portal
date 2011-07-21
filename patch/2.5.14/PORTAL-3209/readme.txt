Summary

    * Status: Upload icon not visible on Firefox 3 (Ubuntu)
    * CCP Issue: N/A, Product Jira Issue: PORTAL-3209.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Upload icon not visible on Firefox 3 (Ubuntu)

Fix description

How is the problem fixed?
Provide a better implementation of upload component to allow customized UI:

    * Move input file element out of IFrame
    * When click upload event, the Form element appends the input file and submit
    * IFrame will be invisible

Patch file: PORTAL-3209.patch

Tests to perform

Reproduction test
Case 1: CS/Calendar

   1. Right click on a calendar
   2. Click Import
   3. The Calendar form is shown to select and import ics file.
   4. However,the upload icon is not displayed top upload the selected file

Case 2: CS/Mail

   1. Click on Contact.
   2. Click on Add account
   3. Fill valid information in all fields
   4. Click on "Change" to change Avatar picture >> Missing upload icon

Case 3: DMS/ECM Administration

   1. Go to ECM Administration -> Types Of Content -> Manage Node Type
   2. Click to the Import button
   3. "Import NodeType from XML file" form is displayed. The upload icon is not displayed on the screen.

Case 4: DMS/File Explorer:

   1. Missing upload icon when upload file in File Exoplorer

Case 5: KS/Forum on IE7

   1. Select 1 category
   2. Click on Manage Category/ Import forum
   3. Import forum form is shown but can not browse file

Tests performed at DevLevel
*

Tests performed at QA/Support Level
*
Documentation changes

Documentation changes:
* No
Configuration changes

Configuration changes:
* No

Will previous configuration continue to work?
* Yes

Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change: no
    * Maybe some problem if a product overrides the template/js of the upload component.
      In case of AIO 1.6.x, the workaround in WCM-1552 must be reverted (in WCM-3029).

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated on behalf of PM

Support Comment
* Patch validated

QA Feedbacks
*

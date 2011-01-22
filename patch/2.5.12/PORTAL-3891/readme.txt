Summary

    * Status: Using an email link with Internet Explorer opens a strange page
    * CCP Issue: CCP-730, Product Jira Issue: PORTAL-3891.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?
Create a text, insert an email link into this text:
* In FCK Editor use "create/modify link" button,
* Select "email link",
* Type in the target an email address.
* Publish text and insert it into a page.
  Using Firefox, display this page and click on email link:
  => all works well (email application windows is displayed with right email address).
  Using Internet Explorer, display this page and click on email link:
  => link works well (email application windows is displayed with right email address).
  However when returning to IE, the web page (containing text with email link) has been replaced by a blank page containing only the "mailto:xxx". The user has to use "previous web page" link to go back to Portal .

Fix description

How is the problem fixed?
* Analysis: 
  FCK editor generates <a> tag with href attribute that executes javascript to invoke email client.
  In IE7, when the email link is clicked, the js snippet is executed and returns a non null value. The browser will load a new page with that value. 
* Solution: modify that js snippet to make it return null value (use void function)

Patch file: PORTAL-3891.patch

Tests to perform

Reproduction test
* cf. above

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
* No

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Patch validated on behalf of PM.

Support Comment
* Validated

QA Feedbacks
*

Summary

    * Status: Problem with CAS authentication:portal displays "Loading" popup, and request doesn't success in private mode
    * CCP Issue: CCP-729, Product Jira Issue: PORTAL-3889.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * Configure a CAS server under one host.
    * In other host, configure eXo server as CAS client.
    * Under acme site add a page 'Test' that contains Account portlet (http://localhost:8080/portal/private/acme/Test).
    * Disconnect from CAS.
    * Reconnect and access to the Test page again then fill and validate =>
      Results: portal displays "Loading" popup, and request doesn't success.

Fix description

How is the problem fixed?
* Fix in PortalStateManager.java: only clean the session if state.getUserName() != null. 
  That means the session won't be cleaned if user is redirecting the private from the public page. 
  Otherwise the session is cleaned normally.

Patch file: PORTAL-3889.patch

Tests to perform

Reproduction test
* Cf. above

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
* No
Risks and impacts

Can this bug fix have any side effects on current client projects?

    * Function or ClassName change

Is there a performance risk/cost?
* No
Validation (PM/Support/QA)

PM Comment
* Patch validated on behalf of PM.

Support Comment
* Patch Validated

QA Feedbacks
*

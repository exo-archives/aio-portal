Summary

    * Status: The If-Modified-Since property in the HTTP header doesn't exist
    * CCP Issue: CCP-528, Product Jira Issue: PORTAL-3828.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * If-Modified-Since property in the HTTP header is missing for cached resources (images/css,js..).

Fix description

How is the problem fixed?

    * If-Modified-Since element is added to Http request by Browser if the last response for the resource have Last-Modified element. Last-Modified element is added by Application Server if the AS can handle the resource directly (retrieve it from the file system, and write it to response stream).
    * But some of our resources, for example : merged.js, some css file... is cached in server's memory and when there is a request from client, the service (SkinService, JavascriptConfigService...) will write the resource's binary to response stream --> we need to add the Last-Modified header element here

Patch information:
Patch files: PORTAL-3828.patch

Tests to perform

Reproduction test

    * cf above.

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
*

Support Comment

    * Patch validated by Support Team

QA Feedbacks
*


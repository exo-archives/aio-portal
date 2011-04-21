Summary

    * Status: Problem with removed remote gadgets from Application Registry
    * CCP Issue: CCP-738, Product Jira Issue: PORTAL-3892.
    * Complexity: N/A

The Proposal
Problem description

What is the problem to fix?

    * After deleting a remote gadget from the admin app registry, the gadget is not removed from the "Dashboard Workspace" of the "Dashboard portlet".

Fix description

How is the problem fixed?

    * During removing gadget, need to remove it from both Gadget Registry and Application Registry spaces
    * Remain gadget in Dashboard portlet and display an error message in Body of gadget

Patch file: PORTAL-3892.patch

Tests to perform

Reproduction test
* Steps to reproduce:
    * Go to Administration -> Application Registry.
    * Go to Gadget -> Add a remote gadget and add a remote gadget URL (for example:http://adwebmaster.net/datetimemulti/datetimemulti.xml).
    * Go to Organize then click Auto Import.
    * Go to the Dashboard page.
    * Click Add Gadgets and add the remote gadget to the Dashboard.
    * Go to Administration -> Application Registry -> Gadget
    * Delete the remote gadget then Auto Import.
    * Go to the Dashboard page -> Add Gadgets: the remote Gadget appear in the Dashboard Workspace and we get the following exception:

            [ERROR] portal:Lifecycle - template : system:/groovy/portal/webui/application/UIGadget.gtmpl   <java.lang.NullPointerException>java.lang.NullPointerException
                    at org.exoplatform.portal.webui.application.GadgetUtil.toGadgetApplication(GadgetUtil.java:48)
                    at org.exoplatform.portal.webui.application.UIGadget.getApplication(UIGadget.java:211)
                    at org.exoplatform.portal.webui.application.UIGadget.getUrl(UIGadget.java:223)
                    at sun.reflect.GeneratedMethodAccessor174.invoke(Unknown Source)
                    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
                    at java.lang.reflect.Method.invoke(Method.java:592)...
                    at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:86)...       

Tests performed at DevLevel
* c.f. above

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

    * Function or ClassName change

Is there a performance risk/cost?
* No

Validation (PM/Support/QA)

PM Comment
* Validated

Support Comment
* Patch validated

QA Feedbacks
*

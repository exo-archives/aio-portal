repos=$HOME/Dev/portal-modules-repos/common/1.2.3/lib
thirdparty=$HOME/Dev/jboss-portal-2.7/thirdparty/jboss-portal/modules/common/lib/

echo "Copies current version of common libraries either to local repository copy or Portal thirdparty to test or release purpose"
echo "Usage: '$0' to release to Portal thirdparty, '$0 repos' to release to repository local copy"
echo "Set 'repos' variable to the snapshot lib directory for the common module of your local repository copy"
echo "Set 'thirdparty' variable to the lib directory for the common module of your local JBoss Portal 2.7 thirparty directory"
echo ""
echo "repos currently set at: $repos"
echo "thirdparty currently set at: $thirdparty"
echo ""

if [[ $1 == "repos" ]]; then
  loc=$repos
  echo "Releasing to repository. Don't forget to update component-info.xml with revision number."
elif [[ $1 == "usage" ]]; then
  echo "Usage shown, nothing was done"
  exit 0
else
  loc=$thirdparty
  echo "Releasing to Portal thirdparty"
fi


cp common/target/common-common-1.2.3.jar $loc/portal-common-lib.jar
cp mc/target/common-mc-1.2.3.jar $loc/portal-common-mc-lib.jar
cp portal/target/common-portal-1.2.3.jar $loc/portal-common-portal-lib.jar

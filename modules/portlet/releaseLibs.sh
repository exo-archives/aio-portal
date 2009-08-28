repos=/home/theute/Development/Repo/repository.jboss.org/jboss-portal/modules/portlet/2.0.8/lib
thirdparty=$HOME/Dev/jboss-portal-2.7/thirdparty/jboss-portal/modules/portlet/lib

echo "Copies current version of portlet libraries either to local repository copy or Portal thirdparty to test or release purpose"
echo "Usage: '$0' to release to Portal thirdparty, '$0 repos' to release to repository local copy"
echo "Set 'repos' variable to the snapshot lib directory for the portlet module of your local repository copy"
echo "Set 'thirdparty' variable to the lib directory for the portlet module of your local JBoss Portal 2.7 thirparty directory"
echo ""
echo "repos currently set at: $repos"
echo "thirdparty currently set at: $thirdparty"
echo ""

if [[ $1 == "repos" ]]; then
  loc=$repos
  echo "Releasing to repository. Don't forget to update component-info.xml with revision number."
elif [[ $1 == "maven" ]]; then
  echo "Releasing to Maven repository. You will need to have your credentials in settings.xml."
  mvn deploy -Djboss.repository.root=https://snapshots.jboss.org/maven2/
  exit 0
elif [[ $1 == "usage" ]]; then
  echo "Usage shown, nothing was done"
  exit 0
else
  loc=$thirdparty
  echo "Releasing to Portal thirdparty"
fi

cp bridge/target/portlet-bridge-2.0.8.jar $loc/portal-portlet-bridge-lib.jar
cp bridge/target/portlet-bridge-2.0.8-sources.jar $loc/portal-portlet-bridge-lib-sources.jar
cp controller/target/portlet-controller-2.0.8.jar $loc/portal-portlet-controller-lib.jar
cp controller/target/portlet-controller-2.0.8-sources.jar $loc/portal-portlet-controller-lib-sources.jar
cp federation/target/portlet-federation-2.0.8.jar $loc/portal-portlet-federation-lib.jar
cp federation/target/portlet-federation-2.0.8-sources.jar $loc/portal-portlet-federation-lib-sources.jar
cp management/target/portlet-management-2.0.8.jar $loc/portal-portlet-management-lib.jar
cp management/target/portlet-management-2.0.8-sources.jar $loc/portal-portlet-management-lib-sources.jar
cp portlet/target/portlet-portlet-2.0.8.jar $loc/portal-portlet-lib.jar
cp portlet/target/portlet-portlet-2.0.8-sources.jar $loc/portal-portlet-lib-sources.jar
cp samples/target/portlet-samples-2.0.8.jar $loc/portal-portlet-samples-lib.jar
cp samples/target/portlet-samples-2.0.8-sources.jar $loc/portal-portlet-samples-lib-sources.jar



Work in progress with the simplification of exobuild

In order to avoid exobuild to fetch binaries in maven repositories by himself, this pom.xml declares the missing dependencies and mvn dependency:tree will ask maven to do the job
(M2_REMOTE_REPOS will only have to contain the local repo in exobuild conf)
1/ In tools/packaging dir:
** mvn dependency:tree (or dependency:go-offline)
** exobuild --product=portal --verions=integration --deploy=tomcat

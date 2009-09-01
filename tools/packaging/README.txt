Portal packaging tooling:
* This pom embbed and autorun and exobuild
* http://svn.exoplatform.org/projects/utils/exopackage/tags/1.0.0
* http://svn.exoplatform.org/projects/utils/exopackage-conf/tags/20090901 

Process:
1/ The exo-tomcat or jboss packaging is done during the "mvn install" process of the "packaging" module
2/ exopackage-1.0.0 is downloaded from maven
2/ exopackage-conf-20090901 is downloaded from maven, and contains official modules for exoproducts
4/ product and module for this version are stored in the current project, and find by exopackage during the run
5/ a maven-exec-plugin calls exobuild from maven with the right parameters
6/ the delivery is in the target directory


*****************
* CONFIGURATION:
*****************

* in your settings.xml file (maven local computer configuration) you should at least add this
    <profile>
      <id>exo-projects</id>
      <activation><activeByDefault>true</activeByDefault></activation>
      <properties>
        <exo.projects.directory.dependencies>c:/ExoPlatform/src/exo-dependencies</exo.projects.directory.dependencies> 
        <exo.projects.app.tomcat.version>tomcat-6.0.16</exo.projects.app.tomcat.version>
        <exo.projects.app.jboss.version>jboss-4.2.0.GA</exo.projects.app.jboss.version> 
      </properties>
    </profile>
* exo.projects.directory.dependencies should contain :
** ${exo.projects.app.tomcat.version}/ a Tomcat clean installation, to be used as packaging template
** ${exo.projects.app.jboss.version}/ a JBoss clean installation, to be used as packaging template

*****************
* PACKAGING:
*****************

* mvn install
** Create a Tomcat delivery in target\working\exo-tomcat\
* mvn install -Pjboss
** Create a Tomcat delivery in target\working\exo-jboss\

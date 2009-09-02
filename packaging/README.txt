Portal packaging tooling:
* This pom embbed and autorun and exobuild
* http://svn.exoplatform.org/projects/utils/exopackage/tags/1.0.0
* http://svn.exoplatform.org/projects/utils/exopackage-conf/tags/20090901 

Process:
1/ Tomcat or JBossAS packaging are done during the "mvn install" process of the "packaging" module
2/ exopackage-1.0.0 is downloaded from maven
2/ exopackage-conf-20090901 is downloaded from maven, and contains official modules for exoproducts
4/ product and module for this version are stored in the current project, and found by exopackage during the run
5/ a maven-exec-plugin calls exobuild from maven with the right parameters
6/ Deliveries is in the target directory


*****************
* CONFIGURATION:
*****************

* in your settings.xml file (maven local computer configuration) you should at least add this
    <profile>
      <id>exo-projects</id>
      <properties>
        <exo.projects.directory.dependencies>c:/ExoPlatform/src/exo-dependencies</exo.projects.directory.dependencies> 
        <exo.projects.app.tomcat.version>tomcat-6.0.20</exo.projects.app.tomcat.version>
        <exo.projects.app.jboss.version>jboss-5.1.0.GA</exo.projects.app.jboss.version> 
      </properties>
    </profile>
  ...
  <activeProfiles>
    <activeProfile>exo-projects</activeProfile>
  </activeProfiles>
    
* ${exo.projects.directory.dependencies} directory should contain :
** ${exo.projects.app.tomcat.version}/ a Tomcat clean installation, to be used as packaging template
** ${exo.projects.app.jboss.version}/ a JBoss clean installation, to be used as packaging template

*****************
* PACKAGING:
*****************

* mvn install -Ppkg-tomcat
** Create a Tomcat delivery in target/tomcat/ and archives are stored in target
* mvn install -Ppkg-jbossas
** Create a JBossAS delivery in target/jboss/ and archives are stored in target

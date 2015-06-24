# Publick Apache Sling + Sightly Blog

This project is intended to host my personal site and blog.

Publick is named after [Publick Occurrences Both Forreign and Domestick](https://en.wikipedia.org/wiki/Publick_Occurrences_Both_Forreign_and_Domestick), the first newspaper in the American colonies.

## Download and start Apache Sling

This project requires Apache Sling 7.

1. [Download the Sling](http://sling.apache.org/downloads.cgi)
2. run `java -jar org.apache.sling.launchpad-7-standalone.jar`

## Install Sightly

Install external dependencies to a running Sling instance with default values of port *8080*, user *admin* and password *admin*:

    mvn clean install -PautoInstallDependencies

## How to build

Build and deploy to a running Sling instance with default values of port *8080*, user *admin* and password *admin*:

    mvn clean install -PautoInstallBundle

## Further information

The following information is not required to run the project.

### Maven Archetypes

Two Maven Archetypes where used to begin the project, one for the core Java bundle and one for the UI bundle.

1. Create parent pom
2. Create sub projects using the following archetypes:

```
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.sling \
    -DarchetypeArtifactId=sling-initial-content-archetype \
    -DgroupId=com.nateyolles.sling.publick \
    -DartifactId=ui \
    -Dversion=1.0.0-SNAPSHOT \
    -Dpackage=com.nateyolles.sling.publick.ui \
    -DappsFolderName=publick \
    -DartifactName="ui" \
    -DpackageGroup="ui"
```
```
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.sling \
    -DarchetypeArtifactId=sling-bundle-archetype \
    -DgroupId=com.nateyolles.sling.publick \
    -DartifactId=core \
    -Dversion=1.0.0-SNAPSHOT \
    -Dpackage=com.nateyolles.sling.publick.core \
    -DappsFolderName=publick \
    -DartifactName="core" \
    -DpackageGroup="core"
```
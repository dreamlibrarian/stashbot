Stashbot Helper is a plugin designed to enable a continuous integration
workflow within stash (similar to gerrit + jenkins).

# INSTALL GUIDE

To work with Jenkins, you MUST install the following jenkins plugins first.

1. Jenkins GIT plugin
2. Post build task

If either of these are missing, shit won't work.

# DEV GUIDE

## Eclipse

1. Generate project files by running `atlas-mvn eclipse:eclipse`
2. Load the code formatter settings by going to preferences, filtering on
"formatter", selecting Java -> Code Style -> Formatter, and importing the xml
file in code-style directory.
3. Load the cleanup settings by going to preferences, filtering on "cleanup" (also under code style) and import the cleanup xml file from the code-style directory.
4. Finally, again under preferences, filter on "save actions" for the java editor and check the options for "format source code", "format all lines", and "organize imports".

Doing these 4 things will ensure you do not introduce unneccessary whitespace changes.

NOTE: Please ensure you add a LICENSE block to the top of each newly added file.

## Jenkins

To run jenkins for testing, simply obtain a suitable jenkins.war, then do the
following to configure it:

1. Run `java -jar jenkins.war` (or use the scripts in bin/)
2. Navigate to http://localhost:8080 to configure
3. Install the git plugin and post build task (required!)
4. Ensure you navigate to a repository settings page and click "save", that is what initially creates/updates jobs in jenkins.


## Custom Jenkins Client

Originally this plugin required a customized version of the jenkins-client
library, but Cosmin, the author of this library, has generously (and expediently)
accepted our patches, so the current version is maven is all that is required.
If you are adding features which require patches to the library, however, you
can do something like this to easily build your own copy and use it:

    git clone https://github.com/RisingOak/jenkins-client.git $REPO_PATH
    # make modifications, build jar using maven
    atlas-mvn install:install-file -Dfile=$REPO_PATH/target/jenkins-client-0.1.5-SNAPSHOT.jar -DgroupId=com.offbytwo.jenkins -DartifactId=jenkins-client -Dversion=0.1.5-SNAPSHOT -Dpackaging=jar -DpomFile=$REPO_PATH/pom.xml

## Releasing

The Maven JGitFlow Plugin requires maven 2.2.1+ (and as of this writing, atlas-mvn is 2.1.1).

You can point atlassian's plugin SDK to a different maven for the purpose of releasing by doing this:

    ATLAS_MVN=/usr/share/maven2/bin/mvn

The above location is where maven 2.2.1 is located on debian/ubuntu if you have it installed.  Obviously, you could manually download it and locate it wherever you like.

# TODO

## KNOWN BUGS
* JenkinsManager.updateAllJobs() and createMissingJobs() are untested.

## PLANNED FEATURES

* Implement git-flow (https://bitbucket.org/atlassian/maven-jgitflow-plugin is a candidate, but doesn't work with the atlassian plugin SDK at this time, see https://bitbucket.org/atlassian/maven-jgitflow-plugin/issue/56/requires-maven-221-doesnt-work-with)
* Better Test coverage - especially integration tests
* Error checking - validate hashes sent to build status, etc.

## POSSIBLE FUTURE FEATURES

* Add authenticator to auth chain to allow dynamic credentials per-repo
* Supposedly jenkins supports groovy scripting.  We could possibly expose more functionality via arbitrary groovy by plugging into this.
* Add support for using Bamboo for CI (or other CI tools)

# LICENSE

Stashbot is released by Palantir Technologies, Inc. under the Apache 2.0
License.  see the included LICENSE file for details.

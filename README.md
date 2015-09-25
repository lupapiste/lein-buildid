# lein-buildid

A Leiningen plugin that writes resources/buildid.edn file under your project.

The file will contain the following information:
* `BUILD_NUMBER`, `BUILD_ID`, `BUILD_URL`, `NODE_NAME`, `JOB_NAME`,
  `BUILD_TAG`, `JENKINS_URL`, `EXECUTOR_NUMBER`, `JAVA_HOME` and `WORKSPACE`
  environment variables
  [set by Jenkins](https://wiki.jenkins-ci.org/display/JENKINS/Building+a+software+project#Buildingasoftwareproject-below).
* `time`: build timestamp in milliseconds (System/currentTimeMillis)
* `host`: hostname of the computer running the build
* `hg-branch`: output from `hg branch` command

Environment variable names are converted to kebab-case.

Content of the file looks like this (sans line breaks):

```clojure
{:build-number "4460",
 :build-id "4460",
 :build-url "http://jenkins.example.com/job/Some-Build/4460/",
 :node-name "master",
 :job-name "Some-Build",
 :build-tag "jenkins-Some-Build-4460",
 :jenkins-url "http://jenkins.example.com/",
 :executor-number "1",
 :java-home "/usr/java/jdk1.8.0_31",
 :workspace "/var/lib/jenkins/jobs/Some-Build/workspace",
 :host "jenkins.example.com",
 :hg-branch "master",
 :time 1443161425921}
```

## Usage

[![Clojars Project](http://clojars.org/lupapiste/lein-buildid/latest-version.svg)](http://clojars.org/lupapiste/lein-buildid)

Put `[lupapiste/lein-buildid "0.4.0"]` into the `:plugins` vector of your project.clj.

Run `lein buildid` on Jenkins before packaging your Clojure software.

You can read the edn file like this:
```clojure
(def build-id (read-string (slurp (clojure.java.io/resource "buildid.edn"))))
```

Note that the example will fail unless resources/buildid.edn exists.
You might want to place this sample file in your local development environment:

```clojure
{:java-home ""
 :workspace ""
 :host "localhost"
 :build-url ""
 :build-number "0"
 :build-id "programmer"
 :jenkins-url ""
 :executor-number ""
 :build-tag "local"
 :node-name ""
 :time 0
 :job-name ""
 :hg-branch "no-branch"}
```

## License

Copyright © 2012-2015 Solita Oy

Distributed under the Eclipse Public License, the same as Clojure.

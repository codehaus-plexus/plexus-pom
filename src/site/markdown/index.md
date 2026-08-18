# Plexus parent POM

Every project in the [Codehaus Plexus](https://codehaus-plexus.github.io/) organization inherits this
parent POM. It sets plugin versions, the Java and Maven baselines, code formatting, reporting, and the
release setup, so each project carries almost no build configuration of its own.

To inherit it, add the following to your project's POM:

```xml
<parent>
  <groupId>org.codehaus.plexus</groupId>
  <artifactId>plexus</artifactId>
  <version>VERSION</version>
</parent>
```

Replace `VERSION` with a released version. The badge on the
[project page](https://github.com/codehaus-plexus/plexus-pom) links to them.

## What you get

### Baselines

The following table lists the settings the parent POM fixes:

| Setting | Value |
|---|---|
| Java, through the `javaVersion` property | 8, which sets `maven.compiler.source`, `target`, and `release` |
| Minimum Maven to build | 3.6.3, or 3.9.0 to release |
| Source encoding | UTF-8 |
| Annotation processing | Off, through `maven.compiler.proc=none` |

To raise the Java baseline, override the `javaVersion` property in your own POM. The
`plexus-sec-dispatcher` and `plexus-xml` 4.x projects set it to 17.

To use annotation processing, turn it on deliberately in your own POM.

### Enforced at build time

The `maven-enforcer-plugin` plugin fails the build on a Maven version below the minimum and on a JDK below
the baseline. Through `extra-enforcer-rules`, it also fails on any dependency containing bytecode newer
than your compiler target.

That last rule surprises people. A dependency compiled for a later JDK than you target fails the build
rather than the application.

### Formatting

Spotless runs at the `process-sources` phase and formats three kinds of file:

- Java, with [palantir-java-format](https://github.com/palantir/palantir-java-format). Spotless removes
  unused imports and orders the rest as `javax`, `java`, everything else, then static imports.
- POM files, sorted with `sortPom`.
- Markdown, with flexmark.

What Spotless does with a violation depends on where the build runs. On your own machine, where the `CI`
environment variable isn't set, the `format` profile sets `spotless.action=apply` and the build rewrites
your sources. In CI, where `CI` is set, the `format-check` profile sets the action to `check` and the
build fails instead. When CI fails on formatting, run `mvn spotless:apply` and commit the result.

**Note:** Spotless formats every `**/*.md` file. Parent 26 and later exclude `**/src/site/markdown/**`,
because flexmark rewrites the fence that closes a YAML front matter block, which drops the page's title
and author without reporting an error. Parent 25 has no such exclusion. If you use parent 25 and keep
site sources in Markdown, add the exclusion to your own POM.

### Reproducible builds

The `project.build.outputTimestamp` property is set, and
[Reproducible Central](https://github.com/jvm-repo-rebuild/reproducible-central) verifies every project
here. Keep the property set, and change its value only as part of a release.

### Reporting

Project information reports run by default. The `reporting` profile adds Javadoc, JXR, surefire, PMD/CPD,
and taglist. To build a site with all of them:

```
mvn -Preporting site
```

A site built without the `reporting` profile contains no API documentation, so pass the profile whenever
you publish.

### Publishing

Snapshots and releases go to the Sonatype Central Portal. Your project inherits
`distributionManagement`, but each project overrides the `site` element to point at its own `gh-pages`
branch:

```xml
<distributionManagement>
  <site>
    <id>github:gh-pages</id>
    <url>${project.scm.developerConnection}</url>
  </site>
</distributionManagement>
```

The `maven-site-plugin` plugin runs with `skipDeploy`, so the `maven-scm-publish-plugin` plugin publishes
sites rather than the `site:deploy` goal.

## Releasing

For the full procedure, including site publishing, see
[RELEASING.md](https://github.com/codehaus-plexus/.github/blob/master/RELEASING.md). The short version:

```
mvn release:prepare
mvn release:perform
```

The `maven-release-plugin` plugin is configured with `<goals>deploy</goals>` and
`<releaseProfiles>plexus-release</releaseProfiles>`, so `release:perform` activates the `plexus-release`
profile. That profile turns on GPG signing, attaches sources and a source-release assembly, and enables
[Njord](https://maveniverse.eu/docs/njord/), which is registered as a build extension.

Njord is configured with `autoPublish=true` and `publishingType=automatic`, so it publishes the deployment
to Maven Central without a manual step in the Portal UI. Outside the release profile, `njord.enabled` is
`false`, so ordinary builds are unaffected.

### Set up a release manager

Add a Central Portal token to your personal `settings.xml` file:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
  <servers>
    <server>
      <id>sonatype-central-portal</id>
      <username>TOKEN_USERNAME</username>
      <password>TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>
```

Replace the following:

- `TOKEN_USERNAME`: the username half of a Central Portal token pair
- `TOKEN_PASSWORD`: the password half of the same pair

Generate the pair from your [Central Portal account](https://central.sonatype.com/account). A token pair
isn't your account password.

You also need a published GPG key, because releases are signed.

## Reference

- [Plugin versions managed here](./plugin-management.html)
- [Dependency versions managed here](./dependency-management.html)
- [Contributing](https://github.com/codehaus-plexus/.github/blob/master/CONTRIBUTING.md)

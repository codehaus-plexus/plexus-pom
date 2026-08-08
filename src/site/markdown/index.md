# Plexus Parent POM

The parent POM inherited by every project in the [Codehaus Plexus](https://codehaus-plexus.github.io/)
organisation. It fixes plugin versions, the Java and Maven baselines, code formatting, reporting and the
release setup, so the individual projects carry almost no build configuration of their own.

```xml
<parent>
  <groupId>org.codehaus.plexus</groupId>
  <artifactId>plexus</artifactId>
  <version><!-- see the badge on the project page --></version>
</parent>
```

## What you get

### Baselines

| Setting | Value |
|---|---|
| Java (`javaVersion`) | 8 — sets `maven.compiler.source`, `target` and `release` |
| Minimum Maven to build | 3.6.3 (3.9.0 when releasing) |
| Source encoding | UTF-8 |
| Annotation processing | off (`maven.compiler.proc=none`) — enable it deliberately if you need it |

Override `javaVersion` in your own POM to raise the baseline; `plexus-sec-dispatcher` and `plexus-xml` 4.x
set it to 17.

### Enforced at build time

`maven-enforcer-plugin` fails the build on a Maven version below the minimum, a JDK below the baseline,
and — via `extra-enforcer-rules` — on any **dependency containing bytecode newer than your compiler
target**. That last rule is the one that usually catches people: it means a dependency compiled for a
later JDK than you target is an error, not a runtime surprise.

### Formatting

Spotless, applied at `process-sources`:

- Java — [palantir-java-format](https://github.com/palantir/palantir-java-format), unused imports removed,
  import order `javax, java, all else, static`
- POMs — sorted with `sortPom`
- Markdown — flexmark

The action depends on where you are. Locally (`!env.CI`) the `format` profile sets `spotless.action=apply`,
so a build **rewrites your sources**. In CI (`env.CI` set) the `format-check` profile sets it to `check`,
so the build fails instead. If CI fails on formatting, run `mvn spotless:apply` and commit.

> Note that Spotless formats `**/*.md`. Parent 26 onward excludes `**/src/site/markdown/**`, because
> flexmark rewrites the fence closing a YAML front matter block and silently destroys a page's title and
> author. **Parent 25 does not have that exclusion** — if you are on 25 and keep site sources in Markdown,
> add the exclusion to your own POM.

### Reproducible builds

`project.build.outputTimestamp` is set, and every project here is verified by
[Reproducible Central](https://github.com/jvm-repo-rebuild/reproducible-central). Keep it set, and bump it
only as part of a release.

### Reporting

Project info reports are on by default. The `reporting` profile adds the rest — Javadoc, JXR, surefire,
PMD/CPD and taglist:

```
mvn -Preporting site
```

Building a site **without** `-Preporting` gives you a site with no API documentation, so always pass it
when publishing.

### Publishing

Snapshots and releases go to the Sonatype Central Portal. `distributionManagement` is inherited;
`site` **must be overridden** in each project, and points at that project's own `gh-pages` branch:

```xml
<distributionManagement>
  <site>
    <id>github:gh-pages</id>
    <url>${project.scm.developerConnection}</url>
  </site>
</distributionManagement>
```

`maven-site-plugin` runs with `skipDeploy`, so sites are published by `maven-scm-publish-plugin` rather
than `site:deploy`.

## Releasing

Full procedure, including site publishing, is in
[RELEASING.md](https://github.com/codehaus-plexus/.github/blob/master/RELEASING.md). The short version:

```
mvn release:prepare
mvn release:perform
```

`maven-release-plugin` is configured with `<goals>deploy</goals>` and
`<releaseProfiles>plexus-release</releaseProfiles>`, so `release:perform` activates the `plexus-release`
profile. That profile turns on GPG signing, attaches sources and a source-release assembly, and enables
[Njord](https://maveniverse.eu/docs/njord/), which is registered as a build extension.

Njord is configured with `autoPublish=true` and `publishingType=automatic`, so the deployment is published
to Central **without a manual step in the Portal UI**. `njord.enabled` is `false` outside the release
profile, so ordinary builds are unaffected.

### Release manager setup

A Central Portal token in your personal `settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
  <servers>
    <server>
      <id>sonatype-central-portal</id>
      <username><!-- Central Portal token username --></username>
      <password><!-- Central Portal token password --></password>
    </server>
  </servers>
</settings>
```

Tokens come from <https://central.sonatype.com/account>. It is a generated token pair, not your account
password.

You also need a published GPG key, since releases are signed.

## Reference

- [Plugin versions managed here](./plugin-management.html)
- [Dependency versions managed here](./dependency-management.html)
- [Contributing](https://github.com/codehaus-plexus/.github/blob/master/CONTRIBUTING.md)

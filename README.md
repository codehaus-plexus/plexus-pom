# Plexus parent POM

[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.plexus/plexus.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.codehaus.plexus/plexus)
[![GitHub CI](https://github.com/codehaus-plexus/plexus-pom/actions/workflows/maven.yml/badge.svg)](https://github.com/codehaus-plexus/plexus-pom/actions)
[![License](https://img.shields.io/github/license/codehaus-plexus/plexus-pom.svg?label=License)](https://www.apache.org/licenses/LICENSE-2.0)

Every project in the [Codehaus Plexus](https://github.com/codehaus-plexus) organization inherits this
parent POM. It sets plugin versions, the Java and Maven baselines, formatting, reporting, and the release
setup, so each project carries almost no build configuration of its own.

## Status

Maintained. A release follows whenever plugin or dependency updates accumulate, roughly every two months.

This POM is infrastructure for the Plexus projects rather than a general-purpose parent. It assumes
publishing through the Sonatype Central Portal, site hosting on GitHub Pages, and the organization's
shared CI workflow. You're welcome to use it, but it isn't designed to be neutral.

## Inherit the parent POM

Add the following to your project's POM:

```xml
<parent>
  <groupId>org.codehaus.plexus</groupId>
  <artifactId>plexus</artifactId>
  <version>VERSION</version>
</parent>
```

Replace `VERSION` with a released version from
[Maven Central](https://central.sonatype.com/artifact/org.codehaus.plexus/plexus).

Your project inherits everything except the `distributionManagement/site` element. Each project overrides
that element to point at its own `gh-pages` branch.

## What the parent POM configures

The parent POM covers the following areas:

- **Baselines**: Java 8 through the `javaVersion` property, Maven 3.9.0 as the minimum, and UTF-8 sources.
- **Enforcer**: the Maven and JDK minimums, and a rule from `extra-enforcer-rules` that rejects a
  dependency whose bytecode is newer than your compiler target.
- **Formatting**: Spotless with palantir-java-format, sorted POM files, and flexmark for Markdown.
  Spotless rewrites your sources locally and checks them in CI.
- **Reproducible builds**: the `project.build.outputTimestamp` property is set, and
  [Reproducible Central](https://github.com/jvm-repo-rebuild/reproducible-central) verifies the results.
- **Reporting**: the `reporting` profile adds Javadoc, JXR, surefire, PMD/CPD, and taglist.
- **Releasing**: the `plexus-release` profile adds GPG signing, sources, and a source-release assembly,
  and enables [Njord](https://maveniverse.eu/docs/njord/) to publish to Maven Central.
- **Version properties**: every managed plugin version has a `version.<artifactId>` property, so a child
  can pin a different version without redeclaring the plugin.

For an explanation of each area, see the [project site](https://codehaus-plexus.github.io/plexus-pom/),
which also lists every
[managed plugin version](https://codehaus-plexus.github.io/plexus-pom/plugin-management.html).

## Requirements

To build a project that inherits this POM, you need Java 8 or later and Maven 3.9.0 or later. Version 27
raised the Maven minimum from 3.6.3; it applies to building the project, not to consuming its artifacts.

## Documentation

- [Project site](https://codehaus-plexus.github.io/plexus-pom/)
- [Release notes](https://github.com/codehaus-plexus/plexus-pom/releases)
- [Releasing and site publishing](https://github.com/codehaus-plexus/.github/blob/master/RELEASING.md)

## Contributing

For the contribution process, see
[CONTRIBUTING.md](https://github.com/codehaus-plexus/.github/blob/master/CONTRIBUTING.md).

A change here reaches every project in the organization, so a version bump that looks routine can break a
downstream build. Say in your pull request description what you checked.

To report a security vulnerability, follow
[SECURITY.md](https://github.com/codehaus-plexus/.github/blob/master/SECURITY.md) rather than opening a
public issue.

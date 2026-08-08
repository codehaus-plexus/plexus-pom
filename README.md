# Plexus Parent POM

[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.plexus/plexus.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.codehaus.plexus/plexus)
[![GitHub CI](https://github.com/codehaus-plexus/plexus-pom/actions/workflows/maven.yml/badge.svg)](https://github.com/codehaus-plexus/plexus-pom/actions)
[![License](https://img.shields.io/github/license/codehaus-plexus/plexus-pom.svg?label=License)](https://www.apache.org/licenses/LICENSE-2.0)

The parent POM inherited by every project in the [Codehaus Plexus](https://github.com/codehaus-plexus)
organisation. It pins plugin versions, the Java and Maven baselines, formatting, reporting and the release
setup, so the individual projects carry almost no build configuration of their own.

## Status

Maintained. Released whenever plugin or dependency updates accumulate, roughly every couple of months.

This is infrastructure for the Plexus projects rather than a general-purpose parent — it assumes Sonatype
Central Portal publishing, GitHub Pages site hosting and the organisation's shared CI workflow. You are
welcome to use it, but it is not designed to be neutral.

## Using it

```xml
<parent>
  <groupId>org.codehaus.plexus</groupId>
  <artifactId>plexus</artifactId>
  <version>25</version>
</parent>
```

Check the badge above for the current version.

Projects must override `distributionManagement/site` to point at their own `gh-pages` branch; everything
else is inherited.

## What it configures

- **Baselines** — Java 8 (`javaVersion`), Maven 3.6.3 minimum, UTF-8 sources
- **Enforcer** — Maven and JDK minimums, plus a rule rejecting dependencies whose bytecode is newer than
  your compiler target
- **Formatting** — Spotless with palantir-java-format, sorted POMs, flexmark for Markdown. Applies locally,
  checks in CI
- **Reproducible builds** — `project.build.outputTimestamp` is set and the results are verified by
  [Reproducible Central](https://github.com/jvm-repo-rebuild/reproducible-central)
- **Reporting** — the `reporting` profile adds Javadoc, JXR, surefire, PMD/CPD and taglist
- **Releasing** — the `plexus-release` profile adds GPG signing, sources and a source-release assembly, and
  enables [Njord](https://maveniverse.eu/docs/njord/) to publish to Central

The [project site](https://codehaus-plexus.github.io/plexus-pom/) explains each of these, and lists every
[managed plugin version](https://codehaus-plexus.github.io/plexus-pom/plugin-management.html).

## Requirements

Java 8 or later to build a consuming project; Maven 3.6.3 or later (3.9.0 when releasing).

## Documentation

- [Project site](https://codehaus-plexus.github.io/plexus-pom/)
- [Release notes](https://github.com/codehaus-plexus/plexus-pom/releases)
- [Releasing and site publishing](https://github.com/codehaus-plexus/.github/blob/master/RELEASING.md)

## Contributing

See [CONTRIBUTING.md](https://github.com/codehaus-plexus/.github/blob/master/CONTRIBUTING.md). Changes here
affect every project in the organisation, so a version bump that looks routine can break a downstream build
— please say in the PR description what you have checked.

Please report security vulnerabilities privately — see
[SECURITY.md](https://github.com/codehaus-plexus/.github/blob/master/SECURITY.md), not a public issue.

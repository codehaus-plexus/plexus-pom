# Plexus Parent POM

Plexus Parent POM defines common [plugins](./plugin-management.html), reporting and release configuration.

## Preparing the environment for publishing vie the Central Portal

Release manager should include the following sections in your personal `settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
  <servers>
    <server>
      <id>sonatype-central-portal</id>
      <username>jqhacker</username> <!-- central portal token -->
      <password>SeCrEt</password> <!-- central portal token -->
    </server>
  </servers>
</settings>
```

Tokens can be obtained from https://central.sonatype.com/account


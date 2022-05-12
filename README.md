# Negative Games - Framework
[![](https://jitpack.io/v/Negative-Games/Framework.svg)](https://jitpack.io/#Negative-Games/Framework) ![licence](https://img.shields.io/github/license/negative-games/framework) ![GitHub commits since latest release (by date)](https://img.shields.io/github/commits-since/negative-games/framework/latest) ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/negative-games/framework/CodeQL)

An expanded plugin library which allows more effective and rapid plugin development.

## Wiki
https://github.com/Negative-Games/Framework/wiki

# ✨Maven Repo✨

## Repository

```xml
<repository>     
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

## Dependency

```xml
<dependency>
    <groupId>com.github.Negative-Games</groupId>
    <artifactId>Framework</artifactId>
    <version>{VERSION}</version>
    <scope>compile</scope>
</dependency>
```

## Build Configuration
Add this to your build configuration for this to work correctly.
```xml
<configuration>
  <relocations>
    <relocation>
      <pattern>games.negative.framework</pattern>
      <shadedPattern>{YOUR PACKAGE NAME}</shadedPattern>
    </relocation>
  </relocations>
</configuration>
```

### Example
An example would be
```xml
<configuration>
  <relocations>
    <relocation>
      <pattern>games.negative.framework</pattern>
      <shadedPattern>me.name.myplugin.framework</shadedPattern>
    </relocation>
  </relocations>
</configuration>
```

# ✨Gradle Repo✨
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
    implementation 'com.github.Negative-Games:Framework:{VERSION}'
}
```
To mask the dependency, add this to your plugins section
```groovy
plugins {
    id 'java'
    id "com.github.johnrengelman.shadow" version "7.1.2"
}
```
And then make a shadowJar section and put the following
```groovy
shadowJar {
    archiveBaseName.set("${id}-${version}")
    archiveClassifier.set("")
    archiveVersion.set("")
    
    relocate "games.negative.framework", "${group}.libs.plugin"
}
```
If you are wondering what `${group}` and `${id}` are, it is this:
```groovy
def id = "MyPlugin" // Replace with the plugin name
def group = 'games.negative' // Replace with your group id
def version = '1.0-SNAPSHOT' // Replace with the version
```



I wonder if I can push this to master :)

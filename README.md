
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->
![Logo](https://cdn.joehosten.me/negative-games/assets/Framework.png)


# Framework
[![](https://jitpack.io/v/Negative-Games/Framework.svg)](https://jitpack.io/#Negative-Games/Framework) ![licence](https://img.shields.io/github/license/negative-games/framework) ![GitHub commits since latest release (by date)](https://img.shields.io/github/commits-since/negative-games/framework/latest) ![GitHub Workflow Status](https://img.shields.io/github/workflow/status/negative-games/framework/CodeQL) ![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.hypews.com%2Fjob%2FFramework%2F&label=jenkins-build)  
An expanded plugin library which allows more effective and rapid plugin development.   
Latest version: `2.2.0`


## Contributing

Contributions are always welcome!

See `contributing.md` for ways to get started.

Please adhere to this project's `code of conduct`.


## Documentation

https://github.com/Negative-Games/Framework/wiki  
https://framework.docs.negative.games/


## License

[We use the MIT licence for this project. Please read it here.](https://choosealicense.com/licenses/mit/)


## Metrics Usage
If you would like to help us gather statistical & usage data for Framework via [Metrics](https://bstats.org), please add the following in your `onEnable()` method:
```java
enableFrameworkUsageTracking()
```
## Maven & Gradle Repositories

### ✨Maven Repository✨

#### Repository

```xml
<repository>     
    <id>negative-games</id>
    <url>https://repo.negative.games/repository/negative-games/</url>
</repository>
```

#### Dependency

```xml
<dependency>
      <groupId>games.negative.framework</groupId>
      <artifactId>Framework</artifactId>
      <version>{VERSION}</version>
      <scope>compile</scope>
</dependency>
```

#### Build Configuration

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

#### Example

An example would be:

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

To view an example in a full pom, head
to [this link](https://gist.github.com/joeecodes/f0d2da7807b256e44cce7da3be0bb188).

### ✨Gradle Repo✨
#### Repository

```groovy
repositories {
    mavenCentral()
    maven { url 'https://repo.negative.games/repository/negative-games/' }
}
```
#### Dependencies

```groovy
dependencies {
    implementation 'games.negative.framework:Framework:{VERSION}'
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
## Support

For support, join https://discord.negative.games, create an issue card or email us at negativegames.dev@gmail.com.


## Feedback

If you have any feedback, please reach out to us at https://discord.negative.games


## Authors

- [@negativedev](https://www.github.com/negativedev)
- [@joeecodes](https://www.github.com/joeecodes)
- Contributors


## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://seailz.com"><img src="https://avatars.githubusercontent.com/u/81972974?v=4?s=100" width="100px;" alt=""/><br /><sub><b>seailz</b></sub></a><br /><a href="https://github.com/Negative-Games/Framework/commits?author=seailz" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
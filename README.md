<p align="center">
  <a href="https://github.com/congcoi123/tenio">
    <img src="https://raw.githubusercontent.com/congcoi123/tenio/master/assets/tenio-github-logo.png" width="360" alt="TenIO — multiplayer game server framework">
  </a>
</p>

<h1 align="center">TenIO Engine</h1>

<p align="center">
  Game logic utilities, entity systems, state machines, and simulation.
</p>

<p align="center">
  <a href="https://github.com/congcoi123/tenio"><img src="https://img.shields.io/badge/Java-21%2B-ED8B00?style=flat&logo=openjdk&logoColor=white" alt="Java 21 or newer"></a>
  <a href="https://central.sonatype.com/artifact/io.github.congcoi123/tenio-engine"><img src="https://img.shields.io/maven-central/v/io.github.congcoi123/tenio-engine?style=flat&label=Maven%20Central" alt="Maven Central version"></a>
  <a href="https://javadoc.io/doc/io.github.congcoi123/tenio-engine"><img src="https://javadoc.io/badge2/io.github.congcoi123/tenio-engine/javadoc.svg" alt="API documentation"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-MIT-blue.svg?style=flat" alt="MIT license"></a>
</p>

<p align="center">
  <a href="https://github.com/congcoi123/tenio-engine/actions/workflows/maven.yml"><img src="https://github.com/congcoi123/tenio-engine/actions/workflows/maven.yml/badge.svg" alt="Maven build"></a>
  <a href="https://coveralls.io/github/congcoi123/tenio-engine?branch=master"><img src="https://coveralls.io/repos/github/congcoi123/tenio-engine/badge.svg?branch=master" alt="Test coverage"></a>
  <a href="https://github.com/congcoi123/tenio-engine/commits/master"><img src="https://img.shields.io/github/last-commit/congcoi123/tenio-engine?style=flat" alt="Latest commit"></a>
  <a href="https://github.com/congcoi123/tenio-engine/issues"><img src="https://img.shields.io/github/issues/congcoi123/tenio-engine?style=flat" alt="Open issues"></a>
  <a href="CONTRIBUTING.md"><img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat" alt="Contributions welcome"></a>
  <a href="https://discord.gg/ybkNU87Psy"><img src="https://img.shields.io/discord/1146091189456613407?style=flat&logo=discord&logoColor=white&label=Discord" alt="Join the TenIO community"></a>
</p>

<p align="center">
  <a href="#installation">Installation</a> ·
  <a href="https://github.com/congcoi123/tenio">TenIO</a> ·
  <a href="https://github.com/congcoi123/tenio-core#readme">Core guide</a> ·
  <a href="https://github.com/congcoi123/tenio-examples#readme">Examples</a> ·
  <a href="https://javadoc.io/doc/io.github.congcoi123/tenio-engine">API reference</a>
</p>

[`TenIO`](https://github.com/congcoi123/tenio) is an open-source project to create multiplayer online games that includes a java NIO (Non-blocking I/O) 
based server specifically designed for multiplayer games, which supports UDP, TCP, Websocket, HTTP transports, and available simple client projects for quick development.

This module provides the necessary mechanisms to build a game. It also supports some kind of pattern such as ECS, FSM, and integrates a simple physic 2D library. You can even observe the running entities on the server side by using the debugger window.

## Requirements
Java 21+

## License
The [`TenIO`](https://github.com/congcoi123/tenio) project is currently available under the [MIT](LICENSE) License.

## Changelog
Please check out the [changelog](CHANGELOG.md) for more details.

## Contributing
Please check out the [checklist](CHECKLIST.md) and [contributing guideline](CONTRIBUTING.md) for more details.

## Installation
Maven Central
```xml
<dependency>
    <groupId>io.github.congcoi123</groupId>
    <artifactId>tenio-engine</artifactId>
    <version>1.0.0</version>
</dependency>
```
GitHub
```sh
$ git clone https://github.com/congcoi123/tenio-engine.git
```

> Happy coding !

# PetarLib

PetarLib is a lightweight library plugin for Paper/Spigot (Minecraft 1.21) that provides reusable 
helper APIs and utilities for other plugins and server-side development. 
You might ask why no Bukkit support? Its because bukkit doesnt have the option to send messages
in the action bar without using NMS (`net.minecraft.server`). Instad PetarLib uses the spigot message sender
(`p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));`)
## Features

The Task Sceduler and HTTP Client are copied from the [PetarLib mod](https://github.com/PetarMc1/PetarLib) for fabric.
Only slight changes were made to make to the logging.

## Requirements
- Java: 21 or newer (recommended Java 21+ for Minecraft 1.21.11).
- Server: Paper or Spigot targeting Minecraft 1.21.

## Usage

PetarLib is library plugin. To see usage guides check the [docs](https://docs.petarmc.com/petarlib-spigot)

## Building from source

- Build: `.\gradlew clean build`
- The built plugin JAR will be in `build/libs/`. Drop it into a server `plugins/` folder to test runtime behavior.

## How to add PetarLib as a dependency in your project
```groovy
repositories {
    mavenCentral()
    maven {url uri'https://repo.petarmc.com/repository/maven-public/'}
}
dependencies {
    implementation 'com.petarmc.petarlib:petarlib-spigot:x.x.x'
}
```


## License
This project is licensed under the [Mit License](LICENSE).
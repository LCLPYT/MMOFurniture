# MMOFurniture
[![Gradle Publish](https://github.com/LCLPYT/MMOFurniture/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/LCLPYT/MMOFurniture/actions/workflows/gradle-publish.yml)

A lightweight Fabric port of [MrCrayfish's Furniture Mod](https://github.com/VazkiiMods/Quark), that aims to provide compatibility for porting worlds created in MinecraftForge with the official Furniture Mod.

## What this mod aims to be
This mod does not aim to be an exact port of the original mod.
It only provides a portion of the blocks, items and entities of the Furniture mod.
The goal of this mod is that worlds, created in MinecraftForge with the original mod,
can be ported to Fabric with ease. A script / mod with instructions, for the conversion process will be
linked here, when it is finished.

Feel free to fork this project and add missing content yourself. Keep in mind, that CFM is licensed [GPL v3](https://github.com/MrCrayfish/MrCrayfishFurnitureMod/blob/1.18.X/LICENSE), therefore you must respect the terms of the license.

All the assets of this project were taken from the original Furniture Mod, for more information,
please refer to this project's LICENSE file.

## Download and installation

- locate your release on the [downloads page](https://github.com/LCLPYT/MMOFurniture/releases) and download the jar file (e.g. `mmofurniture-1.0.0.jar`).
- install [Fabric](https://fabricmc.net/)
- this project requires [FabricAPI](https://www.curseforge.com/minecraft/mc-mods/fabric-api), so download it as well, if you haven't already
- this project requires [MMOContent](https://github.com/LCLPYT/MMOContent) (a modding library), download it as well
- put MMOFurniture, MMOContent and FabricAPI inside your `/mods` directory

## Dev Setup

Clone the repository, and import it into your IDE. (IntelliJ IDEA is recommended, along with the MinecraftDevelopment plugin)
If there are no run configurations, reopen your IDE.
For more information consult the [Fabric wiki](https://fabricmc.net/wiki/start).

### Building

To build the project, use:

```bash
./gradlew build
```

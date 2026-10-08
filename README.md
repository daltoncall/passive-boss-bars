# Aggro Boss Bars

Aggro Boss Bars is a small server-side add-on for **Mob Boss Bars**. Boss bars stay hidden while a mob is idle and appear when that mob acquires a living combat target.

## Features

- Works with any `MobEntity` that has a bar enabled through Mob Boss Bars.
- Shows the bar as soon as the mob enters combat.
- Keeps the bar visible briefly after the target is lost to prevent flickering.
- Can affect every configured boss bar, selected mod namespaces, or exact entity IDs.
- Can show a bar to every nearby player or only to the player being targeted.
- Does not modify other mob mods or contain special handling for any particular mob mod.
- Evaluates each entity's aggression state at most once per server tick, even when multiple players are tracking it.

## Requirements

- Minecraft 1.20.1
- Fabric Loader
- Fabric API
- Mob Boss Bars 1.1.0 or newer
- Java 17

**Mob Boss Bars is a required dependency.** Aggro Boss Bars adds visibility rules to bars created by Mob Boss Bars; it does not create or replace those bars itself.

## Build on Windows

1. Install a Java 17 JDK if Java is not already installed.
2. Extract the source folder.
3. Double-click `build-mod.bat`.
4. Find the finished jar in `build/libs/aggro-boss-bars-1.0.1.jar`.
5. Install it alongside Fabric API and Mob Boss Bars.

The folder can also be opened as a Gradle project in IntelliJ IDEA and built with the `build` task.

## Configuration

After the first launch, edit:

`config/aggro-boss-bars.json`

Default configuration:

```json
{
  "restrictToListedEntities": false,
  "entityNamespaces": [],
  "entityIds": [],
  "excludedEntityIds": [
    "minecraft:ender_dragon"
  ],
  "lingerTicks": 40,
  "showOnlyToTargetedPlayer": false
}
```

### Apply to every configured mob

Leave this setting as `false`:

```json
"restrictToListedEntities": false
```

This affects every compatible mob whose boss bar is enabled in Mob Boss Bars, except entities in `excludedEntityIds`.

### Apply only to selected mods

Set `restrictToListedEntities` to `true`, then add mod namespaces:

```json
{
  "restrictToListedEntities": true,
  "entityNamespaces": [
    "examplemod"
  ],
  "entityIds": [],
  "excludedEntityIds": [
    "minecraft:ender_dragon"
  ],
  "lingerTicks": 40,
  "showOnlyToTargetedPlayer": false
}
```

### Apply only to exact mobs

Leave `entityNamespaces` empty and add full entity IDs:

```json
{
  "restrictToListedEntities": true,
  "entityNamespaces": [],
  "entityIds": [
    "examplemod:example_boss"
  ],
  "excludedEntityIds": [
    "minecraft:ender_dragon"
  ],
  "lingerTicks": 40,
  "showOnlyToTargetedPlayer": false
}
```

### Other settings

- `lingerTicks`: How long the bar remains visible after combat ends. Twenty ticks is approximately one second at normal server speed.
- `showOnlyToTargetedPlayer`: When `true`, a player-targeted mob's bar is shown only to that player. When `false`, all players tracking the mob can see it.
- `excludedEntityIds`: Exact entity IDs that Aggro Boss Bars should never affect.

Restart Minecraft or the server after editing the configuration.

## Compatibility note

Aggro Boss Bars detects combat through Minecraft's standard `MobEntity#getTarget()` state. Most normal and modded mob AI uses this system. A mob that attacks without assigning a standard target may require a compatibility update.

## Licensing and dependency use

Aggro Boss Bars is independently written and licensed under MIT. It uses Mob Boss Bars' public predicate API but does not include or redistribute Mob Boss Bars source code inside this project or its built jar.

Mob Boss Bars is a separate MIT-licensed project by Totobird Creations and must be installed separately. See `THIRD_PARTY_NOTICES.md` for attribution.

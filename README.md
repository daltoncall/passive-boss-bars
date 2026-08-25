Feel free to use this on any Modrinth or Curse Forge modpack.

This mod is specifically for modpack developers and builds upon it's dependency mod: [Mob Boss Bars](https://modrinth.com/mod/mob-boss-bars)

By default this mod will make it so that any boss that is passive/not aggro on the player will not show the boss bar. Once attacked/aggroed it will show a boss bar. This is especially useful if you want something like The Warden to have a boss bar, but only while fighting it, or if you're like me and want dragons spawning that are boss fights if attacked, but you don't want to clutter the screen/giveaway that there is a rare dragon nearby.

To edit the config, boot up the game once with this mod installed and search for the config file titled "aggro-boss-bars.json".

DEFAULT CONFIG:

```
{
  "restrictToListedEntities": false,
  "entityNamespaces": [],
  "entityIds": [],
  "excludedEntityIds": 
[
    "minecraft:ender_dragon"
  ],
  "lingerTicks": 40,
  "showOnlyToTargetedPlayer": false
}

```
Sidenote: You should not have to touch the config unless you do want certain mobs to have boss bars even when passive.

- "restrictToListedEntities" indicates that this mod will by default apply to all mobs that have boss bars via the Mob Boss Bars mod (which includes vanilla bosses). Set to True if you only want to effect the namespaces or IDs you manually input into the config
- "entityNamespaces" not to be confused with Ids, this is for grouping, example: "saintsdragons", "naturalist"
- "entityIds" this is where you would put your ID's you want to manually effect, example: minecraft:warden, saintsdragons:ignivorous, saintsdragons:volitans
- "excludedEntityIds" blacklist for this mod. By default includes the Ender Dragon
- "lingerTicks" the amount of ticks added to the boss bar after the boss is no longer aggro as to avoid flickering boss bars
- "showOnlyToTargetedPlayer" the bar is visible to all nearby players that the boss is aggro to. I recommend having this on as it may be immersion breakinf to fight a boss with your friend and to not have a boss bar simply because it's targeting them

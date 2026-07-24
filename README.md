# SmartCraftWarehouse

SmartCraftWarehouse is a standalone lightweight crafting and remote-storage plugin for **Spigot 1.21.1**.
It does not depend on the previously created FreeLife, NPointShulker, or PlayerHeadsLite plugins.

## Requirements

- Minecraft / Spigot: 1.21.1
- Java: 21
- No required third-party plugins

## Features

### Personal storage chests

Players can link multiple chests to one private virtual material warehouse.

1. Run `/scraft storage add`.
2. Left-click a normal chest or trapped chest.
3. Repeat the process to link more chests.

The linked chest may be in another loaded world or dimension. During crafting, the plugin resolves the saved world and chest coordinates and temporarily requests the required chunk from Spigot.

A chest block can only be registered to one player through this plugin. Locked chests are skipped by default.

### Inventory-first material search

Crafting materials are searched in this order:

1. The player's currently selected hotbar slot
2. The rest of the player's hotbar and inventory
3. Leftovers and intermediate items produced during the current recursive craft
4. Linked storage chests

The entire plan is simulated before any real item is removed. Immediately before committing the craft, the plugin verifies that every source slot is still unchanged. If a source changed, the transaction is cancelled without consuming materials.

### Direct command crafting

```text
/scraft <item> [amount]
```

Examples:

```text
/scraft chest 4
/scraft oak_stairs 64
/scraft minecraft:stone_bricks 32
```

The amount is the minimum requested result count. If a vanilla recipe produces items in batches, SmartCraftWarehouse creates enough recipe batches and delivers the complete output. For example, requesting one slab may produce the normal recipe batch of six slabs.

### Recursive intermediate crafting

The plugin can recursively craft missing intermediate materials from standard shaped and shapeless recipes.

Example:

```text
Logs -> Planks -> Chest
```

If the player and linked chests contain logs but no planks, `/scraft chest 1` can craft the planks first and then craft the chest.

Unused intermediate output and normal crafting remainders, such as empty buckets, are returned to the player.

### Recipe GUI

Run `/scraft` with no arguments to open the GUI.

- Left-click: craft the selected result
- Shift + left-click: bulk craft, default 64 requested items
- Right-click: add or remove the recipe from favorites
- Star / book button: switch between all recipes and favorites
- Arrow buttons: change pages

The GUI lists result items from standard Spigot `ShapedRecipe` and `ShapelessRecipe` registrations.

### Favorites

```text
/scraft favorites
/scraft favorite <item>
```

Favorites are stored separately for each player and remain after server restarts.

### Delivery

Crafted items are inserted directly into the player's inventory in their current dimension. If the inventory is full, remaining stacks are dropped naturally at the player's current location instead of being deleted.

## Commands

| Command | Description |
|---|---|
| `/scraft` | Open the recipe GUI |
| `/scraft <item> [amount]` | Craft and deliver an item |
| `/scraft storage add` | Enter chest-link mode |
| `/scraft storage remove` | Enter chest-unlink mode |
| `/scraft storage list` | List linked chests |
| `/scraft storage clear confirm` | Remove every linked chest |
| `/scraft favorites` | Open the favorites GUI |
| `/scraft favorite <item>` | Toggle a favorite |
| `/scraft reload` | Reload configuration, player data, and recipe cache |

## Permissions

| Permission | Default | Description |
|---|---|---|
| `smartcraft.use` | Everyone | Use SmartCraft commands and GUI |
| `smartcraft.storage` | Everyone | Link and manage storage chests |
| `smartcraft.admin` | Operators | Reload and bypass the linked-chest limit |

## Configuration

```yaml
storage:
  max-linked-chests: 64
  selection-timeout-seconds: 60
  skip-locked-chests: true

craft:
  max-request-amount: 2304
  max-recursion-depth: 12

gui:
  title: "SmartCraft"
  favorites-title: "SmartCraft Favorites"
  shift-click-amount: 64
```

## Supported recipes

SmartCraftWarehouse intentionally supports normal registered `ShapedRecipe` and `ShapelessRecipe` recipes. It does not emulate furnaces, smokers, blasting, campfires, stonecutters, smithing, brewing, or recipes whose result is calculated dynamically from NBT or custom input data.

Third-party plugins may register shaped or shapeless recipes. They are available when their result and ingredient choices can be represented by the public Spigot recipe API.

## Protection-plugin note

Chest selection respects a cancelled `PlayerInteractEvent`, so protection plugins can prevent a player from linking a protected chest. Remote inventory access does not contain direct integrations for LWC, Residence, WorldGuard, or similar plugins. Use the plugin's one-owner storage registration together with your server's normal chest-protection rules.

## Building

```bash
gradle build
```

The output JAR will be located in:

```text
build/libs/SmartCraftWarehouse-1.0.0.jar
```

## Installation

1. Stop the server.
2. Copy `SmartCraftWarehouse-1.0.0-Spigot-1.21.1.jar` into the `plugins` directory.
3. Start the server with Java 21.
4. Run `/scraft storage add` and left-click a chest.
5. Run `/scraft` to open the crafting GUI.

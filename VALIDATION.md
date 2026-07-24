# Validation

- Target API: Spigot 1.21.1
- Java target: Java 21
- `plugin.yml` API version: `1.21.1`
- Runtime JAR contains only SmartCraftWarehouse classes and resources.
- Compile-only Bukkit/Spigot stubs are not included in the runtime JAR.
- Java source compilation completed successfully.
- YAML parsing completed successfully.
- JAR and ZIP integrity checks completed successfully.

## Runtime testing status

The plugin was not started inside a complete Spigot 1.21.1 server in this environment.
Compilation used a minimal compile-only API surface whose method descriptors match the
public Spigot APIs used by the plugin. The Gradle project is configured to compile
against the official `org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT` dependency.

## Recipe scope

Recursive crafting supports registered `ShapedRecipe` and `ShapelessRecipe` recipes.
Cooking, smithing, stonecutting, brewing, and dynamic NBT-dependent recipes are not
simulated.

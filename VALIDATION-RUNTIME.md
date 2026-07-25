# Runtime linkage validation

Version 1.4.4 corrects the Spigot 1.21.1 binary method descriptors used by the plugin build.

Validated descriptors include:

- `PluginManager.registerEvents(Listener, Plugin)`
- `Bukkit.createInventory(InventoryHolder, int, String)`
- `HumanEntity.openInventory(Inventory): InventoryView`
- `InventoryClickEvent.getView(): InventoryView`
- `InventoryClickEvent.getWhoClicked(): HumanEntity`
- `ItemStack.setItemMeta(ItemMeta): boolean`

The JAR archive and embedded `plugin.yml` were also checked for integrity.

package com.sagakenichi.smartcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class SmartCraftWarehousePlugin extends JavaPlugin implements CommandExecutor, Listener {
    private static final String MAIN_GUI_TITLE = ChatColor.DARK_AQUA + "SmartCraft";
    private final Map<UUID, SelectionMode> selectionModes = new HashMap<>();
    private File ownershipFile;
    private YamlConfiguration ownershipData;

    private enum SelectionMode { ADD, REMOVE }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("sc"), "Command 'sc' is missing from plugin.yml").setExecutor(this);
        Objects.requireNonNull(getCommand("scraft"), "Command 'scraft' is missing from plugin.yml").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        loadOwnershipData();
        getLogger().info("SmartCraftWarehouse enabled. Use /sc to open the GUI.");
    }

    @Override
    public void onDisable() {
        saveOwnershipData();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }
        if (!player.hasPermission("smartcraft.use")) {
            player.sendMessage(ChatColor.RED + "このコマンドを使用する権限がありません。");
            return true;
        }
        if (args.length == 0) {
            openMainGui(player);
            return true;
        }
        if (args.length >= 2 && args[0].equalsIgnoreCase("storage")) {
            if (!player.hasPermission("smartcraft.storage")) {
                player.sendMessage(ChatColor.RED + "素材チェストを管理する権限がありません。");
                return true;
            }
            if (args[1].equalsIgnoreCase("add")) {
                selectionModes.put(player.getUniqueId(), SelectionMode.ADD);
                player.sendMessage(ChatColor.GREEN + "登録するチェストを左クリックしてください。");
                return true;
            }
            if (args[1].equalsIgnoreCase("remove")) {
                selectionModes.put(player.getUniqueId(), SelectionMode.REMOVE);
                player.sendMessage(ChatColor.YELLOW + "登録解除するチェストを左クリックしてください。");
                return true;
            }
        }
        player.sendMessage(ChatColor.YELLOW + "GUI: /sc  登録: /sc storage add  解除: /sc storage remove");
        return true;
    }

    private void openMainGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, MAIN_GUI_TITLE);
        inventory.setItem(10, menuItem(Material.COMPASS, ChatColor.AQUA + "アイテムを検索",
                List.of(ChatColor.GRAY + "日本語名または英語IDで検索")));
        inventory.setItem(12, menuItem(Material.NETHER_STAR, ChatColor.GOLD + "お気に入り",
                List.of(ChatColor.GRAY + "登録済みアイテムを表示")));
        inventory.setItem(14, menuItem(Material.CHEST, ChatColor.GREEN + "素材倉庫の案内",
                List.of(ChatColor.GRAY + "チェストをクリックすると所有者を表示")));
        inventory.setItem(16, menuItem(Material.HOPPER, ChatColor.LIGHT_PURPLE + "素材チェストを登録",
                List.of(ChatColor.GRAY + "クリック後、登録するチェストを左クリック")));
        inventory.setItem(22, menuItem(Material.BARRIER, ChatColor.RED + "閉じる", List.of()));
        player.openInventory(inventory);
    }

    private ItemStack menuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        meta.setDisplayName(name);
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(MAIN_GUI_TITLE)) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        switch (event.getRawSlot()) {
            case 10 -> player.sendMessage(ChatColor.AQUA + "検索機能は今後の完全版で利用できます。");
            case 12 -> player.sendMessage(ChatColor.GOLD + "お気に入り機能は今後の完全版で利用できます。");
            case 14 -> {
                player.closeInventory();
                player.sendMessage(ChatColor.GREEN + "確認したいチェストをクリックしてください。");
            }
            case 16 -> {
                player.closeInventory();
                selectionModes.put(player.getUniqueId(), SelectionMode.ADD);
                player.sendMessage(ChatColor.GREEN + "登録するチェストを左クリックしてください。");
            }
            case 22 -> player.closeInventory();
            default -> {
            }
        }
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || !isChest(block.getType())) {
            return;
        }
        Player player = event.getPlayer();
        String key = locationKey(block.getLocation());
        SelectionMode mode = selectionModes.remove(player.getUniqueId());

        if (mode == SelectionMode.ADD) {
            event.setCancelled(true);
            String existingUuid = ownershipData.getString(key + ".uuid");
            if (existingUuid != null && !existingUuid.equals(player.getUniqueId().toString())) {
                player.sendMessage(ChatColor.RED + "この資材チェストは " + ownerName(key) + " さんが保有しています。");
                return;
            }
            ownershipData.set(key + ".uuid", player.getUniqueId().toString());
            ownershipData.set(key + ".name", player.getName());
            saveOwnershipData();
            player.sendMessage(ChatColor.GREEN + "資材チェストとして登録しました。所有者: " + player.getName());
            return;
        }

        if (mode == SelectionMode.REMOVE) {
            event.setCancelled(true);
            String existingUuid = ownershipData.getString(key + ".uuid");
            if (existingUuid == null) {
                player.sendMessage(ChatColor.YELLOW + "このチェストは登録されていません。");
            } else if (existingUuid.equals(player.getUniqueId().toString()) || player.hasPermission("smartcraft.admin")) {
                ownershipData.set(key, null);
                saveOwnershipData();
                player.sendMessage(ChatColor.GREEN + "資材チェストの登録を解除しました。");
            } else {
                player.sendMessage(ChatColor.RED + "他のプレイヤーの資材チェストは解除できません。所有者: " + ownerName(key));
            }
            return;
        }

        String ownerUuid = ownershipData.getString(key + ".uuid");
        if (ownerUuid == null) {
            player.sendMessage(ChatColor.GRAY + "このチェストは資材チェストとして登録されていません。");
        } else {
            player.sendMessage(ChatColor.AQUA + "この資材チェストの所有者: " + ChatColor.WHITE + ownerName(key));
        }
    }

    private boolean isChest(Material material) {
        return material == Material.CHEST || material == Material.TRAPPED_CHEST;
    }

    private String locationKey(Location location) {
        return "chests." + location.getWorld().getUID() + "." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ();
    }

    private String ownerName(String key) {
        String name = ownershipData.getString(key + ".name");
        return name == null ? "不明" : name;
    }

    private void loadOwnershipData() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().warning("Could not create plugin data folder.");
        }
        ownershipFile = new File(getDataFolder(), "chest-owners.yml");
        ownershipData = YamlConfiguration.loadConfiguration(ownershipFile);
    }

    private void saveOwnershipData() {
        if (ownershipData == null || ownershipFile == null) {
            return;
        }
        try {
            ownershipData.save(ownershipFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save chest ownership data: " + e.getMessage());
        }
    }
}

package com.sagakenichi.smartcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public final class SmartCraftWarehousePlugin extends JavaPlugin implements CommandExecutor, Listener {
    private static final String MAIN_GUI_TITLE = ChatColor.DARK_AQUA + "SmartCraft";

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("sc"), "Command 'sc' is missing from plugin.yml")
                .setExecutor(this);
        Objects.requireNonNull(getCommand("scraft"), "Command 'scraft' is missing from plugin.yml")
                .setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SmartCraftWarehouse enabled. Use /sc to open the GUI.");
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

        // /sc always opens the GUI. /scraft is retained as a legacy fallback.
        if (args.length == 0) {
            openMainGui(player);
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "GUIを開くには /sc を使用してください。");
        return true;
    }

    private void openMainGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, MAIN_GUI_TITLE);
        inventory.setItem(10, menuItem(Material.COMPASS, ChatColor.AQUA + "アイテムを検索",
                List.of(ChatColor.GRAY + "日本語名または英語IDで検索")));
        inventory.setItem(12, menuItem(Material.NETHER_STAR, ChatColor.GOLD + "お気に入り",
                List.of(ChatColor.GRAY + "登録済みアイテムを表示")));
        inventory.setItem(14, menuItem(Material.CHEST, ChatColor.GREEN + "素材倉庫の案内",
                List.of(ChatColor.GRAY + "登録チェストと座標を確認")));
        inventory.setItem(16, menuItem(Material.HOPPER, ChatColor.LIGHT_PURPLE + "素材倉庫を追加・削除",
                List.of(ChatColor.GRAY + "倉庫管理GUIを開く")));
        inventory.setItem(22, menuItem(Material.BARRIER, ChatColor.RED + "閉じる", List.of()));
        player.openInventory(inventory);
    }

    private ItemStack menuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
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
            case 10 -> player.sendMessage(ChatColor.AQUA + "検索機能を選択しました。");
            case 12 -> player.sendMessage(ChatColor.GOLD + "お気に入りを選択しました。");
            case 14 -> player.sendMessage(ChatColor.GREEN + "素材倉庫の案内を選択しました。");
            case 16 -> player.sendMessage(ChatColor.LIGHT_PURPLE + "素材倉庫管理を選択しました。");
            case 22 -> player.closeInventory();
            default -> { }
        }
    }
}

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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class SmartCraftWarehousePlugin extends JavaPlugin implements CommandExecutor, Listener {
    private static final String MAIN_GUI_TITLE = ChatColor.DARK_AQUA + "SmartCraft";
    private static final String SEARCH_GUI_TITLE = ChatColor.DARK_AQUA + "SmartCraft 検索結果";
    private static final String FAVORITES_GUI_TITLE = ChatColor.GOLD + "SmartCraft お気に入り";
    private static final String RECIPE_GUI_TITLE = ChatColor.GREEN + "SmartCraft レシピ";
    private static final int RESULTS_PER_PAGE = 45;

    private final Map<UUID, SelectionMode> selectionModes = new HashMap<>();
    private final Set<UUID> awaitingSearch = new HashSet<>();
    private final Map<UUID, SearchState> searchStates = new HashMap<>();
    private final Map<UUID, RecipeState> recipeStates = new HashMap<>();
    private final Map<UUID, Set<String>> favoritesCache = new HashMap<>();
    private File ownershipFile;
    private YamlConfiguration ownershipData;
    private File favoritesFile;
    private YamlConfiguration favoritesData;

    private enum SelectionMode { ADD, REMOVE }
    private record SearchState(String query, int page, List<Material> results, boolean favoritesOnly) {}
    private record RecipeState(Material material, int index, List<Recipe> recipes, boolean fromFavorites) {}

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("sc"), "Command 'sc' is missing from plugin.yml").setExecutor(this);
        Objects.requireNonNull(getCommand("scraft"), "Command 'scraft' is missing from plugin.yml").setExecutor(this);
        Objects.requireNonNull(getCommand("s"), "Command 's' is missing from plugin.yml").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        loadData();
        getLogger().info("SmartCraftWarehouse 1.5.0 enabled. Use /sc to open the GUI.");
    }

    @Override
    public void onDisable() {
        saveOwnershipData();
        saveFavoritesData();
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

        if (command.getName().equalsIgnoreCase("s")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("i")) {
                showMainHandInfo(player);
            } else {
                player.sendMessage(ChatColor.YELLOW + "使い方: メインハンドにアイテムを持って /s i");
            }
            return true;
        }

        if (args.length == 0) {
            openMainGui(player);
            return true;
        }
        if (args[0].equalsIgnoreCase("search")) {
            if (args.length == 1) {
                beginSearch(player);
            } else {
                openSearchResults(player, String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length)), 0);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("favorites") || args[0].equalsIgnoreCase("favourites") || args[0].equalsIgnoreCase("fav")) {
            openFavorites(player, 0);
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
        player.sendMessage(ChatColor.YELLOW + "GUI: /sc  検索: /sc search <名前>  お気に入り: /sc favorites  情報: /s i");
        return true;
    }

    private void showMainHandInfo(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        Material material = item.getType();
        if (material == Material.AIR) {
            player.sendMessage(ChatColor.RED + "メインハンドにアイテムを持ってください。");
            return;
        }
        player.sendMessage(ChatColor.AQUA + "アイテムID: " + ChatColor.WHITE + material.getKey());
        player.sendMessage(ChatColor.AQUA + "日本語名: " + ChatColor.WHITE + JapaneseItemNames.name(material));
    }

    private void openMainGui(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, MAIN_GUI_TITLE);
        inventory.setItem(10, menuItem(Material.COMPASS, ChatColor.AQUA + "アイテムを検索",
                List.of(ChatColor.GRAY + "日本語名または英語IDで検索", ChatColor.YELLOW + "クリックして検索語を入力")));
        inventory.setItem(12, menuItem(Material.NETHER_STAR, ChatColor.GOLD + "お気に入り",
                List.of(ChatColor.GRAY + "登録済みアイテムを表示", ChatColor.YELLOW + "Shift＋右クリックで登録・解除")));
        inventory.setItem(14, menuItem(Material.CHEST, ChatColor.GREEN + "素材倉庫の案内",
                List.of(ChatColor.GRAY + "チェストをクリックすると所有者を表示")));
        inventory.setItem(16, menuItem(Material.HOPPER, ChatColor.LIGHT_PURPLE + "素材チェストを登録",
                List.of(ChatColor.GRAY + "クリック後、登録するチェストを左クリック")));
        inventory.setItem(18, menuItem(Material.TRAPPED_CHEST, ChatColor.YELLOW + "素材チェストを解除",
                List.of(ChatColor.GRAY + "クリック後、解除するチェストを左クリック")));
        inventory.setItem(22, menuItem(Material.BARRIER, ChatColor.RED + "閉じる", List.of()));
        player.openInventory(inventory);
    }

    private void beginSearch(Player player) {
        player.closeInventory();
        awaitingSearch.add(player.getUniqueId());
        player.sendMessage(ChatColor.AQUA + "検索するアイテム名をチャットに入力してください。");
        player.sendMessage(ChatColor.GRAY + "日本語例: チェスト、エリトラ、オークの板材 / 英語例: iron_pickaxe");
    }

    @EventHandler
    public void onSearchChat(AsyncPlayerChatEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!awaitingSearch.remove(uuid)) return;
        event.setCancelled(true);
        String query = event.getMessage().trim();
        Bukkit.getScheduler().runTask(this, () -> openSearchResults(event.getPlayer(), query, 0));
    }

    private void openSearchResults(Player player, String query, int requestedPage) {
        if (query == null || query.isBlank()) {
            player.sendMessage(ChatColor.RED + "検索語を入力してください。");
            return;
        }
        String q = query.trim();
        List<Material> results = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material == Material.AIR || !material.isItem()) continue;
            if (JapaneseItemNames.matches(material, q)) results.add(material);
        }
        results.sort(Comparator
                .comparing((Material m) -> !startsWithSearch(m, q))
                .thenComparing(JapaneseItemNames::name)
                .thenComparing(Material::name));
        openResultPage(player, q, requestedPage, results, false);
    }

    private boolean startsWithSearch(Material material, String query) {
        String q = normalize(query);
        return normalize(material.name()).startsWith(q) || normalize(JapaneseItemNames.name(material)).startsWith(q);
    }

    private void openFavorites(Player player, int requestedPage) {
        Set<String> favoriteNames = favorites(player.getUniqueId());
        List<Material> materials = new ArrayList<>();
        for (String name : favoriteNames) {
            try {
                Material material = Material.valueOf(name);
                if (material.isItem() && material != Material.AIR) materials.add(material);
            } catch (IllegalArgumentException ignored) {
            }
        }
        materials.sort(Comparator.comparing(JapaneseItemNames::name));
        openResultPage(player, "お気に入り", requestedPage, materials, true);
    }

    private void openResultPage(Player player, String query, int requestedPage, List<Material> results, boolean favoritesOnly) {
        int maxPage = Math.max(0, (results.size() - 1) / RESULTS_PER_PAGE);
        int page = Math.max(0, Math.min(requestedPage, maxPage));
        String title = favoritesOnly ? FAVORITES_GUI_TITLE : SEARCH_GUI_TITLE;
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        int start = page * RESULTS_PER_PAGE;
        int end = Math.min(results.size(), start + RESULTS_PER_PAGE);
        Set<String> favs = favorites(player.getUniqueId());
        for (int i = start; i < end; i++) {
            Material material = results.get(i);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "ID: " + material.getKey());
            lore.add(ChatColor.YELLOW + "左・右クリック: クラフトメニュー");
            lore.add(ChatColor.LIGHT_PURPLE + "Shift＋右クリック: お気に入り" + (favs.contains(material.name()) ? "解除" : "登録"));
            inventory.setItem(i - start, menuItem(material, ChatColor.WHITE + JapaneseItemNames.name(material), lore));
        }
        if (page > 0) inventory.setItem(45, menuItem(Material.ARROW, ChatColor.YELLOW + "前のページ", List.of()));
        inventory.setItem(49, menuItem(Material.BOOK, ChatColor.AQUA + query,
                List.of(ChatColor.GRAY + "検索結果: " + results.size() + "件", ChatColor.GRAY + "ページ: " + (page + 1) + "/" + (maxPage + 1))));
        inventory.setItem(48, menuItem(Material.BARRIER, ChatColor.RED + "メインメニューへ戻る", List.of()));
        if (page < maxPage) inventory.setItem(53, menuItem(Material.ARROW, ChatColor.YELLOW + "次のページ", List.of()));
        searchStates.put(player.getUniqueId(), new SearchState(query, page, List.copyOf(results), favoritesOnly));
        player.openInventory(inventory);
    }

    private void openRecipeGui(Player player, Material material, int requestedIndex, boolean fromFavorites) {
        List<Recipe> recipes = findRecipes(material);
        int index = recipes.isEmpty() ? 0 : Math.max(0, Math.min(requestedIndex, recipes.size() - 1));
        Inventory inventory = Bukkit.createInventory(null, 54, RECIPE_GUI_TITLE);
        inventory.setItem(4, menuItem(material, ChatColor.WHITE + JapaneseItemNames.name(material),
                List.of(ChatColor.GRAY + "ID: " + material.getKey())));

        if (recipes.isEmpty()) {
            inventory.setItem(22, menuItem(Material.BARRIER, ChatColor.RED + "クラフトレシピがありません",
                    List.of(ChatColor.GRAY + "かまど・鍛冶・醸造などのレシピは対象外です。")));
        } else {
            Recipe recipe = recipes.get(index);
            drawRecipe(inventory, recipe);
            inventory.setItem(40, menuItem(Material.BOOK, ChatColor.AQUA + "レシピ " + (index + 1) + "/" + recipes.size(),
                    List.of(ChatColor.GRAY + (recipe instanceof ShapedRecipe ? "定形レシピ" : "不定形レシピ"))));
        }
        if (index > 0) inventory.setItem(45, menuItem(Material.ARROW, ChatColor.YELLOW + "前のレシピ", List.of()));
        inventory.setItem(48, menuItem(Material.BARRIER, ChatColor.RED + "検索結果へ戻る", List.of()));
        boolean favorite = favorites(player.getUniqueId()).contains(material.name());
        inventory.setItem(49, menuItem(Material.NETHER_STAR, favorite ? ChatColor.GOLD + "お気に入り登録済み" : ChatColor.GRAY + "お気に入り未登録",
                List.of(ChatColor.LIGHT_PURPLE + "Shift＋右クリックで切り替え")));
        if (!recipes.isEmpty() && index < recipes.size() - 1) inventory.setItem(53, menuItem(Material.ARROW, ChatColor.YELLOW + "次のレシピ", List.of()));
        recipeStates.put(player.getUniqueId(), new RecipeState(material, index, List.copyOf(recipes), fromFavorites));
        player.openInventory(inventory);
    }

    private List<Recipe> findRecipes(Material material) {
        List<Recipe> recipes = new ArrayList<>();
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!(recipe instanceof ShapedRecipe) && !(recipe instanceof ShapelessRecipe)) continue;
            ItemStack result = recipe.getResult();
            if (result != null && result.getType() == material) recipes.add(recipe);
        }
        return recipes;
    }

    private void drawRecipe(Inventory inventory, Recipe recipe) {
        int[] gridSlots = {10, 11, 12, 19, 20, 21, 28, 29, 30};
        if (recipe instanceof ShapedRecipe shaped) {
            String[] shape = shaped.getShape();
            Map<Character, ItemStack> ingredients = shaped.getIngredientMap();
            for (int row = 0; row < Math.min(3, shape.length); row++) {
                String line = shape[row];
                for (int col = 0; col < Math.min(3, line.length()); col++) {
                    ItemStack ingredient = ingredients.get(line.charAt(col));
                    if (ingredient != null && ingredient.getType() != Material.AIR) {
                        inventory.setItem(gridSlots[row * 3 + col], displayIngredient(ingredient));
                    }
                }
            }
        } else if (recipe instanceof ShapelessRecipe shapeless) {
            List<ItemStack> ingredients = shapeless.getIngredientList();
            for (int i = 0; i < Math.min(gridSlots.length, ingredients.size()); i++) {
                ItemStack ingredient = ingredients.get(i);
                if (ingredient != null && ingredient.getType() != Material.AIR) inventory.setItem(gridSlots[i], displayIngredient(ingredient));
            }
        }
        inventory.setItem(23, menuItem(Material.ARROW, ChatColor.YELLOW + "クラフト", List.of()));
        ItemStack result = recipe.getResult().clone();
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + JapaneseItemNames.name(result.getType()) + ChatColor.GRAY + " ×" + result.getAmount());
            result.setItemMeta(meta);
        }
        inventory.setItem(25, result);
    }

    private ItemStack displayIngredient(ItemStack source) {
        ItemStack item = source.clone();
        if (item.getAmount() < 1) item.setAmount(1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + JapaneseItemNames.name(item.getType()));
            meta.setLore(List.of(ChatColor.GRAY + "ID: " + item.getType().getKey()));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals(MAIN_GUI_TITLE) && !title.equals(SEARCH_GUI_TITLE) && !title.equals(FAVORITES_GUI_TITLE) && !title.equals(RECIPE_GUI_TITLE)) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (title.equals(MAIN_GUI_TITLE)) {
            switch (event.getRawSlot()) {
                case 10 -> beginSearch(player);
                case 12 -> openFavorites(player, 0);
                case 14 -> { player.closeInventory(); player.sendMessage(ChatColor.GREEN + "確認したいチェストをクリックしてください。"); }
                case 16 -> { player.closeInventory(); selectionModes.put(player.getUniqueId(), SelectionMode.ADD); player.sendMessage(ChatColor.GREEN + "登録するチェストを左クリックしてください。"); }
                case 18 -> { player.closeInventory(); selectionModes.put(player.getUniqueId(), SelectionMode.REMOVE); player.sendMessage(ChatColor.YELLOW + "登録解除するチェストを左クリックしてください。"); }
                case 22 -> player.closeInventory();
                default -> { }
            }
            return;
        }

        if (title.equals(SEARCH_GUI_TITLE) || title.equals(FAVORITES_GUI_TITLE)) {
            SearchState state = searchStates.get(player.getUniqueId());
            if (state == null) return;
            int slot = event.getRawSlot();
            if (slot == 45 && state.page() > 0) { openResultPage(player, state.query(), state.page() - 1, state.results(), state.favoritesOnly()); return; }
            if (slot == 53) { openResultPage(player, state.query(), state.page() + 1, state.results(), state.favoritesOnly()); return; }
            if (slot == 48) { openMainGui(player); return; }
            if (slot < 0 || slot >= RESULTS_PER_PAGE) return;
            int index = state.page() * RESULTS_PER_PAGE + slot;
            if (index >= state.results().size()) return;
            Material material = state.results().get(index);
            if (event.isShiftClick() && event.isRightClick()) {
                toggleFavorite(player, material);
                if (state.favoritesOnly()) openFavorites(player, state.page());
                else openResultPage(player, state.query(), state.page(), state.results(), false);
                return;
            }
            if (event.isLeftClick() || event.isRightClick()) openRecipeGui(player, material, 0, state.favoritesOnly());
            return;
        }

        RecipeState state = recipeStates.get(player.getUniqueId());
        if (state == null) return;
        int slot = event.getRawSlot();
        if (slot == 45 && state.index() > 0) openRecipeGui(player, state.material(), state.index() - 1, state.fromFavorites());
        else if (slot == 53 && state.index() < state.recipes().size() - 1) openRecipeGui(player, state.material(), state.index() + 1, state.fromFavorites());
        else if (slot == 48) {
            SearchState previous = searchStates.get(player.getUniqueId());
            if (previous == null) openMainGui(player);
            else openResultPage(player, previous.query(), previous.page(), previous.results(), previous.favoritesOnly());
        } else if (slot == 49 && event.isShiftClick() && event.isRightClick()) {
            toggleFavorite(player, state.material());
            openRecipeGui(player, state.material(), state.index(), state.fromFavorites());
        }
    }

    private void toggleFavorite(Player player, Material material) {
        Set<String> favorites = favorites(player.getUniqueId());
        if (favorites.remove(material.name())) {
            player.sendMessage(ChatColor.YELLOW + JapaneseItemNames.name(material) + " をお気に入りから解除しました。");
        } else {
            favorites.add(material.name());
            player.sendMessage(ChatColor.GOLD + JapaneseItemNames.name(material) + " をお気に入りに登録しました。");
        }
        favoritesData.set("players." + player.getUniqueId(), new ArrayList<>(favorites));
        saveFavoritesData();
    }

    private Set<String> favorites(UUID uuid) {
        return favoritesCache.computeIfAbsent(uuid, id -> new LinkedHashSet<>(favoritesData.getStringList("players." + id)));
    }

    private ItemStack menuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
        meta.setDisplayName(name);
        if (!lore.isEmpty()) meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onChestClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || !isChest(block.getType())) return;
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
            if (existingUuid == null) player.sendMessage(ChatColor.YELLOW + "このチェストは登録されていません。");
            else if (existingUuid.equals(player.getUniqueId().toString()) || player.hasPermission("smartcraft.admin")) {
                ownershipData.set(key, null);
                saveOwnershipData();
                player.sendMessage(ChatColor.GREEN + "資材チェストの登録を解除しました。");
            } else player.sendMessage(ChatColor.RED + "他のプレイヤーの資材チェストは解除できません。所有者: " + ownerName(key));
            return;
        }
        String ownerUuid = ownershipData.getString(key + ".uuid");
        if (ownerUuid == null) player.sendMessage(ChatColor.GRAY + "このチェストは資材チェストとして登録されていません。");
        else player.sendMessage(ChatColor.AQUA + "この資材チェストの所有者: " + ChatColor.WHITE + ownerName(key));
    }

    private boolean isChest(Material material) { return material == Material.CHEST || material == Material.TRAPPED_CHEST; }
    private String locationKey(Location location) { return "chests." + location.getWorld().getUID() + "." + location.getBlockX() + "." + location.getBlockY() + "." + location.getBlockZ(); }
    private String ownerName(String key) { String name = ownershipData.getString(key + ".name"); return name == null ? "不明" : name; }

    private void loadData() {
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) getLogger().warning("Could not create plugin data folder.");
        ownershipFile = new File(getDataFolder(), "chest-owners.yml");
        ownershipData = YamlConfiguration.loadConfiguration(ownershipFile);
        favoritesFile = new File(getDataFolder(), "favorites.yml");
        favoritesData = YamlConfiguration.loadConfiguration(favoritesFile);
    }

    private void saveOwnershipData() { saveYaml(ownershipData, ownershipFile, "chest ownership"); }
    private void saveFavoritesData() { saveYaml(favoritesData, favoritesFile, "favorites"); }
    private void saveYaml(YamlConfiguration data, File file, String label) {
        if (data == null || file == null) return;
        try { data.save(file); }
        catch (IOException e) { getLogger().severe("Failed to save " + label + " data: " + e.getMessage()); }
    }

    private String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).replace("minecraft:", "").replace("_", "").replace(" ", "").replace("　", "").replace("の", "");
    }
}

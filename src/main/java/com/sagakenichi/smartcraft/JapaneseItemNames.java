package com.sagakenichi.smartcraft;

import org.bukkit.Material;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

final class JapaneseItemNames {
    private static final Map<String, String> DIRECT = new LinkedHashMap<>();
    private static final Map<String, String> WOODS = new LinkedHashMap<>();
    private static final Map<String, String> COLORS = new LinkedHashMap<>();
    private static final Map<String, String> TOOL_MATERIALS = new LinkedHashMap<>();
    private static final Map<String, String> WORDS = new LinkedHashMap<>();

    static {
        WOODS.put("OAK", "オーク");
        WOODS.put("SPRUCE", "トウヒ");
        WOODS.put("BIRCH", "シラカバ");
        WOODS.put("JUNGLE", "ジャングル");
        WOODS.put("ACACIA", "アカシア");
        WOODS.put("DARK_OAK", "ダークオーク");
        WOODS.put("MANGROVE", "マングローブ");
        WOODS.put("CHERRY", "サクラ");
        WOODS.put("BAMBOO", "竹");
        WOODS.put("CRIMSON", "真紅");
        WOODS.put("WARPED", "歪んだ");

        COLORS.put("WHITE", "白色");
        COLORS.put("LIGHT_GRAY", "薄灰色");
        COLORS.put("GRAY", "灰色");
        COLORS.put("BLACK", "黒色");
        COLORS.put("BROWN", "茶色");
        COLORS.put("RED", "赤色");
        COLORS.put("ORANGE", "橙色");
        COLORS.put("YELLOW", "黄色");
        COLORS.put("LIME", "黄緑色");
        COLORS.put("GREEN", "緑色");
        COLORS.put("CYAN", "青緑色");
        COLORS.put("LIGHT_BLUE", "空色");
        COLORS.put("BLUE", "青色");
        COLORS.put("PURPLE", "紫色");
        COLORS.put("MAGENTA", "赤紫色");
        COLORS.put("PINK", "桃色");

        TOOL_MATERIALS.put("WOODEN", "木");
        TOOL_MATERIALS.put("STONE", "石");
        TOOL_MATERIALS.put("IRON", "鉄");
        TOOL_MATERIALS.put("GOLDEN", "金");
        TOOL_MATERIALS.put("DIAMOND", "ダイヤモンド");
        TOOL_MATERIALS.put("NETHERITE", "ネザライト");
        TOOL_MATERIALS.put("LEATHER", "革");
        TOOL_MATERIALS.put("CHAINMAIL", "チェーン");
        TOOL_MATERIALS.put("TURTLE", "カメ");

        direct("AIR", "空気");
        direct("STONE", "石"); direct("GRANITE", "花崗岩"); direct("DIORITE", "閃緑岩"); direct("ANDESITE", "安山岩");
        direct("DEEPSLATE", "深層岩"); direct("COBBLESTONE", "丸石"); direct("COBBLED_DEEPSLATE", "深層岩の丸石");
        direct("BEDROCK", "岩盤"); direct("SAND", "砂"); direct("RED_SAND", "赤い砂"); direct("GRAVEL", "砂利");
        direct("DIRT", "土"); direct("COARSE_DIRT", "粗い土"); direct("ROOTED_DIRT", "根付いた土"); direct("GRASS_BLOCK", "草ブロック");
        direct("PODZOL", "ポドゾル"); direct("MYCELIUM", "菌糸"); direct("MUD", "泥"); direct("CLAY", "粘土");
        direct("BRICKS", "レンガブロック"); direct("BRICK", "レンガ"); direct("GLASS", "ガラス"); direct("GLASS_PANE", "ガラス板");
        direct("CHEST", "チェスト"); direct("TRAPPED_CHEST", "トラップチェスト"); direct("ENDER_CHEST", "エンダーチェスト");
        direct("CRAFTING_TABLE", "作業台"); direct("CRAFTER", "自動作業台"); direct("FURNACE", "かまど"); direct("BLAST_FURNACE", "溶鉱炉");
        direct("SMOKER", "燻製器"); direct("ANVIL", "金床"); direct("CHIPPED_ANVIL", "欠けた金床"); direct("DAMAGED_ANVIL", "壊れかけの金床");
        direct("ENCHANTING_TABLE", "エンチャントテーブル"); direct("SMITHING_TABLE", "鍛冶台"); direct("CARTOGRAPHY_TABLE", "製図台");
        direct("FLETCHING_TABLE", "矢細工台"); direct("LOOM", "機織り機"); direct("STONECUTTER", "石切台"); direct("GRINDSTONE", "砥石");
        direct("BARREL", "樽"); direct("HOPPER", "ホッパー"); direct("DISPENSER", "ディスペンサー"); direct("DROPPER", "ドロッパー");
        direct("OBSERVER", "オブザーバー"); direct("PISTON", "ピストン"); direct("STICKY_PISTON", "粘着ピストン");
        direct("REDSTONE", "レッドストーンダスト"); direct("REDSTONE_TORCH", "レッドストーントーチ"); direct("REPEATER", "レッドストーンリピーター");
        direct("COMPARATOR", "レッドストーンコンパレーター"); direct("LEVER", "レバー"); direct("TARGET", "的"); direct("DAYLIGHT_DETECTOR", "日照センサー");
        direct("TNT", "TNT"); direct("SLIME_BLOCK", "スライムブロック"); direct("HONEY_BLOCK", "ハチミツブロック");
        direct("TORCH", "松明"); direct("SOUL_TORCH", "魂の松明"); direct("LANTERN", "ランタン"); direct("SOUL_LANTERN", "魂のランタン");
        direct("CAMPFIRE", "焚き火"); direct("SOUL_CAMPFIRE", "魂の焚き火"); direct("CHAIN", "鎖"); direct("LADDER", "はしご");
        direct("SCAFFOLDING", "足場"); direct("BOOKSHELF", "本棚"); direct("CHISELED_BOOKSHELF", "模様入りの本棚");
        direct("NOTE_BLOCK", "音符ブロック"); direct("JUKEBOX", "ジュークボックス"); direct("SPAWNER", "スポナー"); direct("BEACON", "ビーコン");
        direct("CONDUIT", "コンジット"); direct("LODESTONE", "ロードストーン"); direct("RESPAWN_ANCHOR", "リスポーンアンカー");
        direct("OBSIDIAN", "黒曜石"); direct("CRYING_OBSIDIAN", "泣く黒曜石"); direct("NETHERRACK", "ネザーラック");
        direct("SOUL_SAND", "ソウルサンド"); direct("SOUL_SOIL", "ソウルソイル"); direct("BASALT", "玄武岩"); direct("BLACKSTONE", "ブラックストーン");
        direct("END_STONE", "エンドストーン"); direct("PURPUR_BLOCK", "プルプァブロック"); direct("PRISMARINE", "プリズマリン");
        direct("SEA_LANTERN", "シーランタン"); direct("GLOWSTONE", "グロウストーン"); direct("MAGMA_BLOCK", "マグマブロック");
        direct("ICE", "氷"); direct("PACKED_ICE", "氷塊"); direct("BLUE_ICE", "青氷"); direct("SNOW_BLOCK", "雪ブロック");
        direct("POWDER_SNOW_BUCKET", "粉雪入りバケツ"); direct("WATER_BUCKET", "水入りバケツ"); direct("LAVA_BUCKET", "溶岩入りバケツ");
        direct("BUCKET", "バケツ"); direct("MILK_BUCKET", "ミルク入りバケツ"); direct("COD_BUCKET", "タラ入りバケツ");
        direct("SALMON_BUCKET", "サケ入りバケツ"); direct("PUFFERFISH_BUCKET", "フグ入りバケツ"); direct("TROPICAL_FISH_BUCKET", "熱帯魚入りバケツ");
        direct("AXOLOTL_BUCKET", "ウーパールーパー入りバケツ"); direct("TADPOLE_BUCKET", "オタマジャクシ入りバケツ");
        direct("DIAMOND", "ダイヤモンド"); direct("EMERALD", "エメラルド"); direct("COAL", "石炭"); direct("CHARCOAL", "木炭");
        direct("IRON_INGOT", "鉄インゴット"); direct("GOLD_INGOT", "金インゴット"); direct("COPPER_INGOT", "銅インゴット");
        direct("NETHERITE_INGOT", "ネザライトインゴット"); direct("NETHERITE_SCRAP", "ネザライトの欠片"); direct("RAW_IRON", "鉄の原石");
        direct("RAW_GOLD", "金の原石"); direct("RAW_COPPER", "銅の原石"); direct("QUARTZ", "ネザークォーツ"); direct("AMETHYST_SHARD", "アメジストの欠片");
        direct("LAPIS_LAZULI", "ラピスラズリ"); direct("FLINT", "火打石"); direct("CLAY_BALL", "粘土玉"); direct("SNOWBALL", "雪玉");
        direct("STICK", "棒"); direct("BOWL", "ボウル"); direct("STRING", "糸"); direct("FEATHER", "羽根"); direct("LEATHER", "革");
        direct("RABBIT_HIDE", "ウサギの皮"); direct("PAPER", "紙"); direct("BOOK", "本"); direct("WRITABLE_BOOK", "本と羽根ペン"); direct("WRITTEN_BOOK", "記入済みの本");
        direct("MAP", "白紙の地図"); direct("FILLED_MAP", "地図"); direct("COMPASS", "コンパス"); direct("RECOVERY_COMPASS", "リカバリーコンパス");
        direct("CLOCK", "時計"); direct("SPYGLASS", "望遠鏡"); direct("ELYTRA", "エリトラ"); direct("SADDLE", "鞍"); direct("LEAD", "リード");
        direct("NAME_TAG", "名札"); direct("MINECART", "トロッコ"); direct("CHEST_MINECART", "チェスト付きのトロッコ");
        direct("FURNACE_MINECART", "かまど付きのトロッコ"); direct("HOPPER_MINECART", "ホッパー付きのトロッコ"); direct("TNT_MINECART", "TNT付きのトロッコ");
        direct("RAIL", "レール"); direct("POWERED_RAIL", "パワードレール"); direct("DETECTOR_RAIL", "ディテクターレール"); direct("ACTIVATOR_RAIL", "アクティベーターレール");
        direct("APPLE", "リンゴ"); direct("GOLDEN_APPLE", "金のリンゴ"); direct("ENCHANTED_GOLDEN_APPLE", "エンチャントされた金のリンゴ");
        direct("BREAD", "パン"); direct("COOKIE", "クッキー"); direct("CAKE", "ケーキ"); direct("PUMPKIN_PIE", "パンプキンパイ");
        direct("MELON_SLICE", "スイカの薄切り"); direct("SWEET_BERRIES", "スイートベリー"); direct("GLOW_BERRIES", "グロウベリー");
        direct("CARROT", "ニンジン"); direct("GOLDEN_CARROT", "金のニンジン"); direct("POTATO", "ジャガイモ"); direct("BAKED_POTATO", "ベイクドポテト");
        direct("POISONOUS_POTATO", "青くなったジャガイモ"); direct("BEETROOT", "ビートルート"); direct("BEETROOT_SOUP", "ビートルートスープ");
        direct("MUSHROOM_STEW", "キノコシチュー"); direct("RABBIT_STEW", "ウサギシチュー"); direct("SUSPICIOUS_STEW", "怪しげなシチュー");
        direct("BEEF", "生の牛肉"); direct("COOKED_BEEF", "ステーキ"); direct("PORKCHOP", "生の豚肉"); direct("COOKED_PORKCHOP", "焼き豚");
        direct("CHICKEN", "生の鶏肉"); direct("COOKED_CHICKEN", "焼き鳥"); direct("MUTTON", "生の羊肉"); direct("COOKED_MUTTON", "焼いた羊肉");
        direct("RABBIT", "生の兎肉"); direct("COOKED_RABBIT", "焼き兎肉"); direct("COD", "生鱈"); direct("COOKED_COD", "焼き鱈");
        direct("SALMON", "生鮭"); direct("COOKED_SALMON", "焼き鮭"); direct("TROPICAL_FISH", "熱帯魚"); direct("PUFFERFISH", "フグ");
        direct("ROTTEN_FLESH", "腐った肉"); direct("SPIDER_EYE", "クモの目"); direct("FERMENTED_SPIDER_EYE", "発酵したクモの目");
        direct("BONE", "骨"); direct("BONE_MEAL", "骨粉"); direct("GUNPOWDER", "火薬"); direct("ENDER_PEARL", "エンダーパール");
        direct("ENDER_EYE", "エンダーアイ"); direct("BLAZE_ROD", "ブレイズロッド"); direct("BLAZE_POWDER", "ブレイズパウダー");
        direct("GHAST_TEAR", "ガストの涙"); direct("MAGMA_CREAM", "マグマクリーム"); direct("SLIME_BALL", "スライムボール");
        direct("PHANTOM_MEMBRANE", "ファントムの皮膜"); direct("SHULKER_SHELL", "シュルカーの殻"); direct("NAUTILUS_SHELL", "オウムガイの殻");
        direct("HEART_OF_THE_SEA", "海洋の心"); direct("NETHER_STAR", "ネザースター"); direct("TOTEM_OF_UNDYING", "不死のトーテム");
        direct("EXPERIENCE_BOTTLE", "エンチャントの瓶"); direct("DRAGON_BREATH", "ドラゴンブレス"); direct("GLASS_BOTTLE", "ガラス瓶");
        direct("POTION", "ポーション"); direct("SPLASH_POTION", "スプラッシュポーション"); direct("LINGERING_POTION", "残留ポーション");
        direct("BOW", "弓"); direct("CROSSBOW", "クロスボウ"); direct("ARROW", "矢"); direct("SPECTRAL_ARROW", "光の矢");
        direct("TRIDENT", "トライデント"); direct("MACE", "メイス"); direct("SHIELD", "盾"); direct("FISHING_ROD", "釣竿");
        direct("FLINT_AND_STEEL", "火打石と打ち金"); direct("SHEARS", "ハサミ"); direct("BRUSH", "ブラシ");
        direct("FIREWORK_ROCKET", "ロケット花火"); direct("FIREWORK_STAR", "花火の星"); direct("WIND_CHARGE", "ウィンドチャージ");
        direct("TRIAL_KEY", "試練の鍵"); direct("OMINOUS_TRIAL_KEY", "不吉な試練の鍵"); direct("BREEZE_ROD", "ブリーズロッド");
        direct("HEAVY_CORE", "ヘビーコア"); direct("ARMADILLO_SCUTE", "アルマジロのウロコ"); direct("TURTLE_SCUTE", "カメのウロコ");
        direct("BUNDLE", "バンドル"); direct("DECORATED_POT", "飾り壺"); direct("FLOWER_POT", "植木鉢"); direct("PAINTING", "絵画");
        direct("ITEM_FRAME", "額縁"); direct("GLOW_ITEM_FRAME", "輝く額縁"); direct("ARMOR_STAND", "防具立て");
        direct("EGG", "卵"); direct("TURTLE_EGG", "カメの卵"); direct("SNIFFER_EGG", "スニッファーの卵");
        direct("WHEAT", "小麦"); direct("WHEAT_SEEDS", "小麦の種"); direct("PUMPKIN_SEEDS", "カボチャの種"); direct("MELON_SEEDS", "スイカの種");
        direct("BEETROOT_SEEDS", "ビートルートの種"); direct("COCOA_BEANS", "カカオ豆"); direct("SUGAR_CANE", "サトウキビ"); direct("SUGAR", "砂糖");
        direct("NETHER_WART", "ネザーウォート"); direct("BAMBOO", "竹"); direct("KELP", "コンブ"); direct("DRIED_KELP", "乾燥した昆布");
        direct("DRIED_KELP_BLOCK", "乾燥した昆布ブロック"); direct("CACTUS", "サボテン"); direct("VINE", "ツタ"); direct("LILY_PAD", "スイレンの葉");
        direct("SUNFLOWER", "ヒマワリ"); direct("DANDELION", "タンポポ"); direct("POPPY", "ポピー"); direct("BLUE_ORCHID", "ヒスイラン");
        direct("ALLIUM", "アリウム"); direct("AZURE_BLUET", "ヒナソウ"); direct("OXEYE_DAISY", "フランスギク"); direct("CORNFLOWER", "ヤグルマギク");
        direct("LILY_OF_THE_VALLEY", "スズラン"); direct("WITHER_ROSE", "ウィザーローズ"); direct("TORCHFLOWER", "トーチフラワー"); direct("PITCHER_PLANT", "ウツボカズラ");
        direct("CREEPER_HEAD", "クリーパーの頭"); direct("ZOMBIE_HEAD", "ゾンビの頭"); direct("SKELETON_SKULL", "スケルトンの頭蓋骨");
        direct("WITHER_SKELETON_SKULL", "ウィザースケルトンの頭蓋骨"); direct("PLAYER_HEAD", "プレイヤーの頭"); direct("DRAGON_HEAD", "ドラゴンの頭");
        direct("PIGLIN_HEAD", "ピグリンの頭"); direct("DRAGON_EGG", "ドラゴンの卵");

        WORDS.put("ACACIA", "アカシア"); WORDS.put("AMETHYST", "アメジスト"); WORDS.put("ANCIENT", "古代の");
        WORDS.put("BAMBOO", "竹"); WORDS.put("BASALT", "玄武岩"); WORDS.put("BLACKSTONE", "ブラックストーン");
        WORDS.put("BLAZE", "ブレイズ"); WORDS.put("BONE", "骨"); WORDS.put("BRAIN", "脳"); WORDS.put("BREEZE", "ブリーズ");
        WORDS.put("BRICK", "レンガ"); WORDS.put("BUBBLE", "気泡"); WORDS.put("CALCITE", "方解石"); WORDS.put("CHERRY", "サクラ");
        WORDS.put("CHISELED", "模様入りの"); WORDS.put("COBBLED", "丸石"); WORDS.put("COPPER", "銅"); WORDS.put("CRACKED", "ひび割れた");
        WORDS.put("CRIMSON", "真紅の"); WORDS.put("CUT", "切り込み入りの"); WORDS.put("DARK", "ダーク"); WORDS.put("DEEPSLATE", "深層岩");
        WORDS.put("END", "エンド"); WORDS.put("ENDER", "エンダー"); WORDS.put("EXPOSED", "風化した"); WORDS.put("GHAST", "ガスト");
        WORDS.put("GILDED", "きらめく"); WORDS.put("GLOW", "グロウ"); WORDS.put("HONEY", "ハチミツ"); WORDS.put("INFESTED", "虫食い");
        WORDS.put("JUNGLE", "ジャングル"); WORDS.put("MANGROVE", "マングローブ"); WORDS.put("MOSSY", "苔むした");
        WORDS.put("NETHER", "ネザー"); WORDS.put("NETHERITE", "ネザライト"); WORDS.put("OAK", "オーク"); WORDS.put("OXIDIZED", "酸化した");
        WORDS.put("POLISHED", "磨かれた"); WORDS.put("PRISMARINE", "プリズマリン"); WORDS.put("PURPUR", "プルプァ");
        WORDS.put("QUARTZ", "クォーツ"); WORDS.put("RAW", "原石"); WORDS.put("RED", "赤い"); WORDS.put("REINFORCED", "強化された");
        WORDS.put("SCULK", "スカルク"); WORDS.put("SMOOTH", "滑らかな"); WORDS.put("SOUL", "魂の"); WORDS.put("SPRUCE", "トウヒ");
        WORDS.put("STONE", "石"); WORDS.put("STRIPPED", "樹皮を剥いだ"); WORDS.put("TUFF", "凝灰岩"); WORDS.put("WARPED", "歪んだ");
        WORDS.put("WAXED", "錆止めされた"); WORDS.put("WEATHERED", "錆びた"); WORDS.put("WITHER", "ウィザー");
    }

    private JapaneseItemNames() {}

    static String name(Material material) {
        String key = material.name();
        String direct = DIRECT.get(key);
        if (direct != null) return direct;

        String generated = woodName(key);
        if (generated != null) return generated;
        generated = colorName(key);
        if (generated != null) return generated;
        generated = toolName(key);
        if (generated != null) return generated;
        generated = spawnEggName(key);
        if (generated != null) return generated;

        return genericName(key);
    }

    static boolean matches(Material material, String rawQuery) {
        String query = normalize(rawQuery);
        if (query.isEmpty()) return false;
        String id = normalize(material.name());
        String jp = normalize(name(material));
        return id.contains(query) || jp.contains(query) || normalize("minecraft:" + material.name().toLowerCase(Locale.ROOT)).contains(query);
    }

    private static String woodName(String key) {
        for (Map.Entry<String, String> entry : WOODS.entrySet()) {
            String wood = entry.getKey();
            String jp = entry.getValue();
            if (key.equals(wood + "_PLANKS")) return jp + "の板材";
            if (key.equals(wood + "_LOG")) return jp + "の原木";
            if (key.equals(wood + "_WOOD")) return jp + "の木";
            if (key.equals("STRIPPED_" + wood + "_LOG")) return "樹皮を剥いだ" + jp + "の原木";
            if (key.equals("STRIPPED_" + wood + "_WOOD")) return "樹皮を剥いだ" + jp + "の木";
            if (key.equals(wood + "_STEM")) return jp + "の幹";
            if (key.equals(wood + "_HYPHAE")) return jp + "の菌糸";
            if (key.equals("STRIPPED_" + wood + "_STEM")) return "表皮を剥いだ" + jp + "の幹";
            if (key.equals("STRIPPED_" + wood + "_HYPHAE")) return "表皮を剥いだ" + jp + "の菌糸";
            if (key.equals(wood + "_SAPLING")) return jp + "の苗木";
            if (key.equals(wood + "_LEAVES")) return jp + "の葉";
            if (key.equals(wood + "_SLAB")) return jp + "のハーフブロック";
            if (key.equals(wood + "_STAIRS")) return jp + "の階段";
            if (key.equals(wood + "_FENCE")) return jp + "のフェンス";
            if (key.equals(wood + "_FENCE_GATE")) return jp + "のフェンスゲート";
            if (key.equals(wood + "_DOOR")) return jp + "のドア";
            if (key.equals(wood + "_TRAPDOOR")) return jp + "のトラップドア";
            if (key.equals(wood + "_PRESSURE_PLATE")) return jp + "の感圧板";
            if (key.equals(wood + "_BUTTON")) return jp + "のボタン";
            if (key.equals(wood + "_SIGN")) return jp + "の看板";
            if (key.equals(wood + "_HANGING_SIGN")) return jp + "の吊り看板";
            if (key.equals(wood + "_BOAT")) return jp + "のボート";
            if (key.equals(wood + "_CHEST_BOAT")) return "チェスト付きの" + jp + "のボート";
            if (key.equals(wood + "_RAFT")) return jp + "のイカダ";
            if (key.equals(wood + "_CHEST_RAFT")) return "チェスト付きの" + jp + "のイカダ";
            if (key.equals(wood + "_MOSAIC")) return jp + "のモザイク";
            if (key.equals(wood + "_MOSAIC_SLAB")) return jp + "のモザイクのハーフブロック";
            if (key.equals(wood + "_MOSAIC_STAIRS")) return jp + "のモザイクの階段";
        }
        return null;
    }

    private static String colorName(String key) {
        for (Map.Entry<String, String> entry : COLORS.entrySet()) {
            String color = entry.getKey();
            String jp = entry.getValue();
            if (key.equals(color + "_WOOL")) return jp + "の羊毛";
            if (key.equals(color + "_CARPET")) return jp + "のカーペット";
            if (key.equals(color + "_BED")) return jp + "のベッド";
            if (key.equals(color + "_BANNER")) return jp + "の旗";
            if (key.equals(color + "_CANDLE")) return jp + "のろうそく";
            if (key.equals(color + "_STAINED_GLASS")) return jp + "の色付きガラス";
            if (key.equals(color + "_STAINED_GLASS_PANE")) return jp + "の色付きガラス板";
            if (key.equals(color + "_TERRACOTTA")) return jp + "のテラコッタ";
            if (key.equals(color + "_GLAZED_TERRACOTTA")) return jp + "の彩釉テラコッタ";
            if (key.equals(color + "_CONCRETE")) return jp + "のコンクリート";
            if (key.equals(color + "_CONCRETE_POWDER")) return jp + "のコンクリートパウダー";
            if (key.equals(color + "_SHULKER_BOX")) return jp + "のシュルカーボックス";
            if (key.equals(color + "_DYE")) return jp + "の染料";
        }
        return null;
    }

    private static String toolName(String key) {
        for (Map.Entry<String, String> entry : TOOL_MATERIALS.entrySet()) {
            String prefix = entry.getKey();
            String jp = entry.getValue();
            if (key.equals(prefix + "_SWORD")) return jp + "の剣";
            if (key.equals(prefix + "_PICKAXE")) return jp + "のツルハシ";
            if (key.equals(prefix + "_AXE")) return jp + "の斧";
            if (key.equals(prefix + "_SHOVEL")) return jp + "のシャベル";
            if (key.equals(prefix + "_HOE")) return jp + "のクワ";
            if (key.equals(prefix + "_HELMET")) return jp + "のヘルメット";
            if (key.equals(prefix + "_CHESTPLATE")) return jp + "のチェストプレート";
            if (key.equals(prefix + "_LEGGINGS")) return jp + "のレギンス";
            if (key.equals(prefix + "_BOOTS")) return jp + "のブーツ";
            if (key.equals(prefix + "_HORSE_ARMOR")) return jp + "の馬鎧";
        }
        return null;
    }

    private static String spawnEggName(String key) {
        if (!key.endsWith("_SPAWN_EGG")) return null;
        String mob = key.substring(0, key.length() - "_SPAWN_EGG".length());
        return genericName(mob) + "のスポーンエッグ";
    }

    private static String genericName(String key) {
        String result = key;
        result = result.replace("_WALL", "の塀").replace("_SLAB", "のハーフブロック").replace("_STAIRS", "の階段");
        result = result.replace("_BLOCK", "ブロック").replace("_BRICKS", "レンガ").replace("_BRICK", "レンガ");
        result = result.replace("_ORE", "鉱石").replace("_PILLAR", "の柱").replace("_TILES", "タイル").replace("_TILE", "タイル");
        result = result.replace("_CORAL_BLOCK", "サンゴブロック").replace("_CORAL_FAN", "ウチワサンゴ").replace("_CORAL", "サンゴ");
        result = result.replace("_PRESSURE_PLATE", "の感圧板").replace("_TRAPDOOR", "のトラップドア").replace("_DOOR", "のドア");
        result = result.replace("_BUTTON", "のボタン").replace("_FENCE_GATE", "のフェンスゲート").replace("_FENCE", "のフェンス");
        result = result.replace("_HANGING_SIGN", "の吊り看板").replace("_SIGN", "の看板");
        result = result.replace("_NUGGET", "塊").replace("_INGOT", "インゴット").replace("_SHARD", "の欠片");

        for (Map.Entry<String, String> entry : WORDS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        result = result.replace('_', ' ').trim();
        if (result.matches("[A-Z0-9 ]+")) {
            return titleCase(result.toLowerCase(Locale.ROOT));
        }
        return result.replace(" ", "");
    }

    private static String titleCase(String value) {
        StringBuilder out = new StringBuilder();
        for (String part : value.split(" ")) {
            if (part.isEmpty()) continue;
            if (!out.isEmpty()) out.append(' ');
            out.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return out.toString();
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replace("minecraft:", "")
                .replace("_", "")
                .replace(" ", "")
                .replace("　", "")
                .replace("・", "")
                .replace("の", "");
    }

    private static void direct(String key, String value) {
        DIRECT.put(key, value);
    }
}

package io.github.siloonk.prisonServer.enchantments;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.enchantments.effects.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class EnchantmentHandler implements Listener {


    public static HashMap<EnchantmentType, Enchantment> enchantments = new HashMap<>();

    public EnchantmentHandler() {
        loadEnchantmentsFromFile();
    }

    public void registerEnchantment(EnchantmentType type, Enchantment enchantment) {
        enchantments.put(type,enchantment);
    }

    public static Enchantment getEnchantment(EnchantmentType type) {
        return enchantments.get(type);
    }

    public static int getEnchantmentLevel(EnchantmentType type, ItemStack item) {
        if (!item.hasItemMeta()) return 0;
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (!container.has(PDCKeys.ENCHANTMENTS_KEY)) return 0;

        PersistentDataContainer enchants = container.get(PDCKeys.ENCHANTMENTS_KEY, PersistentDataType.TAG_CONTAINER);
        NamespacedKey key = getKeyForEnchantment(getEnchantment(type));
        if (!(enchants.has(key))) return 0;
        return enchants.get(key, PersistentDataType.INTEGER);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getType().toString().contains("_PICKAXE")) return;
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();

        item.editMeta(itemMeta -> {
            PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

            int pickaxeEXP = pdc.getOrDefault(PDCKeys.PICKAXE_EXP_KEY, PersistentDataType.INTEGER, 0);
            int pickaxeEXPRequired = pdc.getOrDefault(PDCKeys.PICKAXE_EXP_REQUIRED_KEY, PersistentDataType.INTEGER, 350);
            int pickaxeLevel = pdc.getOrDefault(PDCKeys.PICKAXE_LEVEL, PersistentDataType.INTEGER, 0);
            pdc.set(PDCKeys.PICKAXE_EXP_KEY, PersistentDataType.INTEGER, ++pickaxeEXP);
            MiniMessage miniMessage = MiniMessage.miniMessage();


            List<Component> lore = itemMeta.lore();

            if (lore == null) {
                lore = new ArrayList<>();
            }

            // Levelup
            if (pickaxeEXP >= pickaxeEXPRequired) {
                pdc.set(PDCKeys.PICKAXE_EXP_KEY, PersistentDataType.INTEGER, 0);
                pickaxeEXPRequired = (int) Math.ceil(pickaxeEXPRequired * 1.25);
                pdc.set(PDCKeys.PICKAXE_EXP_REQUIRED_KEY, PersistentDataType.INTEGER, pickaxeEXPRequired);
                pdc.set(PDCKeys.PICKAXE_LEVEL, PersistentDataType.INTEGER, ++pickaxeLevel);
                event.getPlayer().sendMessage(miniMessage.deserialize("<green>Your pickaxe has leveled up!"));
            }

            // Update pickaxe lore
            lore.set(4, miniMessage.deserialize("<aqua>|<reset><white> EXP: <gray>%d / %d".formatted(pickaxeEXP, pickaxeEXPRequired)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lore.set(5, (miniMessage.deserialize("<aqua>| " + getProgressBar(pickaxeEXP, pickaxeEXPRequired)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
            lore.set(3, miniMessage.deserialize("<aqua>|<reset><white> Level: <gray>%d".formatted(pickaxeLevel)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            itemMeta.lore(lore);
        });



        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(PDCKeys.ENCHANTMENTS_KEY)) return;
        PersistentDataContainer enchantsContainer = pdc.get(PDCKeys.ENCHANTMENTS_KEY, PersistentDataType.TAG_CONTAINER);
        for (NamespacedKey key : enchantsContainer.getKeys()) {
            Enchantment enchantment = enchantments.get(EnchantmentType.valueOf(key.getKey().toUpperCase()));
            int level = enchantsContainer.getOrDefault(key, PersistentDataType.INTEGER, 0);
            double chance = enchantment.getBaseChance() + level * ((enchantment.getChanceAtMaxLevel() - enchantment.getBaseChance()) / enchantment.getMaxLevel());


            // Random value to determine success
            Random random = new Random();
            double roll = random.nextDouble(); // Generates a value between 0.0 and 1.0

            if (roll < chance) {
                enchantment.execute(event.getBlock().getLocation(), PrisonServer.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()), level);
            }

        }

    }



    private void loadEnchantmentsFromFile() {
        File file = new File(PrisonServer.getInstance().getDataFolder() + "/config/enchants.yml");
        FileConfiguration config = new YamlConfiguration();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PrisonServer.getInstance().saveResource("config/enchants.yml", false);
        }

        try {
            config.load(file) ;
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }

        for (String key : config.getKeys(false)) {
            ConfigurationSection enchantConfig = config.getConfigurationSection(key);
            EnchantmentType type = EnchantmentType.valueOf(key.toUpperCase());
            MiniMessage miniMessage = MiniMessage.miniMessage();
            Component name = miniMessage.deserialize(enchantConfig.getString(".name"));
            Component description = miniMessage.deserialize(enchantConfig.getString(".description"));
            int maxLevel = enchantConfig.getInt("max_level");
            int baseCost = enchantConfig.getInt("base_cost");
            double costIncrease = enchantConfig.getInt("cost_increase");
            double chanceAtMaxLevel = enchantConfig.getInt("chance_at_max_level");
            double baseChance = enchantConfig.getInt("base_chance");
            Currency currency = Currency.valueOf(enchantConfig.getString("currency").toUpperCase());

            switch (type) {
                case EnchantmentType.EXPLOSIVE ->  {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new ExplosiveEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, radius));
                }
                case EnchantmentType.JACKHAMMER -> {
                    registerEnchantment(type, new JackhammerEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency));
                }
                case EnchantmentType.LUCKY -> {
                    int minAmount = enchantConfig.getInt("min_amount");
                    int maxAmount = enchantConfig.getInt("max_amount");
                    double scaleAmount = enchantConfig.getDouble("scale_amount");
                    registerEnchantment(type, new LuckyEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, minAmount, maxAmount, scaleAmount));
                }
                case EnchantmentType.FORTUNE -> {
                    registerEnchantment(type, new FortuneEnchantment(name, description, maxLevel, baseCost, costIncrease, 100, currency));
                }
                case EnchantmentType.LIGHTNING -> {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new LightningEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, radius));
                }
                case EnchantmentType.ECLIPSE ->  {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new EclipseEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, radius));
                }
                case EnchantmentType.STAR_CORE -> {
                    ArrayList<String> rewards = (ArrayList<String>) enchantConfig.getStringList("rewards");
                    registerEnchantment(type, new StarCoreEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, rewards, currency));
                }
                case EnchantmentType.COSMIC_RIFT -> {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new CosmicRiftEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, radius));
                }
                case EnchantmentType.STARFALL -> {
                    registerEnchantment(type, new StarFallEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency));
                }
                case EnchantmentType.RELIC_SEEKER -> {
                    registerEnchantment(type, new RelicSeekerEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency));
                }
                case EnchantmentType.COSMIC_BLESSING -> {
                    double minBooster = enchantConfig.getDouble("min_multiplier");
                    double maxBooster = enchantConfig.getDouble("max_multiplier");
                    registerEnchantment(type, new CosmicBlessingEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, minBooster, maxBooster));
                }
                case EnchantmentType.OMNI_TREASURE -> {
                    registerEnchantment(type, new OmniTreasureEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency));
                }
                case EnchantmentType.AETHER_SURGE -> {
                    double boostAmount = enchantConfig.getDouble("boost_amount");
                    registerEnchantment(type, new AetherSurgeEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency, boostAmount));
                }
                case EnchantmentType.ECHO_OF_THE_DEEP -> {
                    registerEnchantment(type, new EchoOfTheDeepEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency));
                }
                case EnchantmentType.QUANTUM_MINER -> {
                    registerEnchantment(type, new QuantumMinerEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency));
                }
            }
        }
    }


    private static NamespacedKey getKeyForEnchantment(Enchantment enchantment) {
        if (enchantment instanceof ExplosiveEnchantment) return PDCKeys.EXPLOSIVE_KEY;
        if (enchantment instanceof JackhammerEnchantment) return PDCKeys.JACKHAMMER_KEY;
        if (enchantment instanceof LuckyEnchantment) return PDCKeys.LUCKY_KEY;
        if (enchantment instanceof FortuneEnchantment) return PDCKeys.FORTUNE_KEY;
        if (enchantment instanceof LightningEnchantment) return PDCKeys.LIGHTNING_KEY;
        if (enchantment instanceof EclipseEnchantment) return PDCKeys.ECLIPSE_KEY;
        if (enchantment instanceof StarCoreEnchantment) return PDCKeys.STAR_CORE_KEY;
        if (enchantment instanceof CosmicRiftEnchantment) return PDCKeys.COSMIC_RIFT_KEY;
        if (enchantment instanceof StarFallEnchantment) return PDCKeys.STAR_FALL_KEY;
        if (enchantment instanceof RelicSeekerEnchantment) return PDCKeys.RELIC_SEEKER_KEY;
        if (enchantment instanceof CosmicBlessingEnchantment) return PDCKeys.COSMIC_BLESSING_KEY;
        if (enchantment instanceof AetherSurgeEnchantment) return PDCKeys.AETHER_SURGE_KEY;
        if (enchantment instanceof OmniTreasureEnchantment) return PDCKeys.OMNI_TREASURE;
        if (enchantment instanceof EchoOfTheDeepEnchantment) return PDCKeys.ECHO_OF_THE_DEEP;
        if (enchantment instanceof QuantumMinerEnchantment) return PDCKeys.QUANTUM_MINER_KEY;
        return null;
    }

    private static ArrayList<Component> createPickaxeLore(Player player) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        return new ArrayList<>() {{
            add(miniMessage.deserialize(String.format("<gray>Owned by <white>%s", player.getName())).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("<aqua><bold>Leveling").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("<aqua>|<reset><white> Level: <gray>1").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("<aqua>|<reset><white> EXP: <gray>0 / 350").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("<aqua>| " + getProgressBar(0, 350)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            add(miniMessage.deserialize("<yellow><bold>Enchants <reset><gold>(0)").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }};
    }

    private static String getProgressBar(int current, int target) {
        StringBuilder builder = new StringBuilder("<green>");
        int percentage = Math.toIntExact(Math.round((double) current / target * 30));
        for (int i =0; i < percentage; i++) {
            builder.append(":");
        }

        builder.append("<red>");
        for (int i =0; i < 30 - percentage; i++) {
            builder.append(":");
        }

        builder.append(String.format(" <gray>%.0f%%", (double)current / target * 100));
        return builder.toString();
    }

    public static void applyEnchantment(Player player, ItemStack item, EnchantmentType type, int level) {
        applyEnchantment(player, item, getEnchantment(type), level);
    }

    public static void applyEnchantment(Player player, ItemStack item, Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        List<Component> lore = meta.lore() != null ? meta.lore() : createPickaxeLore(player);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataContainer enchantsContainer = container.get(PDCKeys.ENCHANTMENTS_KEY, PersistentDataType.TAG_CONTAINER);
        if (enchantsContainer == null) enchantsContainer = container.getAdapterContext().newPersistentDataContainer();

        NamespacedKey key = getKeyForEnchantment(enchantment);
        if (key == null) {
            throw new RuntimeException("Invalid enchantment type: " + enchantment);
        }

        if (enchantsContainer.has(key, PersistentDataType.INTEGER)) {
            enchantsContainer.set(key, PersistentDataType.INTEGER, level);
            for (Component line : lore) {
                if (miniMessage.serialize(line).contains(miniMessage.serialize(enchantment.getName()))) {
                    if (level == 0) {
                        lore.remove(line);
                        enchantsContainer.remove(key);
                    }
                    else lore.set(lore.indexOf(line), miniMessage.deserialize("<yellow>| ").append(enchantment.getName().append(miniMessage.deserialize(" %d".formatted(level)))).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                }
            }
        } else {
            enchantsContainer.set(key, PersistentDataType.INTEGER, level);
            lore.add(miniMessage.deserialize("<yellow>| ").append(enchantment.getName().append(miniMessage.deserialize(" %d".formatted(level)))).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lore.set(7, miniMessage.deserialize("<yellow><bold>Enchants <reset><gold>(%d)".formatted(enchantsContainer.getKeys().size())).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }

        container.set(PDCKeys.ENCHANTMENTS_KEY, PersistentDataType.TAG_CONTAINER, enchantsContainer);
        meta.lore(lore);
        item.setItemMeta(meta);
    }
}

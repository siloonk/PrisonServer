package io.github.siloonk.prisonServer.enchantments;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (!item.getType().toString().contains("_PICKAXE")) return;
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(PDCKeys.ENCHANTMENTS_KEY)) return;

        PersistentDataContainer enchantsContainer = pdc.get(PDCKeys.ENCHANTMENTS_KEY, PersistentDataType.TAG_CONTAINER);
        for (NamespacedKey key : enchantsContainer.getKeys()) {
            Enchantment enchantment = enchantments.get(EnchantmentType.valueOf(key.getKey().toUpperCase()));
            int level = enchantsContainer.get(key, PersistentDataType.INTEGER);
            double chance = enchantment.getBaseChance() + level * ((enchantment.getChanceAtMaxLevel() - enchantment.getBaseChance()) / enchantment.getMaxLevel());

            // Random value to determine success
            Random random = new Random();
            double roll = random.nextDouble(); // Generates a value between 0.0 and 1.0

            System.out.println(chance);
            System.out.println(roll);
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

            switch (type) {
                case EnchantmentType.EXPLOSIVE ->  {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new ExplosiveEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, radius));
                }
                case EnchantmentType.JACKHAMMER -> {
                    registerEnchantment(type, new JackhammerEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance));
                }
                case EnchantmentType.LUCKY -> {
                    int minAmount = enchantConfig.getInt("min_amount");
                    int maxAmount = enchantConfig.getInt("max_amount");
                    double scaleAmount = enchantConfig.getDouble("scale_amount");
                    registerEnchantment(type, new LuckyEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, minAmount, maxAmount, scaleAmount));
                }
                case EnchantmentType.FORTUNE -> {
                    registerEnchantment(type, new FortuneEnchantment(name, description, maxLevel, baseCost, costIncrease, 100));
                }
                case EnchantmentType.LIGHTNING -> {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new LightningEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, radius));
                }
                case EnchantmentType.ECLIPSE ->  {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new EclipseEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, radius));
                }
                case EnchantmentType.STAR_CORE -> {
                    ArrayList<String> rewards = (ArrayList<String>) enchantConfig.getStringList("rewards");
                    registerEnchantment(type, new StarCoreEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, rewards));
                }
                case EnchantmentType.COSMIC_RIFT -> {
                    int radius = enchantConfig.getInt("radius");
                    registerEnchantment(type, new CosmicRiftEnchantment(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, radius));
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

package io.github.siloonk.prisonServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.CustomTimingsHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class CustomItems {

    private HashMap<String, ItemStack> customItems = new HashMap<>();
    private Logger logger = PrisonServer.getInstance().getLogger();
    private MiniMessage miniMessage = MiniMessage.miniMessage();

    private CustomItems() {
        loadItems();
    }

    private static final CustomItems instance = new CustomItems();

    public static CustomItems get() {
        return instance;
    }


    private void loadItems() {
        File file = new File(PrisonServer.getInstance().getDataFolder(), "/config/items.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PrisonServer.getInstance().saveResource("config/items.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);
            if (!section.contains("material")) {
                logger.warning(key + " item is missing the material field!");
                continue;
            }

            Material material = Material.getMaterial(section.getString("material"));
            if (material == null) {
                logger.warning(key + " has an invalid material of " + section.getString("material") + "!");
                continue;
            }

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            if (section.contains("name")) {
                meta.displayName(miniMessage.deserialize(config.getString("name")));
            }

            if (section.contains("lore")) {
                List<Component> lore = convertStringListToComponentList(section.getStringList("lore"));
                meta.lore(lore);
            }

            if (!section.contains("shiny")) {
                logger.warning(key + " item is missing the shiny field!");
                continue;
            }

            boolean isShiny = section.getBoolean("shiny");

            if (isShiny) {
                meta.addEnchant(Enchantment.FLAME, 1, false);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setItemMeta(meta);
            customItems.put(key, item);
        }
    }

    private List<Component> convertStringListToComponentList(List<String> list) {
        List<Component> components = new ArrayList<>();
        for (String s : list) {
            components.add(miniMessage.deserialize(s));
        }
        return components;
    }

    public ItemStack getItem(String key) {
        return customItems.get(key);
    }
}

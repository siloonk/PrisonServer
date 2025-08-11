package io.github.siloonk.prisonServer.items;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.utils.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomItems {

    private HashMap<String, ItemStack> items = new HashMap<>();


    public CustomItems() {
        loadItems();
    }

    private FileConfiguration getConfig() {
        File file = new File(PrisonServer.getInstance().getDataFolder(), "/config/items.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PrisonServer.getInstance().saveResource("config/items.yml", false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void loadItems() {
        items.clear();
        FileConfiguration config = getConfig();

        for (String key : config.getKeys(false)) {
            items.put(key, generateItem(config.getConfigurationSection(key)));
        }
    }


    public ItemStack generateItem(ConfigurationSection section) {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        Material material = Material.matchMaterial(section.getString("material"));
        ArrayList<Component> lore = new ArrayList<>();
        if (section.contains("lore")) {
            lore = Util.convertStringListToComponentList(section.getStringList("lore"));
        }
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(miniMessage.deserialize(section.getString("name")).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        if (!lore.isEmpty())
            meta.lore(lore);
        if (section.getBoolean("shiny")) {
            meta.addEnchant(Enchantment.FEATHER_FALLING, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return item;
    }



    public ItemStack getItem(String key) {
        return items.get(key);
    }

    public List<ItemStack> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public List<String> getAllItemNames() {
        return new ArrayList<>(items.keySet());
    }
}

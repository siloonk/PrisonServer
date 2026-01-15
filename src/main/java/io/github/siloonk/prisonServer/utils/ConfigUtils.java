package io.github.siloonk.prisonServer.utils;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    public static boolean isValidLocation(ConfigurationSection section) {
        if (!section.contains("world")) return false;
        if (!section.contains("x")) return false;
        if (!section.contains("y")) return false;
        if (!section.contains("z")) return false;
        return true;
    }

    public static Location getLocation(ConfigurationSection section) {
        return new Location(
            Bukkit.getWorld(section.getString("world")),
            section.getInt("x"),
            section.getInt("y"),
            section.getInt("z")
        );
    }



    public static List<PotionEffect> generatePotionEffects(ConfigurationSection section) {
        ArrayList<PotionEffect> effects = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            PotionEffectType type = RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(NamespacedKey.minecraft(key));
            if (type == null) {
                System.err.println("Could not find potion with type " + key + "!");
                continue;
            }

            int duration = section.getInt(key + ".duration");
            int tier = section.getInt(key + ".tier");
            PotionEffect effect = new PotionEffect(type, duration, tier);
            effects.add(effect);
        }
        return effects;
    }

    /**
     * Generate a item from the config
     * @param section section in which the item is defined
     * @return an item stack with all sections as defined in the configuration section
     */
    public static ItemStack generateItem(ConfigurationSection section) {
        Material type = Material.valueOf(section.getString("type").toUpperCase());
        ItemStack item = new ItemStack(type);

        // Apply all enchantments
        ConfigurationSection enchants = section.getConfigurationSection("enchantments");
        if (enchants == null) return item;

        for (String key : enchants.getKeys(false)) {
            Enchantment ench = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft(key.toLowerCase()));
            if (ench == null) {
                System.err.println(key + " Is not a valid enchantment!");
                return null;
            }
            int level = enchants.getInt(key);
            item.addUnsafeEnchantment(ench, level);
        }

        return item;
    }
}

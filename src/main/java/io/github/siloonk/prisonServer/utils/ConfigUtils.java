package io.github.siloonk.prisonServer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

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
}

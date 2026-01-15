package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.dungeons.abilities.BossAbility;
import io.github.siloonk.prisonServer.dungeons.rewards.*;
import io.github.siloonk.prisonServer.utils.ConfigUtils;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DungeonManager {
    /**
     * List of all dungeons
     */
    private HashMap<String, Dungeon> dungeons = new HashMap<>();

    public DungeonManager() {
        loadDungeons();
    }

    /**
     * Load all dungeons from the dungeon config
     */
    public void loadDungeons() {

        // Get the dungeon folder directory
        File dungeonFolder = getDungeonFolder();

        if (!dungeonFolder.isDirectory()) {
            System.err.println("Something went wrong trying to load dungeons!");
            return;
        }

        // Iterate over all files within the dungeons folder
        for (File file : Objects.requireNonNull(dungeonFolder.listFiles())) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            DungeonConfig dungeonConfig = new DungeonConfig();
            Dungeon dungeon = dungeonConfig.getDungeonFromConfig(config);

            if (dungeon == null) {
                System.err.println("Something went wrong trying to load the dungeon from file: " + file.getName());
                continue;
            }

            String id = file.getName().split("\\.")[0];
            dungeons.put(id, dungeon);
        }

    }


    /**
     * Get the dungeon folder, if this does not exist create it
     * @return file referencing the dungeon folder
     */
    private File getDungeonFolder() {
        File file = new File(PrisonServer.getInstance().getDataFolder() + "/config/dungeons");

        if (!file.exists()) {
            PrisonServer.getInstance().saveResource("config/dungeons/-example-dungeon.yml", false);
        }
        return file;
    }

}

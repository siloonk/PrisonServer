package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class DungeonManager {
    /**
     * List of all dungeons
     */
    private HashMap<String, Dungeon> dungeons = new HashMap<>();

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
            Dungeon dungeon = getDungeonFromConfig(config);

            if (dungeon == null) {
                System.err.println("Something went wrong trying to load the dungeon from file: " + file.getName());
                continue;
            }
        }

    }


    /**
     * Get the dungeon folder, if this does not exist create it
     * @return file referencing the dungeon folder
     */
    private File getDungeonFolder() {
        File file = new File(PrisonServer.getInstance().getDataFolder() + "/config/dungeons");

        if (!file.exists()) {
            PrisonServer.getInstance().saveResource("config/dungeons", false);
        }
        return file;
    }

    /**
     * Generate a dungeon object from a config file
     * @param config dungeon config file
     * @return Dungeon generated from the config file
     */
    private Dungeon getDungeonFromConfig(YamlConfiguration config) {
        // Make sure the boss section is present
        ConfigurationSection boss = config.getConfigurationSection("boss");
        if (boss == null) {
            return null;
        }

        // Make sure the monster section is present
        ConfigurationSection  monsters = config.getConfigurationSection("monsters");
        if (monsters == null) {
            return null;
        }

        // Make sure the dungeon section is present
        ConfigurationSection dungeon = config.getConfigurationSection("dungeon");
        if (dungeon == null) {
            return null;
        }

        // Make sure all REQUIRED parts of the dungeon config are present
        boolean isValidDungeon = checkDungeonSection(dungeon);


        // Generate the dungeon from the data within the dungeon section
        Dungeon generatedDungeon = new Dungeon();
        generatedDungeon.setName(dungeon.getString("name"));
        generatedDungeon.setPlayerSpawn(
            ConfigUtils.getLocation(
                dungeon.getConfigurationSection("player_spawn")
            )
        );


        return generatedDungeon;
    }

    private boolean checkDungeonSection(ConfigurationSection section) {
        if (!section.contains("name")) return false;

        // Player spawn
        if (!section.contains("player_spawn")) return false;
        if (!ConfigUtils.isValidLocation(section.getConfigurationSection("player_spawn"))) return false;

        // Boss spawn
        if (!section.contains("boss_spawn")) return false;
        if (!ConfigUtils.isValidLocation(section.getConfigurationSection("boss_spawn"))) return false;

        // Rewards

        return true;
    }

}

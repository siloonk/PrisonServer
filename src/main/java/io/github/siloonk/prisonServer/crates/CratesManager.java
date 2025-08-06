package io.github.siloonk.prisonServer.crates;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.utils.RandomCollection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;

public class CratesManager {

    private HashMap<String, Crate> crates = new HashMap<>();

    public CratesManager() {
        loadCrates();
    }


    public void loadCrates() {
        crates.clear();

        FileConfiguration config = getConfig();

        for (String key : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(key);

            String displayName = section.getString("display_name");

            int rows = section.getInt("menu.rows");
            Inventory inventory = Bukkit.createInventory(null, rows*9, key);
            ItemStack background = PrisonServer.getInstance().getCustomItems().getItem(section.getString("menu.background"));

            for (int i = 0; i < rows*9;i++) {
                inventory.setItem(i, background);
            }

            for (String slot : section.getConfigurationSection("menu").getKeys(false)) {
                if (slot.equals("background") || slot.equals("row")) continue;
                int slotNumber = Integer.parseInt(slot);
                inventory.setItem(slotNumber, PrisonServer.getInstance().getCustomItems().getItem(section.getString("menu." + slot)));
            }



            ConfigurationSection rewardsSection = section.getConfigurationSection("rewards");


            RandomCollection<String> rewards = new RandomCollection<>();
            for (String reward : rewardsSection.getKeys(false)) {
                int weight = rewardsSection.getInt(reward + ".weight");
                String command = rewardsSection.getString(reward + ".command");
                rewards.add(weight, command);
            }

            Crate crate = new Crate(displayName, inventory, rewards);
            crates.put(key, crate);
        }
    }


    public Crate getCrate(String name) {
        return crates.get(name);
    }



    private FileConfiguration getConfig() {
        File file = new File(PrisonServer.getInstance().getDataFolder(), "/config/crates.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PrisonServer.getInstance().saveResource("/config/crates.yml", false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

}

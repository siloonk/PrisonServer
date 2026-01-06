package io.github.siloonk.prisonServer.data.relics;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Rarity;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class RelicManager {

    private HashMap<Rarity, Relic> relics = new HashMap<>();

    public RelicManager() {
        loadRelics();
    }

    public void loadRelics() {

        File file = new File(PrisonServer.getInstance().getDataFolder(), "/config/relics.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PrisonServer.getInstance().saveResource("config/relics.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            ConfigurationSection relicSection = config.getConfigurationSection(key);

            List<RelicType> relics = relicSection.getStringList("relics").stream().map(r -> RelicType.valueOf(r.toUpperCase())).toList();

            double minBoost = relicSection.getInt("range.min")/ 100d;
            double maxBoost = relicSection.getInt("range.max")/ 100d;

            Relic relic = new Relic(relics, minBoost, maxBoost);
            Rarity rarity = Rarity.valueOf(key.toUpperCase());
            this.relics.put(rarity, relic);
        }
    }

    public Relic getRelic(Rarity rarity) {
        return relics.get(rarity);
    }
}

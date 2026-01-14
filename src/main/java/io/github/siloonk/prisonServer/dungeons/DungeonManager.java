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
            Dungeon dungeon = getDungeonFromConfig(config);

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
        Dungeon generatedDungeon = getDungeonSettings(dungeon);


        // Make sure we actually generated a dungeon before proceeding
        if (generatedDungeon == null) return null;

        HashMap<String, DungeonMonster> dungeonMonsters = getDungeonMonsters(monsters);
        generatedDungeon.setMonsters(dungeonMonsters);


        DungeonBoss dungeonBoss = getDungeonBoss(boss, generatedDungeon);


        return generatedDungeon;
    }

    /**
     * Generate a dungeon with the basic dungeon settings present
     * @param dungeon the configuration section with all the dungeon settings
     * @return a dungeon object with all dungeon settings applied
     */
    private Dungeon getDungeonSettings(ConfigurationSection dungeon) {
        // Make sure all REQUIRED parts of the dungeon config are present
        boolean isValidDungeon = checkDungeonSection(dungeon);

        if (!isValidDungeon) {
            return null;
        }

        // Generate the dungeon from the data within the dungeon section
        Dungeon generatedDungeon = new Dungeon();
        generatedDungeon.setName(dungeon.getString("name"));

        // Set the spawn for the players
        generatedDungeon.setPlayerSpawn(
            ConfigUtils.getLocation(
                dungeon.getConfigurationSection("player_spawn")
            )
        );


        // Rewards
        ConfigurationSection rewardsSection = dungeon.getConfigurationSection("rewards");
        List<DungeonReward> rewards = new ArrayList<>();
        for (String key : rewardsSection.getKeys(false)) {
            DungeonReward reward = generateDungeonReward(rewardsSection.getConfigurationSection(key));
            if (reward == null) {
                System.err.printf("Invalid reward found at %s!%n", key);
                return null;
            }

            rewards.add(reward);
        }

        generatedDungeon.setRewards(rewards);


        // Participation rewards
        ConfigurationSection participationRewardSection = dungeon.getConfigurationSection("participation_rewards");
        List<DungeonReward> participationRewards = new ArrayList<>();
        for (String key : participationRewardSection.getKeys(false)) {
            DungeonReward reward = generateDungeonReward(participationRewardSection.getConfigurationSection(key));
            if (reward == null) {
                System.err.printf("Invalid reward found at %s!%n", key);
                return null;
            }

            rewards.add(reward);
        }
        generatedDungeon.setRewards(participationRewards);

        return generatedDungeon;
    }

    /**
     * Make sure the dungeon configuration section has all the required
     * settings to generate the dungeon
     * @param section the section that contains all dungeon settings
     * @return whether the dungeon section is valid
     */
    private boolean checkDungeonSection(ConfigurationSection section) {
        // Check for all the basic keys
        if (!section.contains("name")) return false;
        if (!section.contains("player_spawn")) return false;
        if (!section.contains("boss_spawn")) return false;
        if (!section.contains("rewards")) return false;
        if (!section.contains("participation_rewards")) return false;

        // Player spawn
        if (!ConfigUtils.isValidLocation(section.getConfigurationSection("player_spawn"))) return false;

        // Boss spawn
        return ConfigUtils.isValidLocation(section.getConfigurationSection("boss_spawn"));
    }

    /**
     * Generate a dungeon reward based on its type, if the type is invalid we return null
     * @param section the section in which the reward data is present
     * @return a dungeon reward if the type is valid and null if not
     */
    private DungeonReward generateDungeonReward(ConfigurationSection section) {
        DungeonRewardType type = DungeonRewardType.valueOf(section.getString("type").toUpperCase());

        if (type == DungeonRewardType.ITEM) {
            return new ItemDungeonReward(
                section.getString("id"),
                section.contains("amount") ? section.getInt("amount") : 1
            );
        }

        if (type == DungeonRewardType.COMMAND) {
            return new CommandDungeonReward(
                section.getString("command")
            );
        }

        if (type == DungeonRewardType.CURRENCY) {
            return new CurrencyDungeonReward(
                Currency.valueOf(section.getString("currency").toUpperCase()),
                section.getInt("amount")
            );
        }

        return null;
    }

    /**
     * Get a list of all the dungeon monsters defined in this section
     * @param section Section where all the monsters are defined
     * @return a list of all dungeon monsters in the section
     */
    private HashMap<String, DungeonMonster> getDungeonMonsters(ConfigurationSection section) {
        HashMap<String, DungeonMonster> monsters = new HashMap<>();

        // Try to load all monsters present in the config file
        for (String monster : section.getKeys(false)) {
            ConfigurationSection monsterSection = section.getConfigurationSection(monster);

            // Generate the monster
            DungeonMonster dungeonMonster = getMonster(monsterSection);
            monsters.put(monster, dungeonMonster);
        }

        return monsters;
    }

    /**'
     * Generate a DungeonMonster object based on the data present in the ConfigurationSection
     * @param section The section that contains the monster data
     * @return A dungeon monster based on the configuration section
     */
    private DungeonMonster getMonster(ConfigurationSection section) {

        String displayName = section.getString("display_name");
        EntityType type = EntityType.fromName(section.getString("monster_type"));
        double movementSpeed = section.getDouble("movement_speed");
        int maxHealth = section.getInt("max_health");
        List<PotionEffect> potionEffects = new ArrayList<>();

        // Load potion effects if they are present
        if (section.contains("potion_effects")) {
            potionEffects = generatePotionEffects(section.getConfigurationSection("potion_effects"));
        }

        DungeonMonster monster = new DungeonMonster();
        monster.setMonsterType(type)
                .setDisplayName(displayName)
                .setMovementSpeed(movementSpeed)
                .setHealth(maxHealth)
                .setPotionEffect(potionEffects);


        // Check for armor
        if (section.contains("armor")) {
            ConfigurationSection armorSection = section.getConfigurationSection("armor");

            if (armorSection.contains("helmet")) {
                monster.setHelmet(generateItem(armorSection.getConfigurationSection("helmet")));
            }

            if (armorSection.contains("chestplate")) {
                monster.setChestplate(generateItem(armorSection.getConfigurationSection("chestplate")));
            }

            if (armorSection.contains("leggings")) {
                monster.setLeggings(generateItem(armorSection.getConfigurationSection("leggings")));
            }

            if (armorSection.contains("boots")) {
                monster.setBoots(generateItem(armorSection.getConfigurationSection("boots")));
            }
        }

        return monster;
    }

    /**
     * Load the basic data of the dungeon and store it in the dungeon boss
     * @param section
     * @return
     */
    private DungeonBoss getDungeonBoss(ConfigurationSection section, Dungeon dungeon) {
        DungeonBoss boss = new DungeonBoss();
        String displayName = section.getString("display_name");
        int maxHealth = section.getInt("max_health");
        double movementSpeed = section.getDouble("movement_speed");
        EntityType type = EntityType.fromName(section.getString("entity_type"));

        HashMap<Integer, List<DungeonMonster>> monsters = new HashMap<>();

        // Load the monsters
        ConfigurationSection monsterSection = section.getConfigurationSection("monsters");
        for (String key : monsterSection.getKeys(false)) {
            // Load all monsters from the list
            List<DungeonMonster> monsterList = new ArrayList<>();
            List<String> monsterIds = monsterSection.getStringList(key);
            for (String id : monsterIds) {
                monsterList.add(dungeon.getMonsters().get(id));
            }

            // Add the monsters for this phase to the hashmap
            monsters.put(Integer.parseInt(key), monsterList);
        }

        HashMap<Integer, List<BossAbility>> abilities = new HashMap<>();

        // Load the abilities
        ConfigurationSection abilitySection = section.getConfigurationSection("abilities");
        for (String phase : abilitySection.getKeys(false)) {
            List<BossAbility> abilityList = new ArrayList<>();
            ConfigurationSection phaseSection = abilitySection.getConfigurationSection(phase);

            // Load all abilities
            for (String ability : phaseSection.getKeys(false)) {
                BossAbilityType abilityType = BossAbilityType.valueOf(ability.toUpperCase());
                abilityList.add(abilityType.generateAbility(phaseSection.getConfigurationSection(ability)));
            }

            // Register the ability to the boss
            abilities.put(Integer.parseInt(phase), abilityList);
        }

        // Generate the basics of the boss
        boss.setBossType(type)
                .setDisplayName(displayName)
                .setHealth(maxHealth)
                .setMovementSpeed(movementSpeed)
                .setAbilities(abilities)
                .setDungeonMonsters(monsters);

        // Load the armor
        if (section.contains("armor")) {
            ConfigurationSection armorSection = section.getConfigurationSection("armor");

            if (armorSection.contains("helmet")) {
                boss.setHelmet(generateItem(armorSection.getConfigurationSection("helmet")));
            }

            if (armorSection.contains("chestplate")) {
                boss.setChestplate(generateItem(armorSection.getConfigurationSection("chestplate")));
            }

            if (armorSection.contains("leggings")) {
                boss.setLeggings(generateItem(armorSection.getConfigurationSection("leggings")));
            }

            if (armorSection.contains("boots")) {
                boss.setBoots(generateItem(armorSection.getConfigurationSection("boots")));
            }
        }

        return boss;
    }

    private List<PotionEffect> generatePotionEffects(ConfigurationSection section) {
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
    private ItemStack generateItem(ConfigurationSection section) {
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

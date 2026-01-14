package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.dungeons.abilities.BossAbility;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonBoss {

    /**
     * keep track of the boss health
     */
    private int health;

    /**
     * Keep track of the boss' display name
     * This should be stored in the adventure format
     */
    private String displayName;

    /**
     * Keep track of all boss abilities per phase
     */
    private Map<Integer, List<BossAbility>> abilities = new HashMap<>();

    /**
     * Keep track of the boss's movement speed
     */
    private double movementSpeed;

    /**
     * Keep track of the entity type
     */
    private EntityType bossType;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    /**
     * Keep track of all monsters that can spawn based on the phase
     */
    private Map<Integer, List<DungeonMonster>> dungeonMonsters = new HashMap<>();

    public DungeonBoss setHealth(int health) {
        this.health = health;
        return this;
    }

    public DungeonBoss setAbilities(Map<Integer, List<BossAbility>> abilities) {
        this.abilities = abilities;
        return this;
    }

    public DungeonBoss setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
        return this;
    }

    public DungeonBoss setBossType(EntityType bossType) {
        this.bossType = bossType;
        return this;
    }

    public DungeonBoss setDungeonMonsters(Map<Integer, List<DungeonMonster>> dungeonMonsters) {
        this.dungeonMonsters = dungeonMonsters;
        return this;
    }

    public int getHealth() {
        return health;
    }

    public Map<Integer, List<BossAbility>> getAbilities() {
        return abilities;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public EntityType getBossType() {
        return bossType;
    }

    public Map<Integer, List<DungeonMonster>> getDungeonMonsters() {
        return dungeonMonsters;
    }

    public String getDisplayName() {
        return displayName;
    }

    public DungeonBoss setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public DungeonBoss setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public DungeonBoss setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public DungeonBoss setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public DungeonBoss setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }
}

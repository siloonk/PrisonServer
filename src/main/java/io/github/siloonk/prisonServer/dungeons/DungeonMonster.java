package io.github.siloonk.prisonServer.dungeons;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class DungeonMonster {

    /**
     * display name for this monster
     * Should be in the adventure api format
     */
    private String displayName;

    /**
     * health of the monster
     */
    private int health;

    /**
     * movement speed of the monster
     */
    private double movementSpeed;

    /**
     * The type of monster to spawn
     */
    private EntityType monsterType;

    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;

    private PotionEffect potionEffect;

    public String getDisplayName() {
        return displayName;
    }

    public DungeonMonster setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public int getHealth() {
        return health;
    }

    public DungeonMonster setHealth(int health) {
        this.health = health;
        return this;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public DungeonMonster setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
        return this;
    }

    public EntityType getMonsterType() {
        return monsterType;
    }

    public DungeonMonster setMonsterType(EntityType monsterType) {
        this.monsterType = monsterType;
        return this;
    }
}

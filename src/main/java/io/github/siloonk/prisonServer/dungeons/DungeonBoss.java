package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.dungeons.abilities.BossAbility;
import org.bukkit.entity.EntityType;

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
    private Map<Integer, BossAbility> abilities = new HashMap<>();

    /**
     * Keep track of the boss's movement speed
     */
    private float movementSpeed;

    /**
     * Keep track of the entity type
     */
    private EntityType bossType;

    /**
     * Keep track of all monsters that can spawn based on the phase
     */
    private Map<Integer, List<DungeonMonster>> dungeonMonsters = new HashMap<>();

    public DungeonBoss setHealth(int health) {
        this.health = health;
        return this;
    }

    public DungeonBoss setAbilities(Map<Integer, BossAbility> abilities) {
        this.abilities = abilities;
        return this;
    }

    public DungeonBoss setMovementSpeed(float movementSpeed) {
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

    public Map<Integer, BossAbility> getAbilities() {
        return abilities;
    }

    public float getMovementSpeed() {
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
}

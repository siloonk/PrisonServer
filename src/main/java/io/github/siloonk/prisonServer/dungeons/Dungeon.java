package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.dungeons.rewards.DungeonReward;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dungeon {

    /**
     *   Keep track of where the players should spawn
     */
    private Location playerSpawn;

    /**
     * Keep track of where the boss should spawn
     */
    private Location bossLocation;

    /**
     * Keep track of where the dungeon monsters can spawn
     * If this list is empty it'll determine the location based on the current position of the boss
     */
    private List<Location> dungeonMonsterLocations = new ArrayList<>();

    /**
     * Keep track of all dungeon monsters that can be spawned within this dungeon
     */
    private HashMap<String, DungeonMonster> monsters = new HashMap<>();

    /**
     * Name of the dungeon
     */
    private String name;

    /**
     * Keep track of what boss should spawn in this dungeon
     */
    private DungeonBoss dungeonBoss;

    /**
     * Keep track of all the rewards present in the dungeon
     * These rewards are for the top damage dealt
     */
    private List<DungeonReward> rewards = new ArrayList<>();

    /**
     * Keep track of all the participation rewards present in the dungeon
     */
    private List<DungeonReward> participationRewards = new ArrayList<>();


    public List<DungeonReward> getRewards() {
        return rewards;
    }

    public Dungeon setRewards(List<DungeonReward> rewards) {
        this.rewards = rewards;
        return this;
    }

    public List<DungeonReward> getParticipationRewards() {
        return participationRewards;
    }

    public Dungeon setParticipationRewards(ArrayList<DungeonReward> participationRewards) {
        this.participationRewards = participationRewards;
        return this;
    }

    public Location getPlayerSpawn() {
        return playerSpawn;
    }

    public Dungeon setPlayerSpawn(Location playerSpawn) {
        this.playerSpawn = playerSpawn;
        return this;
    }

    public Location getBossLocation() {
        return bossLocation;
    }

    public Dungeon setBossLocation(Location bossLocation) {
        this.bossLocation = bossLocation;
        return this;
    }

    public List<Location> getDungeonMonsterLocations() {
        return dungeonMonsterLocations;
    }

    public Dungeon setDungeonMonsterLocations(List<Location> dungeonMonsterLocations) {
        this.dungeonMonsterLocations = dungeonMonsterLocations;
        return this;
    }

    public String getName() {
        return name;
    }

    public Dungeon setName(String name) {
        this.name = name;
        return this;
    }

    public DungeonBoss getDungeonBoss() {
        return dungeonBoss;
    }

    public Dungeon setDungeonBoss(DungeonBoss dungeonBoss) {
        this.dungeonBoss = dungeonBoss;
        return this;
    }

    public HashMap<String, DungeonMonster> getMonsters() {
        return monsters;
    }

    public Dungeon setMonsters(HashMap<String, DungeonMonster> monsters) {
        this.monsters = monsters;
        return this;
    }

    public Dungeon setParticipationRewards(List<DungeonReward> participationRewards) {
        this.participationRewards = participationRewards;
        return this;
    }
}

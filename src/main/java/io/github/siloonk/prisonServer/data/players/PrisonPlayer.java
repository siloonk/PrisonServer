package io.github.siloonk.prisonServer.data.players;

import io.github.siloonk.prisonServer.data.Booster;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

public class PrisonPlayer {

    private UUID uuid;
    private long money;
    private long tokens;

    private int blocksMined;

    private Timestamp timeJoined;

    private int level;
    private int prestige;

    private float personalMultiplier;

    private int freeBackpackSlots;
    private int totalBackpackSlots;

    private ArrayList<Booster> boosters = new ArrayList<>();

    public PrisonPlayer() {

    }

    public PrisonPlayer(UUID uuid, long money, long tokens, int blocksMined, Timestamp timeJoined, int level, int prestige) {
        this.uuid = uuid;
        this.money = money;
        this.tokens = tokens;
        this.blocksMined = blocksMined;
        this.timeJoined = timeJoined;
        this.level = level;
        this.prestige = prestige;
        this.totalBackpackSlots = 500;
        this.freeBackpackSlots = totalBackpackSlots;

        this.personalMultiplier = 1;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getTokens() {
        return tokens;
    }

    public void setTokens(long tokens) {
        this.tokens = tokens;
    }

    public void setBlocksMined(int blocksMined) {
        this.blocksMined = blocksMined;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public Timestamp getTimeJoined() {
        return timeJoined;
    }

    public void setTimeJoined(Timestamp timeJoined) {
        this.timeJoined = timeJoined;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public float getPersonalMultiplier() {
        return personalMultiplier;
    }

    public void setPersonalMultiplier(float personalMultiplier) {
        this.personalMultiplier = personalMultiplier;
    }

    public ArrayList<Booster> getBoosters() {
        return boosters;
    }

    public void addBooster(Booster booster) {
        this.boosters.add(booster);
    }

    public int getFreeBackpackSlots() {
        return freeBackpackSlots;
    }

    public void setFreeBackpackSlots(int freeBackpackSlots) {
        this.freeBackpackSlots = freeBackpackSlots;
    }

    public int getTotalBackpackSlots() {
        return totalBackpackSlots;
    }

    public void setTotalBackpackSlots(int totalBackpackSlots) {
        this.totalBackpackSlots = totalBackpackSlots;
    }

    public void addBlocks(int blocks) {
        if (this.freeBackpackSlots < 0) {
            return;
        }

        if (this.freeBackpackSlots - blocks < 0) {
            this.freeBackpackSlots = 0;
        }

        this.freeBackpackSlots -= blocks;
    }
}

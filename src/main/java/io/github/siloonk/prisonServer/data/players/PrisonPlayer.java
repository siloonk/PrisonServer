package io.github.siloonk.prisonServer.data.players;

import org.jdbi.v3.core.mapper.reflect.ColumnName;
import java.sql.Timestamp;
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
}

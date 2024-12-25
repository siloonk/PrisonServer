package io.github.siloonk.prisonServer.data;

import io.github.siloonk.prisonServer.data.players.BoosterType;

import java.util.Date;

public class Booster {

    private BoosterType type;
    private long end;
    private double multiplier;

    public Booster(BoosterType type, long end, double multiplier) {
        this.type = type;
        this.end = end;
        this.multiplier = multiplier;
    }

    public BoosterType getType() {
        return type;
    }

    public long getEnd() {
        return end;
    }

    public double getMultiplier() {
        return multiplier;
    }
}

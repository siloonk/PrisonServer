package io.github.siloonk.prisonServer.data.relics;

import io.github.siloonk.prisonServer.data.Rarity;

import java.util.UUID;

public class SelectedRelic {

    private UUID owner;
    private RelicType relicType;
    private Rarity rarity;
    private double boost;

    public SelectedRelic(UUID owner, RelicType relicType, Rarity rarity, double boost) {
        this.owner = owner;
        this.relicType = relicType;
        this.rarity = rarity;
        this.boost = boost;
    }


    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setRelicType(String relicType) {
        this.relicType = RelicType.valueOf(relicType);
    }

    public void setRelicType(RelicType relicType) {
        this.relicType = relicType;
    }

    public void setRarity(String rarity) {
        this.rarity = Rarity.valueOf(rarity);
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }

    public UUID getOwner() {
        return owner;
    }

    public RelicType getRelicType() {
        return relicType;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public double getBoost() {
        return boost;
    }
}

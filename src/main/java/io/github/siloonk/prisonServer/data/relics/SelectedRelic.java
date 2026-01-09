package io.github.siloonk.prisonServer.data.relics;

import io.github.siloonk.prisonServer.data.Rarity;

import java.util.UUID;

public class SelectedRelic {

    private String id;
    private UUID owner;
    private RelicType type;
    private Rarity rarity;
    private double boost;

    public SelectedRelic(String id, UUID owner, RelicType relicType, Rarity rarity, double boost) {
        this.owner = owner;
        this.type = relicType;
        this.rarity = rarity;
        this.boost = boost;
        this.id = id;
    }

    public SelectedRelic() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setType(String relicType) {
        this.type = RelicType.valueOf(relicType);
    }

    public void setType(RelicType relicType) {
        this.type = relicType;
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

    public RelicType getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public double getBoost() {
        return boost;
    }
}

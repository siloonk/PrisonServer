package io.github.siloonk.prisonServer.enchantments;

import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public abstract class Enchantment {

    private Component name;
    private Component description;

    private int maxLevel;
    private int baseCost;
    private double costIncrease;
    private Currency currency;

    private double chanceAtMaxLevel;
    private double baseChance;
    private EnchantmentType type;


    public Enchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency) {
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        this.baseCost = baseCost;
        this.costIncrease = costIncrease;
        this.chanceAtMaxLevel = chanceAtMaxLevel;
        this.baseChance = baseChance;
        this.currency = currency;
    }


    public void setType(EnchantmentType type) {
        this.type = type;
    }

    public EnchantmentType getType() {
        return type;
    }

    public abstract void execute(Location blockLocation, PrisonPlayer player, int level);

    public Component getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public double getCostIncrease() {
        return costIncrease;
    }

    public double getChanceAtMaxLevel() {
        return chanceAtMaxLevel;
    }

    public double getBaseChance() {
        return baseChance;
    }

    public Currency getCurrency() {return currency;}

    @Override
    public String toString() {
        return "Enchantment{" +
                "name=" + name +
                ", description=" + description +
                ", maxLevel=" + maxLevel +
                ", baseCost=" + baseCost +
                ", costIncrease=" + costIncrease +
                ", chanceAtMaxLevel=" + chanceAtMaxLevel +
                ", baseChance=" + baseChance +
                '}';
    }
}

package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public class AetherSurge extends Enchantment {


    public AetherSurge(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {

    }
}

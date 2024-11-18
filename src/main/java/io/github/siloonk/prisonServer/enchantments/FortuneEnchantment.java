package io.github.siloonk.prisonServer.enchantments;

import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public class FortuneEnchantment extends Enchantment{

    public FortuneEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double baseChance) {
        super(name, description, maxLevel, baseCost, costIncrease, 100, baseChance);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {

    }
}

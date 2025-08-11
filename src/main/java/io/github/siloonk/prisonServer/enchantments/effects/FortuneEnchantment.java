package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public class FortuneEnchantment extends Enchantment {

    public FortuneEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double baseChance, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, 100, baseChance, currency);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {

    }
}

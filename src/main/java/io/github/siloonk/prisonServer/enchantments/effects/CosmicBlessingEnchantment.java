package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.data.Booster;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Random;

public class CosmicBlessingEnchantment extends Enchantment {

    private final double minBooster;
    private final double maxBooster;

    public CosmicBlessingEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency, double minBooster, double maxBooster) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
        this.minBooster = minBooster;
        this.maxBooster = maxBooster;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        double booster = new Random().nextDouble(minBooster, maxBooster);
        BoosterType type = BoosterType.values()[new Random().nextInt(BoosterType.values().length)];
        Booster receivedBooster = new Booster(type, System.currentTimeMillis() + 60_000, booster);
        player.addBooster(receivedBooster);
        Bukkit.getPlayer(player.getUuid()).sendActionBar(MiniMessage.miniMessage().deserialize(
                String.format("<dark_purple><bold>Cosmic Blessing<reset> <gray>»You have been given a <light_purple%.1fx %s Booster<gray> for <light_purple>60 seconds<gray>!",
                booster, type.toString().toLowerCase())
        ));
    }
}

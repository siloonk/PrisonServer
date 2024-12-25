package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.data.Booster;
import io.github.siloonk.prisonServer.data.players.BoosterType;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class EchoOfTheDeepEnchantment extends Enchantment {

    public EchoOfTheDeepEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        player.addBooster(new Booster(BoosterType.LUCKY_BLOCK, System.currentTimeMillis() + 30_000, 1));
        Bukkit.getPlayer(player.getUuid()).sendActionBar(MiniMessage.miniMessage().deserialize("<dark_purple><bold>Echo Of The Deep<reset> <gray>Your lucky blocks have been <light_purple>buffed<gray> for the next <light_purple>30 seconds<gray>!"));
    }
}

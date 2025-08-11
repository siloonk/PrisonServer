package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.CustomItems;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RelicSeekerEnchantment extends Enchantment {

    public RelicSeekerEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        bukkitPlayer.getInventory().addItem(CustomItems.get().getItem("RELIC"));
        bukkitPlayer.sendActionBar(MiniMessage.miniMessage().deserialize("<dark_purple><bold>Relic Seeker<reset> <gray>» You have found an ancient relic with <light_purple>Relic Seeker<gray>!"));
    }
}

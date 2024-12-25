package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class OmniTreasureEnchantment extends Enchantment {


    // TODO: IMPLEMENT THE CRATES PART
    public OmniTreasureEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        bukkitPlayer.sendActionBar(MiniMessage.miniMessage().deserialize("<dark_purple><bold>Omni Treasure<reset> <gray>»You have been given a <light_purple>Omni Crate<gray>!"));
    }
}

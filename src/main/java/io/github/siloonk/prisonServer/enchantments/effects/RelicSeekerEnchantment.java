package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RelicSeekerEnchantment extends Enchantment {

    public RelicSeekerEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Random random = new Random();
        int relicDust = random.nextInt(5, 10);
        player.setRelicDust(player.getRelicDust()+relicDust);
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        bukkitPlayer.sendActionBar(MiniMessage.miniMessage().deserialize("<dark_purple><bold>Relic Seeker<reset> <gray>» You have found <light_purple>%d relic dust<gray> with <light_purple>Relic Seeker<gray>!".formatted(relicDust)));
    }
}

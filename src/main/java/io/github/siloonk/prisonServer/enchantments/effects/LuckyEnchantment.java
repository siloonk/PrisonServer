package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.utils.Util;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class LuckyEnchantment extends Enchantment {

    private int minAmount;
    private int maxAmount;
    private double scaleAmount;

    private static Random random = new Random();
    private static MiniMessage miniMessage = MiniMessage.miniMessage();

    public LuckyEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency, int minAmount, int maxAmount, double scaleAmount) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.scaleAmount = scaleAmount;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        int amount = (int) (random.nextInt(minAmount, maxAmount) * player.getPersonalMultiplier() * (scaleAmount * level) * player.getMultiplier(BoosterType.TOKENS));
        player.setTokens(player.getTokens() + amount);
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        bukkitPlayer.sendActionBar(miniMessage.deserialize(String.format("<dark_purple><bold>Lucky<reset> <gray>» <gray>You have found <light_purple>%s Tokens<gray>!", Util.formatNumber(amount, 0))));
    }
}

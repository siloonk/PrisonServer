package io.github.siloonk.prisonServer.enchantments;

import io.github.siloonk.prisonServer.Util;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Random;

public class LuckyEnchantment extends Enchantment{

    private int minAmount;
    private int maxAmount;
    private double scaleAmount;

    private static Random random = new Random();
    private static MiniMessage miniMessage = MiniMessage.miniMessage();

    public LuckyEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, int minAmount, int maxAmount, double scaleAmount) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.scaleAmount = scaleAmount;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        int amount = (int) (random.nextInt(minAmount, maxAmount) * player.getPersonalMultiplier() * (scaleAmount * level));
        player.setTokens(player.getTokens() + amount);
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        Title title = Title.title(miniMessage.deserialize("<dark_purple><bold>Lucky"), miniMessage.deserialize(String.format("<light_purple>You have found %s Tokens!", Util.formatNumber(amount, 0))));
        bukkitPlayer.showTitle(title);
    }
}

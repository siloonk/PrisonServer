package io.github.siloonk.prisonServer.enchantments.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Booster;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import io.github.siloonk.prisonServer.utils.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WeatherType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class AetherSurgeEnchantment extends Enchantment {

    private double boostAmount;

    public AetherSurgeEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency, double boostAmount) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
        this.boostAmount = boostAmount;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        player.addBooster(new Booster(BoosterType.MONEY, System.currentTimeMillis() + 15000, boostAmount));
        player.addBooster(new Booster(BoosterType.TOKENS, System.currentTimeMillis() + 15000, boostAmount));
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());

        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());

        for (int i = 0; i < 5; i++) {
            Util.strikeLightning(blockLocation, bukkitPlayer);

        }

        /*
        bukkitPlayer.setPlayerWeather(WeatherType.DOWNFALL);
        new BukkitRunnable() {
            @Override
            public void run() {
                bukkitPlayer.resetPlayerWeather();
            }
        }.runTaskLater(PrisonServer.getInstance(), 15000);
        bukkitPlayer.sendActionBar(MiniMessage.miniMessage().deserialize(
                "<dark_purple><bold>Aether Surge<reset> <gray>»A storm has been rising raising your <light_purple>rewards <gray>for <light_purple>15 seconds<gray>!"
                )
        );*/
    }

}

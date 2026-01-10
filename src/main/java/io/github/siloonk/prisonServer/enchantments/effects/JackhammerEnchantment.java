package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.data.relics.RelicType;
import io.github.siloonk.prisonServer.data.relics.SelectedRelic;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import io.github.siloonk.prisonServer.utils.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class JackhammerEnchantment extends Enchantment {

    public JackhammerEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());

        Location minLoc =  new Location(
                mine.getCenterLocation().getWorld(),
                mine.getCenterLocation().getBlockX() - mine.getWidth() / 2,
                blockLocation.getBlockY(),
                mine.getCenterLocation().getBlockZ() - (double) mine.getWidth() / 2
        );

        Location maxLoc = new Location(
                mine.getCenterLocation().getWorld(),
                mine.getCenterLocation().getBlockX() + (double) mine.getWidth() /2,
                blockLocation.getBlockY(),
                mine.getCenterLocation().getBlockZ() + (double) mine.getWidth() /2
        );

        Util.setBlocksFast(
                minLoc,
                maxLoc,
                Bukkit.getPlayer(player.getUuid()),
                Material.AIR
        );

        // Calculate blocks broken
        int minedBlocks = (maxLoc.getBlockX() - minLoc.getBlockX()) * (maxLoc.getBlockZ() - minLoc.getBlockZ());



        // Token Relic
        List<SelectedRelic> selectedRelicList = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelicByType(player.getUuid(), RelicType.TOKENS);
        double boost = 1;
        if (!selectedRelicList.isEmpty()) {
            boost += selectedRelicList.stream().mapToDouble(SelectedRelic::getBoost).sum();
        }
        player.setTokens(player.getTokens() + Math.round(minedBlocks * player.getMultiplier(BoosterType.TOKENS) * boost)); // Add tokens
        player.addBlocks(minedBlocks);

    }
}

package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class JackhammerEnchantment extends Enchantment {

    public JackhammerEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());

        new BukkitRunnable() {
            HashMap<Location, BlockData> blockChanges = new HashMap<>();
            final BlockData blockData = Material.AIR.createBlockData();

            @Override
            public void run() {
                for (int x = mine.getCenterLocation().getBlockX() - mine.getWidth()/2; x <= mine.getCenterLocation().getBlockX()+ mine.getWidth()/2; x++) {
                    for (int z = mine.getCenterLocation().getBlockZ()- mine.getWidth()/2; z <= mine.getCenterLocation().getBlockZ() + mine.getWidth()/2; z++) {
                        blockChanges.put(new Location(blockLocation.getWorld(), x, blockLocation.getBlockY(), z), blockData);
                    }
                }


                player.setTokens(player.getTokens() + blockChanges.size()); // Add tokens
                player.addBlocks(blockChanges.size());
                Bukkit.getPlayer(player.getUuid()).sendMultiBlockChange(blockChanges, true);
            }
        }.runTaskAsynchronously(PrisonServer.getInstance());
    }
}

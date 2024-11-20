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
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LightningEnchantment extends Enchantment {

    private final int radius;

    public LightningEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, int radius) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
        this.radius = radius;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());
        blockLocation.getWorld().strikeLightningEffect(blockLocation);
        new BukkitRunnable() {
            private final HashMap<Location, BlockData> blockChanges = new HashMap<>();
            private BlockData blockData = Material.AIR.createBlockData();


            @Override
            public void run() {
                for (int x = blockLocation.getBlockX() - radius; x < blockLocation.getBlockX()+radius; x++) {
                    for (int y = blockLocation.getBlockY() - radius; y < blockLocation.getBlockY()+radius;y++) {
                        for (int z = blockLocation.getBlockZ() - radius; z < blockLocation.getBlockZ()+radius;z++) {
                            Location loc = new Location(blockLocation.getWorld(), x, y, z);
                            if (!mine.isWithin(loc)) continue;
                            if (blockLocation.distance(loc) <= radius){
                                blockChanges.put(loc, blockData);
                            }
                        }
                    }
                }

                Player bukkitPlayer= Bukkit.getPlayer(player.getUuid());
                bukkitPlayer.sendMultiBlockChange(blockChanges);
            }
        }.runTaskAsynchronously(PrisonServer.getInstance());
    }
}

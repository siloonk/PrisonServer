package io.github.siloonk.prisonServer.enchantments.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.data.relics.RelicType;
import io.github.siloonk.prisonServer.data.relics.SelectedRelic;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CosmicRiftEnchantment extends Enchantment {

    private static final double scale = 0.3;

    private int radius;
    private ArrayList<BlockDisplay> displays = new ArrayList<>();

    public CosmicRiftEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency, int radius) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
        this.radius = radius;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());

        Location blackHoleLocation = new Location(blockLocation.getWorld(), blockLocation.getBlockX(), blockLocation.getBlockY() + 10, blockLocation.getBlockZ());

        int blackHoleRadius= 2;
        for (double x = blackHoleLocation.getBlockX() - blackHoleRadius; x <= blackHoleLocation.getBlockX()+blackHoleRadius; x += scale) {
            for (double y = blackHoleLocation.getBlockY() - blackHoleRadius; y <= blackHoleLocation.getBlockY()+blackHoleRadius; y += scale) {
                for (double z = blackHoleLocation.getBlockZ() - blackHoleRadius; z <= blackHoleLocation.getBlockZ() + blackHoleRadius; z += scale) {
                    Location loc = new Location(blackHoleLocation.getWorld(), x, y, z);
                    double distance = blackHoleLocation.distance(loc);
                    if (distance > blackHoleRadius) continue;
                    if (distance < blackHoleRadius + scale && distance > blackHoleRadius - scale) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Display display = createBlackHoleSphere(loc);
                                bukkitPlayer.showEntity(PrisonServer.getInstance(), display);
                                killDisplay(display, 10);
                            }
                        }.runTask(PrisonServer.getInstance());
                    }
                }
            }
        }


        BukkitRunnable runnable = new BukkitRunnable() {
            int runCount = 0;


            @Override
            public void run() {
                runCount++;


                // Token Relic
                List<SelectedRelic> selectedRelicList = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelicByType(player.getUuid(), RelicType.TOKENS);
                double boost = 1;
                if (!selectedRelicList.isEmpty()) {
                    boost += selectedRelicList.stream().mapToDouble(SelectedRelic::getBoost).sum();
                }
                for (int x = blackHoleLocation.getBlockX()-runCount; x <= blackHoleLocation.getBlockX()+runCount; x++) {
                    for (int y = blackHoleLocation.getBlockY()-runCount;y<= blackHoleLocation.getBlockY()+runCount;y++) {
                        for (int z = blackHoleLocation.getBlockZ()-runCount;z<=blackHoleLocation.getBlockZ()+runCount;z++) {
                            Location newLoc = new Location(blackHoleLocation.getWorld(), x, y, z);
                            if (!mine.isWithin(newLoc)) continue;
                            if (newLoc.distance(blackHoleLocation) > runCount) continue;
                            if (newLoc.getBlock().getType() != Material.AIR) continue;

                            BlockData blockType = mine.getBlockType().createBlockData();
                            bukkitPlayer.sendBlockChange(newLoc, Material.AIR.createBlockData());
                            player.addBlocks(1);
                            player.setTokens(player.getTokens() + Math.round(1*boost));

                            if (Math.random() > 0.005) continue;
                            BlockDisplay display = blockLocation.getWorld().spawn(newLoc, BlockDisplay.class, entity -> {
                                entity.setBlock(blockType);
                                entity.setTeleportDuration(59);
                                entity.setVisibleByDefault(false);
                                bukkitPlayer.showEntity(PrisonServer.getInstance(), entity);
                            });
                            display.teleport(blackHoleLocation);
                            killDisplay(display, 3);
                        }
                    }
                }

                if (runCount>radius)
                    cancel();
            }
        };
        runnable.runTaskTimer(PrisonServer.getInstance(), 0L, 5L);

    }

    private void killDisplay(Entity entity, int second) {
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.remove();
            }
        }.runTaskLater(PrisonServer.getInstance(), second * 20L);
    }


    private BlockDisplay createBlackHoleSphere(Location loc) {

        return loc.getWorld().spawn(loc, BlockDisplay.class, entity -> {
            entity.setInterpolationDuration(200);
            entity.setGravity(true);
            entity.setBlock(Material.BLACK_CONCRETE.createBlockData());
            entity.setPersistent(false);
            entity.setVisibleByDefault(false);
            Transformation transformation = entity.getTransformation();
            transformation.getScale().set(scale);
            entity.setTransformation(transformation);
        });
    }
}

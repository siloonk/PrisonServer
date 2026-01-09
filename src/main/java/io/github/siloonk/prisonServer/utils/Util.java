package io.github.siloonk.prisonServer.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import io.github.siloonk.prisonServer.PrisonServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {
    private static char[] c = new char[]{'k', 'm', 'b', 't'};

    public static String formatNumber(double n, int iteration) {
        if ((n < 1000 && n >= 0) || (n > -1000 && n <= 0)) return n + "";

        double d = n / 1000.0;

        if (d < 1000) {
            // round to 1 decimal
            d = Math.round(d * 10.0) / 10.0;

            // remove .0 if whole number
            return (d % 1 == 0 ? (int) d : d) + "" + c[iteration];
        }

        return formatNumber(d, iteration + 1);
    }


    public static ArrayList<Component> convertStringListToComponentList(List<String> list) {
        ArrayList<Component> components = new ArrayList<>();
        for (String line : list) {
            components.add(MiniMessage.miniMessage().deserialize(line));
        }
        return components;
    }

    /**
     * Strike a fake lightning that only the player can see!
     * @param location
     * @param player
     */
    public static void strikeLightning(Location location, Player player) {
        PacketContainer lightning = PrisonServer.getInstance().getProtocolLibrary().createPacket(PacketType.Play.Server.SPAWN_ENTITY);

        // Entity ID
        lightning.getIntegers().write(0, 9999); // random unused ID

        // UUID (random so it's unique)
        lightning.getUUIDs().write(0, java.util.UUID.randomUUID());

        // Entity type: lightning bolt
        lightning.getEntityTypeModifier().write(0, EntityType.LIGHTNING_BOLT);

        // Position
        lightning.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());

        // Send only to this player
        PrisonServer.getInstance().getProtocolLibrary().sendServerPacket(Bukkit.getPlayer(player.getUniqueId()), lightning);
    }

    public static void fakeExplosion(Location location, Player player) {
        Bukkit.getScheduler().runTask(PrisonServer.getInstance(), () -> {
            player.spawnParticle(
                    Particle.EXPLOSION,
                    location,
                    1
            );

            player.playSound(
                    location,
                    Sound.ENTITY_GENERIC_EXPLODE,
                    1.0f,
                    1.0f
            );
        });
    }

    public static void setBlocksFast(Location loc1, Location loc2, Player player, Material block) {
        long now = System.currentTimeMillis();

        Location minLoc = new Location(
                loc1.getWorld(),
                Math.min(loc1.getBlockX(), loc2.getBlockX()),
                Math.min(loc1.getBlockY(), loc2.getBlockY()),
                Math.min(loc1.getBlockZ(), loc2.getBlockZ())
        );

        Location maxLoc = new Location(
                loc1.getWorld(),
                Math.max(loc1.getBlockX(), loc2.getBlockX()),
                Math.max(loc1.getBlockY(), loc2.getBlockY()),
                Math.max(loc1.getBlockZ(), loc2.getBlockZ())
        );

        new BukkitRunnable() {
            private HashMap<Location, BlockData> blockUpdates = new HashMap<>();
            private final BlockData blockData = block.createBlockData();

            @Override
            public void run() {
                for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
                    for (int y = maxLoc.getBlockY(); y >= minLoc.getBlockY(); y--) {
                        for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                            blockUpdates.put(new Location(minLoc.getWorld(), x, y, z), blockData);

                            if (blockUpdates.size() > 1000) {
                                player.sendMultiBlockChange(blockUpdates, true);
                                blockUpdates.clear();
                            }
                        }
                    }
                }

                player.sendMultiBlockChange(blockUpdates, true);

                System.out.println(System.currentTimeMillis() - now);
            }
        }.runTaskAsynchronously(PrisonServer.getInstance());

    }
}

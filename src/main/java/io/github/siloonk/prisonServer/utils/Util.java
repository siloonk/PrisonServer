package io.github.siloonk.prisonServer.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import io.github.siloonk.prisonServer.PrisonServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Util {
    private static char[] c = new char[]{'k', 'm', 'b', 't'};

    public static String formatNumber(double n, int iteration) {
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
}

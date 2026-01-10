package io.github.siloonk.prisonServer.enchantments.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
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
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExplosiveEnchantment extends Enchantment {

    private int radius;


    public ExplosiveEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency, int radius) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
        this.radius = radius;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());

        new BukkitRunnable() {
            HashMap<Location, BlockData> blockChanges = new HashMap<>();
            final BlockData blockData = Material.AIR.createBlockData();

            @Override
            public void run() {
                for (int x = blockLocation.getBlockX() - radius; x <= blockLocation.getBlockX() + radius; x++) {
                    for (int y = blockLocation.getBlockY() - radius; y <= blockLocation.getBlockY() + radius; y++) {
                        for (int z = blockLocation.getBlockZ() - radius; z <= blockLocation.getBlockZ() + radius; z++) {
                            Location location = new Location(blockLocation.getWorld(), x, y, z);
                            if (blockLocation.distance(location) > radius) continue;
                            if (!mine.isWithin(location)) continue;
                            blockChanges.put(location, blockData);
                        }
                    }
                }
                Bukkit.getPlayer(player.getUuid()).sendMultiBlockChange(blockChanges, true);
                player.addBlocks(blockChanges.size());
                // Token Relic
                List<SelectedRelic> selectedRelicList = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelicByType(player.getUuid(), RelicType.TOKENS);
                double boost = 1;
                if (!selectedRelicList.isEmpty()) {
                    boost += selectedRelicList.stream().mapToDouble(SelectedRelic::getBoost).sum();
                }

                player.setTokens(player.getTokens() + Math.round((blockChanges.size() * player.getMultiplier(BoosterType.TOKENS)*boost)));
            }
        }.runTaskAsynchronously(PrisonServer.getInstance());

        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());

        Util.fakeExplosion(blockLocation, bukkitPlayer);
    }
}

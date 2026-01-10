package io.github.siloonk.prisonServer.enchantments.effects;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.data.relics.RelicType;
import io.github.siloonk.prisonServer.data.relics.SelectedRelic;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EclipseEnchantment extends Enchantment {

    private int radius;

    public EclipseEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency, int radius) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
        this.radius = radius;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        // spawn particles around radius, break blocks
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        bukkitPlayer.setPlayerTime(18000, false);
        Particle.DustOptions options = new Particle.DustOptions(Color.BLACK, 100);
        Particle particle = new ParticleBuilder(Particle.DUST)
                .receivers(bukkitPlayer)
                .force(true)
                .count(1)
                .particle();

        Mine mine = PrisonServer.getInstance().getMineManager().getMine(bukkitPlayer.getUniqueId());
        ArrayList<Location> locs = getLocations(blockLocation, mine, false);
        HashMap<Location, BlockData> blockChanges = new HashMap<>();
        BlockData data = Material.AIR.createBlockData();

        new BukkitRunnable() {
            int timesRan = 0;

            @Override
            public void run() {
                if (timesRan >= 10) {
                    removeAllBlocks(blockLocation, bukkitPlayer, player, mine);
                    bukkitPlayer.setPlayerTime(6000, false);
                    cancel();
                }

                locs.forEach((loc) -> {
                    double distance = loc.distance(blockLocation);
                    if (distance < radius + 1 && distance > radius - 1)
                        bukkitPlayer.spawnParticle(particle, loc, 1, options);

                    if (mine.isWithin(loc)) {
                        if (Math.random() < 0.3) {
                            blockChanges.put(loc, data);
                        }
                    }
                });


                bukkitPlayer.sendMultiBlockChange(blockChanges);
                player.addBlocks(blockChanges.size());

                // Token Relic
                List<SelectedRelic> selectedRelicList = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelicByType(player.getUuid(), RelicType.TOKENS);
                double boost = 1;
                if (!selectedRelicList.isEmpty()) {
                    boost += selectedRelicList.stream().mapToDouble(SelectedRelic::getBoost).sum();
                }
                player.setTokens(player.getTokens() + Math.round(blockChanges.size() * player.getMultiplier(BoosterType.TOKENS)*boost));
                blockChanges.clear();
                timesRan++;
            }
        }.runTaskTimer(PrisonServer.getInstance(), 0L, 20L);
    }

    private void removeAllBlocks(Location blockLocation, Player player, PrisonPlayer prisonPlayer, Mine mine) {
        BlockData data = Material.AIR.createBlockData();
        new BukkitRunnable() {

            @Override
            public void run() {
                HashMap<Location, BlockData> blockChanges = new HashMap<>();

                for (Location location : getLocations(blockLocation, mine, true)) {
                    blockChanges.put(location, data);
                }

                player.sendMultiBlockChange(blockChanges);
                prisonPlayer.addBlocks(blockChanges.size());

            }
        }.runTaskAsynchronously(PrisonServer.getInstance());
    }

    private @NotNull ArrayList<Location> getLocations(Location blockLocation, Mine mine, boolean checkMineBorder) {
        ArrayList<Location> locs = new ArrayList<>();
        for (int x = blockLocation.getBlockX() - radius; x <= blockLocation.getBlockX() + radius; x++) {
            for (int y = blockLocation.getBlockY() - radius; y <= blockLocation.getBlockY() + radius; y++) {
                for (int z = blockLocation.getBlockZ() - radius; z <= blockLocation.getBlockZ() + radius; z++) {
                    Location loc = new Location(blockLocation.getWorld(), x, y, z);
                    if (loc.distance(blockLocation) <= radius)
                        if (!checkMineBorder) locs.add(loc);
                        else if (mine.isWithin(loc)) locs.add(loc);
                }
            }
        }
        return locs;
    }
}

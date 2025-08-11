package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;

public class StarFallEnchantment extends Enchantment {



    public StarFallEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {

        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());


        int minX = mine.getCenterLocation().getBlockX() - (mine.getWidth() / 2);
        int maxX = mine.getCenterLocation().getBlockX() + (mine.getWidth() / 2);

        int minZ = mine.getCenterLocation().getBlockZ() - (mine.getWidth() / 2);
        int maxZ = mine.getCenterLocation().getBlockZ() + (mine.getWidth() / 2);
        Random random = new Random();

        for (int i = 0; i < 10; i++) {

            Location randomLoc = new Location(blockLocation.getWorld(), random.nextInt(minX, maxX), blockLocation.getBlockY() + 20, random.nextInt(minZ, maxZ));


            createFireworkEffect(mine, randomLoc, player);
        }
    }

    private void createFireworkEffect(Mine mine, Location loc, PrisonPlayer prisonPlayer) {
        new BukkitRunnable() {

            @Override
            public void run() {
                mine.getCenterLocation().getWorld().spawn(loc, Firework.class, entity -> {
                    FireworkMeta fireworkMeta = entity.getFireworkMeta();
                    fireworkMeta.addEffect(FireworkEffect.builder()
                            .withColor(Color.BLACK, Color.RED, Color.ORANGE)
                            .with(FireworkEffect.Type.BALL)
                            .build());

                    entity.setFireworkMeta(fireworkMeta);
                    entity.setVisualFire(true);
                    entity.detonate();
                });

                loc.add(0, -1, 0);


                if (mine.isWithin(loc)) {
                    Player player = Bukkit.getPlayer(mine.getOwner());
                    createExplosion(player, loc, 10, prisonPlayer);
                    cancel();
                }
            }
        }.runTaskTimer(PrisonServer.getInstance(), 0L, 2L);
    }

    private void createExplosion(Player player, Location loc, int radius, PrisonPlayer prisonPlayer) {

        new BukkitRunnable() {
            final HashMap<Location, BlockData> blockChanges = new HashMap<>();
            final BlockData data = Material.AIR.createBlockData();

            @Override
            public void run() {
                for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
                    for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                        for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                            Location newLoc = new Location(loc.getWorld(), x, y, z);
                            if (newLoc.distance(loc) > radius) continue;

                            blockChanges.put(newLoc, data);
                        }
                    }
                }

                player.sendMultiBlockChange(blockChanges);
                prisonPlayer.setTokens(prisonPlayer.getTokens() + Math.round(blockChanges.size() * prisonPlayer.getMultiplier(BoosterType.TOKENS)));
                prisonPlayer.addBlocks(blockChanges.size());
            }
        }.runTaskAsynchronously(PrisonServer.getInstance());
        loc.createExplosion(0);

    }
}

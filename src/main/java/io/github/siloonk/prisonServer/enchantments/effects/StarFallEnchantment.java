package io.github.siloonk.prisonServer.enchantments.effects;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import io.github.siloonk.prisonServer.utils.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.text.html.Option;
import java.sql.Wrapper;
import java.util.*;

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


            createFireworkEffect(mine, randomLoc, player, blockLocation.getBlockY());
        }
    }

    private void createFireworkEffect(Mine mine, Location loc, PrisonPlayer prisonPlayer, int targetY) {
        new BukkitRunnable() {

            @Override
            public void run() {

                sendFakeFirework(prisonPlayer, loc);

                loc.add(0, -1, 0);


                if (mine.isWithin(loc) && loc.getBlockY() == targetY) {
                    Player mineOwner = Bukkit.getPlayer(mine.getOwner());
                    createExplosion(mineOwner, loc, 10, prisonPlayer, mine);
                    cancel();
                }
            }
        }.runTaskTimer(PrisonServer.getInstance(), 0L, 2L);
    }

    private void sendFakeFirework(PrisonPlayer prisonPlayer, Location loc) {
        Player player = Bukkit.getPlayer(prisonPlayer.getUuid());

        ProtocolManager pm = PrisonServer.getInstance().getProtocolLibrary();

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        int entityID = Integer.valueOf((int) (Math.random() * 10000));
        packet.getIntegers().write(0, entityID);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getEntityTypeModifier().write(0, EntityType.FIREWORK_ROCKET);
        packet.getDoubles().write(0, loc.getX()).write(1, loc.getY()).write(2, loc.getZ());

        pm.sendServerPacket(player, packet);


        PacketContainer metadata = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
        metadata.getIntegers().write(0, entityID);


        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta meta = (FireworkMeta) item.getItemMeta();
        meta.addEffect(FireworkEffect.builder()
                .withColor(Color.BLACK, Color.RED, Color.ORANGE)
                .with(FireworkEffect.Type.BALL)
                .build());

        meta.setPower(0);
        item.setItemMeta(meta);



        List<WrappedDataValue> metadataList = new ArrayList<>();

        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.getItemStackSerializer(false);

        metadataList.add(new WrappedDataValue(8, serializer, BukkitConverters.getItemStackConverter().getGeneric(item)));

        metadata.getDataValueCollectionModifier().write(0, metadataList);
        pm.sendServerPacket(player, metadata);

        PacketContainer status = pm.createPacket(PacketType.Play.Server.ENTITY_STATUS);
        status.getIntegers().write(0, entityID);
        status.getBytes().write(0, (byte)17);
        pm.sendServerPacket(player, status);


        // 4. Destroy the entity after
        PacketContainer destroy = pm.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroy.getIntLists().write(0, Collections.singletonList(entityID));
        pm.sendServerPacket(player, destroy);
    }

    private void createExplosion(Player player, Location loc, int radius, PrisonPlayer prisonPlayer, Mine mine ) {

        new BukkitRunnable() {
            final HashMap<Location, BlockData> blockChanges = new HashMap<>();
            final BlockData data = Material.AIR.createBlockData();

            @Override
            public void run() {
                for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
                    for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                        for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                            Location newLoc = new Location(loc.getWorld(), x, y, z);
                            if (!mine.isWithin(newLoc)) continue;
                            if (newLoc.distance(loc) > radius) continue;

                            blockChanges.put(newLoc, data);
                        }
                    }
                }

                player.sendMultiBlockChange(blockChanges);
                prisonPlayer.setTokens(prisonPlayer.getTokens() + Math.round(blockChanges.size() * prisonPlayer.getMultiplier(BoosterType.TOKENS)));
                prisonPlayer.addBlocks(blockChanges.size());
                Util.fakeExplosion(loc, player);
            }
        }.runTaskAsynchronously(PrisonServer.getInstance());

    }
}

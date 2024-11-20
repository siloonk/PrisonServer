package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class StarCoreEnchantment extends Enchantment {

    private ArrayList<String> rewards;

    private Random random = new Random();
    private BlockData blockData = Material.END_STONE.createBlockData();
    private MiniMessage miniMessage = MiniMessage.miniMessage();

    public StarCoreEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, ArrayList<String> rewards) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance);
        this.rewards = rewards;
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUuid());

        int minX = Math.max(mine.getCenterLocation().getBlockX() - mine.getWidth()/2, blockLocation.getBlockX() - 20);
        int maxX = Math.min(mine.getCenterLocation().getBlockX() + mine.getWidth()/2, blockLocation.getBlockX() + 20);

        int minZ = Math.max(mine.getCenterLocation().getBlockZ() - mine.getWidth()/2, blockLocation.getBlockZ() - 20);
        int maxZ = Math.min(mine.getCenterLocation().getBlockZ() + mine.getWidth()/2, blockLocation.getBlockZ() + 20);


        Location loc = new Location(blockLocation.getWorld(), random.nextInt(minX, maxX), blockLocation.getBlockY(), random.nextInt(minZ, maxZ));
        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
        loc.getWorld().createExplosion(loc, 0f);

        bukkitPlayer.sendBlockChange(loc, blockData);
        bukkitPlayer.sendActionBar(miniMessage.deserialize("<dark_purple><bold>Star Core<reset> <gray>» A <light_purple>Star Core<gray> has been spawned somewhere in your mine!"));

    }
}

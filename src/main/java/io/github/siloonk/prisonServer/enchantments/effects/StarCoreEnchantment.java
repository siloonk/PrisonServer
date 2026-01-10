package io.github.siloonk.prisonServer.enchantments.effects;

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
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarCoreEnchantment extends Enchantment {

    private ArrayList<String> rewards;

    private Random random = new Random();
    private BlockData blockData = Material.END_STONE.createBlockData();
    private MiniMessage miniMessage = MiniMessage.miniMessage();

    public StarCoreEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, ArrayList<String> rewards, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
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
        Util.fakeExplosion(loc, bukkitPlayer);

        bukkitPlayer.sendBlockChange(loc, blockData);
        // Token Relic
        List<SelectedRelic> selectedRelicList = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelicByType(player.getUuid(), RelicType.TOKENS);
        double boost = 1;
        if (!selectedRelicList.isEmpty()) {
            boost += selectedRelicList.stream().mapToDouble(SelectedRelic::getBoost).sum();
        }
        player.setTokens(player.getTokens() + Math.round(player.getMultiplier(BoosterType.TOKENS) * boost));
        player.addBlocks(1);
        mine.addStarCore(loc);
        bukkitPlayer.sendActionBar(miniMessage.deserialize("<dark_purple><bold>Star Core<reset> <gray>» A <light_purple>Star Core<gray> has been spawned somewhere in your mine!"));

    }

    public ArrayList<String> getRewards() {
        return rewards;
    }
}

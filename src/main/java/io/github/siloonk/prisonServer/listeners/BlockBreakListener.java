package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.enchantments.EnchantmentType;
import io.github.siloonk.prisonServer.enchantments.effects.StarCoreEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class BlockBreakListener implements Listener {

    private static Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(event.getPlayer().getUniqueId());
        if (mine == null) return;
        if (!mine.isWithin(event.getBlock().getLocation())) return;

        if (mine.isStarCore(event.getBlock().getLocation())) {
            StarCoreEnchantment enchantment = (StarCoreEnchantment) EnchantmentHandler.getEnchantment(EnchantmentType.STAR_CORE);

            int randomVal = random.nextInt(enchantment.getRewards().size());
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), enchantment.getRewards().get(randomVal).replaceAll("%player%", event.getPlayer().getName()));
            mine.removeStarCore(event.getBlock().getLocation());
        }
    }
}

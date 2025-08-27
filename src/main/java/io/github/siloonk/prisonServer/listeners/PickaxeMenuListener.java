package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.enchantments.EnchantmentInventory;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PickaxeMenuListener implements Listener {

    @EventHandler
    public void onPlayerOpenPickaxeMenu(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.DIAMOND_PICKAXE) return;


        EnchantmentInventory.open(event.getPlayer());

    }
}

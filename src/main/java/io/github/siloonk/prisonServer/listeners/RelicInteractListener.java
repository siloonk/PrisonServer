package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Rarity;
import io.github.siloonk.prisonServer.data.relics.Relic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RelicInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().isEmpty()) return;
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();


        if (!e.getAction().isRightClick()) return;

        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!(container.has(PDCKeys.RARITY))) return;
        if (container.has(PDCKeys.RELIC_TYPE)) return;

        Rarity rarity = Rarity.valueOf(container.get(PDCKeys.RARITY, PersistentDataType.STRING));
        Relic relic = PrisonServer.getInstance().getRelicManager().getRelic(rarity);
        ItemStack relicItem = relic.generateItem();
        e.getPlayer().getInventory().addItem(relicItem);
        item.setAmount(item.getAmount()-1);
    }
}

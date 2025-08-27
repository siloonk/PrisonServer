package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.crates.Crate;
import io.github.siloonk.prisonServer.crates.CrateBlock;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class CrateInteractEvent implements Listener {

    private static final MiniMessage mm = MiniMessage.miniMessage();


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        // Check if clicked block is a crate
        if (PrisonServer.getInstance().getCrateBlocksManager().getBlocks().stream().noneMatch(crateBlock -> crateBlock.getLocation().equals(e.getClickedBlock().getLocation()))) return;
        e.setCancelled(true);
        CrateBlock cb = PrisonServer.getInstance().getCrateBlocksManager().getBlocks().stream().filter(crateBlock -> crateBlock.getLocation().equals(e.getClickedBlock().getLocation())).findFirst().get();
        if (cb.getLocation() == null) return;

        Crate crate = PrisonServer.getInstance().getCratesManager().getCrate(cb.getId());

        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        crate.getCrateItem().setAmount(tool.getAmount());
        if (!tool.equals(crate.getCrateItem())) {
            player.getInventory().addItem(crate.getCrateItem());
            player.sendMessage(mm.deserialize("<red>This is not the correct crate!"));
            return;
        }

        crate.use(player);
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        if (e.getClickedBlock() == null) return;
        // Check if clicked block is a crate
        if (PrisonServer.getInstance().getCrateBlocksManager().getBlocks().stream().noneMatch(crateBlock -> {
            if (crateBlock.getLocation() == null) return false;
            return crateBlock.getLocation().equals(e.getClickedBlock().getLocation());
        })) return;
        e.setCancelled(true);
        Optional<CrateBlock> cb = PrisonServer.getInstance().getCrateBlocksManager().getBlocks().stream().filter(crateBlock -> crateBlock.getLocation().equals(e.getClickedBlock().getLocation())).findFirst();
        if (cb.isEmpty()) return;

        if (cb.get().getLocation() == null) return;
        Crate crate = PrisonServer.getInstance().getCratesManager().getCrate(cb.get().getId());

        player.openInventory(crate.getDisplayInventory());
    }
}

package io.github.siloonk.prisonServer.inventories;

import io.github.siloonk.prisonServer.PrisonServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RelicInventory {

    private static final int ROWS = 5;
    private static final ItemStack BACKGROUND_ITEM = PrisonServer.getInstance().getCustomItems().getItem("BACKGROUND_ITEM");

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final Component GUI_TITLE = mm.deserialize("<dark_gray>Relics");
    private static final Inventory MENU = Bukkit.createInventory(null, ROWS*9, GUI_TITLE);

    public static void open(Player player) {
        for (int i = 0; i < ROWS * 9; i++) {
            MENU.setItem(i, BACKGROUND_ITEM);
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().toString().contains("_PICKAXE")) return;

        ItemStack commonRelicItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        commonRelicItem.editMeta((meta) -> {
            meta.displayName(mm.deserialize("<white>Common Relics").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        });

        // Common Relic Slots
        MENU.setItem(11, commonRelicItem);
        MENU.setItem(20, new ItemStack(Material.STONE_BUTTON));
        MENU.setItem(29, new ItemStack(Material.STONE_BUTTON));

        // Uncommon Relic SLots
        MENU.setItem(12, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
        MENU.setItem(21, new ItemStack(Material.STONE_BUTTON));
        MENU.setItem(30, new ItemStack(Material.STONE_BUTTON));


        // Rare Relic slots
        MENU.setItem(13, new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        MENU.setItem(22, new ItemStack(Material.STONE_BUTTON));
        MENU.setItem(31, new ItemStack(Material.STONE_BUTTON));

        // Epic Relic Slots
        MENU.setItem(14, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE));
        MENU.setItem(23, new ItemStack(Material.STONE_BUTTON));
        MENU.setItem(32, new ItemStack(Material.STONE_BUTTON));

        // Legendary Relic Slots
        MENU.setItem(15, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE));
        MENU.setItem(24, new ItemStack(Material.STONE_BUTTON));
        MENU.setItem(33, new ItemStack(Material.STONE_BUTTON));

        player.openInventory(MENU);
    }

}

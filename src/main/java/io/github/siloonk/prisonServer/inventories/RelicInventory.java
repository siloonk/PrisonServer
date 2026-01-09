package io.github.siloonk.prisonServer.inventories;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Rarity;
import io.github.siloonk.prisonServer.data.relics.RelicType;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.print.DocFlavor;

public class RelicInventory implements Listener {

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

        ItemStack commonRelicItem = getRelicGlass("<white>Common Relics", Material.WHITE_STAINED_GLASS_PANE);
        ItemStack uncommonRelicItem = getRelicGlass("<green>Uncommon Relics", Material.LIME_STAINED_GLASS_PANE);
        ItemStack rareRelicItem = getRelicGlass("<aqua>Rare Relics", Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemStack epicRelicItem = getRelicGlass("<light_purple>Epic Relics", Material.MAGENTA_STAINED_GLASS_PANE);
        ItemStack legendaryRelicItem = getRelicGlass("<red>Legendary Relics", Material.ORANGE_STAINED_GLASS_PANE);



        // Common Relic Slots
        MENU.setItem(11, commonRelicItem);
        MENU.setItem(20, getUnselectedrelicItem(Rarity.COMMON));
        MENU.setItem(29, getUnselectedrelicItem(Rarity.COMMON));

        // Uncommon Relic SLots
        MENU.setItem(12, uncommonRelicItem);
        MENU.setItem(21, getUnselectedrelicItem(Rarity.UNCOMMON));
        MENU.setItem(30, getUnselectedrelicItem(Rarity.UNCOMMON));


        // Rare Relic slots
        MENU.setItem(13, rareRelicItem);
        MENU.setItem(22, getUnselectedrelicItem(Rarity.RARE));
        MENU.setItem(31, getUnselectedrelicItem(Rarity.RARE));

        // Epic Relic Slots
        MENU.setItem(14, epicRelicItem);
        MENU.setItem(23, getUnselectedrelicItem(Rarity.EPIC));
        MENU.setItem(32, getUnselectedrelicItem(Rarity.EPIC));

        // Legendary Relic Slots
        MENU.setItem(15, legendaryRelicItem);
        MENU.setItem(24, getUnselectedrelicItem(Rarity.LEGENDARY));
        MENU.setItem(33, getUnselectedrelicItem(Rarity.LEGENDARY));

        player.openInventory(MENU);
    }

    private static ItemStack getRelicGlass(String displayName, Material itemType) {
        ItemStack relicItem = new ItemStack(itemType);
        relicItem.editMeta((meta) -> {
            meta.displayName(mm.deserialize(displayName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        });

        return relicItem;
    }

    private static ItemStack getUnselectedrelicItem(Rarity rarity) {
        ItemStack unselectedRelic = new ItemStack(Material.STONE_BUTTON);
        unselectedRelic.editMeta(meta -> {
            meta.displayName(mm.deserialize("<white>Unselected Relic").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            meta.getPersistentDataContainer().set(PDCKeys.RARITY, PersistentDataType.STRING, rarity.toString());
        });
        return unselectedRelic;
    }


    @EventHandler
    public void onRelicInventoryInteract(InventoryClickEvent event) {
        // Make sure we are in the Relics GUI
        if (!event.getView().title().equals(GUI_TITLE)) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().equals(event.getWhoClicked().getInventory())) return;
        event.setCancelled(true);

        ItemStack item = event.getWhoClicked().getItemOnCursor();
        if (!item.hasItemMeta()) {
            event.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Relics<reset> <gray>» <white>This item is not a <light_purple>relic<white>!"));
            return;
        }

        // Make sure we are clicking the buttons AKA unselected slots.
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;
        if (clickedItem.getType() != Material.STONE_BUTTON) return;
        PersistentDataContainerView clickedItemContainer = clickedItem.getPersistentDataContainer();


        // Read Relic data from the item
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (!item.getType().equals( PrisonServer.getInstance().getCustomItems().getItem("RELIC").getType()) || !pdc.has(PDCKeys.RELIC_TYPE)) {
            event.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Relics<reset> <gray>» <white>This item is not a <light_purple>relic<white>!"));
            return;
        }

        Rarity rarity = Rarity.valueOf(pdc.get(PDCKeys.RARITY, PersistentDataType.STRING));
        RelicType relicType = RelicType.valueOf(pdc.get(PDCKeys.RELIC_TYPE, PersistentDataType.STRING));
        double boost = pdc.get(PDCKeys.RELIC_BOOST, PersistentDataType.DOUBLE);

        if (!clickedItemContainer.get(PDCKeys.RARITY, PersistentDataType.STRING).equals(rarity.toString())) {
            event.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Relics<reset> <gray>» <white>This relic does <light_purple>not<white> belong in this slot!"));
            return;
        }

        event.getWhoClicked().sendMessage("PLACING ITEM");


    }
}

package io.github.siloonk.prisonServer.inventories;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.dao.RelicDAO;
import io.github.siloonk.prisonServer.data.Rarity;
import io.github.siloonk.prisonServer.data.relics.RelicType;
import io.github.siloonk.prisonServer.data.relics.SelectedRelic;
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

import java.util.List;
import java.util.UUID;

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
        setRelicItems(new int[]{20, 29}, Rarity.COMMON, player.getUniqueId());
        // Uncommon Relic SLots
        MENU.setItem(12, uncommonRelicItem);
        setRelicItems(new int[]{21, 30}, Rarity.UNCOMMON, player.getUniqueId());
        // Rare Relic slots
        MENU.setItem(13, rareRelicItem);
        setRelicItems(new int[]{22, 31}, Rarity.RARE, player.getUniqueId());
        // Epic Relic Slots
        MENU.setItem(14, epicRelicItem);
        setRelicItems(new int[]{23, 32}, Rarity.EPIC, player.getUniqueId());
        // Legendary Relic Slots
        MENU.setItem(15, legendaryRelicItem);
        setRelicItems(new int[]{24, 33}, Rarity.LEGENDARY, player.getUniqueId());

        // Open the inventory to the player
        player.openInventory(MENU);
    }

    private static void setRelicItems(int[] slots, Rarity rarity, UUID owner) {
        RelicDAO relicDao = PrisonServer.getInstance().getDatabase().getRelicDAO();

        List<SelectedRelic> selectedRelics = relicDao.getRelics(owner, rarity);

        for (int i =0; i < 2; i++) {
            if (selectedRelics.size() <= i) MENU.setItem(slots[i], getUnselectedrelicItem(rarity));
            else MENU.setItem(slots[i], getSelectedRelic(selectedRelics.get(i)));
        }


    }

    private static ItemStack getSelectedRelic(SelectedRelic relic) {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        item.editMeta(meta -> {
            meta.displayName(relic.getRarity().getName().append(mm.deserialize(
                    " <light_purple>%s <white>Relic".formatted(relic.getType().toString())
            )));

            meta.lore(List.of(
                    mm.deserialize("<white>Boost <light_purple>%.1f%%".formatted(relic.getBoost() * 100)),
                    mm.deserialize(""),
                    mm.deserialize("<gray>Click to deselect this relic!")
            ));

            meta.getPersistentDataContainer().set(PDCKeys.ID, PersistentDataType.STRING, relic.getId());
        });

        return item;
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

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) return;

        ItemStack item = event.getWhoClicked().getItemOnCursor();
        if (!item.hasItemMeta()) {

            // Check if we should delete the relic
            if (item.getType().isEmpty() && clickedItem.getType() != Material.NETHER_STAR) {
                event.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Relics<reset> <gray>» <white>This item is not a <light_purple>relic<white>!"));
                return;
            }

            // Add deleting of things
            PersistentDataContainer pdc = clickedItem.getItemMeta().getPersistentDataContainer();

            SelectedRelic relic = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelic(pdc.get(PDCKeys.ID, PersistentDataType.STRING));
            ItemStack newRelic = new ItemStack(Material.NETHER_STAR);
            newRelic.editMeta(meta -> {
                meta.displayName(mm.deserialize("<light_purple>%s Relic".formatted(relic.getType().toString())).decoration(TextDecoration.ITALIC, false));
                meta.lore(List.of(mm.deserialize("<gray>This relic gives a <light_purple>%.0f%% %s<gray> boost".formatted(relic.getBoost()*100, relic.getType().toString())).decoration(TextDecoration.ITALIC, false)));

                meta.getPersistentDataContainer().set(PDCKeys.RELIC_BOOST, PersistentDataType.DOUBLE, relic.getBoost());
                meta.getPersistentDataContainer().set(PDCKeys.RELIC_TYPE, PersistentDataType.STRING, relic.getType().toString());
                meta.getPersistentDataContainer().set(PDCKeys.RARITY, PersistentDataType.STRING, relic.getRarity().toString());
            });
            event.getWhoClicked().getInventory().addItem(newRelic);

            PrisonServer.getInstance().getDatabase().getRelicDAO().deleteRelic(pdc.get(PDCKeys.ID, PersistentDataType.STRING));
            open((Player) event.getWhoClicked());
            event.getWhoClicked().sendMessage(mm.deserialize("<dark_purple>Relics<reset> <gray>» <white>You have successfully <light_purple>deselected<white> this relic!"));
            return;
        }

        // Make sure we are clicking the buttons AKA unselected slots.
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

        SelectedRelic relic = new SelectedRelic(
                UUID.randomUUID().toString(),
                event.getWhoClicked().getUniqueId(),
                relicType,
                rarity,
                boost
        );

        item.setAmount(item.getAmount() - 1);
        PrisonServer.getInstance().getDatabase().getRelicDAO().insertRelic(relic);
        event.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Relics<reset> <gray>» <white>You have applied a <light_purple>%.1f%% %s<white> relic!".formatted(boost * 100, relicType)));
        open((Player) event.getWhoClicked());
    }
}

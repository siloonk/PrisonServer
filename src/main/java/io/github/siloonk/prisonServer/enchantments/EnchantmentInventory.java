package io.github.siloonk.prisonServer.enchantments;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.data.players.PrisonPlayerManager;
import io.github.siloonk.prisonServer.utils.Util;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentInventory implements Listener {

    private static final int ROWS = 6;
    private static final ItemStack BACKGROUND_ITEM = PrisonServer.getInstance().getCustomItems().getItem("BACKGROUND_ITEM");

    private static final MiniMessage mm = MiniMessage.miniMessage();
    private static final Component GUI_TITLE = mm.deserialize("<gray>Enchantments");
    private static final Inventory MENU = Bukkit.createInventory(null, ROWS*9, GUI_TITLE);


    private static final Material ENCHANT_ITEM = Material.BOOK;


    public static void open(Player player) {
        for (int i = 0; i < ROWS * 9; i++) {
            MENU.setItem(i, BACKGROUND_ITEM);
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.getType().toString().contains("_PICKAXE")) return;

        ItemStack relicsItem = new ItemStack(Material.NETHER_STAR);
        relicsItem.editMeta((meta) -> {
            meta.displayName(mm.deserialize("<light_purple>Relics").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

            meta.lore(List.of(
                    mm.deserialize("<gray>Select your relics here!").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            ));
        });

        MENU.setItem(19, item);
        MENU.setItem(28, relicsItem);

        int slot = 0;
        int row = 0;
        for (EnchantmentType enchantmentType : EnchantmentType.values()) {
            int guiSlot = 12 + (row * 9) + slot;
            slot++;
            if (slot >= 5) {
                row++;
                slot = 0;
            }

            MENU.setItem(guiSlot, enchantBook(enchantmentType, item));
        }


        player.openInventory(MENU);
    }

    @EventHandler
    public void onEnchantmentInventoryInteractEvent(InventoryClickEvent e) {
        if (!e.getView().title().equals(GUI_TITLE)) return;
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        e.setCancelled(true);
        if (e.getCurrentItem().getType() != Material.BOOK) return;


        // Get the pickaxe the player is holding
        ItemStack tool = e.getWhoClicked().getInventory().getItemInMainHand();
        if (!tool.getType().toString().contains("_PICKAXE")) return;

        // Retrieve the enchantment name from the clicked item
        ItemStack clickedItem = e.getCurrentItem();
        String enchantName = mm.stripTags(mm.serialize(clickedItem.getItemMeta().displayName())).replaceAll(" ", "_").toUpperCase();

        // Get the enchantment data
        Enchantment enchantment = EnchantmentHandler.getEnchantment(EnchantmentType.valueOf(enchantName));

        // Get enchantment level on the player's pickaxe
        int level = EnchantmentHandler.getEnchantmentLevel(EnchantmentType.valueOf(enchantName), tool);
        int nextLevel = level;
        int levelsToBuy = 1;

        // Determine how many levels we should buy
        if (e.getClick().isRightClick()) levelsToBuy = 10;
        if (e.getClick().isKeyboardClick()) levelsToBuy = 100;
        if (e.getClick().isShiftClick()) levelsToBuy = 1000;

        // Retrieve the prison player and the currency for the enchantment
        PrisonPlayer player = PrisonServer.getInstance().getPlayerManager().getPlayer(e.getWhoClicked().getUniqueId());
        Currency currency = enchantment.getCurrency();

        // Buy new enchants
        for (int i = 0; i < levelsToBuy; i++) {
            int cost = (int) (enchantment.getBaseCost() + (enchantment.getCostIncrease() * level + 1));

            // Check if the player has enough of the currency required
            if (player.getCurrency(currency) < cost || nextLevel >= enchantment.getMaxLevel()) {
                e.getWhoClicked().sendMessage("test");
                break;
            }

            player.setCurrency(currency,  player.getCurrency(currency) - cost);
            nextLevel++;
        }

        if (nextLevel == level && nextLevel >= enchantment.getMaxLevel()) {
            e.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Enchantments<reset> <gray>»This enchant is already max level!"));
            return;
        }

        if (nextLevel == level) {
            e.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Enchantments<reset> <gray>»You don't have enough <light_purple>%s<gray> to buy this enchantment!".formatted(currency.toString())));
            return;
        }

        EnchantmentHandler.applyEnchantment((Player) e.getWhoClicked(), tool, EnchantmentType.valueOf(enchantName), nextLevel);
        e.getClickedInventory().setItem(e.getSlot(), enchantBook(EnchantmentType.valueOf(enchantName), tool));
        e.getWhoClicked().sendMessage(mm.deserialize("<dark_purple><bold>Enchantments<reset> <gray>»You have bought <light_purple>%d level(s) <gray>of <dark_purple>".formatted(nextLevel - level)).append(enchantment.getName()));
    }



    private static ItemStack enchantBook(EnchantmentType type, ItemStack item) {
        ItemStack book = new ItemStack(ENCHANT_ITEM);
        Enchantment enchantment = EnchantmentHandler.getEnchantment(type);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(enchantment.getName().decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

        int level = EnchantmentHandler.getEnchantmentLevel(type, item);
        int cost = (int) (enchantment.getBaseCost() + (enchantment.getCostIncrease() * level + 1));
        double chance = enchantment.getBaseChance() + level * ((enchantment.getChanceAtMaxLevel() - enchantment.getBaseChance()) / enchantment.getMaxLevel());



        String desc = mm.serialize(enchantment.getDescription());
        String[] splitter = desc.split(" ");
        String split = "";
        if (splitter.length > 8) {
            split = splitter[7];
        }

        Component description = null;
        Component description2 = null;
        if (split.isEmpty()) {
            description = enchantment.getDescription();
        }

        if (!split.isEmpty()) {
            description = mm.deserialize(desc.split(split)[0]);
            description2 = mm.deserialize(split + desc.split(split)[1]);

        }

        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize(""));
        lore.add(mm.deserialize("<white><bold>Description <gray>").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE).append(description.decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)));
        if (!split.isEmpty()) lore.add(mm.deserialize("<gray>").append(description2).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lore.add(mm.deserialize(""));
        lore.add(mm.deserialize(String.format("<dark_purple> - <light_purple>Level <white>%d<gray>/<white>%d", level, enchantment.getMaxLevel())).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lore.add(mm.deserialize(String.format("<dark_purple> - <light_purple>Cost <white>%s %s", Util.formatNumber(cost, 0), enchantment.getCurrency().toString().charAt(0) + enchantment.getCurrency().toString().substring(1).toLowerCase())).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lore.add(mm.deserialize(String.format("<dark_purple> - <light_purple>Chance <white>%.2f%%", chance)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        meta.lore(lore);

        book.setItemMeta(meta);

        return book;
    }
}

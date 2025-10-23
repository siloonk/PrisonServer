package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.enchantments.EnchantmentInventory;
import io.github.siloonk.prisonServer.enchantments.EnchantmentType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantmentInventoryListener implements Listener {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        // Check if this is the enchantment inventory
        if (!mm.serialize(event.getView().title()).contains("Enchantments")) return;
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Check if the clicked item is an enchantment book
        if (clickedItem.getType() != Material.BOOK) return;
        
        // Get the pickaxe from slot 19
        ItemStack pickaxe = event.getInventory().getItem(19);
        if (pickaxe == null || !pickaxe.getType().toString().contains("_PICKAXE")) return;
        
        // Get the enchantment type from the clicked book
        EnchantmentType enchantmentType = getEnchantmentTypeFromSlot(event.getSlot());
        if (enchantmentType == null) return;
        
        Enchantment enchantment = EnchantmentHandler.getEnchantment(enchantmentType);
        if (enchantment == null) return;
        
        // Get current level and calculate cost
        int currentLevel = EnchantmentHandler.getEnchantmentLevel(enchantmentType, pickaxe);
        if (currentLevel >= enchantment.getMaxLevel()) {
            player.sendMessage(mm.deserialize("<red>This enchantment is already at max level!"));
            return;
        }
        
        int newLevel = currentLevel + 1;
        int cost = (int) (enchantment.getBaseCost() + (enchantment.getCostIncrease() * currentLevel + 1));
        Currency currency = enchantment.getCurrency();
        
        // Get player data
        PrisonPlayer prisonPlayer = PrisonServer.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        if (prisonPlayer == null) {
            player.sendMessage(mm.deserialize("<red>An error occurred. Please try again."));
            return;
        }
        
        // Check if player has enough currency
        long playerCurrency = getCurrencyAmount(prisonPlayer, currency);
        if (playerCurrency < cost) {
            String currencyName = currency.toString().charAt(0) + currency.toString().substring(1).toLowerCase();
            player.sendMessage(mm.deserialize(String.format("<red>You don't have enough %s! Need: <white>%d<red>, Have: <white>%d", currencyName, cost, playerCurrency)));
            return;
        }
        
        // Deduct currency
        deductCurrency(prisonPlayer, currency, cost);
        
        // Apply enchantment
        EnchantmentHandler.applyEnchantment(player, pickaxe, enchantment, newLevel);
        
        // Update the main hand with the enchanted pickaxe
        player.getInventory().setItemInMainHand(pickaxe);
        
        // Send success message
        String currencyName = currency.toString().charAt(0) + currency.toString().substring(1).toLowerCase();
        player.sendMessage(mm.deserialize(String.format("<green>Successfully upgraded <white>%s<green> to level <white>%d<green>! Cost: <white>%d %s", 
            mm.serialize(enchantment.getName()).replace("<gray>", "").replace("</gray>", ""), 
            newLevel, 
            cost, 
            currencyName)));
        
        // Reopen the inventory to show updated information
        player.closeInventory();
        EnchantmentInventory.open(player);
    }
    
    private EnchantmentType getEnchantmentTypeFromSlot(int slot) {
        // Calculate which enchantment was clicked based on the slot
        // Enchantments start at slot 12 and go in rows of 5
        int[] validSlots = {12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};
        
        int index = -1;
        for (int i = 0; i < validSlots.length; i++) {
            if (validSlots[i] == slot) {
                index = i;
                break;
            }
        }
        
        if (index == -1 || index >= EnchantmentType.values().length) return null;
        return EnchantmentType.values()[index];
    }
    
    private long getCurrencyAmount(PrisonPlayer player, Currency currency) {
        return switch (currency) {
            case MONEY -> player.getMoney();
            case TOKENS -> player.getTokens();
            case BUBBLES -> player.getBubbles();
        };
    }
    
    private void deductCurrency(PrisonPlayer player, Currency currency, int amount) {
        switch (currency) {
            case MONEY -> player.setMoney(player.getMoney() - amount);
            case TOKENS -> player.setTokens(player.getTokens() - amount);
            case BUBBLES -> player.setBubbles(player.getBubbles() - amount);
        }
    }
}

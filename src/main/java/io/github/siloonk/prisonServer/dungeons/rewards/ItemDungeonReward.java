package io.github.siloonk.prisonServer.dungeons.rewards;

import io.github.siloonk.prisonServer.CustomItems;
import io.github.siloonk.prisonServer.PrisonServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemDungeonReward implements DungeonReward{

    private ItemStack item;
    private int amount;

    public ItemDungeonReward(String itemId, int amount) {
        ItemStack item = PrisonServer.getInstance().getCustomItems().getItem(itemId);
        item.setAmount(amount);
    }

    public ItemStack getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void give(Player player) {
        // TODO: Add virtual item storage so nothing can be lost!

        // Give the item to the player
        player.getInventory().addItem(item);
    }
}

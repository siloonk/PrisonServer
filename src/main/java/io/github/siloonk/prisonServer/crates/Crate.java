package io.github.siloonk.prisonServer.crates;

import io.github.siloonk.prisonServer.utils.RandomCollection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Crate {


    private RandomCollection<String> commands;
    private final String displayName;
    private final Inventory displayInventory;
    private ItemStack crateItem;

    public Crate(String displayName, Inventory displayInventory, RandomCollection<String> commands, ItemStack crateItem) {
        this.displayInventory = displayInventory;
        this.displayName = displayName;
        this.commands = commands;
        this.crateItem = crateItem;
    }


    public ItemStack getCrateItem() {
        return crateItem;
    }

    public Inventory getDisplayInventory() {
        return displayInventory;
    }

    public void use(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.next().replaceAll("%player%", player.getName()));
        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
    }

    public RandomCollection<String> getCommands() {
        return commands;
    }

    public String getDisplayName() {
        return displayName;
    }
}

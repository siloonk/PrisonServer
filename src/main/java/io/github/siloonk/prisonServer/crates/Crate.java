package io.github.siloonk.prisonServer.crates;

import io.github.siloonk.prisonServer.utils.RandomCollection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Crate {


    private RandomCollection<String> commands;
    private final String displayName;
    private final Inventory displayInventory;

    public Crate(String displayName, Inventory displayInventory, RandomCollection<String> commands) {
        this.displayInventory = displayInventory;
        this.displayName = displayName;
        this.commands = commands;
    }


    public void use(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.next().replaceAll("%player%", player.getName()));
    }

    public RandomCollection<String> getCommands() {
        return commands;
    }

    public String getDisplayName() {
        return displayName;
    }
}

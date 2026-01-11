package io.github.siloonk.prisonServer.commands;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.mines.Mine;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MineCommand extends Command {

    public MineCommand() {
        super("mine");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return false;
        }

        Player player = (Player) commandSender;
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUniqueId());
        // If the player doesn't have a mine yet create one
        if (mine == null) {
            mine = new Mine(player.getUniqueId(), 53, 50, new Location(player.getWorld(), 148, 130, 144), Material.STONE, 10);
            PrisonServer.getInstance().getMineManager().registerMine(mine);
            player.teleport(mine.getCenterLocation());
            mine.displayMineToPlayer(player);
            return false;
        }

        // Home Sub Command
        if (args.length == 1 && args[0].equalsIgnoreCase("home")) {
            player.teleport(mine.getCenterLocation());
            mine.displayMineToPlayer(player);
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            mine.displayMineToPlayer(player);
            player.teleport(mine.getCenterLocation());
        }



        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("home", "reset");
        }

        return new ArrayList<>();
    }
}

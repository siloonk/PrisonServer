package io.github.siloonk.prisonServer.commands;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.crates.Crate;
import io.github.siloonk.prisonServer.crates.CrateBlock;
import io.github.siloonk.prisonServer.data.Warp;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CratesCommand extends Command {

    MiniMessage mm = MiniMessage.miniMessage();

    public CratesCommand() {
        super("crates");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(mm.deserialize("<red>You must be a player to execute this command!"));
                return true;
            }
            Player player = (Player) commandSender;
            Warp warp = PrisonServer.getInstance().getDatabase().getWarpDAO().getWarp("crates");
            if (warp == null) {
                player.sendMessage(mm.deserialize("<dark_purple><bold>Crates<reset> <light_purple>» <gray>Something went wrong trying to teleport to crates, Please contact and admin!"));
                return true;
            }

            player.teleport(warp.getLocation());
            player.sendMessage(mm.deserialize("<dark_purple><bold>Crates<reset> <light_purple>» <gray>You have been teleported to crates!"));
            return true;
        }

        if (args.length == 1) {
            if (!commandSender.hasPermission("crates.admin")) {
                commandSender.sendMessage(mm.deserialize("<red>You don't have permissions to execute this command!"));
                return true;
            }

            if (args[0].equalsIgnoreCase("teleport")) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(mm.deserialize("<red>You must be a player to execute this command!"));
                    return true;
                }
                Player player = (Player) commandSender;

                Warp warp = PrisonServer.getInstance().getDatabase().getWarpDAO().getWarp("crates");
                if (warp == null) {
                    PrisonServer.getInstance().getDatabase().getWarpDAO().insertWarp(new Warp("crates", player.getLocation().getWorld().getName(), player.getLocation()));
                } else {
                    PrisonServer.getInstance().getDatabase().getWarpDAO().updateWarp(new Warp("crates", player.getLocation().getWorld().getName(), player.getLocation()));
                }
                player.sendMessage(mm.deserialize("<dark_purple>Crates<reset> <light_purple>» <gray>You have set the <light_purple>Teleport Location<gray> to your location!"));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                PrisonServer.getInstance().getCratesManager().loadCrates();
                commandSender.sendMessage(mm.deserialize("<dark_purple>Crates<reset> <light_purple>» <gray>Crates have been <green>reloaded<gray>!"));
            }
        }

        if (args.length == 2) {
            if (!commandSender.hasPermission("crates.admin")) {
                commandSender.sendMessage(mm.deserialize("<red>You don't have permissions to execute this command!"));
                return true;
            }

            if (args[0].equalsIgnoreCase("create")) {
                String id = args[1];
                if (PrisonServer.getInstance().getCratesManager().getCrate(id) == null) {
                    commandSender.sendMessage(mm.deserialize("<dark_purple>Crates<reset> <light_purple>» <gray>The <red>%s Crate<gray> does not exist!".formatted(id)));
                    return true;
                }

                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(mm.deserialize("<red>You must be a player to perform this action!"));
                    return true;
                }
                Player player = (Player) commandSender;


                // Crate exists
                PrisonServer.getInstance().getCrateBlocksManager().addCrateBlock(new CrateBlock(id, player.getLocation().getBlock().getLocation()));
                player.sendMessage(mm.deserialize("<dark_purple>Crates<reset> <light_purple>» <gray>The <green>%s Crate<gray> has been created at your location!".formatted(id)));
                return true;
            }
        }


        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission("crates.admin")) {
            return new ArrayList<>();
        }

        if (args.length == 1) {
            return List.of("teleport", "reload", "create");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            return PrisonServer.getInstance().getCratesManager().getCrates().stream().toList();
        }

        return new ArrayList<>();
    }
}

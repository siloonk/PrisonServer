package io.github.siloonk.prisonServer.commands;

import io.github.siloonk.prisonServer.PrisonServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemCommand extends Command {

    MiniMessage mm = MiniMessage.miniMessage();

    protected ItemCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command!");
            return true;
        }

        if (!sender.hasPermission("prisons.items")) {
            sender.sendMessage(mm.deserialize("<red>You don't have the right permissions to execute this command!"));
            return true;
        }

        if (args.length == 0) {
            sendHelpPage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create" -> {

            }

            case "give" -> {
                if (args.length < 3) {
                    sender.sendMessage(mm.deserialize("<red>Invalid use of the /item give command! please refer to the help page"));
                    return true;
                }

                String name = args[1];
                Player player = Bukkit.getPlayer(args[2]);
                if (player == null) {
                    sender.sendMessage(mm.deserialize("<red>Could not find this player!"));
                    return true;
                }

                int amount = 1;
                if (args.length == 4) {
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(mm.deserialize("<red>Invalid number!"));
                    }
                }

                ItemStack item = PrisonServer.getInstance().getCustomItems().getItem(name.toUpperCase());
                item.setAmount(amount);
                player.getInventory().addItem(item);
                player.sendMessage(mm.deserialize("<dark_purple><bold>Custom Items<reset> <light_purple>» <gray>You have been given <light_purple>%dx %s<gray>!".formatted(amount, name)));
            }

            default -> {
                sender.sendMessage("This item does not exist!");
            }
        }

        return true;
    }

    private void sendHelpPage(CommandSender sender) {
        sender.sendMessage(mm.deserialize("<dark_purple>/item Help Page"));
        sender.sendMessage(mm.deserialize("<dark_purple>/item <light_purple>give (name) (player) (amount), <white>Give a custom item to a player!"));
        sender.sendMessage(mm.deserialize("<dark_purple>/item <light_purple>create (name), <white>Create a custom item right inside the game itself!"));
        sender.sendMessage("");
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission("prisons.item")) {
            return new ArrayList<>();
        }

        if (args.length == 0) {
            return List.of("give", "create");
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            return PrisonServer.getInstance().getCustomItems().getAllItemNames();
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return List.of("amount (optional)");
        }


        return new ArrayList<>();
    }
}

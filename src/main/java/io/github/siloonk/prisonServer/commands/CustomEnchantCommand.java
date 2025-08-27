package io.github.siloonk.prisonServer.commands;

import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.enchantments.EnchantmentType;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEnchantCommand extends Command {

    private static final MiniMessage mm = MiniMessage.miniMessage();

    public CustomEnchantCommand() {
        super("ce");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("ce.apply")) {
            player.sendMessage(mm.deserialize("<red>You don't have permission to execute this command!"));
            return true;
        }


        // /ce siloonk EXPLOSIVE 5000
        if (args.length == 3) {
            Player target = (Player) Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(mm.deserialize("<red>Player not found!"));
                return true;
            }

            EnchantmentType type = null;
            try {
                type = EnchantmentType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(mm.deserialize("<red>Invalid enchant type!"));
                return true;
            }

            int level = -1;
            try {
                level = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                player.sendMessage(mm.deserialize("<red>Invalid enchant level!"));
                return true;
            }

            if (target.getInventory().getItemInMainHand().getType() != Material.DIAMOND_PICKAXE) {
                player.sendMessage(mm.deserialize("<red>This item cannot be enchanted!"));
                return true;
            }

            ItemStack item = target.getInventory().getItemInMainHand();
            EnchantmentHandler.applyEnchantment(target, item, type, level);
            player.sendMessage(mm.deserialize(String.format("<green>Successfully applied %s %d to %s's pickaxe!", type.toString(), level, target.getName())));
            target.sendMessage(mm.deserialize(String.format(String.format("<dark_purple><bold>Enchants <reset><light_purple>» <gray>%s %d has been added to your pickaxe!", type, level))));
            return true;
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (!sender.hasPermission("ce.add")) return new ArrayList<>();
        if (args.length == 1) return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        if (args.length == 2) return Arrays.stream(EnchantmentType.values()).map(e -> e.toString().toLowerCase()).toList();

        return super.tabComplete(sender, alias, args);
    }
}

package io.github.siloonk.prisonServer.commands;

import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.enchantments.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TestExplosiveCommand extends Command {

    public TestExplosiveCommand() {
        super("explosivePickaxe");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.LIGHTNING), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.LUCKY), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.STAR_CORE), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.ECLIPSE), 1000);
     //   EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.COSMIC_RIFT), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.STAR_FALL), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.EXPLOSIVE), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.JACKHAMMER), 1000);
//        EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(EnchantmentType.LIGHTNING), 1000);
        for (EnchantmentType type : EnchantmentType.values()) {
            if (EnchantmentHandler.getEnchantment(type) == null) continue;
            EnchantmentHandler.applyEnchantment((Player) sender, item, EnchantmentHandler.getEnchantment(type), 1000);
        }
        Player player = (Player) sender;
        player.getInventory().addItem(item);
        return true;
    }
}

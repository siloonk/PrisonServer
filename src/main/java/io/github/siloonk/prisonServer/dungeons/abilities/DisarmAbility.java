package io.github.siloonk.prisonServer.dungeons.abilities;

import io.github.siloonk.prisonServer.PrisonServer;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DisarmAbility extends BossAbility {

    @Override
    public void trigger(EntityDamageByEntityEvent event) {
        LivingEntity entity = (LivingEntity) event.getDamager();

        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        ItemStack tool = player.getInventory().getItemInMainHand();
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        Sound sound = Sound.sound().type(NamespacedKey.minecraft("block.anvil.break")).build();
        player.playSound(sound);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().setItemInMainHand(tool);
                player.playSound(Sound.sound().type(NamespacedKey.minecraft("entity.experience_orb.pickup")).build());
            }
        }.runTaskLater(PrisonServer.getInstance(), 5*20L /* 5 seconds */);

    }
}

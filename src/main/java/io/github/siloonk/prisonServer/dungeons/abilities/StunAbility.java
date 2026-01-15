package io.github.siloonk.prisonServer.dungeons.abilities;

import io.github.siloonk.prisonServer.PrisonServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class StunAbility extends BossAbility {


    @Override
    public void trigger(EntityDamageByEntityEvent event) {
        LivingEntity entity = (LivingEntity) event.getDamager();

        // Make sure the damager is a player
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        float walkSpeed = player.getWalkSpeed();
        player.setWalkSpeed(0);
        // Hopefully this prevents the player from jumping?
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 100, 128));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setWalkSpeed(walkSpeed);
            }
        }.runTaskLater(PrisonServer.getInstance(), 5*20L);

    }
}

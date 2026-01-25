package io.github.siloonk.prisonServer.dungeons.abilities;

import io.github.siloonk.prisonServer.utils.Util;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EnrageAbility extends BossAbility {

    @Override
    public void trigger(EntityDamageByEntityEvent event) {
        LivingEntity target = (LivingEntity) event.getEntity();
        AttributeInstance damage = target.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        damage.setBaseValue(damage.getValue()*3);

        AttributeInstance health = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        // Increase health by 30% if it's above 70% of the max health set it to the max health
        target.setHealth(Math.min(health.getValue() * 0.3 + target.getHealth(), health.getValue()*0.7));

        // Play explosion effect indicating the buff
        for (Player player : event.getEntity().getLocation().getNearbyPlayers(20, 10, 20)) {
            Util.fakeExplosion(event.getEntity().getLocation(), player);

            Sound sound = Sound.sound().type(NamespacedKey.minecraft("entity.wither.break_block")).build();
            player.playSound(sound);
        }
    }
}

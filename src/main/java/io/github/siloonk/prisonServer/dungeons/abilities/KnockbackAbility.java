package io.github.siloonk.prisonServer.dungeons.abilities;

import io.github.siloonk.prisonServer.utils.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Collection;

public class KnockbackAbility extends BossAbility {

    /**
     * Keep track of the range in which players are affected
     */
    private final int range;

    /**
     * Keep track of the force that should be applied to the player
     */
    private final double force;

    /**
     * The damage that should be dealt to the players
     */
    private final double damage;

    public KnockbackAbility(int range, double force, double damage) {
        this.range = range;
        this.force = force;
        this.damage = damage;
    }

    @Override
    public void trigger(EntityDamageByEntityEvent event) {
        Location location = event.getEntity().getLocation();

        Collection<Player> players = location.getNearbyPlayers(range);

        players.forEach((player) -> {
            Util.fakeExplosion(location, player);
            player.setVelocity(location.toVector().subtract(player.getLocation().toVector()).multiply(force));
            player.damage(damage);
        });
    }
}

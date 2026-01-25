package io.github.siloonk.prisonServer.dungeons.abilities;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EarthquakeAbility extends BossAbility{


    private double damageMultiplier;

    public EarthquakeAbility(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public void trigger(EntityDamageByEntityEvent event) {
        
    }
}

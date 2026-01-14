package io.github.siloonk.prisonServer.dungeons.abilities;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class BossAbility {


    /**
     * Name of the ability
     */
    private String name;

    /**
     * Executed when the ability is triggered
     * @param event damage event that triggered this ability
     */
    public abstract void trigger(EntityDamageByEntityEvent event);
}

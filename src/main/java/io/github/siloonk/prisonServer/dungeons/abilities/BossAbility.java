package io.github.siloonk.prisonServer.dungeons.abilities;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public abstract class BossAbility {


    /**
     * Name of the ability
     */
    private String name;

    /**
     * Executed when the ability is triggered
     * @param location location of the boss
     */
    public abstract void trigger(Location location);
}

package io.github.siloonk.prisonServer.dungeons.abilities;

import org.bukkit.configuration.ConfigurationSection;

public enum BossAbilityType {
    DISARM,
    KNOCKBACK,
    ENRAGED,
    ULTIMATE_DEFENSE;

    public BossAbility generateAbility(ConfigurationSection section) {

        if (this == KNOCKBACK) {
            // get some data
            int range = section.getInt("range");
            int force = section.getInt("force");
            int damage = section.getInt("damage");

            return new KnockbackAbility(range, force, damage);
        }

        return null;

    }
}

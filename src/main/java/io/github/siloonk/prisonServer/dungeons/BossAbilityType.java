package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.dungeons.abilities.BossAbility;
import io.github.siloonk.prisonServer.dungeons.abilities.KnockbackAbility;
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

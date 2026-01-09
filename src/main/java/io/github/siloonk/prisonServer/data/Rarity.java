package io.github.siloonk.prisonServer.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;


public enum Rarity {

    COMMON("<gray>"),
    UNCOMMON("<green>"),
    RARE("<aqua>"),
    EPIC("<light_purple>"),
    LEGENDARY("<gold>");

    private final String color;

    private static final MiniMessage mm = MiniMessage.miniMessage();

    Rarity(String color) {
        this.color = color;
    }

    public Component getName() {
        return mm.deserialize(color + toString().charAt(0) + toString().substring(1).toLowerCase()).decoration(TextDecoration.ITALIC, false);
    }
}

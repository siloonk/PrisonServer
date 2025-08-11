package io.github.siloonk.prisonServer.data;

import org.bukkit.Location;

public class Warp {

    private String name;
    private String worldName;
    private Location location;

    public Warp(String name, String worldName, Location location) {
        this.name = name;
        this.worldName = worldName;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getWorldName() {
        return worldName;
    }

    public Location getLocation() {
        return location;
    }
}

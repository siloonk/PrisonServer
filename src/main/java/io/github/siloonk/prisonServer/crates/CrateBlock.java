package io.github.siloonk.prisonServer.crates;


import org.bukkit.Location;

public class CrateBlock {


    private String id;
    private String worldName;
    private Location location;

    public CrateBlock() {

    }

    public CrateBlock(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWorldName(String worldName) {
        // Required
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getWorldName() {
        return location.getWorld().getName();
    }

    public Location getLocation() {
        return location;
    }
}

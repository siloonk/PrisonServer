package io.github.siloonk.prisonServer.data.mines;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Mine {

    private UUID owner;
    private int width, height;
    private Location centerLocation = new Location(null, 0, 0, 0);
    private String worldName;
    private Material blockType;

    private double luckyBlockChance;

    private ArrayList<Location> starCores = new ArrayList<>();

    public Mine() {

    }

    public Mine(UUID owner, int width, int height, Location centerLocation, Material blockType, double luckyBlockChance) {
        this.owner = owner;
        this.width = width;
        this.height=  height;
        this.centerLocation = centerLocation;
        this.worldName = centerLocation.getWorld().getName();
        this.blockType = blockType;
        this.luckyBlockChance = luckyBlockChance;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setCenterLocation(Location centerLocation) {
        this.centerLocation = centerLocation;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public void setLuckyBlockChance(double luckyBlockChance) {
        this.luckyBlockChance = luckyBlockChance;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Location getCenterLocation() {
        if (centerLocation.getWorld() == null) centerLocation.setWorld(Bukkit.getWorld(worldName));
        return centerLocation;
    }

    public void addStarCore(Location loc) {
        this.starCores.add(loc);
    }

    public void removeStarCore(Location loc) {
        this.starCores.remove(loc);
    }

    public boolean isStarCore(Location loc) {
        return starCores.stream().anyMatch(c -> c.equals(loc));
    }

    public String getWorldName() {
        return worldName;
    }

    public Material getBlockType() {
        return blockType;
    }

    public double getLuckyBlockChance() {
        return luckyBlockChance;
    }

    public void setX(int x) {
        centerLocation.setX(x);
    }

    public void setY(int y) {
        centerLocation.setY(y);
    }

    public void setZ(int z) {
        centerLocation.setZ(z);
    }

    public void displayMineToPlayer(Player player) {
        Util.setBlocksFast(
                new Location(
                    centerLocation.getWorld(),
                    centerLocation.getBlockX() - (double) width /2,
                    centerLocation.getBlockY() - height,
                        centerLocation.getBlockZ() - (double) width /2
                ),
                new Location(
                        centerLocation.getWorld(),
                        centerLocation.getBlockX() + (double) width /2,
                        centerLocation.getBlockY(),
                        centerLocation.getBlockZ() + (double) width /2
                ),
                player,
                blockType
        );

        starCores.clear();

    }


    public boolean isWithin(Location loc) {
        return (loc.getBlockX() >= centerLocation.getBlockX() - width/2 && loc.getBlockX() <= centerLocation.getBlockX() + width/2)
                && (loc.getBlockY() >= centerLocation.getBlockY() - height && loc.getBlockY() <= centerLocation.getBlockY())
                && (loc.getBlockZ() >= centerLocation.getBlockZ() - width/2 && loc.getBlockZ() <= centerLocation.getBlockZ() + width/2);
    }
}

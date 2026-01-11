package io.github.siloonk.prisonServer.data.players;

import io.github.siloonk.prisonServer.PrisonServer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PrisonPlayerManager {

    private HashMap<UUID, PrisonPlayer> players = new HashMap<>();


    public PrisonPlayerManager() {
        loadPlayers();
        savePlayers();
    }


    /**
     * Get the player with a specific UUID
     * @param uuid
     * @return the player or null if no player was found!
     */
    public PrisonPlayer getPlayer(UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    /**
     * Register a new prison player
     * @param prisonPlayer
     */
    public void registerPlayer(PrisonPlayer prisonPlayer) {
        this.players.put(prisonPlayer.getUuid(), prisonPlayer);
        PrisonServer.getInstance().getDatabase().getPlayerDAO().insertPlayer(prisonPlayer);
    }


    private void loadPlayers() {
        System.out.println("Loading all players...");
        long now = new Date().getTime();
        for (PrisonPlayer player : PrisonServer.getInstance().getDatabase().getPlayerDAO().listPlayers()) {
            players.put(player.getUuid(), player);
        }
        System.out.printf("Loaded all players in %dms%n", new Date().getTime() - now);
    }

    /**
     * Start a task to save all players every 60 seconds
     */
    private void savePlayers() {
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("Saving all player data");
                players.forEach((uuid, prisonPlayer) -> PrisonServer.getInstance().getDatabase().getPlayerDAO().update(prisonPlayer));

                // Delete all players from the list that are no longer online, to make sure ram isn't getting wasted
                List<UUID> offlinePlayers = players.keySet().stream().filter((uuid) -> Bukkit.getPlayer(uuid) == null).toList();
                offlinePlayers.forEach((p) -> players.remove(p));

                System.out.printf("Saved all player data Cleaned %d players%n", offlinePlayers.size());
            }
        }.runTaskTimer(PrisonServer.getInstance(), 60*20L, 60*20L);
    }

}

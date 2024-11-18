package io.github.siloonk.prisonServer.data.players;

import io.github.siloonk.prisonServer.PrisonServer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class PrisonPlayerManager {

    private HashMap<UUID, PrisonPlayer> players = new HashMap<>();


    public PrisonPlayerManager() {
        loadPlayers();
        savePlayers();
        System.out.println(players.size());
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
                System.out.println("Saved all player data");
            }
        }.runTaskTimer(PrisonServer.getInstance(), 60*20L, 60*20L);
    }

}

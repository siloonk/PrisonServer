package io.github.siloonk.prisonServer.data.mines;

import io.github.siloonk.prisonServer.PrisonServer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MineManager {

    private HashMap<UUID, Mine> mines = new HashMap<>();

    public MineManager() {
        loadMines();
        saveMines();
    }

    private void loadMines() {
        List<Mine> mines = PrisonServer.getInstance().getDatabase().getMineDAO().listMines();
        for (Mine mine : mines) {
            this.mines.put(mine.getOwner(), mine);
        }
    }

    public void registerMine(Mine mine) {
        mines.put(mine.getOwner(), mine);
        PrisonServer.getInstance().getDatabase().getMineDAO().insertMine(mine);
    }

    private void saveMines() {
        for (Mine mine : mines.values()) {
            PrisonServer.getInstance().getDatabase().getMineDAO().updateMine(mine);
        }
    }

    public Mine getMine(UUID uuid) {
        return mines.get(uuid);
    }
}

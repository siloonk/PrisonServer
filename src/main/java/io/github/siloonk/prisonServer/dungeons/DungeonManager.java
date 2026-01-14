package io.github.siloonk.prisonServer.dungeons;

import io.github.siloonk.prisonServer.PrisonServer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DungeonManager {

    /**
     * List of all dungeons
     */
    private HashMap<String, Dungeon> dungeons = new HashMap<>();

    /**
     * Load all dungeons from the dungeon config
     */
    public void loadDungeons() {

        // Get the dungeon folder directory
        File dungeonFolder = getDungeonFolder();

        if (!dungeonFolder.isDirectory()) {
            System.err.println("Something went wrong trying to load dungeons!");
            return;
        }



    }


    /**
     * Get the dungeon folder, if this does not exist create it
     * @return file referencing the dungeon folder
     */
    private File getDungeonFolder() {
        File file = new File(PrisonServer.getInstance().getDataFolder() + "/config/dungeons");

        if (!file.exists()) {
            PrisonServer.getInstance().saveResource("config/dungeons", false);
        }
        return file;
    }

}

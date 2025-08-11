package io.github.siloonk.prisonServer.crates;

import io.github.siloonk.prisonServer.PrisonServer;

import java.util.Collection;
import java.util.HashMap;

public class CrateBlocksManager {

    private HashMap<String, CrateBlock> crateBlocks = new HashMap<>();


    public CrateBlocksManager() {
        loadCrateBlocks();
    }

    public void loadCrateBlocks() {
        crateBlocks.clear();

        PrisonServer.getInstance().getDatabase().getCrateDAO().listCrateBlocks().forEach(crateBlock -> {
           crateBlocks.put(crateBlock.getId(), crateBlock);
        });
    }

    public Collection<CrateBlock> getBlocks() {
        return crateBlocks.values();
    }

    public void addCrateBlock(CrateBlock crateBlock) {
        removeCrateBlock(crateBlock);
        crateBlocks.put(crateBlock.getId(), crateBlock);
        PrisonServer.getInstance().getDatabase().getCrateDAO().insertCrate(crateBlock);
    }

    public void removeCrateBlock(CrateBlock crateBlock) {
        crateBlocks.remove(crateBlock.getId());
        PrisonServer.getInstance().getDatabase().getCrateDAO().deleteCrate(crateBlock.getId());
    }
}

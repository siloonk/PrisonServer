package io.github.siloonk.prisonServer;

import io.github.siloonk.prisonServer.commands.CratesCommand;
import io.github.siloonk.prisonServer.commands.ItemCommand;
import io.github.siloonk.prisonServer.commands.TestExplosiveCommand;
import io.github.siloonk.prisonServer.commands.TestMineCommand;
import io.github.siloonk.prisonServer.crates.Crate;
import io.github.siloonk.prisonServer.crates.CrateBlock;
import io.github.siloonk.prisonServer.crates.CrateBlocksManager;
import io.github.siloonk.prisonServer.crates.CratesManager;
import io.github.siloonk.prisonServer.data.mines.MineManager;
import io.github.siloonk.prisonServer.data.players.PrisonPlayerManager;
import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.items.CustomItems;
import io.github.siloonk.prisonServer.listeners.BlockBreakListener;
import io.github.siloonk.prisonServer.listeners.CrateInteractEvent;
import io.github.siloonk.prisonServer.listeners.PlayerJoinListener;
import io.github.siloonk.prisonServer.listeners.ScoreboardHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonServer extends JavaPlugin {

    private static PrisonServer instance;
    private Database database;
    private PrisonPlayerManager prisonPlayerManager;
    private MineManager mineManager;
    private EnchantmentHandler enchantmentHandler;
    private CustomItems customItems;
    private CratesManager cratesManager;
    private CrateBlocksManager crateBlocksManager;

    @Override
    public void onEnable() {
        instance = this;
        database = new Database();
        prisonPlayerManager = new PrisonPlayerManager();
        mineManager = new MineManager();
        enchantmentHandler = new EnchantmentHandler();
        customItems = new CustomItems();
        cratesManager = new CratesManager();
        crateBlocksManager = new CrateBlocksManager();
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Database getDatabase() {
        return database;
    }

    public PrisonPlayerManager getPlayerManager() {
        return prisonPlayerManager;
    }

    public MineManager getMineManager() {
        return mineManager;
    }

    public static PrisonServer getInstance() {
        return instance;
    }

    public EnchantmentHandler getEnchantmentHandler() {
        return enchantmentHandler;
    }

    public CrateBlocksManager getCrateBlocksManager() {
        return crateBlocksManager;
    }

    public CustomItems getCustomItems() {
        return customItems;
    }

    public CratesManager getCratesManager() {
        return cratesManager;
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(enchantmentHandler, this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new CrateInteractEvent(), this);
        pm.registerEvents(new ScoreboardHandler(), this);
    }

    private void registerCommands() {
        getServer().getCommandMap().register("prisons", new TestMineCommand());
        getServer().getCommandMap().register("prisons", new TestExplosiveCommand());
        getServer().getCommandMap().register("prisons", new ItemCommand());
        getServer().getCommandMap().register("prisons", new CratesCommand());
    }
}

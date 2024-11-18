package io.github.siloonk.prisonServer;

import io.github.siloonk.prisonServer.commands.TestExplosiveCommand;
import io.github.siloonk.prisonServer.commands.TestMineCommand;
import io.github.siloonk.prisonServer.data.mines.MineManager;
import io.github.siloonk.prisonServer.data.players.PrisonPlayerManager;
import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.listeners.PlayerJoinListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrisonServer extends JavaPlugin {

    private static PrisonServer instance;
    private Database database;
    private PrisonPlayerManager prisonPlayerManager;
    private MineManager mineManager;
    private EnchantmentHandler enchantmentHandler;

    @Override
    public void onEnable() {
        instance = this;
        database = new Database();
        prisonPlayerManager = new PrisonPlayerManager();
        mineManager = new MineManager();
        enchantmentHandler = new EnchantmentHandler();
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


    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(enchantmentHandler, this);
    }

    private void registerCommands() {
        getServer().getCommandMap().register("prisons", new TestMineCommand());
        getServer().getCommandMap().register("prisons", new TestExplosiveCommand());
    }
}

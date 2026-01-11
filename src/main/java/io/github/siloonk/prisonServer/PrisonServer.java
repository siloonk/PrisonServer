package io.github.siloonk.prisonServer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.siloonk.prisonServer.commands.*;
import io.github.siloonk.prisonServer.crates.CrateBlocksManager;
import io.github.siloonk.prisonServer.crates.CratesManager;
import io.github.siloonk.prisonServer.data.mines.MineManager;
import io.github.siloonk.prisonServer.data.players.PrisonPlayerManager;
import io.github.siloonk.prisonServer.data.relics.RelicManager;
import io.github.siloonk.prisonServer.enchantments.EnchantmentHandler;
import io.github.siloonk.prisonServer.inventories.EnchantmentInventory;
import io.github.siloonk.prisonServer.inventories.RelicInventory;
import io.github.siloonk.prisonServer.listeners.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PrisonServer extends JavaPlugin {

    private static PrisonServer instance;
    private Database database;
    private PrisonPlayerManager prisonPlayerManager;
    private MineManager mineManager;
    private EnchantmentHandler enchantmentHandler;
    private CustomItems customItems;
    private CratesManager cratesManager;
    private CrateBlocksManager crateBlocksManager;
    private ProtocolManager protocolManager;
    private RelicManager relicManager;

    @Override
    public void onEnable() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        database = new Database();
        prisonPlayerManager = new PrisonPlayerManager();
        mineManager = new MineManager();
        enchantmentHandler = new EnchantmentHandler();
        customItems = new CustomItems();
        cratesManager = new CratesManager();
        crateBlocksManager = new CrateBlocksManager();
        relicManager = new RelicManager();
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

    public ProtocolManager getProtocolLibrary() {
        return protocolManager;
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

    public RelicManager getRelicManager() {
        return relicManager;
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new CrateInteractEvent(), this);
        pm.registerEvents(new ScoreboardHandler(), this);
        pm.registerEvents(new RelicInteractListener(), this);

        pm.registerEvents(new RelicInventory(), this);
        pm.registerEvents(new EnchantmentInventory(), this);


        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.HIGH, PacketType.Play.Client.USE_ITEM_ON, PacketType.Play.Client.USE_ITEM) {


            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.USE_ITEM_ON) {
                    event.setCancelled(true);
                }

                if (event.getPacketType() == PacketType.Play.Client.USE_ITEM || event.getPacketType() == PacketType.Play.Client.USE_ITEM_ON) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            EnchantmentInventory.open(event.getPlayer());
                        }
                    }.runTask(PrisonServer.getInstance());
                }
            }
        });
    }

    private void registerCommands() {
        getServer().getCommandMap().register("prisons", new TestMineCommand());
        getServer().getCommandMap().register("prisons", new TestExplosiveCommand());
        getServer().getCommandMap().register("prisons", new CustomEnchantCommand());
        getServer().getCommandMap().register("prisons", new ItemCommand());
        getServer().getCommandMap().register("prisons", new CratesCommand());
        getServer().getCommandMap().register("prisons", new MineCommand());
    }
}

package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.utils.ComponentScoreboard;
import io.github.siloonk.prisonServer.utils.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardHandler implements Listener {

    String[] ranks = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public ScoreboardHandler() {
        loadScoreboard();
    }

    private static final MiniMessage mm = MiniMessage.miniMessage();

    private Component title;
    private List<String> lines = new ArrayList<>();

    private void loadScoreboard() {
        File file = new File(PrisonServer.getInstance().getDataFolder(), "/config/scoreboard.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            PrisonServer.getInstance().saveResource("config/scoreboard.yml", false);
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        title = mm.deserialize(yamlConfiguration.getString("title"));
        lines.addAll(yamlConfiguration.getStringList("lines"));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        ComponentScoreboard board = new ComponentScoreboard(title);



        new BukkitRunnable() {
            PrisonPlayer prisonPlayer = PrisonServer.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());

            @Override
            public void run() {
                if (prisonPlayer == null) {
                    prisonPlayer = PrisonServer.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
                    System.out.println("test");
                    return;
                }

                int lineNumber = 0;
                for (String line : lines) {
                    board.setLine(lineNumber, mm.deserialize(formatString(line, prisonPlayer)));
                    lineNumber++;
                }
                board.send(event.getPlayer());
            }
        }.runTaskTimer(PrisonServer.getInstance(), 0L, 20L);
    }

    private String formatString(String s, PrisonPlayer player) {
        return s.replaceAll("%money%", Util.formatNumber(player.getMoney(), 0))
                .replaceAll("%tokens%", Util.formatNumber(player.getTokens(), 0))
                .replaceAll("%bubbles%", player.getBubbles()+"")
                .replaceAll("%mine_rank%", ranks[player.getLevel()-1])
                .replaceAll("%prestige%", player.getPrestige()+"")
                .replaceAll("%blocks_mined%", player.getBlocksMined()+"")
                .replaceAll("%tps%", Math.round(PrisonServer.getInstance().getServer().getTPS()[0])+"")
                .replaceAll("%online_players%", PrisonServer.getInstance().getServer().getOnlinePlayers().size()+"");
    }
}

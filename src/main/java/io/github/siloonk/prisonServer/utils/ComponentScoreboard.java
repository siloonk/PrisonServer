package io.github.siloonk.prisonServer.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

public class ComponentScoreboard {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<Integer, Team> lineTeams = new HashMap<>();
    private final int maxLines = 15; // Minecraft limit for sidebar lines

    public ComponentScoreboard(Component title) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        this.objective = scoreboard.registerNewObjective(
                "dummy",
                Criteria.DUMMY,
                title
        );
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Pre-create lines and unique entries
        for (int i = 1; i <= maxLines; i++) {
            String entry = "§" + Integer.toHexString(i); // unique invisible ID
            Team team = scoreboard.registerNewTeam("line" + i);
            team.addEntry(entry);
            objective.getScore(entry).setScore(maxLines - i); // order: top to bottom
            lineTeams.put(i, team);
        }
    }

    /**
     * Set a line's text using prefix/suffix Components
     * If suffix is null, only prefix is shown
     */
    public void setLine(int lineNumber, Component prefix, Component suffix) {
        if (lineNumber < 1 || lineNumber > maxLines) return;
        Team team = lineTeams.get(lineNumber);
        team.prefix(prefix != null ? prefix : Component.empty());
        team.suffix(suffix != null ? suffix : Component.empty());
    }

    /**
     * Set a line's text (only prefix)
     */
    public void setLine(int lineNumber, Component text) {
        setLine(lineNumber, text, null);
    }

    /**
     * Clear a specific line
     */
    public void clearLine(int lineNumber) {
        setLine(lineNumber, Component.empty(), null);
    }

    /**
     * Show this scoreboard to a player
     */
    public void send(Player player) {
        player.setScoreboard(scoreboard);
    }
}

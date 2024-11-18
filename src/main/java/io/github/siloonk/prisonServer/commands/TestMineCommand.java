package io.github.siloonk.prisonServer.commands;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.mines.Mine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestMineCommand extends Command {
    public TestMineCommand() {
        super("createmine");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        Mine mine = PrisonServer.getInstance().getMineManager().getMine(player.getUniqueId());
        if (mine == null) {
            mine = new Mine(player.getUniqueId(), 100, 50, new Location(player.getWorld(), 0, 100, 0), Material.DIAMOND_BLOCK, 10);
            PrisonServer.getInstance().getMineManager().registerMine(mine);
        }
        mine.displayMineToPlayer(player);
        player.teleport(mine.getCenterLocation());
        return false;
    }
}

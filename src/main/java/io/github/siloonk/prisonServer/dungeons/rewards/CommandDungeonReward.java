package io.github.siloonk.prisonServer.dungeons.rewards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandDungeonReward implements DungeonReward{

    private String command;

    public CommandDungeonReward(String command) {
        this.command = command;
    }

    @Override
    public void give(Player player) {
        Bukkit.getServer().dispatchCommand(
            Bukkit.getServer().getConsoleSender(),
            command.replaceAll("%player%", player.getName())
        );
    }
}

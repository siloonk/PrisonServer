package io.github.siloonk.prisonServer.dungeons.rewards;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import org.bukkit.entity.Player;

public class CurrencyDungeonReward implements DungeonReward{

    private Currency currency;
    private int amount;

    public CurrencyDungeonReward(Currency currency, int amount) {
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public void give(Player player) {
        PrisonPlayer prisonPlayer = PrisonServer.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        prisonPlayer.setCurrency(currency, prisonPlayer.getCurrency(currency)+amount);
    }
}

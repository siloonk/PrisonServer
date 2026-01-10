package io.github.siloonk.prisonServer.enchantments.effects;

import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.BoosterType;
import io.github.siloonk.prisonServer.data.Currency;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import io.github.siloonk.prisonServer.data.relics.RelicType;
import io.github.siloonk.prisonServer.data.relics.SelectedRelic;
import io.github.siloonk.prisonServer.enchantments.Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;

public class QuantumMinerEnchantment extends Enchantment {

    private Random random = new Random();

    public QuantumMinerEnchantment(Component name, Component description, int maxLevel, int baseCost, double costIncrease, double chanceAtMaxLevel, double baseChance, Currency currency) {
        super(name, description, maxLevel, baseCost, costIncrease, chanceAtMaxLevel, baseChance, currency);
    }

    @Override
    public void execute(Location blockLocation, PrisonPlayer player, int level) {
        int blocks = random.nextInt(2, 6);
        player.addBlocks(blocks);

        // Token Relic
        List<SelectedRelic> selectedRelicList = PrisonServer.getInstance().getDatabase().getRelicDAO().getRelicByType(player.getUuid(), RelicType.TOKENS);
        double boost = 1;
        if (!selectedRelicList.isEmpty()) {
            boost += selectedRelicList.stream().mapToDouble(SelectedRelic::getBoost).sum();
        }
        player.setTokens(player.getTokens() + Math.round(blocks * player.getMultiplier(BoosterType.TOKENS)*boost));
        Bukkit.getPlayer(player.getUuid()).sendActionBar(MiniMessage.miniMessage().deserialize(String.format("<dark_purple><bold>Quantum Miner<reset> <gray>»This block has been multiplied into <light_purple>%d blocks<gray>!", blocks)));
    }
}

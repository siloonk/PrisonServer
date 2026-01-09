package io.github.siloonk.prisonServer.data.relics;

import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.data.Rarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Relic {

    private static final MiniMessage mm = MiniMessage.miniMessage();


    private final List<RelicType> types;
    private final double minBoost;
    private final double maxBoost;
    private final Rarity rarity;



    public Relic(List<RelicType> types, double minBoost, double maxBoost, Rarity rarity) {
        this.types = types;
        this.minBoost = minBoost;
        this.maxBoost = maxBoost;
        this.rarity = rarity;
    }


    public List<RelicType> getType() {
        return types;
    }

    public double getMinBoost() {
        return minBoost;
    }

    public double getMaxBoost() {
        return maxBoost;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public ItemStack generateItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        Random random = new Random();

        int index = random.nextInt(types.size());
        RelicType type = types.get(index);


        String name = type.name().charAt(0) + type.name().substring(1);
        meta.displayName(mm.deserialize("<light_purple>%s Relic".formatted(name)));


        double boost = random.nextDouble(minBoost, maxBoost);

        List<Component> lore = new ArrayList<>();
        lore.add(mm.deserialize("<gray>This relic gives a <light_purple>%d%% %s<gray> boost".formatted(Math.round(boost * 100), name)));
        meta.lore(lore);


        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(PDCKeys.RELIC_BOOST, PersistentDataType.DOUBLE, boost);
        container.set(PDCKeys.RELIC_TYPE, PersistentDataType.STRING, type.toString());
        container.set(PDCKeys.RARITY, PersistentDataType.STRING, rarity.toString());

        item.setItemMeta(meta);
        return item;

    }
}

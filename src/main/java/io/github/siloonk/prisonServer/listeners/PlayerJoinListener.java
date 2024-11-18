package io.github.siloonk.prisonServer.listeners;

import io.github.siloonk.prisonServer.Database;
import io.github.siloonk.prisonServer.PDCKeys;
import io.github.siloonk.prisonServer.PrisonServer;
import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class PlayerJoinListener implements Listener {

    private static final String PICKAXE_NAME = "<aqua><bold>Prisoners Pickaxe<reset><gray> [0]";


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PrisonServer plugin = PrisonServer.getInstance();
        Database db = plugin.getDatabase();

        System.out.println(db.getPlayerDAO().getPlayerById(e.getPlayer().getUniqueId().toString()));

        if (db.getPlayerDAO().getPlayerById(e.getPlayer().getUniqueId().toString()) == null) {
            PrisonPlayer prisonPlayer = new PrisonPlayer(e.getPlayer().getUniqueId(), 0, 0, 0, new Timestamp(new Date().getTime()), 1, 0);
            db.getPlayerDAO().insertPlayer(prisonPlayer);
            e.getPlayer().sendMessage("Welcome to the server!");
            givePickaxe(e.getPlayer());
            return;
        }
        e.getPlayer().sendMessage("Welcome back!");
        PrisonPlayer player = PrisonServer.getInstance().getPlayerManager().getPlayer(e.getPlayer().getUniqueId());
        player.setMoney(100 + player.getMoney());
        e.getPlayer().sendMessage("your money is now " + player.getMoney());
    }

    private void givePickaxe(Player player) {
        MiniMessage miniMessage = MiniMessage.miniMessage();

        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.EFFICIENCY, 100, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);


        meta.displayName(miniMessage.deserialize(PICKAXE_NAME).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        List<Component> lore = Arrays.asList(
                miniMessage.deserialize(String.format("<gray>Owned by <white>%s", player.getName())).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("<aqua><bold>Leveling").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("<aqua>|<reset><white> Level: <gray>1").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("<aqua>|<reset><white> EXP: <gray>0 / 350").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("<aqua>| " + getProgressBar(0, 350)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                miniMessage.deserialize("<yellow><bold>Enchants <reset><gold>(0)").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        );

        meta.lore(lore);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(PDCKeys.PICKAXE_EXP_KEY, PersistentDataType.INTEGER, 0);
        container.set(PDCKeys.PICKAXE_EXP_REQUIRED_KEY, PersistentDataType.INTEGER, 350);
        container.set(PDCKeys.PICKAXE_BLOCKS_MINED_KEY, PersistentDataType.INTEGER, 0);
        container.set(PDCKeys.ENCHANTMENTS_KEY, PersistentDataType.TAG_CONTAINER, container.getAdapterContext().newPersistentDataContainer());
        item.setItemMeta(meta);
        player.getInventory().addItem(item);
    }

    private String getProgressBar(int current, int target) {
        StringBuilder builder = new StringBuilder("<green>");
        int percentage = Math.toIntExact(Math.round((double) current / target * 30));
        for (int i =0; i < percentage; i++) {
            builder.append(":");
        }

        builder.append("<red>");
        for (int i =0; i < 30 - percentage; i++) {
            builder.append(":");
        }

        builder.append(String.format(" <gray>%.0f%%", (double)current / target * 100));
        return builder.toString();
    }
}

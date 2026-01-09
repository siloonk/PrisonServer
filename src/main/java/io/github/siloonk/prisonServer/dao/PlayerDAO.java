package io.github.siloonk.prisonServer.dao;

import io.github.siloonk.prisonServer.data.players.PrisonPlayer;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface PlayerDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS players (uuid TEXT PRIMARY KEY, money INTEGER, bubbles INTEGER, tokens INTEGER, relic_dust INTEGER, blocks_mined INTEGER, time_joined TIMESTAMP, level INTEGER, prestige INTEGER, personal_multiplier NUMBER, free_bp_slots INTEGER, total_bp_slots INTEGER)")
    void createTable();


    @SqlUpdate("INSERT INTO players (uuid, money, bubbles, tokens, relic_dust, blocks_mined, time_joined, level, prestige, personal_multiplier, free_bp_slots, total_bp_slots) " +
            "VALUES(:uuid, :money, :bubbles, :tokens, :relicDust, :blocksMined, :timeJoined, :level, :prestige, :personalMultiplier, :freeBackpackSlots, :totalBackpackSlots)")
    void insertPlayer(@BindBean PrisonPlayer player);


    @SqlUpdate("UPDATE players SET uuid=:uuid, money=:money, bubbles=:bubbles, relic_dust=:relicDust, tokens=:tokens, blocks_mined=:blocksMined, " +
            "time_joined=:timeJoined, level=:level, prestige=:prestige, personal_multiplier=:personalMultiplier, free_bp_slots=:freeBackpackSlots, total_bp_slots=:totalBackpackSlots WHERE uuid=:uuid")
    void update(@BindBean PrisonPlayer player);


    @SqlQuery("SELECT * FROM players WHERE uuid = :uuid")
    @RegisterBeanMapper(PrisonPlayer.class)
    PrisonPlayer getPlayerById(@Bind("uuid") String uuid);

    @SqlQuery("SELECT * FROM players")
    @RegisterBeanMapper(PrisonPlayer.class)
    List<PrisonPlayer> listPlayers();

    default void close() {

    }
}

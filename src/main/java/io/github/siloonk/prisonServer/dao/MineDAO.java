package io.github.siloonk.prisonServer.dao;

import io.github.siloonk.prisonServer.data.mines.Mine;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface MineDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS mines (id INTEGER PRIMARY KEY AUTOINCREMENT, owner TEXT, width INTEGER, height INTEGER, world_name TEXT, x INTEGER, y INTEGER, z INTEGER, block_type TEXT, luckyblock_chance INTEGER)")
    void createTable();


    @SqlUpdate("INSERT INTO mines (owner, width, height, world_name, x, y, z, block_type, luckyblock_chance) VALUES(:owner, :width, :height, :worldName, :centerLocation.x, :centerLocation.y, :centerLocation.z, :blockType, :luckyBlockChance)")
    void insertMine(@BindBean Mine mine);

    @SqlUpdate("UPDATE mines set owner=:owner, width=:width, height=:height, world_name=:worldName, x=:centerLocation.x, y=:centerLocation.y, z=:centerLocation.z, block_type=:blockType, luckyblock_chance=:luckyBlockChance WHERE owner=:owner")
    void updateMine(@BindBean Mine mine);

    @SqlQuery("SELECT * FROM mines WHERE owner = :uuid")
    @RegisterBeanMapper(Mine.class)
    Mine getMineById(@Bind("uuid") String uuid);

    @SqlQuery("SELECT * FROM mines")
    @RegisterBeanMapper(Mine.class)
    List<Mine> listMines();

    default void close() {

    }
}

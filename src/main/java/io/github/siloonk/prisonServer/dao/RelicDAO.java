package io.github.siloonk.prisonServer.dao;

import io.github.siloonk.prisonServer.crates.CrateBlock;
import io.github.siloonk.prisonServer.data.Rarity;
import io.github.siloonk.prisonServer.data.relics.SelectedRelic;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.UUID;

public interface RelicDAO {
    @SqlUpdate("CREATE TABLE IF NOT EXISTS relics (id TEXT PRIMARY KEY, owner TEXT, type TEXT, rarity TEXT, boost DOUBLE)")
    void createTable();


    @SqlUpdate("INSERT INTO relics (id, owner, type, rarity, boost) VALUES(:id, :owner, :type, :rarity, :boost)")
    void insertRelic(@BindBean SelectedRelic relic);

    @SqlUpdate("UPDATE crates set id=:id, world_name=:worldName, x=:location.x, y=:location.y, z=:location.z WHERE id=:id")
    void updateRelic(@BindBean SelectedRelic relic);

    @SqlUpdate("DELETE FROM relics WHERE id=:id")
    void deleteRelic(@Bind("id") String id);

    @SqlQuery("SELECT * FROM relics WHERE id = :id")
    SelectedRelic getRelic(@Bind("id") String id);

    @SqlQuery("SELECT * FROM relics")
    @RegisterBeanMapper(SelectedRelic.class)
    List<SelectedRelic> listRelics();

    @SqlQuery("SELECT * FROM relics WHERE owner = :owner AND rarity = :rarity")
    @RegisterBeanMapper(SelectedRelic.class)
    List<SelectedRelic> getRelicts(UUID owner, Rarity rarity);

    default void close() {

    }
}

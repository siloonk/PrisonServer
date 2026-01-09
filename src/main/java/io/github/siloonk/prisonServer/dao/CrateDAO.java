package io.github.siloonk.prisonServer.dao;

import io.github.siloonk.prisonServer.crates.CrateBlock;
import io.github.siloonk.prisonServer.mappers.CrateMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(CrateMapper.class)
public interface CrateDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS crates (id TEXT PRIMARY KEY, world_name TEXT, x INTEGER, y INTEGER, Z INTEGER)")
    void createTable();


    @SqlUpdate("INSERT INTO crates (id, world_name, x, y, z) VALUES(:id, :worldName, :location.x, :location.y, :location.z)")
    void insertCrate(@BindBean CrateBlock crate);

    @SqlUpdate("UPDATE crates set id=:id, world_name=:worldName, x=:location.x, y=:location.y, z=:location.z WHERE id=:id")
    void updateCrate(@BindBean CrateBlock crate);

    @SqlUpdate("DELETE FROM crates WHERE id=:id")
    void deleteCrate(@Bind("id") String id);

    @SqlQuery("SELECT * FROM crates WHERE id = :id")
    CrateBlock getCrate(@Bind("id") String id);

    @SqlQuery("SELECT * FROM crates")
    @RegisterBeanMapper(CrateBlock.class)
    List<CrateBlock> listCrateBlocks();

    default void close() {

    }
}
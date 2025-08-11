package io.github.siloonk.prisonServer.dao;

import io.github.siloonk.prisonServer.data.Warp;
import io.github.siloonk.prisonServer.mappers.WarpMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterRowMapper(WarpMapper.class)
public interface WarpDAO {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, world_name TEXT, x INTEGER, y INTEGER, z INTEGER, yaw INTEGER, pitch INTEGER)")
    void createTable();


    @SqlUpdate("INSERT INTO warps (name, world_name, x, y, z, yaw, pitch) VALUES(:name, :worldName, :location.x, :location.y, :location.z, :location.yaw, :location.pitch)")
    void insertWarp(@BindBean Warp warp);

    @SqlUpdate("UPDATE warps set name=:name, world_name=:worldName, x=:location.x, y=:location.y, z=:location.z, yaw=:location.yaw, pitch=:location.pitch WHERE name=:name")
    void updateWarp(@BindBean Warp warp);

    @SqlQuery("SELECT * FROM warps WHERE name = :name")
    Warp getWarp(@Bind("name") String name);

    @SqlQuery("SELECT * FROM warps")
    @RegisterBeanMapper(Warp.class)
    List<Warp> listWarps();

    default void close() {

    }
}

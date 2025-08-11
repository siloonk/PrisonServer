package io.github.siloonk.prisonServer.mappers;
import io.github.siloonk.prisonServer.data.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WarpMapper implements RowMapper<Warp> {
    @Override
    public Warp map(ResultSet rs, StatementContext ctx) throws SQLException {
        String name = rs.getString("name");
        String worldName = rs.getString("world_name");
        int x = rs.getInt("x");
        int y = rs.getInt("y");
        int z = rs.getInt("z");
        int yaw = rs.getInt("yaw");
        int pitch = rs.getInt("pitch");

        Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

        return new Warp(name, worldName, location);
    }
}


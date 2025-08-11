package io.github.siloonk.prisonServer.mappers;

import io.github.siloonk.prisonServer.crates.CrateBlock;
import io.github.siloonk.prisonServer.data.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CrateMapper implements RowMapper<CrateBlock> {


    @Override
    public CrateBlock map(ResultSet rs, StatementContext ctx) throws SQLException {
        String name = rs.getString("name");
        String worldName = rs.getString("world_name");
        int x = rs.getInt("x");
        int y = rs.getInt("y");
        int z = rs.getInt("z");

        Location location = new Location(Bukkit.getWorld(worldName), x, y, z);

        return new CrateBlock(name, location);
    }
}

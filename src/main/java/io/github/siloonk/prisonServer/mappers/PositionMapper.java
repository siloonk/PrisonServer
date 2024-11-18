package io.github.siloonk.prisonServer.mappers;

import io.papermc.paper.math.Position;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PositionMapper implements ColumnMapper<Position> {
    @Override
    public Position map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        double x = rs.getDouble("x");
        double y = rs.getDouble("y");
        double z = rs.getDouble("z");
        return Position.fine(x, y, z);
    }

}

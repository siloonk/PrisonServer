package io.github.siloonk.prisonServer.mappers;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UUIDColumnMapper implements ColumnMapper<UUID> {

    @Override
    public UUID map(ResultSet rs, int columnNumber, StatementContext ctx) throws SQLException {
        String uuidString = rs.getString(columnNumber);
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }
}

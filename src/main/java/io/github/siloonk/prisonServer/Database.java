package io.github.siloonk.prisonServer;

import io.github.siloonk.prisonServer.dao.MineDAO;
import io.github.siloonk.prisonServer.dao.PlayerDAO;
import io.github.siloonk.prisonServer.mappers.PositionMapper;
import io.github.siloonk.prisonServer.mappers.UUIDColumnMapper;
import io.papermc.paper.math.Position;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.UUID;

public class Database {

    private final Jdbi jdbi;

    private final PlayerDAO playerDAO;
    private final MineDAO mineDAO;

    public Database() {
        jdbi = Jdbi.create("jdbc:sqlite:" + PrisonServer.getInstance().getDataFolder() + "/database.db");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.registerColumnMapper(UUID.class, new UUIDColumnMapper());
        playerDAO = jdbi.onDemand(PlayerDAO.class);
        mineDAO = jdbi.onDemand(MineDAO.class);
        playerDAO.createTable();
        mineDAO.createTable();
    }

    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public MineDAO getMineDAO() {
        return mineDAO;
    }
}

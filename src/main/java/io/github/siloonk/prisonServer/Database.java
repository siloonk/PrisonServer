package io.github.siloonk.prisonServer;

import io.github.siloonk.prisonServer.dao.CrateDAO;
import io.github.siloonk.prisonServer.dao.MineDAO;
import io.github.siloonk.prisonServer.dao.PlayerDAO;
import io.github.siloonk.prisonServer.dao.WarpDAO;
import io.github.siloonk.prisonServer.mappers.UUIDColumnMapper;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.UUID;

public class Database {

    private final Jdbi jdbi;

    private final PlayerDAO playerDAO;
    private final MineDAO mineDAO;
    private final WarpDAO warpDAO;
    private final CrateDAO crateDAO;

    public Database() {
        jdbi = Jdbi.create("jdbc:sqlite:" + PrisonServer.getInstance().getDataFolder() + "/database.db");
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.registerColumnMapper(UUID.class, new UUIDColumnMapper());
        playerDAO = jdbi.onDemand(PlayerDAO.class);
        mineDAO = jdbi.onDemand(MineDAO.class);
        warpDAO = jdbi.onDemand(WarpDAO.class);
        crateDAO = jdbi.onDemand(CrateDAO.class);
        playerDAO.createTable();
        mineDAO.createTable();
        warpDAO.createTable();
        crateDAO.createTable();
    }


    public WarpDAO getWarpDAO() {return warpDAO;}
    public PlayerDAO getPlayerDAO() {
        return playerDAO;
    }

    public MineDAO getMineDAO() {
        return mineDAO;
    }
    public CrateDAO getCrateDAO() {return crateDAO;}
}

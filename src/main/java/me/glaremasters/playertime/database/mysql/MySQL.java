package me.glaremasters.playertime.database.mysql;

import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariDataSource;
import me.glaremasters.playertime.PlayerTime;
import me.glaremasters.playertime.database.DatabaseProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by GlareMasters
 * Date: 7/19/2018
 * Time: 10:19 PM
 */
public class MySQL implements DatabaseProvider {

    private HikariDataSource hikari;

    @Override
    public void initialize() {

        ConfigurationSection databaseSection = PlayerTime.getI().getConfig().getConfigurationSection("database");
        if (databaseSection == null) {
            throw new IllegalStateException("MySQL not configured correctly. Cannot continue.");
        }

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(databaseSection.getInt("pool-size"));

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");

        hikari.addDataSourceProperty("serverName", databaseSection.getString("host"));
        hikari.addDataSourceProperty("port", databaseSection.getInt("port"));
        hikari.addDataSourceProperty("databaseName", databaseSection.getString("database"));

        hikari.addDataSourceProperty("user", databaseSection.getString("username"));
        hikari.addDataSourceProperty("password", databaseSection.getString("password"));

        hikari.addDataSourceProperty("characterEncoding", "utf8");
        hikari.addDataSourceProperty("useUnicode", "true");

        hikari.validate();

        PlayerTime.newChain().async(() -> execute(Query.CREATE_TABLE)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });


    }

    @Override
    public void insertUser(String uuid, String time) {
        PlayerTime.newChain().async(() -> execute(Query.INSERT_USER, uuid, time)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public boolean hasTime(String uuid) {
        try {
            ResultSet rs = executeQuery(Query.EXIST_CHECK, uuid);
            while (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Integer> getTopTen() {
        Map<String, Integer> topTen = new LinkedHashMap<>();
        try {
            ResultSet rs = executeQuery(Query.GET_TOP_TEN);
            while (rs.next()) {
                topTen.put(rs.getString("uuid"), Integer.valueOf(rs.getString("time")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return topTen;
    }

    @Override
    public void setTime(String time, String uuid) {
        PlayerTime.newChain().async(() -> execute(Query.UPDATE_USER, uuid, time)).execute((exception, task) -> {
            if (exception != null) exception.printStackTrace();
        });
    }

    @Override
    public String getTime(String uuid) {
        try {
            ResultSet rs = executeQuery(Query.GET_TIME, uuid);
            while (rs.next()) {
                return rs.getString("time");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    private void execute(String query, Object... parameters) {

        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
                .prepareStatement(query)) {

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private ResultSet executeQuery(String query, Object... parameters) {
        try (Connection connection = hikari
                .getConnection(); PreparedStatement statement = connection
                .prepareStatement(query)) {
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }

            CachedRowSet resultCached = new CachedRowSetImpl();
            ResultSet resultSet = statement.executeQuery();

            resultCached.populate(resultSet);
            resultSet.close();

            return resultCached;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}

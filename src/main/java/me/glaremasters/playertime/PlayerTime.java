package me.glaremasters.playertime;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.glaremasters.playertime.commands.CMDCheck;
import me.glaremasters.playertime.database.DatabaseProvider;
import me.glaremasters.playertime.database.databases.yml.YML;
import me.glaremasters.playertime.database.mysql.MySQL;
import me.glaremasters.playertime.events.Leave;
import me.glaremasters.playertime.updater.SpigotUpdater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static me.glaremasters.playertime.commands.CMDCheck.ticksToMillis;

public final class PlayerTime extends JavaPlugin {

    private static PlayerTime playerTime;

    private static TaskChainFactory taskChainFactory;

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public DatabaseProvider getDatabase() {
        return database;
    }

    private DatabaseProvider database;

    public static PlayerTime getI() {
        return playerTime;
    }

    public File playTime = new File(this.getDataFolder(), "playtime.yml");
    public YamlConfiguration playTimeConfig = YamlConfiguration.loadConfiguration(this.playTime);

    @Override
    public void onEnable() {

        playerTime = this;
        saveDefaultConfig();
        saveTime();

        taskChainFactory = BukkitTaskChainFactory.create(this);

        SpigotUpdater updater = new SpigotUpdater(this, 58915);
        updateCheck(updater);

        setDatabaseType();

        getServer().getPluginManager().registerEvents(new Leave(), this);

        getCommand("ptcheck").setExecutor(new CMDCheck());

    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerTime.getDatabase().hasTime(player.getUniqueId().toString())) {
                playerTime.getDatabase().setTime(player.getUniqueId().toString(), String.valueOf(ticksToMillis(player)));
            } else {
                playerTime.getDatabase().insertUser(player.getUniqueId().toString(),String.valueOf(ticksToMillis(player)));
            }
        }
    }

    public void setDatabaseType() {
        switch (getConfig().getString("database.type").toLowerCase()) {
            case "mysql":
                database = new MySQL();
                break;
            case "yml":
                database = new YML();
                break;
            default:
                database = new YML();
                break;
        }
        database.initialize();
    }

    public void saveTime() {
        try {
            playTimeConfig.save(playTime);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save PlayTime Data!");
            e.printStackTrace();
        }
    }

    private void updateCheck(SpigotUpdater updater) {
        try {
            if (updater.checkForUpdates()) {
                getLogger().info("You appear to be running a version other than our latest stable release." + " You can download our newest version at: " + updater.getResourceURL());
            }
        } catch (Exception ex) {
            getLogger().info("Could not check for updates! Stacktrace:");
            ex.printStackTrace();
        }
    }
}

package me.glaremasters.playertime;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import me.glaremasters.playertime.commands.CMDCheck;
import me.glaremasters.playertime.commands.CMDReload;
import me.glaremasters.playertime.commands.CMDTop;
import me.glaremasters.playertime.database.DatabaseProvider;
import me.glaremasters.playertime.database.mysql.MySQL;
import me.glaremasters.playertime.database.yml.YML;
import me.glaremasters.playertime.events.Announcement;
import me.glaremasters.playertime.events.GUI;
import me.glaremasters.playertime.events.Leave;
import me.glaremasters.playertime.updater.SpigotUpdater;
import me.glaremasters.playertime.utils.SaveTask;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import static me.glaremasters.playertime.commands.CMDCheck.ticksToMillis;
import static me.glaremasters.playertime.utils.AnnouncementUtil.unescape_perl_string;

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

        checkConfig();
        saveDefaultConfig();
        saveTime();

        taskChainFactory = BukkitTaskChainFactory.create(this);

        SpigotUpdater updater = new SpigotUpdater(this, 58915);
        updateCheck(updater);

        setDatabaseType();

        getServer().getPluginManager().registerEvents(new Leave(), this);
        getServer().getPluginManager().registerEvents(new Announcement(this), this);
        getServer().getPluginManager().registerEvents(new GUI(), this);

        getCommand("ptcheck").setExecutor(new CMDCheck());
        getCommand("pttop").setExecutor(new CMDTop(this));
        getCommand("ptreload").setExecutor(new CMDReload(this));

        SaveTask.startTask();

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

    /**
     * Grab the announcement from the API
     *
     * @return announcement in string text form
     */
    public String getAnnouncements() {
        String announcement = "";
        try {
            URL url = new URL("https://glaremasters.me/api/announcements/playertime/?id=" + getDescription()
                    .getVersion());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            try (InputStream in = con.getInputStream()) {
                String encoding = con.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                announcement = unescape_perl_string(IOUtils.toString(in, encoding));
                con.disconnect();
            }
        } catch (Exception exception) {
            announcement = "Could not fetch announcements!";
        }
        return announcement;
    }

    private void checkConfig() {
        if (!getConfig().isSet("config-version") || getConfig().getInt("config-version") != 2) {
            File oldConfig = new File(getDataFolder(), "config.yml");
            File newConfig = new File(getDataFolder(), "config-old.yml");
            oldConfig.renameTo(newConfig);
        }
    }
}

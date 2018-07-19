package me.glaremasters.playertime;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import me.glaremasters.playertime.commands.CMDCheck;
import me.glaremasters.playertime.events.Leave;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTime extends JavaPlugin {

    private static PlayerTime playerTime;

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

        getServer().getPluginManager().registerEvents(new Leave(), this);

        getCommand("ptcheck").setExecutor(new CMDCheck());

    }

    @Override
    public void onDisable() {

    }

    public void saveTime() {
        try {
            playTimeConfig.save(playTime);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Could not save PlayTime Data!");
            e.printStackTrace();
        }
    }
}

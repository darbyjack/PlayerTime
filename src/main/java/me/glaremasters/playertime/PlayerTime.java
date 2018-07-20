package me.glaremasters.playertime;

import me.glaremasters.playertime.commands.CMDCheck;
import me.glaremasters.playertime.events.Leave;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerTime.playTimeConfig.set(player.getUniqueId().toString(), ticksToMillis(player));
            playerTime.saveTime();
        }
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

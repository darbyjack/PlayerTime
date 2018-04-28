package me.glaremasters.playertime;

import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerTime extends JavaPlugin {

    private static PlayerTime i;

    public static PlayerTime getI() {
        return i;
    }

    @Override
    public void onEnable() {

        i = this;

    }

    @Override
    public void onDisable() {

    }
}

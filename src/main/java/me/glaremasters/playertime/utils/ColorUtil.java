package me.glaremasters.playertime.utils;

import org.bukkit.ChatColor;

/**
 * Created by GlareMasters on 4/28/2018.
 */
public class ColorUtil {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}

package me.glaremasters.playertime.events;

import me.glaremasters.playertime.PlayerTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static me.glaremasters.playertime.commands.CMDCheck.ticksToMillis;

/**
 * Created by GlareMasters on 4/28/2018.
 */
public class Leave implements Listener {

    private PlayerTime playerTime = PlayerTime.getI();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (playerTime.getDatabase().hasTime(player.getUniqueId().toString())) {
            playerTime.getDatabase().setTime(player.getUniqueId().toString(), String.valueOf(ticksToMillis(player)));
        } else {
            playerTime.getDatabase().insertUser(player.getUniqueId().toString(),String.valueOf(ticksToMillis(player)));
        }


    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        if (playerTime.getDatabase().hasTime(player.getUniqueId().toString())) {
            playerTime.getDatabase().setTime(player.getUniqueId().toString(), String.valueOf(ticksToMillis(player)));
        } else {
            playerTime.getDatabase().insertUser(player.getUniqueId().toString(),String.valueOf(ticksToMillis(player)));
        }
    }

}

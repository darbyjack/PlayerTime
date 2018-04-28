package me.glaremasters.playertime.events;

import java.util.concurrent.TimeUnit;
import me.glaremasters.playertime.PlayerTime;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by GlareMasters on 4/28/2018.
 */
public class Leave implements Listener {

    PlayerTime i = PlayerTime.getI();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        int time = player.getStatistic(Statistic.PLAY_ONE_TICK);
        i.playTimeConfig.set(player.getUniqueId().toString(), time);
        i.saveTime();
    }

}

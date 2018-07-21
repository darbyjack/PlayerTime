package me.glaremasters.playertime.events;

import me.glaremasters.playertime.PlayerTime;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static me.glaremasters.playertime.utils.ColorUtil.color;

/**
 * Created by GlareMasters
 * Date: 7/20/2018
 * Time: 11:24 PM
 */
public class Announcement implements Listener {

    private static Set<UUID> ALREADY_INFORMED = new HashSet<>();

    private PlayerTime playerTime;

    public Announcement(PlayerTime playerTime) {
        this.playerTime = playerTime;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerTime.getServer().getScheduler().scheduleAsyncDelayedTask(playerTime, () -> {
            if (player.isOp() && !ALREADY_INFORMED.contains(player.getUniqueId())) {
                JSONMessage.create(color(playerTime.getConfig().getString("plugin-prefix") + " &fAnnouncements")).tooltip(playerTime.getAnnouncements()).openURL(playerTime.getDescription().getWebsite()).send(player);
                ALREADY_INFORMED.add(player.getUniqueId());
            }
        }, 120L);
    }
}

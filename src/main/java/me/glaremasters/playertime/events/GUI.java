package me.glaremasters.playertime.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by GlareMasters
 * Date: 7/21/2018
 * Time: 2:05 PM
 */
public class GUI implements Listener {

    public static List<UUID> uuids = new ArrayList<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (uuids.contains(player.getUniqueId())) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        uuids.remove(player.getUniqueId());
    }
}

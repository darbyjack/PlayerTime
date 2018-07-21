package me.glaremasters.playertime.commands;

import me.glaremasters.playertime.PlayerTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static me.glaremasters.playertime.commands.CMDCheck.timeFormat;
import static me.glaremasters.playertime.events.GUI.uuids;
import static me.glaremasters.playertime.utils.ColorUtil.color;

/**
 * Created by GlareMasters
 * Date: 7/20/2018
 * Time: 10:54 PM
 */
public class CMDTop implements CommandExecutor {

    private PlayerTime playerTime;

    public CMDTop(PlayerTime playerTime) {
        this.playerTime = playerTime;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("playertime.top")) return true;
            if (args.length != 0) return true;
            Inventory top = Bukkit.createInventory(null, 18, color(playerTime.getConfig().getString("gui.title")));
            Map<String, Integer> map = playerTime.getDatabase().getTopTen();
            ItemStack material = new ItemStack(Material.getMaterial(playerTime.getConfig().getString("gui.item.material")));
            ItemMeta meta = material.getItemMeta();
            for (int i = 0; i < map.size(); i++) {
                List<String> lore = new ArrayList<>();
                UUID uuid = UUID.fromString(map.keySet().toArray()[i].toString());
                String name = Bukkit.getServer().getOfflinePlayer(uuid).getName();
                String time = map.values().toArray()[i].toString();
                meta.setDisplayName(color(playerTime.getConfig().getString("gui.item.name").replace("{player}", name)));
                for (String text : playerTime.getConfig().getStringList("gui.item.lore")) {
                    lore.add(color(text).replace("{slot}", String.valueOf(i + 1)).replace("{format}", timeFormat(Integer.valueOf(time))));
                }
                meta.setLore(lore);
                material.setItemMeta(meta);
                top.setItem(i, material);
            }
            player.openInventory(top);
            uuids.add(player.getUniqueId());
        }
        return true;
    }

}

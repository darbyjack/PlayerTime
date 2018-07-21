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

import java.util.Map;

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
            if (!player.hasPermission("playertime.top")) {
                return true;
            }
            if (args.length != 0) return true;
            Inventory top = Bukkit.createInventory(null, 18, "Top 10 GUI");
            Map<String, Integer> map = playerTime.getDatabase().getTopTen();
            for (int i = 0; i < playerTime.getDatabase().getTopTen().size(); i++) {
                ItemStack paper = new ItemStack(Material.PAPER);
                ItemMeta meta = paper.getItemMeta();
                String ID = map.keySet().toArray()[i].toString();
                String name = Bukkit.getServer().getOfflinePlayer(ID).getName();
                String time = map.values().toArray()[i].toString();
                meta.setDisplayName(name + " - " + time);
                paper.setItemMeta(meta);
                top.setItem(i, paper);
            }
            player.openInventory(top);
        }
        return true;
    }

}

package me.glaremasters.playertime.commands;

import me.glaremasters.playertime.PlayerTime;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static me.glaremasters.playertime.utils.ColorUtil.color;

/**
 * Created by GlareMasters on 4/28/2018.
 */
public class CMDCheck implements CommandExecutor {

    private FileConfiguration config = PlayerTime.getI().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("playertime.check")) {
                return true;
            }
            if (args.length < 1) {
                messageConvert(sender, player);
                return true;
            }
            if (args.length == 1) {
                if (!player.hasPermission("playertime.others")) {
                    return true;
                }
                if (Bukkit.getServer().getPlayer(args[0]) != null) {
                    Player target = Bukkit.getServer().getPlayer(args[0]);
                    messageConvert(sender, target);
                    return true;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (!offlinePlayer.hasPlayedBefore()) {
                    player.sendMessage(color(config.getString("messages.never-played-before")));
                    return true;
                }
                if (!PlayerTime.getI().getDatabase().hasTime(player.getUniqueId().toString())) {
                    player.sendMessage(color(config.getString("messages.no-playtime-data")));
                    return true;
                }
                messageConvertOffline(sender, offlinePlayer);
                return true;
            }
            player.sendMessage(color(config.getString("messages.incorrect-usage")));
            return true;
        }
        return true;
    }


    public static int ticksToMillis(Player player) {
        int time = player.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        int millis = seconds * 1000;
        return millis;
    }

    public static void messageConvert(CommandSender sender, Player player) {
        String endTime = DurationFormatUtils.formatDuration(ticksToMillis(player), "dd:HH:mm:ss");
        String[] parts = endTime.split(":");
        String days = parts[0];
        String hours = parts[1];
        String minutes = parts[2];
        String sec = parts[3];
        sender.sendMessage(color(PlayerTime.getI().getConfig().getString("format").replace("{days}", days).replace("{hours}", hours).replace("{minutes}", minutes).replace("{seconds}", sec).replace("{name}", player.getName())));
    }

    public static void messageConvertOffline(CommandSender sender, OfflinePlayer player) {
        String endTime = DurationFormatUtils.formatDuration(Integer.valueOf(PlayerTime.getI().getDatabase().getTime(player.getUniqueId().toString())), "dd:HH:mm:ss");
        String[] parts = endTime.split(":");
        String days = parts[0];
        String hours = parts[1];
        String minutes = parts[2];
        String sec = parts[3];
        sender.sendMessage(color(PlayerTime.getI().getConfig().getString("format").replace("{days}", days).replace("{hours}", hours).replace("{minutes}", minutes).replace("{seconds}", sec).replace("{name}", player.getName())));
    }

}

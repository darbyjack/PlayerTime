package me.glaremasters.playertime.commands;

import static me.glaremasters.playertime.utils.ColorUtil.color;
import java.util.concurrent.TimeUnit;
import me.glaremasters.playertime.PlayerTime;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * Created by GlareMasters on 4/28/2018.
 */
public class CMDCheck implements CommandExecutor {

    private FileConfiguration config = PlayerTime.getI().getConfig();
    private FileConfiguration ptime = PlayerTime.getI().playTimeConfig;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("playertime.check")) {
                return false;
            }
            if (args.length < 1) {
                int time = player.getStatistic(Statistic.PLAY_ONE_TICK);
                int seconds = time / 20;
                int millis = seconds * 1000;

                String playTime = color(String.format(config.getString("messages.self-check"), TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                player.sendMessage(playTime);
                return true;
            }
            if (args.length == 1) {
                if (!player.hasPermission("playertime.others")) {
                    return false;
                }
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (!offlinePlayer.hasPlayedBefore()) {
                    player.sendMessage(color(config.getString("messages.never-played-before")));
                    return true;
                }
                if (!ptime.contains(offlinePlayer.getUniqueId().toString())) {
                    player.sendMessage(color(config.getString("messages.no-playtime-data")));
                    return true;
                }

                int time = ptime.getInt(offlinePlayer.getUniqueId().toString());
                int seconds = time / 20;
                int millis = seconds * 1000;

                String playTime = color(String.format(config.getString("messages.check-others").replace("{name}", offlinePlayer.getName()), TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
                player.sendMessage(playTime);

                return true;
            }
            player.sendMessage(color(config.getString("messages.incorrect-usage")));
            return true;
        }
        return true;
    }

}

package me.glaremasters.playertime.commands;

import static me.glaremasters.playertime.utils.ColorUtil.color;
import java.util.concurrent.TimeUnit;
import me.glaremasters.playertime.PlayerTime;
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

    FileConfiguration config = PlayerTime.getI().getConfig();

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
                int miliseconds = seconds * 1000;

                String playTime = color(String.format(config.getString("messages.self-check"), TimeUnit.MILLISECONDS.toMinutes(miliseconds),
                        TimeUnit.MILLISECONDS.toSeconds(miliseconds) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(miliseconds)))
                );
                player.sendMessage(playTime);
            }
        }
        return true;
    }

}

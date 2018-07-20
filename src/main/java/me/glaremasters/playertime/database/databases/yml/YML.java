package me.glaremasters.playertime.database.databases.yml;

import me.glaremasters.playertime.PlayerTime;
import me.glaremasters.playertime.database.DatabaseProvider;

/**
 * Created by GlareMasters
 * Date: 7/19/2018
 * Time: 10:18 PM
 */
public class YML implements DatabaseProvider {

    private PlayerTime playerTime = PlayerTime.getI();

    @Override
    public void initialize() {
        System.out.println("Initializing PlayerTime storage file...");
    }

    @Override
    public void insertUser(String uuid, String time) {
        playerTime.playTimeConfig.set(uuid, time);
        playerTime.saveTime();
    }

    @Override
    public boolean hasTime(String uuid) {
        return PlayerTime.getI().playTimeConfig.getString(uuid) != null;
    }

    @Override
    public void setTime(String time, String uuid) {
        playerTime.playTimeConfig.set(time, uuid);
        playerTime.saveTime();
    }

    @Override
    public String getTime(String uuid) {
        return PlayerTime.getI().playTimeConfig.getString(uuid);
    }


}

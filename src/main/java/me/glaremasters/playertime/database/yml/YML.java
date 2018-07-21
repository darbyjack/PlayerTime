package me.glaremasters.playertime.database.yml;

import me.glaremasters.playertime.PlayerTime;
import me.glaremasters.playertime.database.DatabaseProvider;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Map<String, Integer> getTopTen() {
        Map<String, Integer> topTen = new LinkedHashMap<>();

        for (String key : playerTime.playTimeConfig.getKeys(false)) {
            topTen.put(key, Integer.valueOf(playerTime.playTimeConfig.getString(key)));
        }

        List<Map.Entry<String, Integer>> list = new LinkedList<>(topTen.entrySet());
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        topTen.clear();

        for (Map.Entry<String, Integer> aList : list) {
            topTen.put(aList.getKey(), aList.getValue());
        }

        return topTen;
    }

}

package me.glaremasters.playertime.database;

import java.util.Map;

/**
 * Created by GlareMasters
 * Date: 7/19/2018
 * Time: 10:17 PM
 */
public interface DatabaseProvider {

    void initialize();

    void insertUser(String uuid, String time);

    boolean hasTime(String uuid);

    void setTime(String time, String uuid);

    String getTime(String uuid);

    Map<String, Integer> getTopTen();


}

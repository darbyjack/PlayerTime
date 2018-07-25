package me.glaremasters.playertime.utils;

import com.google.gson.annotations.Expose;

import java.util.UUID;

/**
 * Created by GlareMasters
 * Date: 7/22/2018
 * Time: 4:54 PM
 */
public class Time {

    @Expose
    private UUID uuid;

    public Time(UUID uuid) {
        this.uuid = uuid;
    }

}

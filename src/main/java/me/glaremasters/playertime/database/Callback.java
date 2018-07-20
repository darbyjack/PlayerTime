package me.glaremasters.playertime.database;

/**
 * Created by GlareMasters
 * Date: 7/19/2018
 * Time: 10:16 PM
 */
public interface Callback<T, E extends Exception> {

    void call(T result, E exception);
}
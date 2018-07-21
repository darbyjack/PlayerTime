package me.glaremasters.playertime.database.mysql;

/**
 * Created by GlareMasters
 * Date: 7/19/2018
 * Time: 10:19 PM
 */
class Query {

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `playertime` (\n"
            + "    `uuid` VARCHAR(40)  NOT NULL ,\n"
            + "    `time` varchar(32)  NOT NULL ,\n"
            + "    PRIMARY KEY (`uuid`), \n"
            + "    UNIQUE (`uuid`)\n"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

    static final String INSERT_USER = "INSERT IGNORE INTO `playertime` (uuid, time) VALUES(?, ?)";

    static final String EXIST_CHECK = "SELECT uuid from `playertime` WHERE uuid=?";

    static final String UPDATE_USER = "UPDATE playertime set time=? WHERE uuid=?";

    static final String GET_TIME = "SELECT `time` FROM `playertime` WHERE uuid=?";

    static final String GET_TOP_TEN = "SELECT * FROM `playertime` ORDER BY `time` DESC LIMIT 10";

}

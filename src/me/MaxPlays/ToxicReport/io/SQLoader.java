package me.MaxPlays.ToxicReport.io;

import me.MaxPlays.ToxicReport.ToxicReport;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class SQLoader {

    public void load(){
        String host = ToxicReport.getConfig().getString("MySQL.host");
        String username = ToxicReport.getConfig().getString("MySQL.username");
        String password = ToxicReport.getConfig().getString("MySQL.password");
        String database = ToxicReport.getConfig().getString("MySQL.database");
        String port = ToxicReport.getConfig().getString("MySQL.port");

        ToxicReport.sql = new SQL(host, database, username, password, port, ToxicReport.instance);
        ToxicReport.sql.connect();
        ToxicReport.sql.update("CREATE TABLE IF NOT EXISTS reports(id VARCHAR(64), time LONG, reported VARCHAR(64), reporter VARCHAR(64), reason INT, url TEXT);");
        ToxicReport.sql.update("CREATE TABLE IF NOT EXISTS chatlogs(id VARCHAR(64), name VARCHAR(64), content TEXT);");

        ToxicReport.sqlite = new SQL("chatlog", ToxicReport.instance);
        ToxicReport.sqlite.connect();
        ToxicReport.sqlite.update("CREATE TABLE IF NOT EXISTS chatlog(name VARCHAR(64), time LONG, message TEXT);");
    }

}

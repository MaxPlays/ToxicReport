package me.MaxPlays.ToxicReport.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.MaxPlays.ToxicReport.ToxicReport;
import me.MaxPlays.ToxicReport.util.IDGenerator;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class ChatlogManager {

    public static String getChatlog(ProxiedPlayer p) {

        String id = IDGenerator.getRandomID(15);

        BungeeCord.getInstance().getScheduler().runAsync(ToxicReport.instance, new Runnable() {
            @Override
            public void run() {

                ResultSet rs = ToxicReport.sql.query("SELECT * FROM chatlog WHERE name='" + p.getName() + "' ORDER BY time DESC;");

                JsonArray array = new JsonArray();

                try {
                    while(rs.next()){
                        JsonObject o = new JsonObject();
                        o.addProperty("time", rs.getLong("time"));
                        o.addProperty("message", rs.getString("message"));
                        array.add(o);
                    }

                    PreparedStatement ps = ToxicReport.sql.getCon().prepareStatement("INSERT INTO chatlogs VALUES(?, ?, ?)");
                    ps.setString(1, id);
                    ps.setString(2, p.getName());
                    ps.setString(3, array.toString());
                    ps.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        return ToxicReport.webdir + "?id=" + id;
    }
}

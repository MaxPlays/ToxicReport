package me.MaxPlays.ToxicReport.listeners;

import me.MaxPlays.ToxicReport.ToxicReport;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Max_Plays on 13.08.2017.
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(ServerConnectedEvent e){
        ProxiedPlayer p = e.getPlayer();
        if(p.hasPermission("ToxicReport.supporter")){
            BungeeCord.getInstance().getScheduler().runAsync(ToxicReport.instance, new Runnable() {
                @Override
                public void run() {
                    ResultSet rs = ToxicReport.sql.query("SELECT COUNT(*) AS total FROM reports;");
                    int count = 0;
                    try {
                        if(rs.next())
                            count = rs.getInt("total");
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    if(count > 0)
                        ToxicReport.sendMessage(p, "Es sind im Moment §4" + count + " §7Reports offen. Benutze §c/report list §7 um eine Liste aller offenen Reports zu sehen");
                }
            });
        }
    }
}

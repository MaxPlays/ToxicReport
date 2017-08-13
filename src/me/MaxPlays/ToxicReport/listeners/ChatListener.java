package me.MaxPlays.ToxicReport.listeners;

import me.MaxPlays.ToxicReport.ToxicReport;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent e){
        if(e.getSender() instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) e.getSender();
            Connection con = ToxicReport.sql.getCon();
            try {
                PreparedStatement ps = con.prepareStatement("INSERT INTO chatlog VALUES(?, ?, ?);");
                ps.setString(1, p.getName());
                ps.setLong(2, System.currentTimeMillis());
                ps.setString(3, e.getMessage());
                ps.executeUpdate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

}

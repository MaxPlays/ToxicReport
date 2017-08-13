package me.MaxPlays.ToxicReport;

import me.MaxPlays.ToxicReport.commands.CommandChatlog;
import me.MaxPlays.ToxicReport.commands.CommandReport;
import me.MaxPlays.ToxicReport.io.ConfigLoader;
import me.MaxPlays.ToxicReport.io.SQL;
import me.MaxPlays.ToxicReport.io.SQLoader;
import me.MaxPlays.ToxicReport.listeners.ChatListener;
import me.MaxPlays.ToxicReport.listeners.JoinListener;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class ToxicReport extends Plugin{

    private static Configuration config;
    public static SQL sql, sqlite;
    public static BaseComponent[] prefix = TextComponent.fromLegacyText("§2Report §8» §7");
    public static final long reportTimeout = 60*1000;
    public static ToxicReport instance;
    public static String webdir;

    public void onEnable(){
        instance = this;
        new ConfigLoader().load(getDataFolder());
        new SQLoader().load();

        webdir = getConfig().getString("webDir");

        runDeleteScheduler();

        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new CommandReport("report"));
        pm.registerCommand(this, new CommandChatlog("chatlog"));
        pm.registerListener(this, new ChatListener());
        pm.registerListener(this, new JoinListener());

    }

    public void onDisable(){
        sql.disconnect();
        sqlite.disconnect();
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void setConfig(Configuration config) {
        ToxicReport.config = config;
    }

    public static void sendMessage(ProxiedPlayer p, String msg){
        TextComponent tc = new TextComponent(prefix);
        tc.addExtra(new TextComponent(TextComponent.fromLegacyText(msg)));
        p.sendMessage(tc);
    }

    private void runDeleteScheduler() {
        BungeeCord.getInstance().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() { sqlite.update("DELETE FROM chatlog WHERE (time + " + (10*60*1000) + ") < " + System.currentTimeMillis() + ";"); }
        }, 0, 30, TimeUnit.SECONDS);
    }
}

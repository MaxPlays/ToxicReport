package me.MaxPlays.ToxicReport.commands;

import me.MaxPlays.ToxicReport.ToxicReport;
import me.MaxPlays.ToxicReport.managers.ReportManager;
import me.MaxPlays.ToxicReport.util.ReportType;
import me.MaxPlays.ToxicReport.util.UUIDFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class CommandReport extends Command {

    public CommandReport(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(args.length == 0){
                ReportType.sendMessages(p, null);
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("takeover")){
                    ToxicReport.sendMessage(p, "Verwendung: §c/report takeover <ID>");
                }else if(args[0].equalsIgnoreCase("list")){
                    if(p.hasPermission("ToxicReport.supporter")){
                        ToxicReport.sendMessage(p, "§8§oLade Reports...");
                        BungeeCord.getInstance().getScheduler().runAsync(ToxicReport.instance, new Runnable() {
                            @Override
                            public void run() {
                                ResultSet rs = ToxicReport.sql.query("SELECT * FROM reports WHERE reporter='" + p.getUniqueId().toString() + "' ORDER BY time ASC LIMIT 5;");
                                try {
                                    if(rs.next()){
                                        ToxicReport.sendMessage(p, "Offene Reports):");
                                        do{
                                            TextComponent tc = new TextComponent(TextComponent.fromLegacyText("§7Spieler: §a" + UUIDFetcher.getName(rs.getString("reported")) + "§8, §7Grund: §e"
                                                    + ReportType.getById(rs.getInt("reason")).getText() + " "));
                                            TextComponent click = new TextComponent("§c[Übernehmen]");
                                            click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "report takeover " + rs.getString("id")));
                                            tc.addExtra(click);
                                            p.sendMessage(tc);
                                        }while (rs.next());
                                    }else{
                                        ToxicReport.sendMessage(p, "§cEs wurden keine Reports gefunden");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        ToxicReport.sendMessage(p, "§8§oLade Reports...");
                        BungeeCord.getInstance().getScheduler().runAsync(ToxicReport.instance, new Runnable() {
                            @Override
                            public void run() {
                                ResultSet rs = ToxicReport.sql.query("SELECT * FROM reports WHERE reporter='" + p.getUniqueId().toString() + "' ORDER BY time DESC;");
                                try {
                                    if(rs.next()){
                                        ToxicReport.sendMessage(p, "Deine offenen Reports:");
                                        do{
                                            ToxicReport.sendMessage(p, "Spieler: §a" + UUIDFetcher.getName(rs.getString("reported")) + "§8, §7Grund: §e"
                                            + ReportType.getById(rs.getInt("reason")).getText() + "§8, §7ID: §c" + rs.getString("id") +
                                            "§8, §7Erstellt: §a" + (new SimpleDateFormat("dd.MM.yyyy").format(new Date(rs.getLong("time")))));
                                        }while (rs.next());
                                    }else{
                                        ToxicReport.sendMessage(p, "§cEs wurden keine Reports gefunden");
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }else{
                    ReportType.sendMessages(p, args[0]);
                }
            }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("takeover")){
                    ReportManager.takeOverReport(p, args[1]);
                }else if(args[0].equalsIgnoreCase("list")){
                    ToxicReport.sendMessage(p, "Verwendung: §c/report list");
                }else{
                    int reason = 0;
                    try{
                        reason = Integer.valueOf(args[1]);
                    }catch (Exception e){
                        ToxicReport.sendMessage(p, "Diese Grund-ID wurde nicht gefunden");
                        return;
                    }
                    ReportManager.report(args[0], p, reason);
                }
            }else{
                sendHelp(p);
            }
        }

    }

    private void sendHelp(ProxiedPlayer p){
        p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§8§m--------------§8 §cReport §8§m--------------§8")));
        p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§c/report <Spieler> §8- §7Reporte einen Spieler")));
        p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§c/report list §8- §7Liste aller Reports")));
        if(p.hasPermission("ToxicReport.supporter"))
            p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§c/report takeover <ID> §8- §7Übernehme einen Report")));
    }

}

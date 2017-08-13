package me.MaxPlays.ToxicReport.managers;

import me.MaxPlays.ToxicReport.ToxicReport;
import me.MaxPlays.ToxicReport.io.SQL;
import me.MaxPlays.ToxicReport.util.IDGenerator;
import me.MaxPlays.ToxicReport.util.ReportType;
import me.MaxPlays.ToxicReport.util.TimeParser;
import me.MaxPlays.ToxicReport.util.UUIDFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class ReportManager {

    private static HashMap<ProxiedPlayer, Long> timeouts = new HashMap<>();

    public static void report(String player, ProxiedPlayer reporter, int reason){
        if(canReport(reporter)){
            if(BungeeCord.getInstance().getPlayer(player) != null){
                ReportType type = ReportType.getById(reason);
                if(type != null){
                    String uuidReported = BungeeCord.getInstance().getPlayer(player).getUniqueId().toString();
                    String uuidReporter = reporter.getUniqueId().toString();
                    String reportID = IDGenerator.getRandomID(10);

                    try {
                        PreparedStatement ps = ToxicReport.sql.getCon().prepareStatement("INSERT INTO reports VALUES(?, ?, ?, ?, ?, ?);");
                        ps.setString(1, reportID);
                        ps.setLong(2, System.currentTimeMillis());
                        ps.setString(3, uuidReported);
                        ps.setString(4, uuidReporter);
                        ps.setInt(5, reason);

                        String url = ChatlogManager.getChatlog(BungeeCord.getInstance().getPlayer(player));

                        ps.setString(6, url);

                        ps.executeUpdate();

                        int online = 0;

                        for(ProxiedPlayer pl: BungeeCord.getInstance().getPlayers()){
                            if(pl.hasPermission("ToxicReport.supporter")){
                                ToxicReport.sendMessage(pl, "§4" + BungeeCord.getInstance().getPlayer(player).getName() + " §7wurde wegen §c" + type.getText() + " §7von §8" + reporter.getName() + " §7reportet.");
                                TextComponent tc = new TextComponent(ToxicReport.prefix);
                                TextComponent click = new TextComponent(TextComponent.fromLegacyText("§a[Report übernehmen]"));
                                click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "report takeover " + reportID));

                                online++;
                            }
                        }

                        ToxicReport.sendMessage(reporter, "Vielen Dank für deinen Report des Spielers §c" + player + "§8! §7" +
                                (online > 0 ? "Es wird sich in Kürze eines der Teammitglieder, die online sind, §8(§2" + online + "§8) §7um die Angelegenheit kümmern." : "Es sind im Moment leider §ckeine §7Teammitglieder online. Es wird sich trotzdem so schnell wie möglich um deinen Report gekümmert.") +
                                        " §7Die ID dieses Reports lautet §c§l" + reportID);
                        ToxicReport.sendMessage(reporter, "Eine Liste deiner Reports findest du unter §c/report list§7. Dort werden alle §aoffenen §7Reports angezeigt");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    ToxicReport.sendMessage(reporter, "Die Grund-ID §c" + reason + " §7wurde nicht gefunden. Benutze §c/report " + player + " §7um eine Liste aller IDs zu erhalten");
                }
            }else{
                ToxicReport.sendMessage(reporter, "Der Spieler §c" + player + " §7ist nicht online");
            }
        }else{
            ToxicReport.sendMessage(reporter, "Bitte warte noch §c" + new TimeParser((timeouts.get(reporter) - System.currentTimeMillis())/1000) + "§7, bevor du wieder einen Spieler meldest");
        }
    }

    public static void takeOverReport(ProxiedPlayer p, String id){
        if(p.hasPermission("ToxicReport.supporter")){
            BungeeCord.getInstance().getScheduler().runAsync(ToxicReport.instance, new Runnable() {
                @Override
                public void run() {
                    ResultSet rs = ToxicReport.sql.query("SELECT * FROM reports WHERE id='" + id + "';");
                    try {
                        if(rs.next()){
                            ProxiedPlayer reported = BungeeCord.getInstance().getPlayer(UUID.fromString(rs.getString("reported")));
                            ReportType type = ReportType.getById(rs.getInt("reason"));
                            String id = rs.getString("id");
                            ToxicReport.sendMessage(p, "Chatlog:§c " + rs.getString("url"));
                            if(type.getId() != 1){
                                if(reported != null){
                                    p.connect(reported.getServer().getInfo());
                                    ToxicReport.sendMessage(p, "Du wurdest mit dem Server des reporteten Spielers §4§l" + reported.getName() + " §7verbunden");
                                }else{
                                    String name = UUIDFetcher.getName(rs.getString("reported"));
                                    ToxicReport.sendMessage(p, "Der gemeldete Spieler §c" + name + " §7ist nicht online. Der Chatlog muss als Beweis reichen. Falls er das nicht tut, kann ein Gespräch mit dem Ersteller des Reports §a" + UUIDFetcher.getName(rs.getString("reporter")) + " gesucht werden");
                                }
                            }
                            ToxicReport.sql.update("DELETE FROM reports WHERE id='" + id + "';");
                        }else{
                            ToxicReport.sendMessage(p, "§cDieser Report existiert nicht oder wurde bereits von einem anderen Teammitglied übernommen");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            ToxicReport.sendMessage(p, "§cDazu hst du keine Rechte");
        }
    }


    private static boolean canReport(ProxiedPlayer p){
        if(timeouts.containsKey(p) && timeouts.get(p) > System.currentTimeMillis())
            return false;
        return true;
    }
    private static void timeout(ProxiedPlayer p){
        if(timeouts.containsKey(p))
            timeouts.remove(p);
        timeouts.put(p, System.currentTimeMillis() + ToxicReport.reportTimeout);
    }

}

package me.MaxPlays.ToxicReport.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;


/**
 * Created by Max_Plays on 12.08.2017.
 */
public enum ReportType {

    HACKING("Hacking"),
    CHAT("Chatvergehen/Werbung"),
    TEAM("Teaming"),
    BUG("Bugusing"),
    INAPPROPRIATE("Anstößiger Skin/Name"),
    FRAUD("Betrug/Scam/Phishing");

    private static int current = 0;
    private int id;
    private String text;

    ReportType(String text){
        this.text = text;
        this.id = getCurrent();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public static void sendMessages(ProxiedPlayer p, String arg1){
        for(ReportType t: ReportType.values()){
            TextComponent cp = new TextComponent(TextComponent.fromLegacyText("§8» §2ID: §a" + t.getId() + "§8; §2Grund: §a" + t.getText()));
            if(arg1 == null)
                cp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "report <Spieler>"));
            else
                cp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "report " + arg1 + " " + t.getId()));
            p.sendMessage(cp);
        }
        p.sendMessage(new TextComponent("§8»"));
        if(arg1 == null)
            p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§8» §7Benutze §a/report <Spieler> §7um einen Spieler zu melden")));
        else
            p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§8» §7Klicke auf einen der Gründe, um den Spieler §a" + arg1 + " §7zu melden")));
        p.sendMessage(new TextComponent(TextComponent.fromLegacyText("§8» §cDer Missbrauch dieser Funktion wird bestraft")));
    }

    private static int getCurrent(){
        return ++current;
    }

    public static ReportType getById(int id){
        for(ReportType t: values()){
            if(t.getId() == id)
                return t;
        }
        return null;
    }
}

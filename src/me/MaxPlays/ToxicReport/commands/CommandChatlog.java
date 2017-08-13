package me.MaxPlays.ToxicReport.commands;

import me.MaxPlays.ToxicReport.ToxicReport;
import me.MaxPlays.ToxicReport.managers.ChatlogManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created by Max_Plays on 12.08.2017.
 */
public class CommandChatlog extends Command {

    public CommandChatlog(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer){
            ProxiedPlayer p = (ProxiedPlayer) sender;
            if(p.hasPermission("ToxicReport.supporter")){
                if(args.length == 1){
                    ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
                    if(target != null){
                        ToxicReport.sendMessage(p, "Erstelle Chatlog...");
                        ToxicReport.sendMessage(p, ChatColor.RED + ChatlogManager.getChatlog(target));
                    }else{
                        ToxicReport.sendMessage(p, "Der Spieler §c" + args[0] + " §7ist nicht online");
                    }
                }else{
                    ToxicReport.sendMessage(p, "Syntaxfehler. Verwendung: §c/chatlog <Spieler>");
                }
            }else{
                ToxicReport.sendMessage(p, "§cDu hast keine Rechte, um diesen Command zu benutzen");
            }
        }
    }
}

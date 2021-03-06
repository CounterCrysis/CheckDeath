package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EventTabComplete implements TabCompleter {

    private YAMLServices ys;

    public EventTabComplete(YAMLServices ys) {
        this.ys = ys;
    }

    private boolean hasPerm(Player player, String perm) {
        return (player.hasPermission("checkdeath."+perm) || player.isOp());
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            List<String> list = new ArrayList<String>();
            if (args.length == 1) {
                String arg0 = args[0].toLowerCase();
                Set<String> players = ys.getCachedPlayers().keySet();

                if (hasPerm(p, "others") || hasPerm(p, "others.admin")) {
                    players.forEach(n -> { if (n.startsWith(arg0)) { list.add(n); }});
                } else if (hasPerm(p, "self") || hasPerm(p, "self.admin")) {
                    list.add(p.getName());
                }

            } else if (args.length == 2) {

                if (((  hasPerm(p, "self") || hasPerm(p, "self.admin")) && p.getName().equalsIgnoreCase(args[0]))
                        || hasPerm(p, "others")
                        || hasPerm(p, "others.admin")){
                    List<String> deathIds = ys.getDeathIds(args[0]);
                    if (deathIds != null) {
                        deathIds.forEach(id -> { if (id.startsWith(args[1])) list.add(id); });
                    }
                }
            }
            return list;
        }
        return null;
    }

}
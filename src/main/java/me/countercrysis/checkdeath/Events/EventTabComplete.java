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
        return (player.hasPermission("checkdeath."+perm));
    }

    private boolean contains(String s1, String s2) {
        return String.valueOf(s1).contains(s2);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            List<String> list = new ArrayList<String>();
            if (args.length == 1) {
                String arg0 = args[0].toLowerCase();
                Set<String> players = ys.getCachedPlayers().keySet();

                if (hasPerm(p, "others")) {
                    players.forEach(n -> { if (contains(n, arg0)) { list.add(n); }});
                } else if (hasPerm(p, "self")) {
                    list.add(p.getName());
                }

                /*
                if (hasPerm(p, "example") && contains("example", arg0))
                    list.add("example");
                */

            } else if (args.length == 2) {
                list.addAll(ys.getDeathIds(args[0]));
            }

            return list;
        }
        return null;
    }

}
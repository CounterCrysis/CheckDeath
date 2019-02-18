package me.countercrysis.checkdeath.Events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventTabComplete implements TabCompleter {

    private boolean hasPerm(Player player, String perm) {
        return (player.hasPermission("checkdeath."+perm));
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<String> list = new ArrayList<String>();
            if (args.length == 1) {
                String arg1 = args[0].toLowerCase();
                if (hasPerm(player, "example") && String.valueOf("example").contains(arg1))
                    list.add("open");
            }

            return list;
        }
        return null;
    }

}
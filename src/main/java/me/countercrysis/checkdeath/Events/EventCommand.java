package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.Base64Services;
import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public class EventCommand implements CommandExecutor{

    private Plugin plugin;
    private YAMLServices ys;
    private Base64Services bs;

    public EventCommand(Plugin plugin, YAMLServices ys) {
        this.plugin = plugin;
        this.ys = ys;
        bs = new Base64Services();
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        String uuid     = ys.getUUID(args[0]);
        String death    = args[1];
        String data     = (String) ys.get(uuid, "deaths."+death+".data");

        Server server = player.getServer();
        String w    = (String) ys.get(uuid, "deaths."+death+".pos.world");
        Integer x    = (Integer) ys.get(uuid, "deaths."+death+".pos.x");
        Integer y    = (Integer) ys.get(uuid, "deaths."+death+".pos.y");
        Integer z    = (Integer) ys.get(uuid, "deaths."+death+".pos.z");
        Integer xp   = (Integer) ys.get(uuid, "deaths."+death+".xp");
        String msg  = (String) ys.get(uuid, "deaths."+death+".msg");

        server.broadcastMessage(w + "(" + x + ", " + y + ", " + z + ")");
        server.broadcastMessage("Exp: " + xp);
        server.broadcastMessage("Msg: " + msg);

        System.out.println(player);
        System.out.println(uuid);
        System.out.println(death);
        System.out.println(data);
        try {
            Inventory inventory = bs.fromBase64(data);
            player.openInventory(inventory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}

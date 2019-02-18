package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.Base64Services;
import me.countercrysis.checkdeath.Services.YAMLServices;
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

        try {
            Inventory inventory = bs.fromBase64(data);
            player.openInventory(inventory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}

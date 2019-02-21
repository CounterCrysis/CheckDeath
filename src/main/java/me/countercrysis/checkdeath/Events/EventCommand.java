package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.Base64Services;
import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.UUID;

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
        //String uuid     = ys.getUUID(args[0]);
        String username = args[0];
        UUID uuid       = ys.getUUID(username);
        String death    = args[1];
        String data     = (String) ys.get(uuid.toString(), "deaths."+death+".data");

        Server server = player.getServer();
        String w    = (String)  ys.get(uuid.toString(), "deaths."+death+".pos.world");
        Integer x   = (Integer) ys.get(uuid.toString(), "deaths."+death+".pos.x");
        Integer y   = (Integer) ys.get(uuid.toString(), "deaths."+death+".pos.y");
        Integer z   = (Integer) ys.get(uuid.toString(), "deaths."+death+".pos.z");
        Integer xp  = (Integer) ys.get(uuid.toString(), "deaths."+death+".xp");
        String msg  = (String)  ys.get(uuid.toString(), "deaths."+death+".msg");

        server.broadcastMessage(w + "(" + x + ", " + y + ", " + z + ")");
        server.broadcastMessage("Exp: " + xp);
        server.broadcastMessage("Msg: " + msg);

        System.out.println(player);
        System.out.println(uuid);
        System.out.println(death);
        //System.out.println(data);
        Inventory inventory;
        try {
            inventory = bs.fromBase64(data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        ItemStack head = getHead(username, uuid);

        inventory.setItem(inventory.getSize()-1, head);
        player.openInventory(inventory);

        return true;
    }

    private static ItemStack getHead(String username, UUID uuid) {
        ItemStack itemSkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metaSkull = (SkullMeta) itemSkull.getItemMeta();
        metaSkull.setDisplayName("§fSkull of " + username);
        metaSkull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        itemSkull.setItemMeta(metaSkull);

        return itemSkull;
    }

}

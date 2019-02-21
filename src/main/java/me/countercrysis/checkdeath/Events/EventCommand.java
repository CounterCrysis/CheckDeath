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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        String username = args[0];
        UUID uuid       = ys.getUUID(username);
        String death    = args[1];
        String data     = (String) ys.get(uuid.toString(), "deaths."+death+".data");

        /*
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
        System.out.println(data);
        */

        Map<String, Object> deathDetails = new HashMap<>();
        deathDetails.put("n",   username);
        deathDetails.put("t",   death);
        deathDetails.put("w",   ys.get(uuid.toString(), "deaths."+death+".pos.world"));
        deathDetails.put("x",   ys.get(uuid.toString(), "deaths."+death+".pos.x"));
        deathDetails.put("y",   ys.get(uuid.toString(), "deaths."+death+".pos.y"));
        deathDetails.put("z",   ys.get(uuid.toString(), "deaths."+death+".pos.z"));
        deathDetails.put("xp",  ys.get(uuid.toString(), "deaths."+death+".xp"));
        deathDetails.put("msg", ys.get(uuid.toString(), "deaths."+death+".msg"));


        Inventory inventory;
        try {
            inventory = bs.fromBase64(data);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        ItemStack head = getHead(username, uuid);
        addDeathDetails(head, deathDetails);

        inventory.setItem(inventory.getSize()-1, head);
        player.openInventory(inventory);

        return true;
    }

    // Translate Color Codes, allowing use of & prefix
    private static String tcc(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private static void addDeathDetails(ItemStack item, Map<String, Object> details) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§c§lDeath " + details.get("n"));
        Float timeDif = Float.parseFloat((String)details.get("t")) - (System.currentTimeMillis()/1000);
        itemMeta.setLore(Arrays.asList(
                tcc("&1World: &r" + details.get("w")),
                tcc("&9X: &r" + details.get("x") + " &9Y: &r" + details.get("y") + " &9Z: &r" + details.get("z")),
                tcc("&1Exp: &r" + details.get("xp")),
                tcc("&9Time: &r" + String.format("%.02f", (timeDif / 60 / 60)) + "h ago"),
                tcc("&e\"" + details.get("msg") + "\"")
        ));
        item.setItemMeta(itemMeta);
    }

    private static ItemStack getHead(String username, UUID uuid) {
        ItemStack itemSkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metaSkull = (SkullMeta) itemSkull.getItemMeta();
        metaSkull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        itemSkull.setItemMeta(metaSkull);
        return itemSkull;
    }

}

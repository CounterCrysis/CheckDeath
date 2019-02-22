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

    private boolean hasPerm(Player player, String perm) {
        return (player.hasPermission("checkdeath."+perm));
    }

    public EventCommand(Plugin plugin, YAMLServices ys) {
        this.plugin = plugin;
        this.ys = ys;
        bs = new Base64Services();
    }

    /*
        checkdeath.???

        user
            self        /checkdeath player_name 012345  (check details of own death)
                admin        /checkdeath admin player_name 012345 (check details of own death + retrieve items)
            others      /checkdeath player_name 012345  (check details of other players death)
                admin      /checkdeath admin player_name 012345 (check details of other players death + retrieve items)




     */

    private Inventory getDeathInv (String username, UUID uuid, String death, boolean adminPerm) {
        String data     = (String) ys.get(uuid.toString(), "deaths."+death+".data");
        if (data == null) return null;

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
            inventory = bs.fromBase64(data, "CheckDeath" + (adminPerm ? " - Admin" : "" ));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        ItemStack head = getHead(username, uuid);
        addDeathDetails(head, deathDetails);

        inventory.setItem(inventory.getSize()-1, head);
        return inventory;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        int argCount = args.length;
        if (argCount >= 1) {
            String username = args[0].toLowerCase();
            UUID uuid       = ys.getUUID(username);
            if (uuid == null)  {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "Either the Username or Death ID you provided was incorrect!");
                return true;
            }
            if (((hasPerm(player, "self")
                    || hasPerm(player, "self.admin"))
                    && player.getUniqueId().equals(uuid))
                    || hasPerm(player, "others")
                    || hasPerm(player, "others.admin")){
                if (argCount >= 2) {
                    String death = args[1];
                    Inventory inventory = getDeathInv(username, uuid, death,
                            (hasPerm(player, "self.admin") && player.getUniqueId().equals(uuid)) ||
                            hasPerm(player, "others.admin"));
                    if (inventory != null) {
                        player.openInventory(inventory);
                    } else {
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Either the Username or Death ID you provided was incorrect!");
                    }
                } else {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You did not include a Death ID");
                    player.sendMessage(ChatColor.WHITE + "Usage -> /checkdeath " + username + " <death_id>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
            }
        } else {
            player.sendMessage(ChatColor.WHITE + "Usage -> /checkdeath " + player.getName() + " <death_id>");
        }
        return true;
    }


    // Translate Color Codes, allowing use of & prefix
    private static String tcc(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private static void addDeathDetails(ItemStack item, Map<String, Object> details) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("§c§lDeath " + details.get("n"));
        Long now = System.currentTimeMillis(); //in milli
        Double timeDif = (now.doubleValue()/1000.0) - Double.parseDouble((String)details.get("t"));
        itemMeta.setLore(Arrays.asList(
                tcc("&1World: &r" + details.get("w")),
                tcc("&9X: &r" + details.get("x") + " &9Y: &r" + details.get("y") + " &9Z: &r" + details.get("z")),
                tcc("&1Exp: &r" + details.get("xp")),
                tcc("&9Time: &r" + String.format("%.02f", (timeDif / 60.0 / 60.0)) + "h ago"),
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

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

import java.util.*;

public class EventCommand implements CommandExecutor{

    private YAMLServices ys;
    private Base64Services bs;

    private final ItemStack[] INV_SELECT_DEATH;

    private boolean hasPerm(Player player, String perm) {
        return (player.hasPermission("checkdeath."+perm) || player.isOp());
    }

    public EventCommand(YAMLServices ys) {
        this.ys = ys;
        bs = new Base64Services();
        final ItemStack GRAY = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemStack BLACK = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        final ItemStack LIGHT = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta blankMeta = GRAY.getItemMeta();
        blankMeta.setDisplayName("`");
        GRAY.setItemMeta(blankMeta);
        BLACK.setItemMeta(blankMeta);
        LIGHT.setItemMeta(blankMeta);
        INV_SELECT_DEATH = new ItemStack[] {
            /*  1     2       3       4       5       6       7       8       9     */
                GRAY, GRAY,   BLACK,  BLACK,  LIGHT,  BLACK,  BLACK,  GRAY,   GRAY,
                GRAY, BLACK,  LIGHT,  LIGHT,  LIGHT,  LIGHT,  LIGHT,  BLACK,  GRAY,
                GRAY, BLACK,  LIGHT,  LIGHT,  LIGHT,  LIGHT,  LIGHT,  BLACK,  GRAY,
                GRAY, BLACK,  LIGHT,  LIGHT,  LIGHT,  LIGHT,  LIGHT,  BLACK,  GRAY,
                GRAY, GRAY,   BLACK,  BLACK,  BLACK,  BLACK,  BLACK,  GRAY,   GRAY
        };
    }

    private Inventory getPlayerSelectInv (Player player) {

        List<UUID> players = new ArrayList<>();
        UUID playerUUID = player.getUniqueId();
        List<ItemStack> playerHeads = new ArrayList<>();

        // Determine which players should be show in the menu
        players.add(playerUUID);
        if (hasPerm(player, "others") || hasPerm(player, "others.admin")){
            ys.getCachedPlayers().forEach((key,value) -> {if (!playerUUID.equals(value)) players.add(value); });
        }

        // Determine menu size
        int invSize = (9*(players.size() / 9)) + ((players.size() % 9 == 0) ? 0 : 9);
        if (invSize>54) invSize = 54;

        Inventory inv = Bukkit.createInventory(null, invSize, "CheckDeath - Select Player");

        ys.getCachedPlayers().forEach((key,uuid) -> playerHeads.add(getHead(uuid)) );

        playerHeads.forEach(skull -> inv.addItem(skull));

        return inv;
    }

    private Inventory getDeathSelectInv (UUID uuid) {
        List<String> deathIds = ys.getDeathIds(uuid);
        String username = ys.getUsername(uuid);

        //int invSize = (9*(deathIds.size() / 9)) + ((deathIds.size() % 9 == 0) ? 0 : 9);
        int invSize = 45;
        //if (invSize>54) invSize = 54;
        //if (invSize<9) invSize = 9;
        Inventory inv = Bukkit.createInventory(null, invSize, "CheckDeath - Select Death");


        inv.setContents(INV_SELECT_DEATH);

        //HEAD
        ItemStack deathHead = getHead(uuid);
        ItemMeta headItemMeta = deathHead.getItemMeta();
        headItemMeta.setDisplayName(username);
        deathHead.setItemMeta(headItemMeta);
        inv.setItem(4,deathHead);

        //5x3 Death Slots
        //11-15, 20-24, 29-33
        ItemStack red = new ItemStack(Material.RED_TULIP);
        ItemStack org = new ItemStack(Material.ORANGE_TULIP);

        int start = 0;
        int end = deathIds.size()>15 ? 15 : deathIds.size();
        long now = System.currentTimeMillis();
        int numDeaths = deathIds.size();

        for (int i=start; i<end; i++) {
            int slot = 11 + i%5 + (9*(i/5));
            ItemStack item = slot%2==1 ? red : org;
            ItemMeta meta = item.getItemMeta();
            double timeDif = (now/1000.0) - Double.parseDouble(deathIds.get(numDeaths-i-1));
            meta.setDisplayName("§9Time: §r" + String.format("%.02f", (timeDif / 60.0 / 60.0)) + "h ago");
            meta.setLore(Arrays.asList(deathIds.get(numDeaths-i-1)));
            item.setItemMeta(meta);
            inv.setItem(slot, item);
        }

        /*
        deathIds.forEach((id) -> {
            ItemStack deathHead = getHead(username, uuid);
            ItemMeta itemMeta = deathHead.getItemMeta();
            itemMeta.setDisplayName(username + " " + id);
            deathHead.setItemMeta(itemMeta);
            inv.addItem(deathHead);
        } );
        */

        return inv;
    }

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

        ItemStack head = getHead(uuid);
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

        if (!(hasPerm(player, "self") || hasPerm(player, "self.admin") || hasPerm(player, "others") || hasPerm(player, "others.admin"))) {
            player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
            return true;
        }

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
                    player.openInventory(getDeathSelectInv(uuid));
                    //player.sendMessage(ChatColor.LIGHT_PURPLE + "You did not include a Death ID");
                    //player.sendMessage(ChatColor.WHITE + "Usage -> /checkdeath " + username + " <death_id>");
                }
            } else {
                player.sendMessage(ChatColor.RED + "You do not have permission to do that!");
            }
        } else {
            player.openInventory(getPlayerSelectInv(player));
            //player.sendMessage(ChatColor.WHITE + "Usage -> /checkdeath " + player.getName() + " <death_id>");
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
        long now = System.currentTimeMillis(); //in milli
        double timeDif = (now/1000.0) - Double.parseDouble((String)details.get("t"));
        itemMeta.setLore(Arrays.asList(
                tcc("&1World: &r" + details.get("w")),
                tcc("&9X: &r" + details.get("x") + " &9Y: &r" + details.get("y") + " &9Z: &r" + details.get("z")),
                tcc("&1Exp: &r" + details.get("xp")),
                tcc("&9Time: &r" + String.format("%.02f", (timeDif / 60.0 / 60.0)) + "h ago"),
                tcc("&e\"" + details.get("msg") + "\"")
        ));
        item.setItemMeta(itemMeta);
    }

    private static ItemStack getHead(UUID uuid) {
        ItemStack itemSkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metaSkull = (SkullMeta) itemSkull.getItemMeta();
        metaSkull.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        itemSkull.setItemMeta(metaSkull);
        return itemSkull;
    }

}

package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class EventInventoryClick implements Listener {

    private YAMLServices ys;

    public EventInventoryClick(YAMLServices ys) {
        this.ys = ys;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();

        if (inventory == null || item == null || item.getType().equals(Material.AIR)) {
            return;
        }

        String invName = inventory.getName();

        if (invName.startsWith("CheckDeath")) {

            if (invName.startsWith("CheckDeath - Select Player")) {
                event.setCancelled(true);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                String name = meta.getOwningPlayer().getName();
                player.getServer().dispatchCommand(player, "checkdeath " + name);
            } else if (invName.startsWith("CheckDeath - Select Death")) {
                event.setCancelled(true);
                //11-15, 20-24, 29-33

                if (slot>=11 && slot <=33 && slot%9>1 && slot%9<7){ // Within the 5x3 grid
                    if (item.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) {
                        return;
                    }
                    String deathId = item.getItemMeta().getLore().get(0);
                    String deathName = inventory.getContents()[4].getItemMeta().getDisplayName();
                    player.getServer().dispatchCommand(player, "checkdeath " + deathName + " " + deathId);
                }

            }

        }

        if (invName.equals("CheckDeath") ||
                (invName.equals("CheckDeath - Admin") && slot == inventory.getSize()-1)) {
            event.setCancelled(true);
        }
    }

}

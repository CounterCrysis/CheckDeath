package me.countercrysis.checkdeath.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EventInventoryClick implements Listener {


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();

        if (inventory == null || item == null) {
            return;
        }

        String inventoryName = inventory.getName();
        if (inventoryName.equals("CheckDeath") ||
                (inventoryName.equals("CheckDeath - Admin") && slot == inventory.getSize()-1)) {
            event.setCancelled(true);
        }
    }

}

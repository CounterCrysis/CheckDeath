package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.Base64Services;
import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class EventDeath implements Listener {

    private Plugin plugin;
    private YAMLServices ys;
    private Base64Services bs;

    public EventDeath(Plugin plugin, YAMLServices ds) {
        this.plugin = plugin;
        this.ys = ds;
        bs = new Base64Services();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player   = event.getEntity();
        String uuid     = player.getUniqueId().toString();
        String epoch    = Long.toString(System.currentTimeMillis()/1000);
        String prefix   = "deaths." + epoch + ".";
        Location pos    = player.getLocation();

        ys.set(uuid, prefix+"pos.world", player.getWorld().getName());
        ys.set(uuid, prefix+"pos.x", (int)pos.getX());
        ys.set(uuid, prefix+"pos.y", (int)pos.getY());
        ys.set(uuid, prefix+"pos.z", (int)pos.getZ());
        ys.set(uuid, prefix+"xp", player.getTotalExperience());
        ys.set(uuid, prefix+"msg", event.getDeathMessage());
        ys.set(uuid, prefix+"data", bs.itemStackArrayToBase64(event.getDrops()));

    }



}
